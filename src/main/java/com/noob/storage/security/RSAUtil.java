package com.noob.storage.security;

import org.apache.commons.codec.binary.Base64;

import javax.crypto.Cipher;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;

/**
 * RSA+Base64加密解密工具类<br>
 * RSA 一般用在数据传输过程中的加密和解密 先用RSA加密后是字节数组  再用BASE64加密 成字符串进行传输<br>
 * 测试  RSA1024产生的公钥字节长度在160-170之间  私钥长度在630-640之间<br>
 * 经过base64加密后长度  公钥字节产度在210-220之间  私钥长度在840-850之间<br>
 * 所以数据库设计时如果存公钥长度设计varchar(256)  私钥长度varchar(1024)<br>
 *
 * <p>非对称加密算法</p>
 */
public class RSAUtil {
    public static final String KEY_ALGORITHM = "RSA";

    private static final String PUBLIC_KEY = "rsa_public_key";
    private static final String PRIVATE_KEY = "rsa_private_key";
    private static final String ENCODING = "UTF-8";

    /**
     * 加密<br>
     * <h2>用公钥加密 <h3><br>
     */
    public static String encryptByPublicKey(String content, String base64PublicKeyStr)
            throws Exception {
        byte[] inputBytes = content.getBytes(ENCODING);
        byte[] outputBytes = encryptByPublicKey(inputBytes, base64PublicKeyStr);
        return Base64.encodeBase64String(outputBytes);
    }

    /**
     * 加密<br>
     * <h2>用私钥加密<h2> <br>
     */
    public static String encryptByPrivateKey(String content, String base64PrivateKeyStr)
            throws Exception {
        byte[] inputBytes = content.getBytes(ENCODING);
        byte[] outputBytes = encryptByPrivateKey(inputBytes, base64PrivateKeyStr);
        return Base64.encodeBase64String(outputBytes);
    }

    /**
     * 解密<br>
     * <h2>用公钥解密<h2> <br>
     */
    public static String decryptByPublicKey(String content, String base64PublicKeyStr)
            throws Exception {
        byte[] inputBytes = Base64.decodeBase64(content);
        byte[] outputBytes = decryptByPublicKey(inputBytes, base64PublicKeyStr);
        return new String(outputBytes, ENCODING);
    }

    /**
     * 解密<br>
     * <h2>用私钥解密<h2> <br>
     */
    public static String decryptByPrivateKey(String content, String base64PrivateKeyStr)
            throws Exception {
        byte[] inputBytes = Base64.decodeBase64(content);
        byte[] outputBytes = decryptByPrivateKey(inputBytes, base64PrivateKeyStr);
        return new String(outputBytes, ENCODING);
    }


    /**
     * 加密<br>
     * <h2>用公钥加密 <h2><br>
     */
    public static byte[] encryptByPublicKey(byte[] content, String base64PublicKeyStr)
            throws Exception {
        // 对公钥解密  
        byte[] publicKeyBytes = Base64.decodeBase64(base64PublicKeyStr);

        // 取得公钥  
        X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(publicKeyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        Key publicKey = keyFactory.generatePublic(x509KeySpec);

        // 对数据加密  
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);

        return cipher.doFinal(content);
    }

    /**
     * 加密<br>
     * <h2>用私钥加密 <h2><br>
     */
    public static byte[] encryptByPrivateKey(byte[] content, String base64PrivateKeyStr)
            throws Exception {
        // 对密钥解密  
        byte[] privateKeyBytes = Base64.decodeBase64(base64PrivateKeyStr);

        // 取得私钥  
        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(privateKeyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        Key privateKey = keyFactory.generatePrivate(pkcs8KeySpec);

        // 对数据加密  
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.ENCRYPT_MODE, privateKey);

        return cipher.doFinal(content);
    }

    /**
     * 解密<br>
     * <h2>用公钥解密 <h2><br>
     */
    public static byte[] decryptByPublicKey(byte[] content, String base64PublicKeyStr)
            throws Exception {
        // 对密钥解密  
        byte[] publicKeyBytes = Base64.decodeBase64(base64PublicKeyStr);

        // 取得公钥  
        X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(publicKeyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        Key publicKey = keyFactory.generatePublic(x509KeySpec);

        // 对数据解密  
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.DECRYPT_MODE, publicKey);

        return cipher.doFinal(content);
    }

    /**
     * 解密<br>
     * <h2>用私钥解密<h2><br>
     */
    public static byte[] decryptByPrivateKey(byte[] content, String base64PrivateKeyStr)
            throws Exception {
        // 对密钥解密  
        byte[] privateKeyBytes = Base64.decodeBase64(base64PrivateKeyStr);

        // 取得私钥  
        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(privateKeyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        Key privateKey = keyFactory.generatePrivate(pkcs8KeySpec);

        // 对数据解密  
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.DECRYPT_MODE, privateKey);

        return cipher.doFinal(content);
    }


    /**
     * 取得私钥 <br>
     */
    public static String getBase64PrivateKeyStr(Map<String, Object> keyMap)
            throws Exception {
        Key key = (Key) keyMap.get(PRIVATE_KEY);
        return Base64.encodeBase64String(key.getEncoded());
    }

    /**
     * 取得公钥 <br>
     */
    public static String getBase64PublicKeyStr(Map<String, Object> keyMap)
            throws Exception {
        Key key = (Key) keyMap.get(PUBLIC_KEY);
        return Base64.encodeBase64String(key.getEncoded());
    }

    /**
     * 初始化密钥
     */
    public static Map<String, Object> initKey() throws Exception {
        KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance(KEY_ALGORITHM);
        keyPairGen.initialize(1024);  //初始化RSA1024安全些

        KeyPair keyPair = keyPairGen.generateKeyPair();
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();      // 公钥

        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();  // 私钥  

        Map<String, Object> keyMap = new HashMap<String, Object>(2);
        keyMap.put(PUBLIC_KEY, publicKey);
        keyMap.put(PRIVATE_KEY, privateKey);
        return keyMap;
    }

    public static void main(String[] args) throws Exception {
        Map<String, Object> keyMap = initKey();
        String base64PublicKeyStr = getBase64PublicKeyStr(keyMap);
        String base64PrivateKeyStr = getBase64PrivateKeyStr(keyMap);
        System.out.println("公钥:" + base64PublicKeyStr);
        System.out.println("私钥:" + base64PrivateKeyStr);

        String inputStr3 = "zhwWasHere";
        String middleStr3 = encryptByPublicKey(inputStr3, base64PublicKeyStr);
        String outputStr3 = decryptByPrivateKey(middleStr3, base64PrivateKeyStr);
        System.out.println("公钥加密前:" + inputStr3);
        System.out.println("公钥加密前:" + middleStr3);
        System.out.println("私钥解密后:" + outputStr3);

        String inputStr4 = "zhwWasHere";
        String middleStr4 = encryptByPrivateKey(inputStr4, base64PrivateKeyStr);
        String outputStr4 = decryptByPublicKey(middleStr4, base64PublicKeyStr);
        System.out.println("私钥加密前:" + inputStr4);
        System.out.println("私钥加密后:" + middleStr4);
        System.out.println("公钥解密后:" + outputStr4);
    }
}  




