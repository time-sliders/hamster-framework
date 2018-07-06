package com.noob.storage.transfer.calc;

import com.noob.storage.transfer.model.RouteCalcReq;
import com.noob.storage.transfer.model.RouteCalcResp;

/**
 * @author LuYun
 * @version app 7.0.0
 * @since 2018.07.06
 */
public interface RouteCalculator {

    /**
     * 计算最优转让资产列表
     */
    RouteCalcResp calculate(RouteCalcReq req);

}
