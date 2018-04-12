package com.noob.storage.model.test;

/**
 * @author LuYun
 * @since 2018.04.12
 */
public enum TestEnum {

    E1(1, "E1"),
    E2(2, "E2");

    Integer code;
    String name;

    TestEnum(Integer code, String name) {
        this.code = code;
        this.name = name;
    }

    public Integer getCode() {
        return code;
    }

    public String getName() {
        return name;
    }
}
