package com.noob.storage.thread.sync;

import com.noob.storage.thread.ob.Counter;

import java.util.concurrent.Executor;

/**
 * 抽象增强完成服务的默认实现
 *
 * @author luyun
 * @version 2017.7.31 TB to HQB
 * @since 2017.07.27
 */
public class DefaultEnhanceCompletionService
        extends AbstractEnhanceCompletionService<BizTaskResult, Counter> {

    public DefaultEnhanceCompletionService(Executor executor) {
        super(executor,new DefaultResultHandler());
    }

    @Override
    protected void submitTask() {

    }

}
