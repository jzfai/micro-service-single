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

    @ApiOperation(value = "上传图片或文件")
    @PostMapping("getTokenFromService")
    public void getTokenFromService() {
        ResResult tokenInfo = testGetTokenFeign.getTokenInfo();
        log.info(tokenInfo.getData().toString());

    }
}
