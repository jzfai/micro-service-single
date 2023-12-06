
docker-skywalking.yml

```yml
version: '3'
services: 
  sky-oap:
    image: apache/skywalking-oap-server:8.9.1
    container_name: sky-oap
    ports:
      - "11800:11800"
      - "12800:12800"
    environment:
      JAVA_OPTS: -Xms256m -Xmx512m
      #记录数据的有效期，单位天
      SW_CORE_RECORD_DATA_TTL: 7
      #分析指标数据的有效期，单位天
      SW_CORE_METRICS_DATA_TTL: 7
      SW_STORAGE: elasticsearch
      SW_STORAGE_ES_CLUSTER_NODES: xxx:9200
      TZ: Asia/Shanghai
    network_mode: "host"

  sky-ui:
    image: apache/skywalking-ui:8.9.1
    container_name: sky-ui
    ports:
      - "18080:18080"
    environment:
      SW_OAP_ADDRESS: http://xxx:12800
      TZ: Asia/Shanghai
      JAVA_OPTS: "-Dserver.port=18080"
    depends_on:
      - sky-oap
    network_mode: "host"
```

```shell
docker-compose -f  docker-skywalking.yml up -d sky-oap sky-ui
```



开发端口

```
11800,12800,18080
```

配置探针

```
-javaagent:D:\github\RuoYi-Cloud-Plus\docker\skywalking\agent\skywalking-agent.jar -DSW_AGENT_NAME=demo-skywalking-service -DSW_AGENT_COLLECTOR_BACKEND_SERVICES=8.134.81.8:11800
```

控制台地址 http://8.134.81.8:18080/