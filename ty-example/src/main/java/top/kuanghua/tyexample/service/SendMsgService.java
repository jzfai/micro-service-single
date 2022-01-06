package top.kuanghua.tyexample.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import top.kuanghua.khcomomon.utils.NumberUtilsSelf;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @Title: SendMsgService
 * @Description:
 * @Auther:
 * @create 2020/8/20 11:26
 */
@Service
@Slf4j
public class SendMsgService {

    @Autowired
    protected AmqpTemplate amqpTemplate;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    
    public void sendMsg(String phone) {
        String code = NumberUtilsSelf.generateCode(6);
        Map<String, String> smsMap = new HashMap<String, String>();
        smsMap.put("phone",phone);
        smsMap.put("code",code);
        amqpTemplate.convertAndSend("kuanghua.sms.exchange", "sms.verify.code",smsMap);
        stringRedisTemplate.opsForValue().set("user:code:phone:"+phone,code,60, TimeUnit.SECONDS);
        log.info("发送短信的code"+code);
    }
}



