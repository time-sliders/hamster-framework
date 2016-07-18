package com.noob.storage.pattern.state;

/**
 * @author luyun
 * @since 1.0
 */
public class TradeRecord {

    //业务类型
    private String businessType;

    //申请状态
    private String applyState;

    //确认状态
    private Integer confirmState;

    public TradeRecord() {
    }

    public TradeRecord(String businessType,String applyState, Integer confirmState) {
        this.businessType = businessType;
        this.applyState = applyState;
        this.confirmState = confirmState;
    }

    public String getBusinessType() {
        return businessType;
    }

    public void setBusinessType(String businessType) {
        this.businessType = businessType;
    }

    public String getApplyState() {
        return applyState;
    }

    public void setApplyState(String applyState) {
        this.applyState = applyState;
    }

    public Integer getConfirmState() {
        return confirmState;
    }

    public void setConfirmState(Integer confirmState) {
        this.confirmState = confirmState;
    }
}
