package top.hugo.admin.service;

import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.LineCaptcha;
import org.junit.jupiter.api.Test;
import top.hugo.admin.captcha.UnsignedMathGenerator;

public class RbacServiceTest {
    @Test
    public void test() {
        LineCaptcha captcha = CaptchaUtil.createLineCaptcha(200, 45, 4, 2);
        captcha.setGenerator(new UnsignedMathGenerator(1));
        captcha.createCode();

        String code = captcha.getCode();
        System.out.println(code);
    }
}