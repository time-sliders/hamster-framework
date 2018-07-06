package com.noob.storage.transfer.calc.impl;

import com.noob.storage.transfer.calc.RouteCalculator;
import com.noob.storage.transfer.model.*;
import org.apache.commons.collections.CollectionUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * ［穷举法］
 *
 * @author LuYun
 * @version app 7.0.0
 * @since 2018.07.06
 */
public class ExhaustionRouteCalculator implements RouteCalculator {

    private Comparator<Position> positionComparator = new PositionComparator();

    public static void main(String[] args) {

        ExhaustionRouteCalculator calculator = new ExhaustionRouteCalculator();
        RouteCalcReq req = new RouteCalcReq();
        Target target = new Target();
        target.setRedeemApplyAmount(new BigDecimal(10000));
        target.setMaturingAmount(new BigDecimal(1000));
        target.setMinTransferAmount(new BigDecimal(9000));
        req.setTarget(target);

        List<Position> positions = new ArrayList<>(10000 * 4 / 3 + 1);
        for (int i = 0; i < 10000; i++) {
            Position position = new Position();

            //position.setTotalAmount();
        }


    }


    @Override
    public RouteCalcResp calculate(RouteCalcReq req) {

        RouteCalcResp resp = new RouteCalcResp();

        /*
         * 请求数据验证
         */
        Target target = req.getTarget();
        List<Position> transferablePositions = req.getTransferablePositions();
        transferablePositions.sort(positionComparator);
        BigDecimal matchedAmount = target.getMaturingAmount();
        BigDecimal redeemApplyAmount = target.getRedeemApplyAmount();

        /*
         * 穷举所有解
         */
        List<Route> routes = getAvailableRouteWithFirstElement(
                transferablePositions, matchedAmount, redeemApplyAmount);
        if (CollectionUtils.isEmpty(routes)) {
            return resp.buildFail("无解");
        }

        /*
         * 权重计算最优解
         */
        Route optimumRoute = getOptimumRoute(routes, req);
        /*
         * 返回结果
         */
        resp.setSuccess(true);
        resp.setRoute(optimumRoute);
        return resp;
    }

    private List<Route> getAvailableRouteWithFirstElement(
            List<Position> transferablePositions, BigDecimal matchedAmount,
            BigDecimal redeemApplyAmount) {

        if (CollectionUtils.isEmpty(transferablePositions)) {
            return null;
        }

        List<Route> routes = new ArrayList<Route>();
        Position firstPosition = transferablePositions.get(0);
        BigDecimal needMatchAmount = redeemApplyAmount.subtract(matchedAmount);
        if (firstPosition.getTotalAmount().compareTo(needMatchAmount) >= 0) {
            routes.add(Route.buildWithSingle(firstPosition));
        } else {
            needMatchAmount = needMatchAmount.subtract(firstPosition.getTotalAmount());
            List<Route> subRoutes = getAvailableRouteWithFirstElement(
                    transferablePositions.subList(1, transferablePositions.size()),
                    needMatchAmount, redeemApplyAmount);
            if (subRoutes == null) {
                return null;
            }
            for (Route route : subRoutes) {
                route.addPosition(firstPosition);
                routes.add(route);
            }
        }

        List<Route> nextRoutes = getAvailableRouteWithFirstElement(
                transferablePositions.subList(1, transferablePositions.size()),
                needMatchAmount, redeemApplyAmount);
        if (CollectionUtils.isNotEmpty(nextRoutes)) {
            routes.addAll(nextRoutes);
        }

        return routes;
    }

    private Route getOptimumRoute(List<Route> routes, RouteCalcReq req) {
        BigDecimal cv = null;
        Route optimumRoute = null;
        for (Route route : routes) {
            BigDecimal rv = route.getTotalAmount()
                    .subtract(req.getTarget().getMinTransferAmount());
            if (cv == null || rv.compareTo(cv) < 0) {
                cv = rv;
                optimumRoute = route;
            }
        }
        return optimumRoute;
    }


    class PositionComparator implements Comparator<Position> {
        @Override
        public int compare(Position p1, Position p2) {
            return p1.getTotalAmount().compareTo(p2.getTotalAmount());
        }
    }
}
