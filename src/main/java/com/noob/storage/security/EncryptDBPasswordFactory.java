package com.noob.storage.security;

import org.springframework.beans.factory.FactoryBean;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import javax.naming.NamingException;
import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

/**
 * 用于数据库密码加密、解密，跟jboss4.0.5兼容
 * 加解密代码源自于org.jboss.resource.security.SecureIdentityLoginModule
 */
public final class EncryptDBPasswordFactory implements FactoryBean<String>{

    /**
     * 加密密码
     */
    private String password;

    /**
     * 加密
     */
    private String encode(String secret) throws NoSuchPaddingException,
                                               NoSuchAlgorithmException, InvalidKeyException,
                                               BadPaddingException, IllegalBlockSizeException {
        byte[] kbytes = "strong".getBytes();
        SecretKeySpec key = new SecretKeySpec(kbytes, "Blowfish");

        Cipher cipher = Cipher.getInstance("Blowfish");
        cipher.init(Cipher.ENCRYPT_MODE, key);
        byte[] encoding = cipher.doFinal(secret.getBytes());
        BigInteger n = new BigInteger(encoding);
        return n.toString(16);
    }

    /**
     * 解密
     */
    private char[] decode(String secret) throws NoSuchPaddingException, NoSuchAlgorithmException,
                                               InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        byte[] kbytes = "strong".getBytes();
        SecretKeySpec key = new SecretKeySpec(kbytes, "Blowfish");

        BigInteger n = new BigInteger(secret, 16);
        byte[] encoding = n.toByteArray();

        Cipher cipher = Cipher.getInstance("Blowfish");
        cipher.init(Cipher.DECRYPT_MODE, key);
        byte[] decode = cipher.doFinal(encoding);
        return new String(decode).toCharArray();
    }

    public String getObject() throws Exception {
        if (password != null) {
            return String.valueOf(decode(password));
        } else {
            return null;
        }
    }

    public Class<String> getObjectType() {
        return String.class;
    }

    public boolean isSingleton() {
        return true;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
