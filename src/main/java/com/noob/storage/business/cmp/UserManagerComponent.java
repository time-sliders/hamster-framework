package com.noob.storage.business.cmp;

import com.noob.storage.business.dao.UserInfoDAO;
import com.noob.storage.business.dao.model.UserInfoDO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import sun.reflect.CallerSensitive;
import sun.reflect.Reflection;

/**
 * 有接口  jdkDynamicAopProxy  No_Transaction
 * 无接口  CglibAopProxy       No_Transaction
 */
@Component
public class UserManagerComponent
//        implements TransactionalInterface
{

    @Autowired
    private UserInfoDAO userInfoDAO;

    public void noTransaction() {
        System.out.println(Reflection.getCallerClass());
        transaction();
    }

    @Transactional(
            propagation = Propagation.REQUIRED,
            isolation = Isolation.READ_COMMITTED,
            rollbackFor = Exception.class)
    public void transaction() {

        UserInfoDO userInfo = new UserInfoDO();
        userInfo.setId(1L);
        userInfo.setName("No_Transaction");

        boolean isUpdateSucc = userInfoDAO.updateById(userInfo) > 0;

        if (isUpdateSucc) {
            throw new RuntimeException("手动回滚事物");
        }
    }
}