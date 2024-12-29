package top.hugo.generator.controller;

import cn.dev33.satoken.annotation.SaIgnore;
import cn.hutool.core.lang.Dict;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.hugo.common.domain.R;
import top.hugo.common.utils.JacksonUtils;
import top.hugo.ms.core.AliyunSMSTemplate;


@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/send/sms")
@SaIgnore
public class SmsController {
    private final AliyunSMSTemplate aliyunSMSTemplate;

    /**
     * 发送短信Aliyun
     *
     * @param phones 电话号
     */
    @GetMapping("/sendAliyun")
    public R<Void> sendAliyun(String phones) {
        Integer code = aliyunSMSTemplate.generateValidateCode(6);
        String result = aliyunSMSTemplate.sendMessage(phones, code.toString());
        Dict dict = JacksonUtils.parseMap(result);
        Object message = dict.get("Message");
        return R.result(result.contains("\"Message\":\"OK\""), message.toString());
    }


//    @GetMapping("/sendString")
//    public R<Void> sendAliyun(String phones,String gpString) {
//        String result = aliyunSMSTemplate.sendMessageString(phones, gpString);
//        Dict dict = JacksonUtils.parseMap(result);
//        Object message = dict.get("Message");
//        return R.result(result.contains("\"Message\":\"OK\""), message.toString());
//    }
}
