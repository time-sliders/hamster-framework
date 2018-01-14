package com.noob.storage.component.cache;

import com.alibaba.dubbo.common.utils.StringUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.util.TypeUtils;
import com.noob.storage.common.Millisecond;
import com.noob.storage.component.RedisComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 异步缓存引擎<br/>
 * <li>数据始终保存在缓存</li>
 * <li>当达到超时时间之后,依然以[旧数据]响应,但会尝试重新从数据源查询数据,只有当获取到新数据之后,才替换旧数据</li>
 * <li>thread-safe</li>
 *
 * @author luyun
 */
public abstract class AsyncCacheEngine<Q/*query param Type*/, R/*result type*/> implements InitializingBean {

    private static final Logger logger = LoggerFactory.getLogger(AsyncCacheEngine.class);

    @Autowired
    private RedisComponent redisComponent;

    /**
     * 所有引擎共享的异步数据加载线程池
     */
    private static final ThreadPoolExecutor pool = new ThreadPoolExecutor(1, 2, 5,
            TimeUnit.SECONDS, new LinkedBlockingDeque<Runnable>(10000), new RejectedExecutionHandler() {
        @Override
        public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
            logger.warn("AsyncCacheEngine share pool full,reject task:[" + JSON.toJSONString(r) + "]");
        }
    });

    static {
        pool.allowCoreThreadTimeOut(true);
    }

    //缓存数据超时时间
    private long timeout = Millisecond.HALF_HOUR;

    private Class<R> clazz;

    /**
     * 是否正在加载数据
     */
    private AtomicBoolean isDataLoading = new AtomicBoolean(false);

    @SuppressWarnings("unchecked")
    public R get(Q param){

        String key = getKey(param);
        if (StringUtils.isBlank(key)) {
            return null;
        }

        CacheWrapper<R> wrapper = redisComponent.get(key, CacheWrapper.class);

        //如果缓存中没有数据,直接同步去数据源查询
        if (wrapper == null) {
            return syncLoadData(param);
        }

        //如果缓存有数据且已经超时,那么通知异步线程去尝试获取新数据
        if (wrapper.alreadyTimeout()) {
            asyncReloadData(param);
        }

        Object data = wrapper.getData();
        if (data != null) {
            JSONObject jsonObject = (JSONObject) data;
            return TypeUtils.castToJavaBean(jsonObject, clazz);
        }
        return null;

    }

    /**
     * 将数据保存到Redis缓存中
     */
    public void set(Q param, R obj) {

        if (obj == null) {
            return;
        }

        //对象包装
        CacheWrapper<R> wrapper = new CacheWrapper<R>(obj, timeout);
        redisComponent.set(getKey(param), wrapper);
    }

    /**
     * 异步重新加载数据
     * <p>
     * 异步加载数据时,优先获取缓存数据(理论上缓存数据一旦存储进去,就不会出现为空的情况)
     * 当缓存数据发现超时时,才会去异步重新查询数据源,这里为了避免多个线程同时查询,对系
     * 统资源造成浪费,添加了一个加载标记{@link AsyncCacheEngine#isDataLoading},同
     * 一时间只允许存在一个异步加载线程.
     */
    private synchronized void asyncReloadData(Q param) {

        if (isDataLoading.compareAndSet(false, true)) {
            return;
        }

        try {

            DataLoadTask<Q, R> task = new DataLoadTask<Q, R>(this, param);
            task.setEngine(this);
            task.setParam(param);

            pool.submit(task);

        } catch (Exception e) {
            logger.error("AsyncCacheEngine添加DataLoadTask到ThreadPoolExecutor时发生异常,Q["
                    + JSON.toJSONString(param) + "]", e);
        } finally {
            isDataLoading.compareAndSet(true, false);
        }
    }

    /**
     * 同步获取数据
     * <p>
     * 当异步引擎第一次被访问时,此时缓存中并没有存储数据,所以在多线程下,可能会存在多个
     * 线程同时查询数据源的情况,这里并没有添加查询锁。
     */
    private R syncLoadData(Q param){

        R result = getDataFromSource(param);

        if (result != null) {
            set(param, result);
        }

        return result;
    }

    protected void setTimeout(long timeout) {
        this.timeout = timeout;
    }

    /**
     * 从数据源重新获取数据
     */
    protected abstract R getDataFromSource(Q param);

    /**
     * 缓存中的Key
     */
    protected abstract String getKey(Q param);

    @SuppressWarnings("unchecked")
    public void afterPropertiesSet() throws Exception {
        Type genType = getClass().getGenericSuperclass();
        clazz = (Class<R>) ((ParameterizedType) genType).getActualTypeArguments()[1];
    }
}
