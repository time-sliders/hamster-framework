package com.noob.storage.security;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.Map;

/**
 * 加密工具
 */
public class EncryptUtil {

    private final static Logger logger = LoggerFactory.getLogger(EncryptUtil.class);

    public <T> T encrypt(T bean, String[] attributes) {
        for (String attribute : attributes) {
            encrypt(bean, attribute);
        }
        return bean;
    }

    public <T> T encrypt(T bean, String attribute) {
        if (attribute == null) {
            return bean;
        }
        if (bean instanceof Collection) {
            Collection collection = (Collection) bean;
            if (CollectionUtils.isEmpty(collection)) {
                return bean;
            }
            for (Object val : collection) {
                encryptSingle(val, attribute);
            }
        } else if (bean instanceof Map) {
            Map<?, ?> map = (Map<?, ?>) bean;
            if (MapUtils.isEmpty(map)) {
                return bean;
            }
            for (Map.Entry<?, ?> entry : map.entrySet()) {
                encryptSingle(entry.getValue(), attribute);
            }
        } else {
            encryptSingle(bean, attribute);
        }
        return bean;
    }

    private void encryptSingle(Object value, String attribute) {
        try {
            Object sourceData = BeanUtils.getProperty(value, attribute);
            if (sourceData == null) {
                return;
            }
            BeanUtils.setProperty(value, attribute, AesUtil.encryptAES((String) sourceData, AesUtil.getKeystore()));
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }

    public <T> T decrypt(T bean, String[] attributes) {
        for (String attribute : attributes) {
            decrypt(bean, attribute);
        }
        return bean;
    }


    public <T> T decrypt(T bean, String attribute) {
        if (bean == null) {
            return null;
        }
        if (bean instanceof Collection) {
            Collection collection = (Collection) bean;
            if (CollectionUtils.isEmpty(collection)) {
                return bean;
            }
            for (Object val : collection) {
                decryptSingle(val, attribute);
            }
        } else if (bean instanceof Map) {
            Map<?, ?> map = (Map<?, ?>) bean;
            if (MapUtils.isEmpty(map)) {
                return bean;
            }
            for (Map.Entry<?, ?> entry : map.entrySet()) {
                decryptSingle(entry.getValue(), attribute);
            }
        } else {
            decryptSingle(bean, attribute);
        }
        return bean;
    }

    private <T> T decryptSingle(T bean, String attribute) {
        if (attribute == null) {
            return bean;
        }
        try {
            Object sourceData = BeanUtils.getProperty(bean, attribute);
            if (sourceData == null) {
                return bean;
            }
            BeanUtils.setProperty(bean, attribute, AesUtil.decrypt((String) sourceData, AesUtil.getKeystore()));
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return bean;
    }
}
