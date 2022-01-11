package top.kuanghua.integrationfront;

import io.seata.spring.annotation.datasource.EnableAutoDataSourceProxy;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @Title: UserApplication
 * @Description:
 * @Auther: kuanghua
 * @create 2020/11/7 20:07
 */
@SpringBootApplication
@EnableAutoDataSourceProxy
public class IntegrationFrontApplication {
    public static void main(String[] args) {
        SpringApplication.run(IntegrationFrontApplication.class, args);
    }
}
