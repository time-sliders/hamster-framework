package com.noob.storage.transfer.model;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author LuYun
 * @version app 7.0.0
 * @since 2018.07.06
 */
public class RouteCalcReq {

    /**
     * 权重因子
     */
    private BigDecimal weightFactor;

    /**
     * 转让目标
     */
    private Target target;

    /**
     * 可转让资产列表
     */
    private List<Position> transferablePositions;

    /**
     * 刚好到期的资产列表
     */
    private List<Position> maturePositions;

    public BigDecimal getWeightFactor() {
        return weightFactor;
    }

    public void setWeightFactor(BigDecimal weightFactor) {
        this.weightFactor = weightFactor;
    }

    public Target getTarget() {
        return target;
    }

    public void setTarget(Target target) {
        this.target = target;
    }

    public List<Position> getTransferablePositions() {
        return transferablePositions;
    }

    public void setTransferablePositions(List<Position> transferablePositions) {
        this.transferablePositions = transferablePositions;
    }

    public List<Position> getMaturePositions() {
        return maturePositions;
    }

    public void setMaturePositions(List<Position> maturePositions) {
        this.maturePositions = maturePositions;
    }
}
