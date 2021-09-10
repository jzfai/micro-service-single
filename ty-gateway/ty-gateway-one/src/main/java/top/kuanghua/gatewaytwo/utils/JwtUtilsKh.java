package top.kuanghua.gatewaytwo.utils;

import io.jsonwebtoken.*;
import org.joda.time.DateTime;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Map;

/**
 * @Title: JwtUtilsKh
 * @Description:
 * @Auther: kuanghua
 * @create 2020/9/11 12:08
 */

public class JwtUtilsKh {
    public static String generateToken(Map hashMap, PrivateKey privateKey, int expireMinutes) throws Exception {
        JwtBuilder jwtBuilder = Jwts.builder()
                .setExpiration(DateTime.now().plusMinutes(expireMinutes).toDate())
                .signWith(SignatureAlgorithm.RS256, privateKey);
        jwtBuilder.addClaims(hashMap);
        //将jwt返回
        return jwtBuilder.compact();
    }
    public static String updateToken(Map hashMap, PrivateKey privateKey, int expireMinutes) throws Exception {
        hashMap.remove("exp");
        JwtBuilder jwtBuilder = Jwts.builder()
                .setExpiration(DateTime.now().plusMinutes(expireMinutes).toDate())
                .signWith(SignatureAlgorithm.RS256, privateKey);
        jwtBuilder.addClaims(hashMap);
        //将jwt返回
        return jwtBuilder.compact();
    }
    public static Jws<Claims> parserToken(String token, PublicKey publicKey){
        return Jwts.parser().setSigningKey(publicKey).parseClaimsJws(token);
    }
    /*获取jwt的载荷*/
    public static Claims parserTokenGetBody(String token, PublicKey publicKey){
        return Jwts.parser().setSigningKey(publicKey).parseClaimsJws(token).getBody();
    }
}
