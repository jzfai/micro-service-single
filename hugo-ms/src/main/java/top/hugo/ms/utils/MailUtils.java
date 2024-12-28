//package top.hugo.ms.utils;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.core.io.FileSystemResource;
//import org.springframework.mail.SimpleMailMessage;
//import org.springframework.mail.javamail.JavaMailSender;
//import org.springframework.mail.javamail.MimeMessageHelper;
//import org.springframework.stereotype.Service;
//
//import javax.annotation.Resource;
//import javax.mail.MessagingException;
//import javax.mail.internet.MimeMessage;
//import java.io.File;
//
///**
// * Description
// * Author  流星
// * Date 2022/1/13 22:06
// **/
//@Service
//public class MailUtils {
//
//    private final Logger logger = LoggerFactory.getLogger(this.getClass());
//
//    @Value("${spring.mail.username}")
//    private String from;
//
//    @Resource
//    private JavaMailSender mailSender;
//
//    /**
//     * 简单文本邮件
//     *
//     * @param to      接收者邮件
//     * @param subject 邮件主题
//     * @param content 邮件内容
//     */
//    public void sendSimpleMail(String to, String subject, String content) {
//
//        SimpleMailMessage message = new SimpleMailMessage();
//        message.setTo(to);
//        message.setSubject(subject);
//        message.setText(content);
//        message.setFrom(from);
//
//        mailSender.send(message);
//    }
//
//    /**
//     * HTML 文本邮件
//     *
//     * @param to      接收者邮件
//     * @param subject 邮件主题
//     * @param content HTML内容
//     * @throws MessagingException
//     */
//    public void sendHtmlMail(String to, String subject, String content) throws MessagingException {
//        MimeMessage message = mailSender.createMimeMessage();
//
//        MimeMessageHelper helper = new MimeMessageHelper(message, true);
//        helper.setTo(to);
//        helper.setSubject(subject);
//        helper.setText(content, true);
//        helper.setFrom(from);
//
//        mailSender.send(message);
//    }
//
//    /**
//     * 附件邮件
//     *
//     * @param to       接收者邮件
//     * @param subject  邮件主题
//     * @param content  HTML内容
//     * @param filePath 附件路径
//     * @throws MessagingException
//     */
//    public void sendAttachmentsMail(String to, String subject, String content,
//                                    String filePath) throws MessagingException {
//        MimeMessage message = mailSender.createMimeMessage();
//
//        MimeMessageHelper helper = new MimeMessageHelper(message, true);
//        helper.setTo(to);
//        helper.setSubject(subject);
//        helper.setText(content, true);
//        helper.setFrom(from);
//
//        FileSystemResource file = new FileSystemResource(new File(filePath));
//        String fileName = file.getFilename();
//        helper.addAttachment(fileName, file);
//
//        mailSender.send(message);
//    }
//
//    /**
//     * 图片邮件
//     *
//     * @param to      接收者邮件
//     * @param subject 邮件主题
//     * @param content HTML内容
//     * @param rscPath 图片路径
//     * @param rscId   图片ID
//     * @throws MessagingException
//     */
//    public void sendInlinkResourceMail(String to, String subject, String content,
//                                       String rscPath, String rscId) {
//        logger.info("发送静态邮件开始: {},{},{},{},{}", to, subject, content, rscPath, rscId);
//
//        MimeMessage message = mailSender.createMimeMessage();
//        MimeMessageHelper helper = null;
//
//        try {
//
//            helper = new MimeMessageHelper(message, true);
//            helper.setTo(to);
//            helper.setSubject(subject);
//            helper.setText(content, true);
//            helper.setFrom(from);
//
//            FileSystemResource res = new FileSystemResource(new File(rscPath));
//            helper.addInline(rscId, res);
//            mailSender.send(message);
//            logger.info("发送静态邮件成功!");
//
//        } catch (MessagingException e) {
//            logger.info("发送静态邮件失败: ", e);
//        }
//    }
//}
