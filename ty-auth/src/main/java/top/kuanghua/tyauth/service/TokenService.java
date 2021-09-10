package top.kuanghua.tyauth.service;

import io.jsonwebtoken.Claims;
import org.springframework.stereotype.Service;
import top.kuanghua.tyauth.utils.JwtUtilsSelf;

import java.util.Map;

/**
 * @Title: TokenService
 * @Description:
 * @Auther: kuanghua
 * @create 2021/1/31 16:19
 */
@Service
public class TokenService {

    private static  final String AUTHORIZE_TOKEN="AUTHORIZE_TOKEN";
//    @Value("#{'${filter.allowPaths:}'.empty ? null : '${filter.allowPaths:}'.split(',')}")
//    private List<String> allowPaths;
//    @Value("${jwt-properties.pubKeyPath}")
//    private String pubKeyPath;
//    @Value("${jwt-properties.priKeyPath}")
//    private String priKeyPath;


//    @PostConstruct
//    public void postConstruct(){
//        try {
//            String classPath = ResourceUtils.getURL("classpath:").getPath();
//            this.pubKeyPath=classPath+"";
//            this.priKeyPath=classPath+"";
//        } catch (Exception e) {
//            throw new RuntimeException();
//        }
//    }

    /*
    * 生成token
    * */
    public String generateToken(Map map){
        try {
            return  JwtUtilsSelf.createJWT(map,null);
        } catch (Exception e) {
            throw new RuntimeException("token生成有误");
        }
    }
    /*
    * 更新token
    * */
    public String updateToken(Map map){
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
    public Claims parseToken(String jwtToken){
        try {
            Claims claims = JwtUtilsSelf.parseJWT(jwtToken);
            return claims;
        } catch (Exception e) {
            throw new RuntimeException("token解析错误");
        }
    }
}
