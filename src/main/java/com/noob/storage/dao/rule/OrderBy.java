package com.noob.storage.dao.rule;

import com.tongbanjie.commons.dao.enums.OrderByTypeEnum;

/**
 * @author luyun
 * @since 2016.06.07
 */
public class OrderBy {

    private String propertyName;

    private OrderByTypeEnum type = OrderByTypeEnum.ASC;

    public OrderBy(String propertyName) {
        this.propertyName = propertyName;
    }

    public OrderBy(String propertyName, OrderByTypeEnum type) {
        this.propertyName = propertyName;
        this.type = type;
    }

    public String getPropertyName() {
        return propertyName;
    }

    public void setPropertyName(String propertyName) {
        this.propertyName = propertyName;
    }

    public OrderByTypeEnum getType() {
        return type;
    }

    public void setType(OrderByTypeEnum type) {
        this.type = type;
    }
}
