package top.kuanghua.tyexample.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * @Title: Configuration
 * @Description:
 * @Auther: kuanghua
 * @create 2022/1/05 10:04
 */
@Configuration
@Slf4j
public class RabbitDelayConfig {

    /*死信部分*/
    public static final String ORDER_DELAY_QUEUE = "queue.delay.user.order";
    public static final String ORDER_DELAY_EXCHANGE = "exchange.delay.user.order";
    public static final String ORDER_DELAY_ROUTING_KEY = "delay_order";
    /*普通交换机*/
    public static final String ORDER_QUEUE_NAME = "queue.user.order";
    public static final String ORDER_EXCHANGE_NAME = "exchange.user.order";
    public static final String ORDER_ROUTING_KEY = "order";

    /*
    *   DELAY_EXCHANGE
    *   死信交换机原理：发送到死信交换机（在有效期时间内接收信息不进行转发，超时则进行转发到其他队列）
    *  */
    @Bean
    public Queue delayOrderQueue() {
        Map<String, Object> params = new HashMap<>();
        // 转发的队列交换机的名称
        params.put("x-dead-letter-exchange", ORDER_EXCHANGE_NAME);
        // 转发的队列队列的路由名称
        params.put("x-dead-letter-routing-key", ORDER_ROUTING_KEY);
        return new Queue(ORDER_DELAY_QUEUE, true, false, false, params);
    }
    @Bean
    public DirectExchange orderDelayExchange() {
        return new DirectExchange(ORDER_DELAY_EXCHANGE);
    }


    @Bean
    public Binding dlxBinding() {
        return BindingBuilder.bind(delayOrderQueue()).to(orderDelayExchange()).with(ORDER_DELAY_ROUTING_KEY);
    }

    /*普通交换机*/
    @Bean
    public Queue orderQueue() {
        return new Queue(ORDER_QUEUE_NAME, true);
    }
    @Bean
    public TopicExchange orderTopicExchange() {
        return new TopicExchange(ORDER_EXCHANGE_NAME);
    }
    @Bean
    public Binding orderBinding() {
        return BindingBuilder.bind(orderQueue()).to(orderTopicExchange()).with(ORDER_ROUTING_KEY);
    }
}
