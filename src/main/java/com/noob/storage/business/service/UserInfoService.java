package com.noob.storage.business.service;

import com.noob.storage.business.model.UserInfo;
import com.noob.storage.business.model.UserInfoQuery;

import java.util.List;

public interface UserInfoService {

    void noTransaction();

    void transaction();

    /**
     * 批量查询
     *
     * @param query 查询参数
     */
    List<UserInfo> list(UserInfoQuery query);

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
    UserInfo findById(Long id);

    /**
     * 根据id更新一调数据
     *
     * @param updateParam 更新参数
     */
    int updateById(UserInfo updateParam);

    /**
     * 保存数据
     */
    int insert(UserInfo userInfo);
}