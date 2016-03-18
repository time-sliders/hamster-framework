package com.noob.storage.thread.schedule;

import java.util.List;

/**
 * 资源分配策略
 */
public interface ResourceAllocatePolicy {

    /**
     * 将一定数量的可以用资源分配给一组持有对象
     *
     * @param maxnum  可以分配的资源总量
     * @param holders 资源的持有者
     */
    public void allocate(int maxnum, List<Holder> holders);

}
