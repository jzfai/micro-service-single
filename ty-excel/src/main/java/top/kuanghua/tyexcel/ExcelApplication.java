package top.kuanghua.tyexcel;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class ExcelApplication {
    public static void main(String[] args) {
        SpringApplication.run(ExcelApplication.class,args);
    }
}

