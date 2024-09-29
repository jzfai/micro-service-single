package top.hugo.admin.controller;//package top.hugo.admin.controller;
//
//import cn.hutool.core.lang.Dict;
//import lombok.RequiredArgsConstructor;
//import org.springframework.validation.annotation.Validated;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//import top.hugo.common.domain.R;
//import top.hugo.common.utils.JacksonUtils;
//import top.hugo.ms.core.AliyunSMSTemplate;
//
///**
// * 短信演示案例
// * 请先阅读文档 否则无法使用
// *
// * @author Lion Li
// * @version 4.2.0
// */
//@Validated
//@RequiredArgsConstructor
//@RestController
//@RequestMapping("/demo/sms")
//public class SmsController {
//    private final AliyunSMSTemplate aliyunSMSTemplate;
//
//    /**
//     * 发送短信Aliyun
//     *
//     * @param phones 电话号
//     */
//    @GetMapping("/sendAliyun")
//    public R<Void> sendAliyun(String phones) {
//        Integer code = aliyunSMSTemplate.generateValidateCode(6);
//        String result = aliyunSMSTemplate.sendMessage(phones, code.toString());
//        Dict dict = JacksonUtils.parseMap(result);
//        Object message = dict.get("Message");
//        return R.result(result.contains("\"Message\":\"OK\""), message.toString());
//    }
//}
