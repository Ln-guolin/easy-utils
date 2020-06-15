package cn.soilove.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import java.io.ByteArrayOutputStream;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * rsa非对称加解密
 *
 * @author: Chen GuoLin
 * @create: 2020-04-17 14:30
 **/
@Slf4j
public class RSAUtils {

    // 加密方式
    public static final String ALGORITHM = "RSA";
    // 签名算法
    private static final String SIGNATURE_ALGORITHM = "SHA1WithRSA";
    // 创建密钥对初始长度
    private static final int KEY_SIZE = 512;
    // 字符编码格式
    private static final String CHARSET = "UTF-8";
    // RSA最大解密密文大小
    private static final int MAX_DECRYPT_BLOCK = 128;

    private static KeyFactory keyFactory;

    static  {
        try {
            keyFactory = KeyFactory.getInstance(ALGORITHM);
        } catch (NoSuchAlgorithmException e) {
           throw new RuntimeException("[rsa]创建KeyFactory实例异常");
        }
    }

    /**
     * RSA key 枚举
     */
    public enum RSAKeyType{
        PUBLIC_KEY,
        PRIVATE_KEY;
    }

    /**
     * 私钥加密
     *
     * @param content    待加密字符串
     * @param privateKey 私钥
     * @return 加密后字符串（BASE64编码）
     */
    public static String encryptByPrivateKey(String content, String privateKey)  {
        try(ByteArrayOutputStream out = new ByteArrayOutputStream()){
            byte[] keyBytes = new Base64().decode(privateKey);
            PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
            PrivateKey pKey = keyFactory.generatePrivate(pkcs8KeySpec);
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, pKey);

            byte[] data = content.getBytes(CHARSET);
            write2Stream(cipher, data, out);
            byte[] resultBytes = out.toByteArray();

            return Base64.encodeBase64String(resultBytes);
        }catch (Exception e){
            throw new RuntimeException("[rsa]私钥加密发生异常"+e.getMessage());
        }
    }

    /**
     * 公钥解密
     *
     * @param content   已加密字符串（BASE64加密）
     * @param publicKey 公钥
     * @return
     */
    public static String decryptByPublicKey(String content, String publicKey)  {
        try(ByteArrayOutputStream out = new ByteArrayOutputStream()){
            byte[] keyBytes = new Base64().decode(publicKey);
            X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(keyBytes);
            PublicKey pKey = keyFactory.generatePublic(x509KeySpec);
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, pKey);

            byte[] data = Base64.decodeBase64(content);
            write2Stream(cipher, data, out);
            byte[] resultBytes = out.toByteArray();

            return new String(resultBytes);
        }catch (Exception e){
            throw new RuntimeException("[rsa]公钥解密发生异常"+e.getMessage());
        }
    }

    /**
     * 公钥加密
     *
     * @param content   待加密字符串
     * @param publicKey 公钥
     * @return 加密后字符串（BASE64编码）
     */
    public static String encryptByPublicKey(String content, String publicKey) {
       try(ByteArrayOutputStream out = new ByteArrayOutputStream()){
           byte[] keyBytes = new Base64().decode(publicKey);
           X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(keyBytes);
           PublicKey pKey = keyFactory.generatePublic(x509KeySpec);
           Cipher cipher = Cipher.getInstance(ALGORITHM);
           cipher.init(Cipher.ENCRYPT_MODE, pKey);

           byte[] data = content.getBytes(CHARSET);
           write2Stream(cipher,
                   data, out);
           byte[] resultBytes = out.toByteArray();
           return Base64.encodeBase64String(resultBytes);
       }catch (Exception e){
           throw new RuntimeException("[rsa]公钥加密发生异常"+e.getMessage());
       }
    }

    /**
     * 私钥解密
     *
     * @param content    已加密字符串
     * @param privateKey 私钥
     * @return 解密后字符串
     */
    public static String decryptByPrivateKey(String content, String privateKey)  {
        try(ByteArrayOutputStream out = new ByteArrayOutputStream()){
            byte[] keyBytes = new Base64().decode(privateKey);
            PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
            PrivateKey pKey = keyFactory.generatePrivate(pkcs8KeySpec);
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, pKey);

            byte[] data = Base64.decodeBase64(content);
            write2Stream(cipher, data, out);
            byte[] resultBytes = out.toByteArray();
            return new String(resultBytes);
        }catch (Exception e){
            throw new RuntimeException("[rsa]私钥解密发生异常"+e.getMessage());
        }
    }


    /**
     * 用私钥对信息生成数字签名
     *
     * @param data       已加密数据
     * @param privateKey 私钥(BASE64编码)
     * @return sign
     */
    public static String sign(String data, String privateKey)  {
        try{
            byte[] keyBytes = new Base64().decode(privateKey);
            PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
            PrivateKey privateK = keyFactory.generatePrivate(pkcs8KeySpec);
            Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
            signature.initSign(privateK);
            signature.update(parse2HexStr(data).getBytes(CHARSET));

            return new Base64().encodeToString(signature.sign());
        }catch (Exception e){
            throw new RuntimeException("[rsa]用私钥对信息生成数字签名发生异常"+e.getMessage());
        }
    }

    /**
     * 校验数字签名
     *
     * @param data      已加密数据
     * @param publicKey 公钥(BASE64编码)
     * @param sign      数字签名
     * @return
     * @
     */
    public static boolean verify(String data, String publicKey, String sign) {
        try{
            byte[] keyBytes = new Base64().decode(publicKey);
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
            PublicKey publicK = keyFactory.generatePublic(keySpec);
            Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
            signature.initVerify(publicK);
            signature.update(parse2HexStr(data).getBytes(CHARSET));
            return signature.verify(new Base64().decode(sign));
        }catch (Exception e){
            throw new RuntimeException("[rsa]校验数字签名验证失败");
        }
    }

    /**
     * 将二进制转换成16进制
     *
     * @param data
     * @return
     */
    private static String parse2HexStr(String data)  {
        try{
            byte[] buf = data.getBytes(CHARSET);

            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < buf.length; i++) {
                String hex = Integer.toHexString(buf[i] & 0xFF);
                if (hex.length() == 1) {
                    hex = '0' + hex;
                }
                sb.append(hex.toUpperCase());
            }
            return sb.toString();
        }catch (Exception e){
            throw new RuntimeException("[rsa]将二进制转换成16进制发生异常"+e.getMessage());
        }
    }

    /**
     * 生成公钥与私钥
     */
    public static Map<RSAKeyType,String> createKey()  {
        try{
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(ALGORITHM);
            keyPairGenerator.initialize(KEY_SIZE);
            KeyPair keyPair = keyPairGenerator.generateKeyPair();
            RSAPublicKey rsaPublicKey = (RSAPublicKey) keyPair.getPublic();
            RSAPrivateKey rsaPrivateKey = (RSAPrivateKey) keyPair.getPrivate();

            String publicKey = Base64.encodeBase64String(rsaPublicKey.getEncoded());
            String privateKey = Base64.encodeBase64String(rsaPrivateKey.getEncoded());

            Map<RSAKeyType,String> res = new HashMap<>();
            res.put(RSAKeyType.PUBLIC_KEY,publicKey);
            res.put(RSAKeyType.PRIVATE_KEY,privateKey);
            return res;
        }catch (Exception e){
            throw new RuntimeException("[rsa]生成公钥与私钥发生异常"+e.getMessage());
        }
    }

    private static void write2Stream(Cipher cipher, byte[] data, ByteArrayOutputStream out) throws
            BadPaddingException, IllegalBlockSizeException {
        int dataLen = data.length;
        int offSet = 0;
        byte[] cache;
        int i = 0;
        // 对数据分段解密
        while (dataLen - offSet > 0) {
            if (dataLen - offSet > MAX_DECRYPT_BLOCK) {
                cache = cipher.doFinal(data, offSet, MAX_DECRYPT_BLOCK);
            } else {
                cache = cipher.doFinal(data, offSet, dataLen - offSet);
            }
            out.write(cache, 0, cache.length);
            i++;
            offSet = i * MAX_DECRYPT_BLOCK;
        }
    }

    public static void main(String[] args)  {

        // 发放公钥与私钥
        Map<RSAKeyType,String> keys = RSAUtils.createKey();
        String publicKey = keys.get(RSAKeyType.PUBLIC_KEY);
        String privateKey = keys.get(RSAKeyType.PRIVATE_KEY);
        String appId = UUID.randomUUID().toString();

        System.out.println("公钥："+publicKey);
        System.out.println("私钥："+privateKey);
        System.out.println("appId："+appId);

        String service = "app.create";
        String content = "{\"code\":\"123\",\"pic\":\"111\",\"obj\":{\"id\":1,\"num\":\"10\"}}";

        // 获取摘要
        String md5Hex = DigestUtils.md5Hex(appId + service + content);

        // 生成签名
        String sign = RSAUtils.sign(md5Hex,privateKey);

        System.out.println("签名如下：" + sign);

        // 验证签名
        boolean res = RSAUtils.verify(md5Hex,publicKey,sign);
        if(!res){
            System.out.println("验签失败！");
        }else{
            System.out.println("验签通过！");
        }

    }
}