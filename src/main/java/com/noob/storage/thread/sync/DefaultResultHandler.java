package com.noob.storage.thread.sync;

import com.noob.storage.thread.ob.Counter;

/**
 * 默认的结果处理器
 *
 * @author luyun
 * @version 2017.7.31 TB to HQB
 * @since 2017.07.27
 */
public class DefaultResultHandler extends AbstractResultHandler<BizTaskResult, Counter> {

    @Override
    public void consume(BizTaskResult result) {

    }

    @Override
    public Counter getResult() {
        return null;
    }
}
