package top.kuanghua.tyexample.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.kuanghua.feign.tyauth.feign.TestGetTokenFeign;
import top.kuanghua.khcomomon.entity.ResResult;

import javax.annotation.Resource;

/**
 * @Title: GetTokenFromOtherService
 * @Description:
 * @Auther: kuanghua
 * @create 2022-01-14 17:31
 */
@RestController
@Api(tags = "feign转发请求头测试")
@RequestMapping("testFeign")
@Slf4j
public class GetTokenFromOtherService {
    @Resource
    private TestGetTokenFeign testGetTokenFeign;

    @ApiOperation(value = "获取token信息(需要配置gateway拦截，然后用postman在头部添加token进行测试，token可以从vue3-admin-plus中获取)")
    @PostMapping("getTokenFromService")
    public ResResult getTokenFromService() {
        ResResult tokenInfo = testGetTokenFeign.getTokenInfo();
        log.info(tokenInfo.getData().toString());
        return new ResResult<>().success(tokenInfo);
    }
}
