package com.noob.storage.component;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;

import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 默认线程池组件
 * <p>
 * 会在创建完毕之后初始化一个线程池
 *
 * @author luyun
 * @since 2017.10.20
 */
public class AbstractTpeCmp implements InitializingBean {

    private static final Logger logger = LoggerFactory.getLogger(AbstractTpeCmp.class);

    protected ThreadPoolExecutor tpe;

    @Override
    public void afterPropertiesSet() throws Exception {
        tpe = new ThreadPoolExecutor(4, 4, 10, TimeUnit.SECONDS,
                new LinkedBlockingDeque<Runnable>(1000), new RejectedExecutionHandler() {
            @Override
            public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
                try {
                    tpe.getQueue().put(r);
                } catch (InterruptedException e) {
                    logger.error(e.getMessage(), e);
                }
            }
        });
        tpe.allowCoreThreadTimeOut(true);
    }
}
