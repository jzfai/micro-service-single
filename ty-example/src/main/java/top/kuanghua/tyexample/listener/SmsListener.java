package top.kuanghua.tyexample.listener;


import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;
import top.kuanghua.tyexample.config.SmsProperties;
import top.kuanghua.tyexample.utils.SendSmsUtils;


import java.util.Map;

@Component
@EnableConfigurationProperties(SmsProperties.class)
@Slf4j
public class SmsListener {

    @Autowired
    private SendSmsUtils smsUtils;

    @Autowired
    private SmsProperties smsProperties;

    @RabbitListener(
            bindings = @QueueBinding(value = @Queue(value = "kuanghua.sms.queue", durable = "true"),
            exchange = @Exchange(value = "kuanghua.sms.exchange", ignoreDeclarationExceptions = "true",durable = "true"),
            key = {"sms.verify.code"})
    )
    public void listenSms(Map<String, String> msg) throws Exception {
        if (msg == null || msg.size() <= 0) {
            // 放弃处理
            return;
        }
        String phone = msg.get("phone");
        String code = msg.get("code");
        if (StringUtils.isBlank(phone) || StringUtils.isBlank(code)) {
            // 放弃处理
            log.error("发送短信mq phone或code字段为空");
            return;
        }
        // 发送短信
        this.smsUtils.sendMsg(phone,code,smsProperties.getSignName(),smsProperties.getVerifyCodeTemplate());
    }
}