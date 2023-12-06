[集成推荐阅读文档](https://blog.csdn.net/qq_22744093/article/details/129710621?spm=1001.2101.3001.6650.3&utm_medium=distribute.pc_relevant.none-task-blog-2%7Edefault%7ECTRLIST%7ERate-3-129710621-blog-120306729.235%5Ev38%5Epc_relevant_anti_t3&depth_1-utm_source=distribute.pc_relevant.none-task-blog-2%7Edefault%7ECTRLIST%7ERate-3-129710621-blog-120306729.235%5Ev38%5Epc_relevant_anti_t3&utm_relevant_index=6)

docker-prometheus.yml

```yml
version: '3'
services: 
  prometheus:
    image: prom/prometheus:v2.40.1
    container_name: prometheus
    ports:
      - "9090:9090"
    volumes:
      - /docker/prometheus/prometheus.yml:/etc/prometheus/prometheus.yml
    network_mode: "host"
  grafana:
    image: grafana/grafana:9.2.4
    container_name: grafana
    environment:
      TZ: Asia/Shanghai
      # 服务地址 用于指定外网ip或域名
      GF_SERVER_ROOT_URL: ""
      # admin 管理员密码
      GF_SECURITY_ADMIN_PASSWORD: 123456
    ports:
      - "3000:3000"
    volumes:
      - /docker/grafana/grafana.ini:/etc/grafana/grafana.ini
      - /docker/grafana:/var/lib/grafana
    network_mode: "host"
```

```
docker-compose -f  docker-prometheus.yml up -d prometheus grafana
```



开发端口

```
9090,3000
```




控制台地址 http://8.134.81.8:18080/

集成到 springboot 


pom.xml

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-actuator</artifactId>
</dependency>
<dependency>
    <groupId>io.micrometer</groupId>
    <artifactId>micrometer-registry-prometheus</artifactId>
</dependency>
```

启动访问

http://localhost:8095/actuator/prometheus


修改 prometheus.yml

```yml
  - job_name: 'Nacos'
    metrics_path: '/nacos/actuator/prometheus'
    static_configs:
      - targets: ['127.0.0.1:8095']
```

