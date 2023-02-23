package top.hugo.web.controller.common;


import cn.dev33.satoken.annotation.SaIgnore;
import cn.hutool.captcha.AbstractCaptcha;
import cn.hutool.captcha.generator.CodeGenerator;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.RandomUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import top.hugo.common.constant.CacheConstants;
import top.hugo.common.constant.Constants;
import top.hugo.common.domain.R;
import top.hugo.common.enums.CaptchaType;
import top.hugo.common.spring.SpringUtils;
import top.hugo.common.utils.RedisUtils;
import top.hugo.common.utils.StringUtils;
import top.hugo.common.utils.reflect.ReflectUtils;
import top.hugo.framework.config.properties.CaptchaProperties;

import javax.validation.constraints.NotBlank;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;


/**
 * 验证码操作处理
 *
 * @author  hugo
 */
@SaIgnore
@Slf4j
@Validated
@RequiredArgsConstructor
@RestController
public class CaptchaController {
        private final CaptchaProperties captchaProperties;
        /**
         * 生成验证码
         */
        @GetMapping("/captchaImage")
        public R<Map<String, Object>> getCode() {
                Map<String, Object> ajax = new HashMap<>();
                boolean captchaEnabled = true;
                ajax.put("captchaEnabled", captchaEnabled);
                if (!captchaEnabled) {
                        return R.ok(ajax);
                }
                // 保存验证码信息
                String uuid = IdUtil.simpleUUID();
                String verifyKey = CacheConstants.CAPTCHA_CODE_KEY + uuid;
                // 生成验证码
                CaptchaType captchaType = captchaProperties.getType();
                boolean isMath = CaptchaType.MATH == captchaType;
                Integer length = isMath ? captchaProperties.getNumberLength() : captchaProperties.getCharLength();
                CodeGenerator codeGenerator = ReflectUtils.newInstance(captchaType.getClazz(), length);
                //获取been
                AbstractCaptcha captcha = SpringUtils.getBean(captchaProperties.getCategory().getClazz());
                captcha.setGenerator(codeGenerator);
                captcha.createCode();
                String code = captcha.getCode();
                //如果是数字的话需要计算
                if (isMath) {
                        ExpressionParser parser = new SpelExpressionParser();
                        //移除“=”号，计算二维码的值
                        Expression exp = parser.parseExpression(StringUtils.remove(code, "="));
                        code = exp.getValue(String.class);
                }
                RedisUtils.setCacheObject(verifyKey, code, Duration.ofMinutes(Constants.CAPTCHA_EXPIRATION));
                ajax.put("uuid", uuid);
                ajax.put("img", captcha.getImageBase64());
                return R.ok(ajax);
        }

}