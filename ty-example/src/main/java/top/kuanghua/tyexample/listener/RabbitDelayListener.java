package top.kuanghua.tyexample.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import top.kuanghua.tyexample.config.RabbitDelayConfig;

/**
 * @Title: orderDelayQueue
 * @Description:
 * @Auther: kuanghua
 * @create 2021/1/18 10:27
 */
@Component
@Slf4j
public class RabbitDelayListener {
    @RabbitListener(queues = {RabbitDelayConfig.ORDER_QUEUE_NAME})
    public void ORDER_QUEUE_NAME( String outTradeno) {
        /*
        * 微信服务器查询订单,并更新订单状态
        * */
        log.info("receive the Delay info "+outTradeno);
    }
    //    note: not dill the msg will re to redirect ORDER_QUEUE_NAME
    //    @RabbitListener(queues = {RabbitDelayConfig.ORDER_DELAY_QUEUE})
    //    public void ORDER_DELAY_QUEUE( String outTradeno) {
    //        log.info("ORDER_DELAY_QUEUE"+outTradeno);
    //    }
}
