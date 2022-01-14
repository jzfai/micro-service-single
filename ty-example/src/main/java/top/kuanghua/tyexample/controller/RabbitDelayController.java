package top.kuanghua.tyexample.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import top.kuanghua.khcomomon.entity.ResResult;
import top.kuanghua.tyexample.service.RabbitDelayService;

import javax.annotation.Resource;

/**
 * @Title: RabbitDelayController
 * @Description:
 * @Auther: kuanghua
 * @create 2022-01-06 17:23
 */
@Api(tags = "延时队列")
@RestController
@RequestMapping("delayQueue")
public class RabbitDelayController {

    @Resource
    private RabbitDelayService rabbitDelayService;

    @ApiOperation(value = "测试延时队列")
    @GetMapping("convertAndSend")
    public ResResult convertAndSend(@ApiParam("延时时间(s)") @RequestParam("time") Integer time) {
        rabbitDelayService.convertAndSend(time);
        return new ResResult().success();
    }
}
