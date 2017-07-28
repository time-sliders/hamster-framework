package com.noob.storage.thread.sync;

/**
 * 抽象完成服务的结果处理器
 *
 * @param <V> 单条数据处理结果类型
 * @param <S> 所有V的汇总结果
 * @author luyun
 * @version 2017.7.31 TB to HQB
 * @since 2017.07.27
 */
public abstract class AbstractResultHandler<V, S> {

    public abstract void consume(V v);

    public abstract S getResult();

}
