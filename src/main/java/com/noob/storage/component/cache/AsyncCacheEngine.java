package com.noob.storage.component.cache;

import com.alibaba.dubbo.common.utils.ConcurrentHashSet;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.util.TypeUtils;
import com.noob.storage.component.RedisComponent;
import com.noob.storage.component.cache.config.DataLoadConfig;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 异步缓存引擎<br/>
 * <li>数据始终保存在缓存</li>
 * <li>当达到超时时间之后,依然以[旧数据]响应,但会尝试重新从数据源查询数据,只有当获取到新数据之后,才替换旧数据</li>
 * <li>子类务必保证线程安全</li>
 *
 * @author luyun
 */
public abstract class AsyncCacheEngine implements InitializingBean {

    private static final Logger logger = LoggerFactory.getLogger(AsyncCacheEngine.class);

    @SuppressWarnings("SpringJavaAutowiredMembersInspection")
    @Autowired
    private RedisComponent redisComponent;
    //当前正在执行的包括等待的key的集合
    private Set<String/*cacheKey*/> currentKeySet = new ConcurrentHashSet<String>();
    //异步线程池
    private ThreadPoolExecutor pool;
    //缓存数据超时时间
    private long timeout = CacheWrapper.HALF_HOUR;
    private Class clazz;

    /**
     * 配置MAP,不同的Class根据不同的配置来查找数据
     */
    private Map<Class, DataLoadConfig> configMap = new HashMap<>();

    private Object get(Class clazz) {
        DataLoadConfig config = configMap.get(clazz);
        if (config == null) {
            throw new RuntimeException("异步缓存引擎无对应的配置,class:" + clazz);
        }
        return get(config);
    }

    /**
     * 添加一个配置
     */
    public boolean addConfig(DataLoadConfig config) {
        return config != null
                && config.selfCheck()
                && configMap.put(config.getDataType(), config) != null;
    }

    /**
     * 引擎应该是通过配置去加载数据
     * 而不是每一个配置创建一个对应的引擎
     *
     * @param config 数据加载配置
     */
    private Object get(DataLoadConfig config) {
        String key = config.getKey();
        if (StringUtils.isBlank(key)) {
            return null;
        }

        CacheWrapper wrapper;
        if (StringUtils.isNotBlank(config.getMapKey())) {
            wrapper = redisComponent.hget(key, config.getMapKey(), CacheWrapper.class);
        } else {
            wrapper = redisComponent.get(key, CacheWrapper.class);
        }

        //如果缓存中没有数据,直接同步去数据源查询
        if (wrapper == null) {
            config.getLoadPolicy().asyncLoadData(config.getDataLoadTask());
            return null;
        }

        //如果缓存有数据且已经超时,那么通知异步线程去尝试获取新数据
        if (wrapper.alreadyTimeout()) {
            config.getLoadPolicy().asyncLoadData(config.getDataLoadTask());
        }

        Object object = wrapper.getData();
        if (object != null) {
            JSONObject jsonObject = (JSONObject) object;
            return TypeUtils.castToJavaBean(jsonObject, clazz);
        }
        return null;
    }

    /**
     * 将数据保存到Redis缓存中
     */
    public void set(Object param, Object obj) {

        if (obj == null || param == null) {
            return;
        }

        //对象包装
        CacheWrapper wrapper = new CacheWrapper(obj, timeout);
        redisComponent.hset(getKey(), getMapKey(param), wrapper);
    }

    /**
     * 异步重新加载数据
     */
    private synchronized void asyncReloadData(Object param, DataLoadConfig loadConfig) {
        String cacheKey = getMapKey(param);
        try {

            // 如果已经正在执行,则不再重复添加
            if (currentKeySet.contains(cacheKey)) {
                return;
            }

            //超过最大等待长度,根据拒绝策略拒绝请求
            if (currentKeySet.size() >= getMaxWaitSize()) {
                loadConfig.getRejectPolicy().reject(param, this);
                return;
            }

            //添加任务到等待队列
            DataLoadTask task = new DataLoadTask(this, param, loadConfig.getLoadPolicy());
            task.setEngine(this);
            task.setParam(param);
            currentKeySet.add(cacheKey);
            pool.submit(task);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            currentKeySet.remove(cacheKey);
        }
    }

    /**
     * 同步获取数据
     */
    private Object syncLoadData(Object param) throws Exception {

        Object result = getDataFromSource(param);

        this.set(param, result);

        return result;
    }

    /**
     * 删除所有缓存
     */
    public Long deleteAllCache() {
        return redisComponent.del(getKey());
    }

    /**
     * 删除某一条缓存
     */
    public Long deleteCache(Object param) {
        return redisComponent.del(getMapKey(param));
    }

    //关闭引擎
    public void closeQuietly() {
        try {
            pool.shutdown();
            pool.awaitTermination(3, TimeUnit.MINUTES);//阻塞
            if (!pool.isTerminated()) {
                logger.info("AsyncCacheEngine shutdown pool time out,use shutdownNow!");
                pool.shutdownNow();//忽略返回的Runnable List,GC自动回收
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }

    //最大等待队列长度
    protected abstract int getMaxWaitSize();

    //最大线程池大小
    protected abstract int getPoolSize();

    Set<String> getCurrentKeySet() {
        return currentKeySet;
    }

    public void setTimeout(long timeout) {
        this.timeout = timeout;
    }

    /**
     * 从数据源重新获取数据
     */
    protected abstract Object getDataFromSource(Object param) throws Exception;

    /**
     * 获取对象在缓存的Map中的Key值
     */
    protected abstract String getMapKey(Object param);

    /**
     * 一个引擎管理同一类数据,
     * 同一类数据放在同一个缓存Map中,
     * Key表示该Map在缓存中的Key
     */
    protected abstract String getKey();


    @SuppressWarnings("unchecked")
    public void afterPropertiesSet() throws Exception {
        if (pool == null || pool.isTerminated()) {
            int poolSize = getPoolSize();
            poolSize = poolSize <= 0 ? 1 : poolSize;
            pool = new ThreadPoolExecutor(poolSize, poolSize, 0L,
                    TimeUnit.MILLISECONDS,
                    new LinkedBlockingQueue<Runnable>());
        }

        Type genType = getClass().getGenericSuperclass();
        clazz = (Class) ((ParameterizedType) genType).getActualTypeArguments()[1];
    }

    public void setPoolSize(int poolSize) {
        if (pool != null && !pool.isTerminated()) {
            pool.setCorePoolSize(poolSize);
            pool.setMaximumPoolSize(poolSize);
        }
    }

}
