package top.kuanghua.tyexample.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.kuanghua.khcomomon.entity.ResResult;

/**
 * @Title: HystrixRibbonController
 * @Description:
 * @Auther: kuanghua
 * @create 2022-01-08 17:20
 */
@Api(tags = "HystrixRibbon和限流测试")
@RestController
@RequestMapping("hystrixRibbon")
public class HystrixRibbonController {
    @ApiOperation(value = "测试Hystrix熔断和Ribbon")
    @GetMapping("timeoutTest")
    public ResResult timeoutTest() {
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return new ResResult<>().success();

    }
}
