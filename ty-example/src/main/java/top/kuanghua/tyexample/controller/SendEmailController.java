package top.kuanghua.tyexample.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import top.kuanghua.khcomomon.entity.ResResult;
import top.kuanghua.tyexample.service.SendEmailService;

import javax.mail.MessagingException;
import java.io.IOException;

/**
 * @Title: SendEmailController
 * @Description:
 * @Auther: kuanghua
 * @create 2020/8/20 22:41
 */
@Api(tags = "发送邮件")
@RestController
@RequestMapping("email")
public class SendEmailController {

    @Autowired
    private SendEmailService sendEmailService;

    @ApiOperation(value = "发送普通文本邮件")
    @GetMapping("sendEmailText")
    public ResResult sendEmailText(@RequestParam(name = "subject",defaultValue = "这是主题") String subject,
                                   @RequestParam(name = "text",defaultValue = "发送的文本(这是测试文本)") String text,
                                   @RequestParam(name = "sendTo",defaultValue = "1319404169@qq.com") String sendTo) {
        sendEmailService.sendEmailText(subject, text, sendTo);
        return new ResResult().success("发送邮件成功");
    }

    @ApiOperation(value = "发送文本邮件(可以带附件)", notes = "可以带附件 ")
    @PostMapping("sendMimeMail")
    public ResResult sendMimeMail(@RequestParam(name = "subject",defaultValue = "这是主题") String subject,
                                  @RequestParam(name = "text",defaultValue = "发送的文本(这是测试文本)") String text,
                                  @RequestParam(name = "sendTo",defaultValue = "1319404169@qq.com") String sendTo,
                                  MultipartFile file)
            throws IOException, MessagingException, InterruptedException {
        if (file.getOriginalFilename().isEmpty()) {
            return new ResResult().error("文件上传空");
        }
        sendEmailService.sendMimeMail(subject, text, sendTo, file);
        return new ResResult().success("发送邮件成功");
    }
}
