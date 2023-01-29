package top.hugo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.metrics.buffering.BufferingApplicationStartup;

@SpringBootApplication
public class HugoApplication {
    public static void main(String[] args) {
        //关闭热更新
        // System.setProperty("spring.devtools.restart.enabled", "false");
        SpringApplication application = new SpringApplication(HugoApplication.class);
        application.setApplicationStartup(new BufferingApplicationStartup(2048));
        application.run(args);
        System.out.println("(♥◠‿◠)ﾉﾞ  micro-service-single启动成功   ლ(´ڡ`ლ)ﾞ");
    }
}
