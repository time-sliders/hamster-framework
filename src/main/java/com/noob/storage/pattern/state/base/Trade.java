package com.noob.storage.pattern.state.base;

import java.util.Deque;

/**
 * 交易类型<br/>
 * <p>
 * 系统中可能存在很多交易类型，如：购买，转让，出售等<br/>
 *
 * @author luyun
 * @since 1.0
 */
public abstract class Trade {

    /**
     * 一个交易所要经过的交易节点列表
     */
    protected Deque<StatefulObject> nodeList;

    /**
     * 交易当前所处的节点
     */
    protected StatefulObject currentNode;

    protected void toNextNode(){

    }

}
