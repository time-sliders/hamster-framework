package com.noob.storage.transfer.model;

import java.math.BigDecimal;

/**
 *
 * @author LuYun
 * @version app 7.0.0
 * @since 2018.07.06
 */
public class Position {

    /**
     * 资产ID
     */
    private Long id;

    /**
     * 资产价格 ＝ 持仓份额 + 未付收益
     */
    private BigDecimal totalAmount;

    /**
     * 持仓份额
     */
    private BigDecimal amount;

    /**
     * 未付收益
     */
    private BigDecimal earning;

    /**
     * 年化收益率
     */
    private BigDecimal profit;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Position() {
    }

    public Position(int totalAmount) {
        this.totalAmount = new BigDecimal(totalAmount);
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getEarning() {
        return earning;
    }

    public void setEarning(BigDecimal earning) {
        this.earning = earning;
    }

    public BigDecimal getProfit() {
        return profit;
    }

    public void setProfit(BigDecimal profit) {
        this.profit = profit;
    }
}
