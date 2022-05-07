package com.smartcampus.utils;

import org.apache.commons.codec.binary.Base64;

import javax.crypto.Cipher;
import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
public class Rsa {
    public static final  String privateKey = "MIICeAIBADANBgkqhkiG9w0BAQEFAASCAmIwggJeAgEAAoGBALobjUsz5nxjhVYA\n" +
            "uQ1GpBElcaLbhNbRBCH24/96Ziw7qidPz1CD0YfdL97HJm/siDlmCXBGEZuIr/R4\n" +
            "7UZ9ztIGhnvSffnoKtwMUTE1tyOtrfsjTH43YCe6vWTL76xReS7lqY8tCDJNtwAA\n" +
            "DIjVEt+LYXfHOf7Qqgu09ltWxdbhAgMBAAECgYEAhD5Zu2tox1DMiV1AIY/lu41Y\n" +
            "3zmPfjnN7viJ8JsgCSRod0C99t+EPz/L6UioJnX0Ip0/7bjMgDGznktjh2iUUfAE\n" +
            "51xq+d6iD6N8BOjuw5avSlzKx/1axZv5rxRdCSwgwGQXQJNt6QYRAybhPAiXvxOF\n" +
            "oxcOkyRzGrn++e149mUCQQDujiF2m9OB0o1jXuMYV3/Y2MlG+jcn3zkWQozhqAj8\n" +
            "eMrZc+5ZwjXuOp+mQ/4A2QFPzADMDvw4FKawdXMRRNWvAkEAx7eTv4TFzJ8AALNT\n" +
            "qs1ma2LDr1Y8nXcTbQAUR6TbXqqCqZ5rBS96TERyS1LUsMrMcTjHPgFKZ/QtOzPX\n" +
            "m2HQbwJBAOdnD6bSOTTxXR1Ladau6eEbSQOErAQNUH77R1WNfkoJkhuljUucTFwu\n" +
            "mbbatYV0+wTAyvUmhBqSTa9V/qx9rRkCQQCY1TsTjJ9xfxuZbaHRS23dL69gNjYc\n" +
            "qksLVswuJ1JYl1N2SezMAxEr3BU63yA0Sn05B6IxKPuvoYQbkgikt62xAkAJHtDi\n" +
            "uk3O6Eg+EFF+M78lzTUlqdwkwft5KV1cHwaUEpbMHdsw4xtxDj0tWS2teDWujcPh\n" +
            "VXkGyrugGofCPQ/+";
    /**
     * RSA最大加密明文大小
     */
    private static final int MAX_ENCRYPT_BLOCK = 117;
    /**
     * RSA最大解密密文大小
     */
    private static final int MAX_DECRYPT_BLOCK = 128;

    private static final String ALGORITHM_NAME = "RSA";
    private static final String MD5_RSA = "MD5withRSA";
    /**
     * 获取公钥
     *
     * @param publicKey base64加密的公钥字符串
     */
    public static PublicKey getPublicKey(String publicKey) throws Exception {
        byte[] decodedKey = Base64.decodeBase64(publicKey.getBytes());
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(decodedKey);
        KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM_NAME);
        return keyFactory.generatePublic(keySpec);
    }
    /**
     * 获取私钥
     *
     * @param privateKey base64加密的私钥字符串
     */
    public static PrivateKey getPrivateKey(String privateKey) throws Exception {
        byte[] decodedKey = Base64.decodeBase64(privateKey.getBytes());
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(decodedKey);
        KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM_NAME);
        return keyFactory.generatePrivate(keySpec);
    }
    /**
     * RSA加密
     *
     * @param data      待加密数据
     * @param publicKey 公钥
     */
    public static String encrypt(String data, PublicKey publicKey) throws Exception {
        Cipher cipher = Cipher.getInstance(ALGORITHM_NAME);
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        int inputLen = data.getBytes().length;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int offset = 0;
        byte[] cache;
        int i = 0;
        // 对数据分段加密
        while (inputLen - offset > 0) {
            if (inputLen - offset > MAX_ENCRYPT_BLOCK) {
                cache = cipher.doFinal(data.getBytes(), offset, MAX_ENCRYPT_BLOCK);
            } else {
                cache = cipher.doFinal(data.getBytes(), offset, inputLen - offset);
            }
            out.write(cache, 0, cache.length);
            i++;
            offset = i * MAX_ENCRYPT_BLOCK;
        }
        byte[] encryptedData = out.toByteArray();
        out.close();
        // 获取加密内容使用base64进行编码,并以UTF-8为标准转化成字符串
        // 加密后的字符串
        return new String(Base64.encodeBase64(encryptedData));
    }

    /**
     * RSA解密
     *
     * @param data       待解密数据
     * @param privateKey 私钥
     */
    public static String decrypt(String data, PrivateKey privateKey) throws Exception {
        Cipher cipher = Cipher.getInstance(ALGORITHM_NAME);
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        byte[] dataBytes = Base64.decodeBase64(data);
        int inputLen = dataBytes.length;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int offset = 0;
        byte[] cache;
        int i = 0;
        // 对数据分段解密
        while (inputLen - offset > 0) {
            if (inputLen - offset > MAX_DECRYPT_BLOCK) {
                cache = cipher.doFinal(dataBytes, offset, MAX_DECRYPT_BLOCK);
            } else {
                cache = cipher.doFinal(dataBytes, offset, inputLen - offset);
            }
            out.write(cache, 0, cache.length);
            i++;
            offset = i * MAX_DECRYPT_BLOCK;
        }
        byte[] decryptedData = out.toByteArray();
        out.close();
        // 解密后的内容
        return new String(decryptedData, StandardCharsets.UTF_8);
    }
}

