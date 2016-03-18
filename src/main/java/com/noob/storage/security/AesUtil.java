package com.noob.storage.security;

import org.apache.commons.codec.binary.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * <p>高级加密标准（英语：Advanced Encryption Standard，缩写：AES），在密码学中又称Rijndael加密法，
 * 是美国联邦政府采用的一种区块加密标准。这个标准用来替代原先的DES，已经被多方分析且广为全世界所使用。
 * 经过五年的甄选流程，高级加密标准由美国国家标准与技术研究院（NIST）于2001年11月26日发布于FIPS PUB
 * 197，并在2002年5月26日成为有效的标准。2006年，高级加密标准已然成为对称密钥加密中最流行的算法之一。</p>
 *
 * <p>对称加密算法，密钥16位</p>
 */
public class AesUtil {

    private static final String keystore = "zhwWas5242501707";

    public static String getKeystore() {
        return keystore;
    }

    private final static String encoding = "UTF-8";

    public static String encryptAES(String content, String password) {
        try {
            SecretKeySpec secretKeySpec = getKey(password);
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            IvParameterSpec iv = new IvParameterSpec(password.getBytes(encoding));
            cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, iv);
            byte[] encrypted = cipher.doFinal(content.getBytes(encoding));

            return new Base64().encodeAsString(encrypted);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String decrypt(String content, String password) {

        try {
            SecretKeySpec secretKeySpec = getKey(password);
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            IvParameterSpec iv = new IvParameterSpec(password.getBytes(encoding));
            cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, iv);
            byte[] encrypted1 = new Base64().decode(content);

            byte[] original = cipher.doFinal(encrypted1);
            return new String(original, encoding);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    private static SecretKeySpec getKey(String strKey) throws Exception {
        byte[] arrBTmp = strKey.getBytes();
        byte[] arrB = new byte[16]; // 创建一个空的16位字节数组（默认值为0）

        for (int i = 0; i < arrBTmp.length && i < arrB.length; i++) {
            arrB[i] = arrBTmp[i];
        }

        return new SecretKeySpec(arrB, "AES");
    }

    public static void main(String[] args) {
        String s = "hello world!";
        System.out.println(s = encryptAES(s, getKeystore()));    //KXUuF9jT0eolN6SitO4kuQ==
        System.out.println(s = decrypt(s, getKeystore()));        //hello world!
    }

}
