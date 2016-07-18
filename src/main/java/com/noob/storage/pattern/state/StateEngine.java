package com.noob.storage.pattern.state;

import com.noob.storage.pattern.state.base.Trade;

/**
 * 状态引擎
 * 状态管理的一个抽象<br/>
 * <p>
 * 系统中可能存在很多交易类型，如：购买，转让，出售等<br/>
 * <p>
 * 每个交易类型存在不同的交易节点：如 申请 -> 确认 -> 成功<br/>
 * <p>
 * 每个交易节点存在不同的状态，如：申请提交成功,申请失败,申请无效<br/>
 *
 * @author luyun
 * @since 1.0
 */
public interface StateEngine {

    /**
     * 初始化交易状态
     *
     * @param args 初始化可能需要一些参数
     */
    Trade initState(Object... args);

}
