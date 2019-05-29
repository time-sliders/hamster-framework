package com.noob.storage.business.service.converter;

import com.noob.storage.business.dao.model.UserInfoDO;
import com.noob.storage.business.model.UserInfo;
import com.noob.storage.common.converter.AbstractObjectConverter;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

@Component
public class UserInfoDOConverter extends AbstractObjectConverter<UserInfo, UserInfoDO> {

    @Override
    protected UserInfoDO onBuildDto(UserInfo model) {
        UserInfoDO domain = new UserInfoDO();
        BeanUtils.copyProperties(model, domain);
        return domain;
    }

    @Override
    protected UserInfo onBuildModel(UserInfoDO domain) {
        UserInfo model = new UserInfo();
        BeanUtils.copyProperties(domain, model);
        return model;
    }
}
