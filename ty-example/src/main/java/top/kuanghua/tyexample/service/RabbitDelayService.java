package top.kuanghua.tyexample.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.kuanghua.tyexample.config.RabbitDelayConfig;

/**
 * @Title: RabbitDelayService
 * @Description:
 * @Auther: kuanghua
 * @create 2022-01-06 17:18
 */
@Service
public class RabbitDelayService {

    @Autowired
    private  RabbitTemplate rabbitTemplate;

    /**
     * @param time 延时的时间
     * 死信延时转发队列
     */
    public void convertAndSend(Integer time){
        rabbitTemplate.convertAndSend(RabbitDelayConfig.ORDER_DELAY_EXCHANGE, RabbitDelayConfig.ORDER_DELAY_ROUTING_KEY, "发送的数据", message -> {
            // 如果配置了 params.put("x-message-ttl", 5 * 1000); 那么这一句也可以省略,具体根据业务需要是声明 Queue 的时候就指定好延迟时间还是在发送自己控制时间
            message.getMessageProperties().setExpiration(time * 1000 + "");
            return message;
        });
    }
}

