package com.noob.storage.business.cmp;

/**
 * @author 卢云(luyun)
 * @version 1.0
 * @since 2019.05.29
 */
public interface TransactionalInterface {

    void noTransaction();

    void transaction();
}
