package top.kuanghua.tyexample.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import top.kuanghua.khcomomon.entity.ResResult;
import top.kuanghua.tyexample.service.SendMsgService;

/**
 * @Title: SmsController @Description: @Auther:
 *
 * @create 2020/8/20 11:33
 */
@Api(tags = "短信")
@RestController
@RequestMapping("sms")
public class SmsController {

  @Autowired
  private SendMsgService sendMsgService;
  @ApiOperation(value = "发送短信")
  @PostMapping("sendSms")
  public ResResult sendSms(@ApiParam("手机号") @RequestParam("phone") String phone) {
    sendMsgService.sendMsg(phone);
    return new ResResult().success("短信发送成功");
  }
}
