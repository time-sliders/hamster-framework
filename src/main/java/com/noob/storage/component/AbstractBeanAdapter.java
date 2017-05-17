package com.noob.storage.component;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * 抽象的Spring Bean适配器
 *
 * @author luyun
 */
public class AbstractBeanAdapter<K, E> extends AbstractAdapter<K, E>
        implements ApplicationContextAware, InitializingBean {

    private ApplicationContext context;

    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        context = applicationContext;
    }

    @SuppressWarnings("unchecked")
    protected void registerDefaultExecutor(String defaultExecutorBeanName) {
        if (StringUtils.isNotBlank(defaultExecutorBeanName)) {
            registerDefaultExecutor((E) context.getBean(defaultExecutorBeanName));
        }
    }

    @SuppressWarnings("unchecked")
    protected void registerExecutor(K k, String executorBeanName) {
        if (k != null && StringUtils.isNotBlank(executorBeanName)) {
            register(k, (E) context.getBean(executorBeanName));
        }
    }

    /**
     * 根据不同的字段类型,获取不同的执行者
     */
    @SuppressWarnings("unchecked")
    public E getExecutor(K k) {

        E executorBean;

        if (k == null) {
            executorBean = defaultExecutor;
        } else {
            executorBean = executorMapping.get(k);
            if (executorBean == null) {
                executorBean = defaultExecutor;
            }
        }

        return executorBean;
    }

}
