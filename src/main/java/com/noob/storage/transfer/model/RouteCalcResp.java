package com.noob.storage.transfer.model;

/**
 * @author LuYun
 * @version app 7.0.0
 * @since 2018.07.06
 */
public class RouteCalcResp {

    /**
     * 是否成功
     */
    private boolean success;

    /**
     * 异常信息
     */
    private String errorMsg;

    /**
     * 路由结果
     * 即需要转让的资产信息
     */
    private Route route;

    public RouteCalcResp buildFail(String errorMsg) {
        this.success = false;
        this.errorMsg = errorMsg;
        return this;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public Route getRoute() {
        return route;
    }

    public void setRoute(Route route) {
        this.route = route;
    }
}
