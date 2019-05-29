package com.noob.storage.business.service.impl;

import com.noob.storage.business.dao.UserInfoDAO;
import com.noob.storage.business.dao.model.UserInfoDO;
import com.noob.storage.business.model.UserInfo;
import com.noob.storage.business.model.UserInfoQuery;
import com.noob.storage.business.service.UserInfoService;
import com.noob.storage.business.service.converter.UserInfoDOConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserInfoServiceImpl implements UserInfoService {

    @Autowired
    private UserInfoDAO userInfoDAO;

    @Autowired
    private UserInfoDOConverter converter;

    @Override
    public void noTransaction() {
        transaction();
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED, rollbackFor = Throwable.class)
    public void transaction() {

        UserInfoDO userInfo = new UserInfoDO();
        userInfo.setId(1L);
        userInfo.setName("Use_Interface_Changed");

        boolean isUpdateSucc = userInfoDAO.updateById(userInfo) > 0;

        if (isUpdateSucc) {
            throw new RuntimeException("手动回滚事物");
        }
    }

    @Override
    public List<UserInfo> list(UserInfoQuery query) {
        return converter.asModelList(userInfoDAO.list(query));
    }

    @Override
    public Integer count(UserInfoQuery query) {
        return userInfoDAO.count(query);
    }

    @Override
    public UserInfo findById(Long id) {
        return converter.toModel(userInfoDAO.findById(id));
    }

    @Override
    public int updateById(UserInfo updateParam) {
        return userInfoDAO.updateById(converter.toDto(updateParam));
    }

    @Override
    public int insert(UserInfo userInfo) {
        return userInfoDAO.insert(converter.toDto(userInfo));
    }


}