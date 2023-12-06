## docker-compose 安装 kafka

上传 [kafka](https://gitee.com/jzfai/micro-serivce-learn/tree/master/docs-file/kafka) 目录到 /docker目录下  并执行

```shell
chmod -R  777 /docker/kafka
```

配置文件

```yml
cat> /docker/compose/kafka.yml<<EOF
version: '3'
services:
  zookeeper:
    image: 'bitnami/zookeeper:3.8.0'
    container_name: zookeeper
    ports: nb
      - "2181:2181"
    environment:
      TZ: Asia/Shanghai
      ALLOW_ANONYMOUS_LOGIN: "yes"
      ZOO_SERVER_ID: 1
      ZOO_PORT_NUMBER: 2181
    network_mode: "host"

  kafka:
    image: 'bitnami/kafka:3.2.0'
    container_name: kafka
    ports:
      - "9092:9092"
    environment:
      TZ: Asia/Shanghai
      # 更多变量 查看文档 https://github.com/bitnami/bitnami-docker-kafka/blob/master/README.md
      KAFKA_BROKER_ID: 1
      # 监听端口
      KAFKA_CFG_LISTENERS: PLAINTEXT://:9092
      # 实际访问ip 本地用 127 内网用 192 外网用 外网ip
      KAFKA_CFG_ADVERTISED_LISTENERS: PLAINTEXT://xxx:9092
      KAFKA_CFG_ZOOKEEPER_CONNECT: xxx:2181
      ALLOW_PLAINTEXT_LISTENER: "yes"
    volumes:
      - /docker/kafka/data:/bitnami/kafka/data
    depends_on:
      - zookeeper
    network_mode: "host"

  kafka-manager:
    image: sheepkiller/kafka-manager:latest
    container_name: kafka-manager
    ports:
      - "19092:19092"
    environment:
      ZK_HOSTS: xxx:2181
      APPLICATION_SECRET: letmein
      KAFKA_MANAGER_USERNAME: admin
      KAFKA_MANAGER_PASSWORD: admin
      KM_ARGS: -Dhttp.port=19092
    depends_on:
      - kafka
    network_mode: "host"
EOF
```

>xxx： 改成你服务ip

部署

```shell
docker-compose -f  /docker/compose/kafka.yml up -d zookeeper kafka kafka-manager
```

开发端口：2181，19092，9092



kafka控制台

服务ip:19092



## springboot集成kafka

相关文章：
https://blog.csdn.net/qq_35387940/article/details/100514134

### maven

```xml
<!--kafka-->
<dependency>
    <groupId>org.springframework.kafka</groupId>
    <artifactId>spring-kafka</artifactId>
</dependency>
```

### yml

```yml
--- # KAFKA
spring:
  # KAFKA
  kafka:
    # ָkafka服务器地址，可以指定多个
    bootstrap-servers: xxx:9092
    #=============== producer生产者配置 =======================
    producer:
      retries: 0
      # 每次批量发送消息的数量
      batch-size: 16384
      # 缓存容量
      buffer-memory: 33554432
      # ָ指定消息key和消息体的编解码方式
      key-serializer: org.apache.kafka.common.serialization.StringSerializer
      value-serializer: org.apache.kafka.common.serialization.StringSerializer
    #=============== consumer消费者配置  =======================
    consumer:
      #指定默认消费者的group id
      group-id: test-app
      #earliest
      #当各分区下有已提交的offset时，从提交的offset开始消费；无提交的offset时，从头开始消费
      #latest
      #当各分区下有已提交的offset时，从提交的offset开始消费；无提交的offset时，消费新产生的该分区下的数据
      #none
      #topic各分区都存在已提交的offset时，从offset后开始消费；只要有一个分区不存在已提交的offset，则抛出异常
      auto-offset-reset: latest
      enable-auto-commit: true
      auto-commit-interval: 100ms
      #指定消费key和消息体的编解码方式
      key-deserializer: org.apache.kafka.common.serialization.StringDeserializer
```

>xxx -> 您部署的 ip

### productor

```java
package top.hugo.admin.controller;
import com.alibaba.fastjson.JSONObject;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import top.hugo.admin.config.KafkaSender;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
 
/**
 * @Author : JCccc
 * @CreateTime : 2019/9/3
 * @Description :
 **/
@RestController
public class SendKafkaController {
    @Autowired
    private KafkaSender kafkaSender ;  //使用RabbitTemplate,这提供了接收/发送等等方法
    @GetMapping("/sendMessageToKafka")
    public  String sendMessageToKafka() {
        Map<String,String> messageMap=new HashMap();
        messageMap.put("message","我是一条消息");
        String taskid="123456";
        String jsonStr= JSONObject.toJSONString(messageMap);
//kakfa的推送消息方法有多种，可以采取带有任务key的，也可以采取不带有的（不带时默认为null）
        kafkaSender.send("testTopic",taskid,jsonStr);
        return "hi guy!";
    }
}
```



### consumer

KafkaConsumer

```java
package top.hugo.admin.config;
 
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
/**
 * Hello!
 * Created By  xiongmaoge
 * 13:13
 */
@Component
public class KafkaConsumer  {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
//下面的主题是一个数组，可以同时订阅多主题，只需按数组格式即可，也就是用“，”隔开
    @KafkaListener(topics = {"testTopic"},groupId = "")
    public void receive(ConsumerRecord<?, ?> record){
        logger.info("消费得到的消息---key: " + record.key());
        logger.info("消费得到的消息---value: " + record.value().toString());
    }
}
```
