package top.kuanghua.integrationfront.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;


@Configuration
@ComponentScan(basePackages = {
        "top.kuanghua.commonpom",
        "top.kuanghua.authpom",
        "top.kuanghua.integrationfront"
})
@EnableFeignClients(basePackages = {"top.kuanghua.commonpom.feign.tyauth"})
@MapperScan(basePackages = {"top.kuanghua.integrationfront.mapper"})
public class BaseConfig {

}
