package com.noob.storage.business.cmp;

import com.noob.storage.business.dao.UserInfoDAO;
import com.noob.storage.business.dao.model.UserInfoDO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author luyun
 * @version 1.0
 * @since 2017.12.30
 */
@Component
public class UserManagerComponent {

    @Autowired
    private UserInfoDAO userInfoDAO;

    public void noTransaction() {
        transaction();
    }

    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED, rollbackFor = Throwable.class)
    public void transaction() {

        UserInfoDO userInfo = new UserInfoDO();
        userInfo.setId(1L);
        userInfo.setName("No_Interface_Changed");

        boolean isUpdateSucc = userInfoDAO.updateById(userInfo) > 0;

        if (isUpdateSucc) {
            throw new RuntimeException("手动回滚事物");
        }
    }

}
