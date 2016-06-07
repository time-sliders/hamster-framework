package com.noob.storage.dao.rule;

/**
 * 数据库的查询规则,如 name = 'luYun' , age > 20 等.<br/>
 * 这个类里面定义的方法名参考了Hibernate的规则类Restrictions
 *
 * @author luyun
 * @since 2016.06.07
 */
public class Rule {

    /**
     * 等于 ＝
     */
    public void addEq(String propertyName, Object value) {

    }

    /**
     * 大于 >
     */
    public void addGt(String propertyName, Object value) {

    }

    /**
     * 大于等于 >=
     */
    public void addGe(String propertyName, Object value) {

    }

    /**
     * 小于 <
     */
    public void addLt(String propertyName, Object value) {

    }

    /**
     * 小于等于 <=
     */
    public void addLe(String propertyName, Object value) {

    }

    public void addLike(String propertyName, String regexValue) {

    }

    public void addIn(String propertyName, Object[] valueArray) {

    }

    public void addIsNull(String propertyName) {

    }

    public void addIsNotNull(String propertyName) {

    }

    public void addOrder(OrderBy orderBy){

    }

    public void addLimit(Limit limit){

    }


}
