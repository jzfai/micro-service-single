package top.kuanghua.tyexample.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @Title: SmsProperties
 * @Description:
 * @Auther:jzfai
 * @Version: 1.0
 * @create 2020/3/13 18:19
 */
@Component
@ConfigurationProperties(prefix = "kuanghua.sms")
@Data
public class SmsProperties {
    String accessKeyId;

    String accessKeySecret;

    String signName;

    String verifyCodeTemplate;
}
