package cn.soilove.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;

/**
 * JWT工具
 *
 * @author: Chen GuoLin
 * @create: 2020-01-29 13:08
 **/
@Slf4j
public class JWTUtils {

    /**
     * 1小时的毫秒定义
     */
    private static final long ONE_HOURS_TIME = 60 * 60 * 1000;

    /**
     * CLAIM_NAME，token包含的名称可用于校验用户
     */
    private static final String CLAIM_NAME = "claimName";

    /**
     * 校验token是否正确
     * @param token 密钥
     * @param claimName 校验名称
     * @param secret 用户的密码
     * @return 是否正确
     */
    public static boolean verify(String token, String claimName, String secret) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            JWTVerifier verifier = JWT.require(algorithm)
                    .withClaim(CLAIM_NAME, claimName)
                    .build();
            verifier.verify(token);
            return true;
        } catch (Exception e) {
            log.error("[token][verify]token验证不通过！token=" + token + "，claimName=" + claimName,e);
            return false;
        }
    }

    /**
     * 获得token中的信息无需secret解密也能获得
     * @return token中包含的ClaimName
     */
    public static String getClaimName(String token) {
        try {
            DecodedJWT jwt = JWT.decode(token);
            return jwt.getClaim(CLAIM_NAME).asString();
        } catch (JWTDecodeException e) {
            log.error("[token][getClaimName]token参数获取异常！token=" + token,e);
            return null;
        }
    }

    /**
     * 生成签名
     * @param claimName 用户名
     * @param secret 用户的密码
     * @param hours 失效小时
     * @return 加密的token
     */
    public static String sign(String claimName, String secret, long hours) {
        Date date = new Date(System.currentTimeMillis() + expireMillisecond(hours));
        Algorithm algorithm = Algorithm.HMAC256(secret);
        // 附带claimName信息
        return JWT.create()
                .withClaim(CLAIM_NAME, claimName)
                .withExpiresAt(date)
                .sign(algorithm);
    }

    /**
     * 获取失效小时
     * @param hours
     * @return
     */
    private static long expireMillisecond(long hours){
        return ONE_HOURS_TIME * hours;
    }

}
