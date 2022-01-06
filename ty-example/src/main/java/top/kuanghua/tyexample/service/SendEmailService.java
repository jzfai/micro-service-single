package top.kuanghua.tyexample.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import top.kuanghua.khcomomon.utils.ObjectUtilsSelf;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * @Title: SendEmailService
 * @Description:
 * @Auther: kuanghua
 * @create 2020/8/20 22:09
 */
@Service
public class SendEmailService {

    @Autowired
    private JavaMailSenderImpl javaMailSender;

    @Value("${spring.mail.username}")
    private String sendForm;

    /**
     * @Description: 发送文本邮件
     * @Param: subject 主题
     * @Param: text 发送的文本
     * @Param: sendForm 发送人
     * @Param: sendTo 发送给谁
     * @return:
     * @Date: 2020-08-20
     */
    @Async
    public void sendEmailText(String subject, String text, String sendTo) {
        //封装简单的邮件内容
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        //邮件主题
        simpleMailMessage.setSubject(subject);
        simpleMailMessage.setText(text);
        //发件人和收件人
        simpleMailMessage.setFrom(sendForm);
        simpleMailMessage.setTo(sendTo);
        //发送
        javaMailSender.send(simpleMailMessage);
        //demo
        //sendEmailText("邮件主题测试setSubject","邮件主题测试setText","869653722@qq.com","1319404169@qq.com")
    }

    /**
     * @Description: 发送附件邮件
     * @Param: * @param null
     * @return:
     * @Date: 2020-08-20
     */
    @Async
    public void sendMimeMail(String subject, String text, String sendTo,MultipartFile file)
            throws MessagingException, IOException {
        // String path = ResourceUtils.getURL("classpath:").getPath();
        // System.out.println(path + file.getOriginalFilename());
        //先将文件保存在本地
        //file.transferTo(filePath);

        File toFile = ObjectUtilsSelf.MultipartFileToFile(file);
        //创建一个发送复杂消息对象
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        //通过消息帮助对象，来设置发送的内容
        MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
        //邮件主题
        messageHelper.setSubject(subject);
        //第2个参数为true表示是html
        messageHelper.setText(text, true);
        //上传文件 (文件名，File或IO对象)
        String filename = file.getOriginalFilename();

        messageHelper.addAttachment(filename, toFile);
        //发件人和收件人
        messageHelper.setFrom(sendForm);
        messageHelper.setTo(sendTo);
        //发送
        javaMailSender.send(mimeMessage);
        //发完后清空文件
        //filePath.delete();
    }
    //@Scheduled(cron = "*/1 * * * * *")
    //public void scheduled() {
    //this.sendEmailText("邮件主题测试setSubject", "邮件主题测试setText", "869653722@qq.com", "1319404169@qq.com");
    //System.out.println("邮件发送了好烦啊");
    //    }
}
