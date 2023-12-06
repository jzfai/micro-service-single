## springboot集成email

这边以163邮箱为例，qq邮箱只需要跟换下配置就行



### maven

```xml
<!--邮件-->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-mail</artifactId>
</dependency>
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-thymeleaf</artifactId>
</dependency>
```



yml配置文件

```yml
spring:
  mail:
    host: smtp.163.com
    username: 13302254692@163.com
    password: WWVICETDVBSHMPXJ
    port: 25
```

如果是qq邮箱换成下面配置

```yml
spring:
  mail:
    host: smtp.qq.com
    username: 869653722@qq.com
    password: kljddnvedyuxbcfd
    port: 587
```





MailUtils 

```java
package top.hugo.ms.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;

/**
 * Description
 * Author  流星
 * Date 2022/1/13 22:06
 **/
@Service
public class MailUtils {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Value("${spring.mail.username}")
    private String from;

    @Resource
    private JavaMailSender mailSender;

    /**
     * 简单文本邮件
     *
     * @param to      接收者邮件
     * @param subject 邮件主题
     * @param content 邮件内容
     */
    public void sendSimpleMail(String to, String subject, String content) {

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(content);
        message.setFrom(from);

        mailSender.send(message);
    }

    /**
     * HTML 文本邮件
     *
     * @param to      接收者邮件
     * @param subject 邮件主题
     * @param content HTML内容
     * @throws MessagingException
     */
    public void sendHtmlMail(String to, String subject, String content) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();

        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(content, true);
        helper.setFrom(from);

        mailSender.send(message);
    }

    /**
     * 附件邮件
     *
     * @param to       接收者邮件
     * @param subject  邮件主题
     * @param content  HTML内容
     * @param filePath 附件路径
     * @throws MessagingException
     */
    public void sendAttachmentsMail(String to, String subject, String content,
                                    String filePath) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();

        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(content, true);
        helper.setFrom(from);

        FileSystemResource file = new FileSystemResource(new File(filePath));
        String fileName = file.getFilename();
        helper.addAttachment(fileName, file);

        mailSender.send(message);
    }

    /**
     * 图片邮件
     *
     * @param to      接收者邮件
     * @param subject 邮件主题
     * @param content HTML内容
     * @param rscPath 图片路径
     * @param rscId   图片ID
     * @throws MessagingException
     */
    public void sendInlinkResourceMail(String to, String subject, String content,
                                       String rscPath, String rscId) {
        logger.info("发送静态邮件开始: {},{},{},{},{}", to, subject, content, rscPath, rscId);

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = null;

        try {

            helper = new MimeMessageHelper(message, true);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(content, true);
            helper.setFrom(from);

            FileSystemResource res = new FileSystemResource(new File(rscPath));
            helper.addInline(rscId, res);
            mailSender.send(message);
            logger.info("发送静态邮件成功!");

        } catch (MessagingException e) {
            logger.info("发送静态邮件失败: ", e);
        }
    }
}
```



MailTestController测试类

```java
package top.hugo.admin.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import top.hugo.ms.utils.MailUtils;

import javax.annotation.Resource;
import javax.mail.MessagingException;

/**
 * 邮件测试
 *
 * @author 邝华
 * @date 2023-10-10 11:39
 **/
@RestController
@RequestMapping
public class MailTestController {

    @Autowired
    private MailUtils mailUtil;

    @Resource
    private TemplateEngine templateEngine;

    /**
     * 测试一个简单的、由自己撰写邮件标题和内容的邮件
     **/
    @GetMapping("mail01")
    public String sendSimpleMail() {
        mailUtil.sendSimpleMail("869653722@qq.com", "测试spring boot mail-主题", "测试spring boot mail - 内容");
        return "发送成功";
    }

    /**
     * 测试一个简单的html邮件
     **/
    @GetMapping("mail02")
    public String sendHtmlMail() throws MessagingException {

        String content = "<html>\n" +
                "<body>\n" +
                "<h3>hello world</h3>\n" +
                "<h1>html</h1>\n" +
                "<body>\n" +
                "</html>\n";

        mailUtil.sendHtmlMail("869653722@qq.com", "hello world邮件", content);
        return "发送成功";
    }

    /**
     * 测试文档之类附件传输的邮件
     **/
    @GetMapping("mail03")
    public String sendAttachmentsMail() throws MessagingException {
        String filePath = "D:\\temp-dir\\static/test.xlsx";
        String content = "<html>\n" +
                "<body>\n" +
                "<h3>hello world</h3>\n" +
                "<h1>html</h1>\n" +
                "<h1>附件传输</h1>\n" +
                "<body>\n" +
                "</html>\n";
        mailUtil.sendAttachmentsMail("869653722@qq.com", "附件传输", content, filePath);
        return "发送成功";
    }

    /**
     * 测试图片之类的、且显示在邮件内容上的邮件
     **/
    @GetMapping("mail04")
    public String sendInlinkResourceMail() throws MessagingException {
        //TODO 相对路径
        String imgPath = "D:\\temp-dir\\static/test01.jpeg";
        String rscId = "test";
        String content = "<html>" +
                "<body>" +
                "<h3>hello world</h3>" +
                "<h1>html</h1>" +
                "<h1>图片邮件</h1>" +
                "<img src='cid:" + rscId + "'></img>" +
                "<body>" +
                "</html>";

        mailUtil.sendInlinkResourceMail("869653722@qq.com", "这是一封图片邮件", content, imgPath, rscId);
        return "发送成功";
    }

    /**
     * 测试一个HTML模板的邮件
     **/
    @GetMapping("mail05")
    public String testTemplateMailTest() throws MessagingException {
        Context context = new Context();
        context.setVariable("id", "liuxing121380110");
        String emailContent = templateEngine.process("emailTemplate", context);
        mailUtil.sendHtmlMail("869653722@qq.com", "这是一封HTML模板邮件", emailContent);
        return "发送成功";
    }
}
```


