package com.noob.storage.other.benefit;

import java.util.Date;

/**
 * 每月周期性的投资
 *
 * @author 卢云(luyun)
 * @version hello java
 * @since 2019.07.11
 */
public class MonthlyPeriodicallyInvest {

    /**
     * 开始投资时间
     */
    private Date startInvestTime;

    /**
     * 结束投资时间
     */
    private Date endInvestTime;

    /**
     * 本金退出时间，也就是终止计息时间
     */
    private Date amountOutTime;

    public Date getStartInvestTime() {
        return startInvestTime;
    }

    public void setStartInvestTime(Date startInvestTime) {
        this.startInvestTime = startInvestTime;
    }

    public Date getEndInvestTime() {
        return endInvestTime;
    }

    public void setEndInvestTime(Date endInvestTime) {
        this.endInvestTime = endInvestTime;
    }

    public Date getAmountOutTime() {
        return amountOutTime;
    }

    public void setAmountOutTime(Date amountOutTime) {
        this.amountOutTime = amountOutTime;
    }
}
