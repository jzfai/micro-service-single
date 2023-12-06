package top.hugo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.metrics.buffering.BufferingApplicationStartup;

@SpringBootApplication
public class HugoDemoApplication {
    public static void main(String[] args) {
        //关闭热更新(开启设置为true)
        System.setProperty("spring.devtools.restart.enabled", "false");
        SpringApplication application = new SpringApplication(HugoDemoApplication.class);
        application.setApplicationStartup(new BufferingApplicationStartup(2048));
        application.run(args);
    }
}