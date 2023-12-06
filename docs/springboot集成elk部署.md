## docker 安装 elk

上传 [elk](https://gitee.com/jzfai/micro-serivce-learn/tree/master/docs-file/elk) 文件夹复制到 /docker目录下  并执行

```shell
chmod -R  777 /docker/elk
```

配置docker-compose文件

elk.yml


```yml
cat>/docker/compose/elk.yml<<EOF
version: '3'
services:
  elasticsearch:
    image: elasticsearch:7.14.0
    container_name: elasticsearch
    ports:
      - "9200:9200"
      - "9300:9300"
    environment:
      # 设置集群名称
      cluster.name: elasticsearch
      # 以单一节点模式启动
      discovery.type: single-node
      ES_JAVA_OPTS: "-Xms256m -Xmx256m"
    volumes:
      - /docker/elk/elasticsearch/plugins:/usr/share/elasticsearch/plugins
      - /docker/elk/elasticsearch/data:/usr/share/elasticsearch/data
      - /docker/elk/elasticsearch/config:/usr/share/elasticsearch/config
      - /docker/elk/elasticsearch/logs:/usr/share/elasticsearch/logs
    network_mode: "host"
    
  kibana:
    image: kibana:7.14.0
    container_name: kibana
    ports:
      - "5601:5601"
    depends_on:
      # kibana在elasticsearch启动之后再启动
      - elasticsearch
    environment:
      #设置系统语言文中文
      I18N_LOCALE: zh-CN
      # 访问域名
      # SERVER_PUBLICBASEURL: https://kibana.cloud.com
    volumes:
      - /docker/elk/kibana/config/kibana.yml:/usr/share/kibana/config/kibana.yml
    network_mode: "host"

  logstash:
    image: logstash:7.14.0
    container_name: logstash
    ports:
      - "4560:4560"
    volumes:
      - /docker/elk/logstash/pipeline/logstash.conf:/usr/share/logstash/pipeline/logstash.conf
      - /docker/elk/logstash/config/logstash.yml:/usr/share/logstash/config/logstash.yml
    depends_on:
      - elasticsearch
    network_mode: "host"
EOF
```

复制容器文件到宿主机(可忽略)

```
docker cp elasticsearch:/usr/share/elasticsearch/config  /docker/elk/elasticsearch/config
```

部署

```shell
docker-compose -f  /docker/compose/elk.yml  up -d elasticsearch kibana logstash
```


开发端口

```
5601,4560,9200
```

访问ip:5601




## 集成到springboot中

maven

```xml
 <!--log收集-->
<dependency>
    <groupId>net.logstash.logback</groupId>
    <artifactId>logstash-logback-encoder</artifactId>
    <version>7.1.1</version>
</dependency>
```



logback.xml

```xml
    <!--集成logstash-->
    <springProperty scope="context" name="appName" source="spring.application.name"/>
    <!--输出到logstash的appender-->
    <appender name="logstash" class="net.logstash.logback.appender.LogstashTcpSocketAppender">
        <!--可以访问的logstash日志收集端口-->
        <destination>xxx:4560</destination>
        <encoder charset="UTF-8" class="net.logstash.logback.encoder.LogstashEncoder">
            <customFields>{"spring.application.name":"${appName}"}</customFields>
        </encoder>
    </appender>
    <root level="info">
        <appender-ref ref="logstash"/>
    </root>
```

xxx: 服务ip



配置 kibana 查看效果，并配置