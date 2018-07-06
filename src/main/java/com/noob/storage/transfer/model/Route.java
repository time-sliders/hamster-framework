package com.noob.storage.transfer.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * @author LuYun
 * @version app 7.0.0
 * @since 2018.07.06
 */
public class Route {

    private BigDecimal totalAmount = BigDecimal.ZERO;

    private List<Position> routePositions;

    public static Route buildWithSingle(Position position) {
        return initRouteWithPosition(position, 2);
    }

    public static Route initRouteWithPosition(Position position) {
        return initRouteWithPosition(position, 16);
    }

    private static Route initRouteWithPosition(Position position, int initialCapacity) {
        if (position == null) {
            throw new IllegalArgumentException("position must not be null!");
        }
        Route r = new Route();
        List<Position> routePositions = new ArrayList<>(initialCapacity);
        routePositions.add(position);
        r.routePositions = routePositions;
        r.totalAmount = position.getTotalAmount();
        return r;
    }

    public void addPosition(Position position) {
        if (position == null) {
            throw new IllegalArgumentException("position must not be null!");
        }
        if (routePositions == null) {
            routePositions = new ArrayList<Position>();
        }
        totalAmount = totalAmount.add(position.getTotalAmount());
        routePositions.add(position);
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public List<Position> getRoutePositions() {
        return routePositions;
    }

}
