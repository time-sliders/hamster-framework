package com.noob.storage.pattern.state;

/**
 * 流程节点<br/>
 * <p/>
 * 每个交易类型存在不同的交易节点：如 申请 -> 确认 -> 成功<br/>
 *
 * @author luyun
 * @since 1.0
 */
public class FlowNode {

    private int name;

    private int code;

    //当前的节点状态
    private NodeState state;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public int getName() {
        return name;
    }

    public void setName(int name) {
        this.name = name;
    }
}
