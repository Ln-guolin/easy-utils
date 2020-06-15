package cn.soilove.utils;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

/**
 * AES加解密
 *
 * @author: Chen GuoLin
 * @create: 2020-02-04 10:26
 **/
public class AESUtils {
    private static final String ALG_NAME = "AES";
    // 密钥长度
    private static final int ALG_KEY_SIZE = 128;
    // 字符编码格式
    private static final String CHARSET = "UTF-8";

    /**
     * AES算法全称
     */
    private static final String ALG_FULL_NAME_AES_CBC_PKCS5 = "AES/CBC/PKCS5Padding";
    private static final byte[] AES_IV = initIv(ALG_FULL_NAME_AES_CBC_PKCS5);


    /**
     * 生成实时AES密钥，长度128.
     *
     * @return 长度为128的AES密钥
     */
    public static String generatorKey() {
        try {
            KeyGenerator keyGenerator = KeyGenerator.getInstance(ALG_NAME);
            keyGenerator.init(ALG_KEY_SIZE); // 密钥长度，可选：128，192或256
            return Base64.getEncoder().encodeToString(keyGenerator.generateKey().getEncoded());
        } catch (Exception e) {
            throw new RuntimeException("获取密钥失败" + e.getMessage());
        }
    }

    /**
     * AES加密实现.
     *
     * @param content 待加密内容
     * @param key     密钥, Base64编码后的字符串
     * @return 文本机密结果
     * @throws Exception 加密失败抛出异常
     */
    public static String encrypt(final String content, final String key) {
        try {
            Cipher cipher = Cipher.getInstance(ALG_FULL_NAME_AES_CBC_PKCS5);
            IvParameterSpec iv = new IvParameterSpec(AES_IV);
            cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(Base64.getDecoder().decode(key.getBytes()), ALG_NAME), iv);

            byte[] encBytes = cipher.doFinal(content.getBytes(CHARSET));
            return Base64.getEncoder().encodeToString(encBytes);
        } catch (Exception e) {
            throw new RuntimeException("AES加密失败" + e.getMessage());
        }
    }

    /**
     * AES解密实现.
     *
     * @param content 待解密内容
     * @param key     密钥，Base64编码后的字符串
     * @return 解密后的明文内容
     * @throws Exception 解密失败抛出异常
     */
    public static String decrypt(final String content, final String key) {
        try {
            Cipher cipher = Cipher.getInstance(ALG_FULL_NAME_AES_CBC_PKCS5);
            IvParameterSpec iv = new IvParameterSpec(AES_IV);
            cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(Base64.getDecoder().decode(key.getBytes()), ALG_NAME), iv);

            byte[] cleanBytes = cipher.doFinal(Base64.getDecoder().decode(content.getBytes()));
            return new String(cleanBytes, CHARSET);
        } catch (Exception e) {
            throw new RuntimeException("AES解密失败" + e.getMessage());
        }
    }

    /**
     * 初始向量的方法, 全部为0. 这里的写法适合于其它算法,针对AES算法的话,IV值一定是128位的(16字节).
     *
     * @param algFullName 完整算法
     * @return 初始向量
     */
    private static byte[] initIv(String algFullName) {
        try {
            Cipher cipher = Cipher.getInstance(algFullName);
            int blockSize = cipher.getBlockSize();
            byte[] iv = new byte[blockSize];
            for (int i = 0; i < blockSize; ++i) {
                iv[i] = 0;
            }
            return iv;
        } catch (Exception e) {

            int blockSize = 16;
            byte[] iv = new byte[blockSize];
            for (int i = 0; i < blockSize; ++i) {
                iv[i] = 0;
            }
            return iv;
        }
    }

    public static void main(String[] args) {
        String key = generatorKey();

        System.out.println("密钥="+key);

        String data = "{\"code\":\"123\",\"name\":\"zzzzz\",\"pic\":\"111\"}";

        String encryptStr = AESUtils.encrypt(data,key);

        System.out.println("加密后="+encryptStr);

        String decryptStr = AESUtils.decrypt(encryptStr,key);

        System.out.println("解密后="+decryptStr);
    }
}