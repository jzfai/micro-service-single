//package top.hugo.admin.controller;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//import org.thymeleaf.TemplateEngine;
//import org.thymeleaf.context.Context;
//import top.hugo.ms.utils.MailUtils;
//
//import javax.annotation.Resource;
//import javax.mail.MessagingException;
//
///**
// * 邮件测试
// *
// * @author 邝华
// * @date 2023-10-10 11:39
// **/
//@RestController
//@RequestMapping
//public class MailTestController {
//
//    @Autowired
//    private MailUtils mailUtil;
//
//    @Resource
//    private TemplateEngine templateEngine;
//
//    /**
//     * 测试一个简单的、由自己撰写邮件标题和内容的邮件
//     **/
//    @GetMapping("mail01")
//    public String sendSimpleMail() {
//        mailUtil.sendSimpleMail("869653722@qq.com", "测试spring boot mail-主题", "测试spring boot mail - 内容");
//        return "发送成功";
//    }
//
//    /**
//     * 测试一个简单的html邮件
//     **/
//    @GetMapping("mail02")
//    public String sendHtmlMail() throws MessagingException {
//
//        String content = "<html>\n" +
//                "<body>\n" +
//                "<h3>hello world</h3>\n" +
//                "<h1>html</h1>\n" +
//                "<body>\n" +
//                "</html>\n";
//
//        mailUtil.sendHtmlMail("869653722@qq.com", "hello world邮件", content);
//        return "发送成功";
//    }
//
//    /**
//     * 测试文档之类附件传输的邮件
//     **/
//    @GetMapping("mail03")
//    public String sendAttachmentsMail() throws MessagingException {
//        String filePath = "D:\\temp-dir\\static/test.xlsx";
//        String content = "<html>\n" +
//                "<body>\n" +
//                "<h3>hello world</h3>\n" +
//                "<h1>html</h1>\n" +
//                "<h1>附件传输</h1>\n" +
//                "<body>\n" +
//                "</html>\n";
//        mailUtil.sendAttachmentsMail("869653722@qq.com", "附件传输", content, filePath);
//        return "发送成功";
//    }
//
//    /**
//     * 测试图片之类的、且显示在邮件内容上的邮件
//     **/
//    @GetMapping("mail04")
//    public String sendInlinkResourceMail() throws MessagingException {
//        //TODO 相对路径
//        String imgPath = "D:\\temp-dir\\static/test01.jpeg";
//        String rscId = "test";
//        String content = "<html>" +
//                "<body>" +
//                "<h3>hello world</h3>" +
//                "<h1>html</h1>" +
//                "<h1>图片邮件</h1>" +
//                "<img src='cid:" + rscId + "'></img>" +
//                "<body>" +
//                "</html>";
//
//        mailUtil.sendInlinkResourceMail("869653722@qq.com", "这是一封图片邮件", content, imgPath, rscId);
//        return "发送成功";
//    }
//
//    /**
//     * 测试一个HTML模板的邮件
//     **/
//    @GetMapping("mail05")
//    public String testTemplateMailTest() throws MessagingException {
//        Context context = new Context();
//        context.setVariable("id", "liuxing121380110");
//        String emailContent = templateEngine.process("emailTemplate", context);
//        mailUtil.sendHtmlMail("869653722@qq.com", "这是一封HTML模板邮件", emailContent);
//        return "发送成功";
//    }
//}
