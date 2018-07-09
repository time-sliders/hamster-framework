package com.noob.storage.transfer.calc.impl;

import com.noob.storage.transfer.calc.RouteCalculator;
import com.noob.storage.transfer.model.*;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.util.StopWatch;

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
        test2(20);
        i = 0;
        test2(30);
        i = 0;
        test2(40);
    }

    private static void test1() {
        ExhaustionRouteCalculator calculator = new ExhaustionRouteCalculator();
        RouteCalcReq req = new RouteCalcReq();
        Target target = new Target();
        target.setRedeemApplyAmount(new BigDecimal(100));
        target.setMaturingAmount(new BigDecimal(0));
        target.setMinTransferAmount(new BigDecimal(100));
        req.setTarget(target);

        List<Position> positions = new ArrayList<>(7 * 4 / 3 + 1);
        positions.add(new Position(120));
        positions.add(new Position(70));
        positions.add(new Position(60));
        positions.add(new Position(40));
        positions.add(new Position(30));
        positions.add(new Position(8));
        positions.add(new Position(7));
        req.setTransferablePositions(positions);

        StopWatch sw = new StopWatch();
        sw.start();
        RouteCalcResp resp = calculator.calculate(req);
        for (Position p : resp.getRoute().getRoutePositions()) {
            System.out.println("Result position: " + p.getTotalAmount());
        }
        sw.stop();
        System.out.println("CostTimeMills: " + sw.getTotalTimeMillis() + "ms");
    }

    /**
     * o(n) = 2^n - 1
     * 复杂度太高，不可取
     */
    private static void test2(int size) {
        ExhaustionRouteCalculator calculator = new ExhaustionRouteCalculator();
        RouteCalcReq req = new RouteCalcReq();
        Target target = new Target();
        target.setRedeemApplyAmount(new BigDecimal(size));
        target.setMaturingAmount(new BigDecimal(0));
        target.setMinTransferAmount(new BigDecimal(size));
        req.setTarget(target);

        List<Position> positions = new ArrayList<>(7 * 4 / 3 + 1);
        for (int i = 0; i < size; i++) {
            positions.add(new Position(i));
        }
        req.setTransferablePositions(positions);

        StopWatch sw = new StopWatch();
        sw.start();
        RouteCalcResp resp = calculator.calculate(req);
        for (Position p : resp.getRoute().getRoutePositions()) {
            System.out.println("Result position: " + p.getTotalAmount());
        }
        sw.stop();
        System.out.println("CostTimeMills: " + sw.getTotalTimeMillis() + "ms");
    }


    /**
     * 最大递归长度 3576
     */
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

    private static int i = 0;

    private List<Route> getAvailableRouteWithFirstElement(
            List<Position> transferablePositions, BigDecimal matchedAmount,
            BigDecimal redeemApplyAmount) {

        System.out.println(i++);
        if (CollectionUtils.isEmpty(transferablePositions)) {
            return null;
        }

        List<Route> routes = new ArrayList<Route>();
        Position firstPosition = transferablePositions.get(0);
        BigDecimal needMatchAmount = redeemApplyAmount.subtract(matchedAmount);
        if (firstPosition.getTotalAmount().compareTo(needMatchAmount) >= 0) {
            routes.add(Route.buildWithSingle(firstPosition));
        } else {
            if (transferablePositions.size() == 1) {
                return null;
            }
            List<Route> subRoutes = getAvailableRouteWithFirstElement(
                    transferablePositions.subList(1, transferablePositions.size()),
                    matchedAmount.add(firstPosition.getTotalAmount()), redeemApplyAmount);
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
                matchedAmount, redeemApplyAmount);
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
            return p2.getTotalAmount().compareTo(p1.getTotalAmount());
        }
    }
}
