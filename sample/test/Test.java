package test;

/**
 * @Copyright (c) 2002-2012 Travelsky Limited. All rights reserved.
 */

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Arrays;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * @author 刘永健(liuyongjian@travelsky.com)
 * @since 2013/01/08
 */
public class Test {
    /**
     * 加密算法
     */
    private static final String[] CIPHER_ALGORITHM = {"AES", "DES", "TripleDes", "Blowfish"};
//    private static final String[] CIPHER_ALGORITHM = { "AES"};
    private static SecretKey key;

    public static void main(String[] args) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException, InvalidKeySpecException, UnsupportedEncodingException, InvalidAlgorithmParameterException {
        byte[] plainData1 = getPlainData();

        for (String algorithm : CIPHER_ALGORITHM) {
            SecretKey secretKey = generateKey(algorithm);
//            SecretKey secretKey = getKey(algorithm);
            byte[] encryptedData = encryptData(plainData1, secretKey, algorithm);
            byte[] plainData2 = decryptData(encryptedData, secretKey, algorithm);
            System.out.println("加密前后对比：" + Arrays.equals(plainData2, plainData1));
            System.out.println();
        }
    }

    /**
     * 获取明文
     *
     * @return 以字节数据形式返回
     * @throws UnsupportedEncodingException
     */
    private static byte[] getPlainData() throws UnsupportedEncodingException {
        String plainText = "abc123";
        byte[] plainData1 = plainText.getBytes("UTF-8");
        System.out.println("明文：" + Arrays.toString(plainData1));
        return plainData1;
    }

    /**
     * 产生一个密钥
     *
     * @param algorithm
     * @return
     * @throws NoSuchAlgorithmException
     */
    private static SecretKey generateKey(String algorithm) throws NoSuchAlgorithmException {
        SecureRandom sr = new SecureRandom();
        KeyGenerator generator = KeyGenerator.getInstance(algorithm);
        generator.init(sr);
        SecretKey secretKey = generator.generateKey();
        System.out.println("密钥: " + secretKey);
        System.out.println("密钥长度: " + secretKey.getEncoded().length);
        System.out.println("密钥格式: " + secretKey.getFormat());
        System.out.println("密钥生成算法: " + secretKey.getAlgorithm());
        return secretKey;
    }

    private static byte[] generateIV(){
        int ivLen = 16;
        byte[] iv = new byte[ivLen];
        for(byte i = 0; i< ivLen; i++){
            iv[i] = i;
        }
        return iv;
    }
    /**
     * 加密数据
     *
     * @param plainData 明文
     * @param secretKey 密钥
     * @param algorithm 加密/解密算法
     * @return
     * @throws NoSuchPaddingException
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeyException
     * @throws BadPaddingException
     * @throws IllegalBlockSizeException
     */
    private static byte[] encryptData(byte[] plainData, SecretKey secretKey, String algorithm) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException, InvalidAlgorithmParameterException {
        Cipher cipher = Cipher.getInstance(algorithm);
//        IvParameterSpec ivSpec = new IvParameterSpec(generateIV());
        cipher.init(Cipher.ENCRYPT_MODE, secretKey,  new SecureRandom());
        cipher.update(plainData);
        byte[] encryptedData = cipher.doFinal();
        System.out.println("加密结果：" + Arrays.toString(encryptedData));
        return encryptedData;
    }

    /**
     * 解密
     *
     * @param encryptedData 密文
     * @param secretKey     密钥
     * @param algorithm     加密/解密算法
     * @return
     * @throws NoSuchPaddingException
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeyException
     * @throws BadPaddingException
     * @throws IllegalBlockSizeException
     */
    private static byte[] decryptData(byte[] encryptedData, SecretKey secretKey, String algorithm) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException, InvalidAlgorithmParameterException {
        Cipher cipher = Cipher.getInstance(algorithm);
//        IvParameterSpec ivSpec = new IvParameterSpec(generateIV());
        cipher.init(Cipher.DECRYPT_MODE, secretKey ,new SecureRandom());
        cipher.update(encryptedData);
        byte[] plainData2 = cipher.doFinal();
        System.out.println("解密结果：" + Arrays.toString(plainData2));
        return plainData2;
    }

    public static SecretKey getKey( String algorithm) throws NoSuchAlgorithmException, InvalidKeyException, InvalidKeySpecException {
         // 生成一把固定的密钥
        int keySize = 24;
        byte[] key = new byte[keySize];
        for (byte i = 0; i < keySize; i++) {
            key[i] = i;
        }
//        KeySpec keySpec = new SecretKeySpec(key, algorithm);  //  AES密钥格式
//        KeySpec keySpec = new DESKeySpec(key);  //  DES密钥格式
//        KeySpec keySpec = new DESedeKeySpec(key); // 三重DES密钥格式
        KeySpec keySpec = new SecretKeySpec(key,"AES");
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(algorithm);
        SecretKey secretKey = keyFactory.generateSecret(keySpec);
        return secretKey;
    }
}

