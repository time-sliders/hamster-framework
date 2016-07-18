package com.noob.storage.pattern.state.base;

/**
 * 有状态的对象<br/>
 * 继承该类的对象 即 表示 存在一种状态
 *
 * @author luyun
 * @since 1.0
 */
public abstract class StatefulObject {

    //当前对象所处的状态
    protected State state;

    //上一个节点
    protected StatefulObject preNode;

    //下一个节点
    protected StatefulObject nextNode;

    //下一个节点
    public StatefulObject next(){
        return nextNode;
    }

}
