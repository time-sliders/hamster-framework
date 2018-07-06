package com.noob.storage.transfer.model;

import java.math.BigDecimal;

/**
 * @author LuYun
 * @version app 7.0.0
 * @since 2018.07.06
 */
public class Target {

    /**
     * 汇款申请总额
     * 即汇款计划中对应的期望汇款金额
     */
    private BigDecimal redeemApplyAmount;

    /**
     * 最低转让金额
     */
    private BigDecimal minTransferAmount;

    /**
     * 刚好回款的资产总额
     */
    private BigDecimal maturingAmount;


    public BigDecimal getRedeemApplyAmount() {
        return redeemApplyAmount;
    }

    public void setRedeemApplyAmount(BigDecimal redeemApplyAmount) {
        this.redeemApplyAmount = redeemApplyAmount;
    }

    public BigDecimal getMinTransferAmount() {
        return minTransferAmount;
    }

    public void setMinTransferAmount(BigDecimal minTransferAmount) {
        this.minTransferAmount = minTransferAmount;
    }

    public BigDecimal getMaturingAmount() {
        return maturingAmount;
    }

    public void setMaturingAmount(BigDecimal maturingAmount) {
        this.maturingAmount = maturingAmount;
    }
}
