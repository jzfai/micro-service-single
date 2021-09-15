package top.kuanghua.tyupload;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

/**
 * @Title: SmsApplication
 * @Description:
 * @Auther:
 * @create 2020/8/20 10:46
 */
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class UploadApplication {
    public static void main(String[] args) {
        SpringApplication.run(UploadApplication.class, args);
    }
}
