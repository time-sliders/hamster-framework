package com.noob.storage.other.benefit;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 原子投资
 * 一笔复杂投资可能会包含多笔原子投资
 *
 * @author 卢云(luyun)
 * @version hello java
 * @since 2019.07.11
 */
public class AtomicInvest {

    /**
     * 本金进入时间
     */
    private Date inDate;

    /**
     * 本金退出时间
     */
    private Date outDate;

    /**
     * 投资金额
     */
    private BigDecimal amount;

    public Date getInDate() {
        return inDate;
    }

    public void setInDate(Date inDate) {
        this.inDate = inDate;
    }

    public Date getOutDate() {
        return outDate;
    }

    public void setOutDate(Date outDate) {
        this.outDate = outDate;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}
