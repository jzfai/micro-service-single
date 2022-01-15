package top.kuanghua.tyauth.controller;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;
import top.kuanghua.khcomomon.entity.ResResult;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Map;

/**
 * @Title: TokenController
 * @Description:
 * @Auther: kuanghua
 * @create 2021/1/31 16:31
 */

@RestController
@RequestMapping("feignTest")
@Slf4j
public class TestGetTokenController {
    /*
     * 解析token
     * */
    @PostMapping("getTokenInfo")
    public ResResult getTokenInfo(
            @ApiIgnore @RequestHeader("TOKEN_INFO") String TOKEN_INFO,
            @ApiIgnore @RequestHeader("AUTHORIZE_TOKEN") String AUTHORIZE_TOKEN
    ) {
        Map tokenInfo=null;
        try {
            tokenInfo = JSON.parseObject(URLDecoder.decode(TOKEN_INFO, "utf-8"), Map.class);
            log.info(tokenInfo.toString());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        log.info(AUTHORIZE_TOKEN);
        return new ResResult().success(tokenInfo.toString());
    }
}


