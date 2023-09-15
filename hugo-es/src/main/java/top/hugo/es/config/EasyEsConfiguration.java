package top.hugo.es.config;

import cn.easyes.starter.register.EsMapperScan;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(value = "easy-es.enable", havingValue = "true")
@EsMapperScan("top.hugo.**.esmapper")
public class EasyEsConfiguration {
}