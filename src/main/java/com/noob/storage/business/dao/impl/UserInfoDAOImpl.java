package com.noob.storage.business.dao.impl;

import com.noob.storage.business.dao.UserInfoDAO;
import com.noob.storage.business.dao.model.UserInfoDO;
import com.noob.storage.business.model.UserInfoQuery;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import java.util.Date;
import java.util.List;

@Repository
public class UserInfoDAOImpl extends SqlSessionDaoSupport implements UserInfoDAO {

    @Override
    public List<UserInfoDO> list(UserInfoQuery query) {

        Assert.notNull(query, "查询参数不能为空");

        query.decorate();

        //noinspection unchecked
        return this.getSqlSession().selectList("UserInfo.QUERY", query);
    }

    @Override
    public Integer count(UserInfoQuery query) {

        Assert.notNull(query, "查询参数不能为空");

        return (Integer) this.getSqlSession().selectOne("UserInfo.COUNT", query);
    }

    @Override
    public UserInfoDO findById(Long id) {

        Assert.notNull(id, "id不能为空");

        return (UserInfoDO) this.getSqlSession().selectOne("UserInfo.FIND_BY_ID", id);
    }

    @Override
    public int updateById(UserInfoDO updateParam) {

        Assert.notNull(updateParam, "更新参数不能为空");
        Assert.notNull(updateParam.getId(), "id不能为空");

        updateParam.setGmtCreate(null);
        updateParam.setGmtModified(new Date());

        return this.getSqlSession().update("UserInfo.UPDATE_BY_ID", updateParam);
    }

    @Override
    public int insert(UserInfoDO userInfoDO) {

        checkParamForInsert(userInfoDO);

        userInfoDO.setGmtCreate(new Date());
        userInfoDO.setGmtModified(null);

        return this.getSqlSession().insert("UserInfo.INSERT", userInfoDO);
    }

    private void checkParamForInsert(UserInfoDO userInfoDO) {

        Assert.notNull(userInfoDO, "userInfoDO 参数不能为空");
        Assert.notNull(userInfoDO.getName(), "Name不能为空");
        Assert.notNull(userInfoDO.getSeqNo(), "SeqNo不能为空");

    }


}