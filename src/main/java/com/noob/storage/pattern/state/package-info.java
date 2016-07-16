/**
 * 状态管理的一个抽象<br/>
 *
 * 系统中可能存在很多交易类型，如：购买，转让，出售等<br/>
 *
 * 每个交易类型存在不同的交易节点：如 申请 -> 确认 -> 成功<br/>
 *
 * 每个交易节点存在不同的状态，如：申请提交成功,申请失败,申请无效<br/>
 *
 * @author luyun
 * @since 1.0
 */
package com.noob.storage.pattern.state;