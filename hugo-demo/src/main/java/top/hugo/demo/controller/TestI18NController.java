package top.hugo.demo.controller;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import top.hugo.framework.utils.MessageUtils;

/**
 * i18n测试
 */
@RestController
public class TestI18NController {
    /**
     * test接口
     */
    @GetMapping("i18n")
    public String test() {
        return MessageUtils.message("sms.code.retry.limit.exceed", 1, 2);
    }
}