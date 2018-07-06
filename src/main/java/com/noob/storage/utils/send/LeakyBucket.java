package com.noob.storage.utils.send;

import java.util.concurrent.LinkedBlockingQueue;

/**
 * 漏桶算法
 *
 * 在单位时间内最多之有单位数据流出
 *
 * @author LuYun
 * @version legends1.13
 * @since 2018.05.10
 */
public class LeakyBucket<E> {

    /**
     * 漏桶
     * 桶中元素以恒定速度流出
     */
    private LinkedBlockingQueue<E> bucket;
}
