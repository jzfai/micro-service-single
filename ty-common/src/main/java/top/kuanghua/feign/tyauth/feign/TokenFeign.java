package top.kuanghua.feign.tyauth.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import top.kuanghua.feign.config.FeignConfiguration;
import top.kuanghua.khcomomon.entity.ResResult;

import java.util.Map;

/**
 * @Title: TokenController
 * @Description:
 * @Auther: kuanghua
 * @create 2021/1/31 16:31
 */


//FeignConfiguration.class -> forward req params of include the token
@FeignClient(name="ty-auth",configuration = FeignConfiguration.class)
@RequestMapping("token")
public interface TokenFeign {

    /*
     *  parse token
     * */
    @PostMapping("parseToken")
    ResResult parseToken(@RequestParam("jwtToken") String jwtToken);

    /*
     * create  token
     * */
    @PostMapping("generateToken")
    ResResult generateToken(@RequestBody Map map);

    /*
     * update token
     * */
    @PostMapping("updateToken")
    ResResult updateToken(@RequestBody Map map);
}


