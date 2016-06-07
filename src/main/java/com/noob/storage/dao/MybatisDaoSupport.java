package com.noob.storage.dao;

import com.tongbanjie.commons.dao.rule.Rule;
import org.mybatis.spring.support.SqlSessionDaoSupport;

import java.util.List;

/**
 * 所有DAO层继承此类进行功能增强<br/>
 * <p/>
 * 范型:<br/>
 * <li>E - 当前DAO对应的实体类 如:Product</li>
 * <li>P - 实体类在数据库中主键类型 如:Integer</li>
 *
 * @author luyun
 * @since 2016.06.07
 */
public class MybatisDaoSupport<E/*Entity Class*/, P/*PK Type*/> extends SqlSessionDaoSupport {

    public List<E> findListByRule(List<Rule> ruleList) {
        return null;
    }

    public E findByRule(List<Rule> ruleList) {
        return null;
    }
}
