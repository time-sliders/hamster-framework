package com.noob.storage.pattern.state;

/**
 * 一个具有流程属性的对象
 *
 * @author luyun
 * @since 1.0
 */
public interface FlowObject {

    AbstractBusinessType type = null;

    /**
     * 状态
     */
    FlowNode node = null;

}
