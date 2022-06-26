package top.kuanghua.basisfunc.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;


@Configuration
@ComponentScan(basePackages = {
        "top.kuanghua.commonpom",
        "top.kuanghua.basisfunc"
})
@MapperScan(basePackages = {"top.kuanghua.basisfunc.mapper"})
public class BaseConfig {

}
