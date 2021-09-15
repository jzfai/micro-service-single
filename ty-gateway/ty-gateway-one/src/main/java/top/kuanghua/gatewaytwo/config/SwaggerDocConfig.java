package top.kuanghua.gatewaytwo.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import  java.util.*;
/**
 * @Title: SwaggerDocConfig
 * @Description:
 * @Auther: kuanghua
 * @create 2021/9/14 13:36
 */

@Component
@ConfigurationProperties(prefix = "swagger-doc")
@Data
public class SwaggerDocConfig {
    private Map<String,Object> serviceMap;
}
