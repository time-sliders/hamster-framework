package com.noob.storage.http.base;

import org.apache.commons.lang.StringUtils;

/**
 * HTTP交互的参数对象
 */
public class Property {


    private String name;

    private String value;

    /**
     * 无视空值的情况下,toString()方法不会返回值
     */
    private boolean ingnoreNullValue = true;

    public Property() {
    }

    public Property(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public boolean isIngnoreNullValue() {
        return ingnoreNullValue;
    }

    public void setIngnoreNullValue(boolean ingnoreNullValue) {
        this.ingnoreNullValue = ingnoreNullValue;
    }

    @Override
    public String toString() {
        if ((ingnoreNullValue && StringUtils.isNotBlank(name) && StringUtils.isNotBlank(value))
                || !ingnoreNullValue) {
            return name + "=" + value + "&";
        } else {
            return "";
        }
    }

}
