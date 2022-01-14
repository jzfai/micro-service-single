package top.kuanghua.tyexample.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @Title: BaseConfig
 * @Description:
 * @Auther: kuanghua
 * @create 2020/8/23 21:52
 */
/*
 * 总结：1.@Configuration 下的@ComponentScan回将包下带有@Component扫描变成配置类，
 * 而@SpringBootApplication扫描的只会变成普通类
 * */
@Configuration
//扫描公用包的配置和自身的配置类
@ComponentScan(basePackages = {"top.kuanghua.khcomomon"})
//mapper  scanning
@MapperScan(basePackages = {"top.kuanghua.tyexample.mapper"})
//feign scanning
@EnableFeignClients(basePackages = {"top.kuanghua.feign.integrationfront", "top.kuanghua.feign.tyauth",})
public class BaseConfig {

}
