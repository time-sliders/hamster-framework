package com.noob.storage.pattern.state.base;

import java.util.Set;

/**
 * 节点状态<br/>
 * <p>
 * 每个交易节点存在不同的状态，如：成功,失败,处理中等<br/>
 *
 * @author luyun
 * @since 1.0
 */
public abstract class State {

    //上一个状态的集合
    protected Set<State> preStateList;

    //下一个状态的集合
    protected Set<State> nextStateList;

    /**
     * 切换到下一个状态
     */
    protected State toNextState(State nextState) {
        if (nextStateList.contains(nextState)) {
            return nextState;
        }
        throw new IllegalStateException(String.format("can not change state from:%s to:%s",
                new Object[]{this, nextState}));
    }
}
