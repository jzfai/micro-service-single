## docker-compose 安装xxl-job

分布式任务调度平台

#### 设置 xxl-job配置文件

```yml
cat>/docker/compose/xxl-job.yml<<EOF
version: "3"
services:
  xxl-job-admin:
    restart: always
    # docker 镜像
    image: xuxueli/xxl-job-admin:2.3.0
    # 容器名称
    container_name: xxl-job-admin
    volumes:
      # 日志目录映射到主机目录
      - /docker/xxl-job/data:/data
    ports:
      # 端口映射
      - "8800:8800"
    environment:
      # 设置启动参数
      PARAMS: '
      --server.port=8800
      --server.servlet.context-path=/xxl-job-admin
      --spring.datasource.url=jdbc:mysql://127.0.0.1:3310/xxl_job?useUnicode=true&characterEncoding=UTF-8&autoReconnect=true&serverTimezone=Asia/Shanghai
      --spring.datasource.username=root
      --spring.datasource.password=@Root123
      --xxl.job.accessToken=xxl-job'
    network_mode: "host"  
EOF
```

>注：涉及到的数据库信息要根据自己部署的填写



#### 设置sql

运行文件 [xxl-job.sql](https://gitee.com/jzfai/micro-serivce-learn/tree/master/docs-file/xxl-job.sql) 设置sql



部署

```shell
docker-compose -f /docker/compose/xxl-job.yml  up -d  xxl-job-admin
```

开放：8800 端口

访问：http://xxx:8800/xxl-job-admin



用户密码 admin:123456

## springboot集成xxl-job

相关文章：
使用：https://blog.csdn.net/xiaoshitou_2015/article/details/130009350



### maven

```xml
<!-- xxl-job-core -->
<dependency>
    <groupId>com.xuxueli</groupId>
    <artifactId>xxl-job-core</artifactId>
    <version>2.3.1</version>
</dependency>
```

### yml

```yml
--- # xxl-job 配置
xxl.job:
  # 执行器开关
  enabled: true
  # 调度中心地址：如调度中心集群部署存在多个地址则用逗号分隔。
  admin-addresses: http://xxx:8800/xxl-job-admin/
  # 执行器通讯TOKEN：非空时启用
  access-token: xxl-job
  executor:
    # 执行器AppName：执行器心跳注册分组依据；为空则关闭自动注册
    appname: xxl-job-executor
    # 执行器端口号 执行器从9101开始往后写
    port: 9101
    # 执行器注册：默认IP:PORT
    address:
    # 执行器IP：默认自动获取IP
    ip:
    # 执行器运行日志文件存储磁盘路径
    logpath: ./logs/xxl-job
    # 执行器日志文件保存天数：大于3生效
    logretentiondays: 30
```

>xxx -> 您部署的 ip



properties

```java
package top.hugo.framework.config.properties;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * xxljob配置类
 *
 * @author kuanghua
 */
@Data
@ConfigurationProperties(prefix = "xxl.job")
public class XxlJobProperties {

    private Boolean enabled;

    private String adminAddresses;

    private String accessToken;

    private Executor executor;

    @Data
    @NoArgsConstructor
    public static class Executor {

        private String appname;

        private String address;

        private String ip;

        private int port;

        private String logPath;

        private int logRetentionDays;
    }
}
```



XxlJobConfig

```java
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import top.hugo.framework.config.properties.XxlJobProperties;
/**
 * xxl-job config
 *
 * @author kuanghua
 */
@Slf4j
@Configuration
@EnableConfigurationProperties(XxlJobProperties.class)
@AllArgsConstructor
@ConditionalOnProperty(prefix = "xxl.job", name = "enabled", havingValue = "true")
public class XxlJobConfig {
    private final XxlJobProperties xxlJobProperties;
    @Bean
    public XxlJobSpringExecutor xxlJobExecutor() {
        log.info(">>>>>>>>>>> xxl-job config init.");
        XxlJobSpringExecutor xxlJobSpringExecutor = new XxlJobSpringExecutor();
        xxlJobSpringExecutor.setAdminAddresses(xxlJobProperties.getAdminAddresses());
        xxlJobSpringExecutor.setAccessToken(xxlJobProperties.getAccessToken());
        XxlJobProperties.Executor executor = xxlJobProperties.getExecutor();
        xxlJobSpringExecutor.setAppname(executor.getAppname());
        xxlJobSpringExecutor.setAddress(executor.getAddress());
        xxlJobSpringExecutor.setIp(executor.getIp());
        xxlJobSpringExecutor.setPort(executor.getPort());
        xxlJobSpringExecutor.setLogPath(executor.getLogPath());
        xxlJobSpringExecutor.setLogRetentionDays(executor.getLogRetentionDays());
        return xxlJobSpringExecutor;
    }
}
```



### xxljob 测试案例

```java
package top.hugo.admin.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;

@Slf4j
@Service
public class XxlJobService {
    /**
     * 1、简单任务示例（Bean模式）
     */
    @XxlJob("demoJobHandler")
    public void demoJobHandler() throws Exception {
        System.out.println("demoJobHandler执行了");
        // default success
    }

    /**
     * 5、生命周期任务示例：任务初始化与销毁时，支持自定义相关逻辑；
     */
    @XxlJob(value = "demoJobHandler2", init = "init", destroy = "destroy")
    public void demoJobHandler2() throws Exception {
        System.out.println("demoJobHandler2执行了");
    }

    public void init() {
        log.info("init");
    }

    public void destroy() {
        log.info("destory");
    }
}
```



### 将服务打包部署到服务器上测试



复制 [deploy-jar]() 到 /test目录下

install构建服务， 上传到 deploy-jar 同级目录下

修改变量 module_name(构建后的jar包名字)， build_version，module_port（服务启动端口）



执行部署jar包

```shell
sh deploy.sh
```



通过 xxl-job-admin 配置执行任务查看效果
