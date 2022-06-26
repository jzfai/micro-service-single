package top.kuanghua.authpom.service;

import io.jsonwebtoken.Claims;
import org.springframework.stereotype.Service;
import top.kuanghua.authpom.utils.JwtUtilsSelf;

import java.util.Map;

/**
 * @Title: TokenService
 * @Description:
 * @Auther: kuanghua
 * @create 2021/1/31 16:19
 */
@Service
public class TokenService {

    private static final String AUTHORIZE_TOKEN = "AUTHORIZE_TOKEN";

    public String generateToken(Map map) {
        try {
            return JwtUtilsSelf.createJWT(map, null);
        } catch (Exception e) {
            throw new RuntimeException("token生成有误");
        }
    }

    /*
     * 更新token
     * */
    public String updateToken(Map map) {
        try {
            String updateToken = JwtUtilsSelf.createJWT(map, null);
            return updateToken;
        } catch (Exception e) {
            throw new RuntimeException("token更新有误");
        }
    }
    /*
     * 解析token
     * */
    public Claims parseToken(String jwtToken) {
        try {
            Claims claims = JwtUtilsSelf.parseJWT(jwtToken);
            return claims;
        } catch (Exception e) {
            throw new RuntimeException("token解析错误");
        }
    }
}
