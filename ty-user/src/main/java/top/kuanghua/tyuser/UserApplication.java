package top.kuanghua.tyuser;

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
@EnableAutoDataSourceProxy //开启seata事务管理
public class UserApplication {
    public static void main(String[] args) {
        SpringApplication.run(UserApplication.class, args);
    }
}
