package com.noob.storage.business.dao;

import com.noob.storage.business.dao.model.UserInfoDO;
import com.noob.storage.business.model.UserInfoQuery;

import java.util.List;

public interface UserInfoDAO {

    /**
     * 批量查询
     *
     * @param query 查询参数
     */
    List<UserInfoDO> list(UserInfoQuery query);

    /**
     * 查询总量
     *
     * @param query 查询参数
     */
    Integer count(UserInfoQuery query);

    /**
     * 根据ID查询
     *
     * @param id 数据库ID
     */
    UserInfoDO findById(Long id);

    /**
     * 根据id更新
     *
     * @param updateParam 更新参数
     */
    int updateById(UserInfoDO updateParam);

    /**
     * 保存数据
     */
    int insert(UserInfoDO userInfoDO);

}