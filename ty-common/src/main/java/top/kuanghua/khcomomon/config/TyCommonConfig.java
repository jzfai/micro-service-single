package top.kuanghua.khcomomon.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import top.kuanghua.khcomomon.utils.IdWorker;

;

/**
 * @Title: TyCommonConfig
 * @Description:
 * @Auther: kuanghua
 * @create 2020/12/22 15:49
 */
@Configuration
public class TyCommonConfig {
    /***
     * IdWorker
     * @return
     */
    @Bean
    public IdWorker idWorker() {
        return new IdWorker(0, 0);
    }
}
