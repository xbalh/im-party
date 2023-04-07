package com.im.imparty.common.util;

import cn.hutool.core.io.IoUtil;
import cn.hutool.crypto.CipherMode;
import cn.hutool.crypto.asymmetric.KeyType;
import cn.hutool.crypto.asymmetric.RSA;
import com.im.imparty.spring.util.SpringFactoryUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.ClassPathResource;
import sun.security.rsa.RSAPrivateCrtKeyImpl;
import sun.security.rsa.RSAPublicKeyImpl;

import javax.crypto.Cipher;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.KeyPairGenerator;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.Base64;

import static cn.hutool.crypto.CipherMode.decrypt;

public class RsaUtils {

    // 加密算法
    private final static String ALGORITHM_RSA = "RSA";

    /**
     * 公钥加密
     *
     * @param data
     * @param publicKey
     * @return
     * @throws Exception
     */
    public static String encryptByPublicKey(String data, RSAPublicKey publicKey)
            throws Exception {
        Cipher cipher = Cipher.getInstance(ALGORITHM_RSA);
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        // 模长n转换成字节数
        int modulusSize = publicKey.getModulus().bitLength() / 8;
        // PKCS Padding长度为11字节，所以实际要加密的数据不能要 - 11byte
        int maxSingleSize = modulusSize - 11;
        // 切分字节数组，每段不大于maxSingleSize
        byte[][] dataArray = splitArray(data.getBytes(), maxSingleSize);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        // 分组加密，并将加密后的内容写入输出字节流
        for (byte[] s : dataArray) {
            out.write(cipher.doFinal(s));
        }
        // 使用Base64将字节数组转换String类型
        return Base64.getEncoder().encodeToString(out.toByteArray());
    }

    /**
     * 私钥解密
     *
     * @param data
     * @param privateKey
     * @return
     * @throws Exception
     */
    public static String decryptByPrivateKey(String data, RSAPrivateKey privateKey)
            throws Exception {
        Cipher cipher = Cipher.getInstance(ALGORITHM_RSA);
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        // RSA加密算法的模长 n
        int modulusSize = privateKey.getModulus().bitLength() / 8;
        byte[] dataBytes = data.getBytes();
        // 之前加密的时候做了转码，此处需要使用Base64进行解码
        byte[] decodeData = Base64.getDecoder().decode(dataBytes);
        // 切分字节数组，每段不大于modulusSize
        byte[][] splitArrays = splitArray(decodeData, modulusSize);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        for(byte[] arr : splitArrays){
            out.write(cipher.doFinal(arr));
        }
        return new String(out.toByteArray());
    }

    public static String decrypt(String data) throws Exception {
        ClassPathResource private_key = new ClassPathResource("private_key");
        InputStream inputStream = private_key.getInputStream();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        IoUtil.copy(inputStream, byteArrayOutputStream);
        //String key = new String(byteArrayOutputStream.toByteArray()).replaceAll("\n", "");
        RSAPrivateKey rsaPrivateKey = RSAPrivateCrtKeyImpl.newKey(byteArrayOutputStream.toByteArray());
        return decryptByPrivateKey(data, rsaPrivateKey);
    }

    public static void main(String[] args) throws Exception {

        String str = "1234";
        RSA rsa = new RSA();
        String privateKeyBase64 = rsa.getPrivateKeyBase64();
        String publicKeyBase64 = rsa.getPublicKeyBase64();
        RSA rsa1 = new RSA(privateKeyBase64, publicKeyBase64);
        byte[] encrypt = rsa1.encrypt(str, KeyType.PublicKey);
        System.out.println(encrypt);
        byte[] decrypt = rsa1.decrypt(encrypt, KeyType.PrivateKey);
        System.out.println(decrypt);
        // String data = "HK3GD6YhjUkgDAazhd7NhV7qDSm30Z1DLARn2W3gOZ+WHMWIJUJOesKHwWcXeK02hJfOFPr7PRpe01f7kVIIVQ==";
        // String decrypt = decrypt("HK3GD6YhjUkgDAazhd7NhV7qDSm30Z1DLARn2W3gOZ+WHMWIJUJOesKHwWcXeK02hJfOFPr7PRpe01f7kVIIVQ==");
        System.out.println(new RSA().getPrivateKeyBase64());
    }

    /**
     * 按指定长度切分数组
     *
     * @param data
     * @param len 单个字节数组长度
     * @return
     */
    private static byte[][] splitArray(byte[] data,int len){

        int dataLen = data.length;
        if (dataLen <= len) {
            return new byte[][]{data};
        }
        byte[][] result = new byte[(dataLen-1)/len + 1][];
        int resultLen = result.length;
        for (int i = 0; i < resultLen; i++) {
            if (i == resultLen - 1) {
                int slen = dataLen - len * i;
                byte[] single = new byte[slen];
                System.arraycopy(data, len * i, single, 0, slen);
                result[i] = single;
                break;
            }
            byte[] single = new byte[len];
            System.arraycopy(data, len * i, single, 0, len);
            result[i] = single;
        }
        return result;
    }
}
