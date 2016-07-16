package com.noob.storage.pattern.state;

/**
 * 类型<br/>
 * <p>
 * 系统中可能存在很多交易类型，如：购买，转让，出售等<br/>
 *
 * @author luyun
 * @since 1.0
 */
public abstract class AbstractBusinessType {

    //类型名称
    private String name;

    //类型代码
    private String code;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
