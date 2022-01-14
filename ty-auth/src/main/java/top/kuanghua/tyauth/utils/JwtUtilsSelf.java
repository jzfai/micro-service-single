package top.kuanghua.tyauth.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;
import java.util.Date;
import java.util.Map;

/**
 * 描述
 *
 * @author kuanghua
 * @version 1.0
 * @package entity *
 * @since 1.0
 */
public class JwtUtilsSelf {

    //https://www.cnblogs.com/passedbylove/p/11207827.html
    //有效期为
    public static final Long JWT_TTL = 259200000L;//60 * 60 * 1000 * 24 * 3  三天
    //Jwt令牌信息
    public static final String JWT_KEY = "kh@Login(Auth}*^31)&kuanghua%";

    /**
     * 生成令牌
     * //* @param id
     * //* @param subject
     *
     * @param ttlMillis
     * @return
     */
    public static String createJWT(Map userInfo, Long ttlMillis) {
        //指定算法
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
        //当前系统时间
        long nowMillis = System.currentTimeMillis();
        //令牌签发时间
        Date now = new Date(nowMillis);
        //如果令牌有效期为null，则默认设置有效期1小时
        if (ttlMillis == null) {
            ttlMillis = JwtUtilsSelf.JWT_TTL;
        }
        //令牌过期时间设置
        long expMillis = nowMillis + ttlMillis;
        Date expDate = new Date(expMillis);

        //生成秘钥
        SecretKey secretKey = generalKey();

        //封装Jwt令牌信息
        JwtBuilder builder = Jwts.builder()
                //.setId(id)
                //.setSubject(subject)          // 主题  可以是JSON数据
                //.setIssuer("admin")          // 签发者
                .setIssuedAt(now)             // 签发时间
                .signWith(signatureAlgorithm, secretKey) // 签名算法以及密匙
                .setExpiration(expDate);      // 设置过期时间

        JwtBuilder jwtBuilder = builder.addClaims(userInfo);
        return builder.compact();
    }

    /**
     * 生成加密 secretKey
     *
     * @return
     */
    public static SecretKey generalKey() {
        byte[] encodedKey = Base64.getEncoder().encode(JwtUtilsSelf.JWT_KEY.getBytes());
        SecretKey key = new SecretKeySpec(encodedKey, 0, encodedKey.length, "AES");
        return key;
    }

    /**
     * 解析令牌数据
     *
     * @param jwt
     * @return
     * @throws Exception
     */
    public static Claims parseJWT(String jwt) throws Exception {
        SecretKey secretKey = generalKey();
        return Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(jwt)
                .getBody();
    }
}
