package com.noob.storage.pattern.state;

import java.util.Set;

/**
 * 节点状态<br/>
 *
 * 每个交易节点存在不同的状态，如：申请提交成功,申请失败,申请无效<br/>
 *
 * @author luyun
 * @since 1.0
 */
public abstract class NodeState {

    Set<NodeState> availablePreState;

    Set<NodeState> availableNextState;

}
