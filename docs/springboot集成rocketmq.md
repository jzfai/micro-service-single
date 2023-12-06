## docker-compose 安装 rocketmq

上传文件夹  [rocketmq](https://gitee.com/jzfai/micro-serivce-learn/tree/master/docs-file/rabbitmq)   到   /docker目录下

开放目录权限

```shell
chmod -R  777 /docker/rocketmq
```

生成rocketmq配置文件

```shell
cat>/docker/compose/rocketmq.yml<<EOF
version: '3'
services:
  mqnamesrv:
    image: apache/rocketmq:4.9.4
    container_name: rocketmq
    ports:
      - "9876:9876"
    environment:
      JAVA_OPT: -server -Xms256m -Xmx256m -Xmn128m
    command: sh mqnamesrv
    volumes:
      - /docker/rocketmq/namesrv/logs:/home/rocketmq/logs/rocketmqlogs
    network_mode: "host"

  mqbroker1:
    image: apache/rocketmq:4.9.4
    container_name: mqbroker1
    ports:
      - "10911:10911"
      - "10909:10909"
      - "10912:10912"
    environment:
      JAVA_OPT_EXT: -server -Xms256m -Xmx512m -Xmn256m
    command: sh mqbroker -c /home/rocketmq/rocketmq-4.9.4/conf/broker.conf
    depends_on:
      - mqnamesrv
    volumes:
      - /docker/rocketmq/broker1/conf/broker.conf:/home/rocketmq/rocketmq-4.9.4/conf/broker.conf
      - /docker/rocketmq/broker1/logs:/home/rocketmq/logs/rocketmqlogs
      - /docker/rocketmq/broker1/store:/home/rocketmq/store
    network_mode: "host"

  mqconsole:
    image: styletang/rocketmq-console-ng
    container_name: mqconsole
    ports:
      - "19876:19876"
    environment:
      JAVA_OPTS: -Dserver.port=19876 -Drocketmq.namesrv.addr=xxx:9876 -Dcom.rocketmq.sendMessageWithVIPChannel=false
    depends_on:
      - mqnamesrv
    network_mode: "host"
EOF
```

>mqconsole ： rocketmq控制台



部署

```shell
docker-compose -f /docker/compose/rocketmq.yml up -d mqnamesrv mqbroker1 mqconsole
```

>开放端口 9876，19876，10911，10909，10912



rocketmq 控制台   http://xxx:19876



注： broker.conf  配置文件里的 ip 要修改



## springboot集成rocketmq

相关文章：
https://blog.csdn.net/Decade_Faiz/article/details/131011270

### maven

```xml
<!--rocketmq-->
<dependency>
    <groupId>org.apache.rocketmq</groupId>
    <artifactId>rocketmq-spring-boot-starter</artifactId>
    <version>2.2.3</version>
</dependency>
```

### yml

```yml
--- # rocketmq
rocketmq:
  name-server: xxx:9876
  producer:
    group: "producer-group"
```

>xxx   -> 您部署的 ip



### 生产者

```java
package top.hugo.admin.controller;

import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.apache.rocketmq.spring.support.RocketMQHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import top.hugo.admin.utils.JacksonUtils;

import java.util.HashMap;

/**
 * rocketmq
 * @Author : 熊猫哥
 **/
@RestController
public class SendRocketMqController {
    @Autowired
    private RocketMQTemplate rocketMQTemplate;
    /**
     * 同步消息
     */
    @GetMapping("/syncSend")
    public String syncSend() {
        //将消息携带绑定键值：TestDirectRouting 发送到交换机TestDirectExchange
        for (int i = 0; i < 100; i++) {
            SendResult sendResult = rocketMQTemplate.syncSend("commonTopic", "我是一个boot消息"+i);
            System.out.println(sendResult.getSendStatus());
        }
        return "ok";
    }


    /**
     * 异步消息
     */
    @GetMapping("/asyncSend")
    public String asyncSend() {
        //将消息携带绑定键值：TestDirectRouting 发送到交换机TestDirectExchange
        for (int i = 0; i < 100; i++) {
            //异步消息
            rocketMQTemplate.asyncSend("commonTopic", "我是一个boot异步消息"+i, new SendCallback() {
                @Override
                public void onSuccess(SendResult sendResult) {
                    System.out.println("成功");
                }
                @Override
                public void onException(Throwable throwable) {
                    System.out.println("失败");
                }
            });
        }
        return "ok";
    }

    /**
     *  单向消息
     */
    @GetMapping("/sendOneWay")
    public String sendOneWay() {
        for (int i = 0; i < 100; i++) {
            rocketMQTemplate.sendOneWay("commonTopic", "我是一个单向消息"+i);
        }
        return "ok";
    }

    /**
     *  延时消息
     */
    @GetMapping("/syncSendDelay")
    public String syncSendDelay() {
        for (int i = 0; i < 100; i++) {
            //延迟消息
            Message<String> msg = MessageBuilder.withPayload("我是一个延迟消息"+i).build();
            rocketMQTemplate.syncSend("commonTopic", msg, 3000, 2);
        }
        return "ok";
    }

    /**
     *  顺序消息
     */
    @GetMapping("/syncSendOrderly")
    public String syncSendOrderly() {
        for (int i = 0; i < 100; i++) {
            HashMap<Integer, Object> hashMap = new HashMap<>();
            hashMap.put(i,"hashMap数据"+i);
            //发送一般都是以json的格式发送
            rocketMQTemplate.syncSendOrderly("bootOrderlyTopic", JacksonUtils.objToStr(hashMap),i+"");
        }
        return "ok";
    }

    /**
     *  带tag的消息
     */
    @GetMapping("/bootTagTopic")
    public String bootTagTopic() {
        for (int i = 0; i < 100; i++) {
            //topic:tag
            rocketMQTemplate.syncSend("bootTagTopic:tagA", "我是一个带tag的消息"+i);
        }
        return "ok";
    }


    /**
     *  带key的消息
     */
    @GetMapping("/bootKeyTopic")
    public String bootKeyTopic() {
        for (int i = 0; i < 100; i++) {
            //topic:tag
            //key是携带在消息头上的
            Message<String> message = MessageBuilder.withPayload("我是一个带key的消息"+i)
                    .setHeader(RocketMQHeaders.KEYS, "key")
                    .build();
            rocketMQTemplate.syncSend("bootKeyTopic", message);
        }
        return "ok";
    }
}
```



## 消费者

普通队列消费

```java
package top.hugo.admin.consumer;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Component;
@Component
@RocketMQMessageListener(topic = "commonTopic", consumerGroup = "consumer-common-group")
public class RocketMQSimpleMsgConsumer implements RocketMQListener<MessageExt> {
    /**
     * 没有报错，就签收
     * 如果没有报错，就是拒收 就会重试
     *
     * @param messageExt
     */
    @Override
    public void onMessage(MessageExt messageExt) {
        System.out.println(new String(messageExt.getBody()));
    }
}
```

顺序消息消费

```java
package top.hugo.admin.consumer;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Component;
@Component
@RocketMQMessageListener(topic = "bootOrderlyTopic", consumerGroup = "consumer-common-group")
public class RocketMQOrderlyConsumer implements RocketMQListener<MessageExt> {
    /**
     * 没有报错，就签收
     * 如果没有报错，就是拒收 就会重试
     *
     * @param messageExt
     */
    @Override
    public void onMessage(MessageExt messageExt) {
        System.out.println(new String(messageExt.getBody()));
    }
}
```

 带tag的消息消费

```java
@Component
@RocketMQMessageListener(topic = "bootTagTopic",
        consumerGroup = "boot-tag-consumer-group",
        selectorType = SelectorType.TAG,//tah过滤模式
        selectorExpression = "tagA || tagB"
)
public class RocketMQTagConsumer implements RocketMQListener<MessageExt> {


    /**
     * 没有报错，就签收
     * 如果没有报错，就是拒收 就会重试
     *
     * @param messageExt
     */
    @Override
    public void onMessage(MessageExt messageExt) {
        System.out.println(new String(messageExt.getBody()));
    }
}
```

带key的消息消费

```java
@Component
@RocketMQMessageListener(topic = "bootKeyTopic", consumerGroup = "boot-key-consumer-group")
public class DKeyMsgListener implements RocketMQListener<MessageExt> {


    /**
     * 没有报错，就签收
     * 如果没有报错，就是拒收 就会重试
     *
     * @param messageExt
     */
    @Override
    public void onMessage(MessageExt messageExt) {
        System.out.println(new String(messageExt.getBody())+"\tkey为："+messageExt.getKeys());
    }
}

```