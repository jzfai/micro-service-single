package top.hugo.satoken.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Security 配置属性
 *
 * @author kuanghua
 */
@Data
@Component
@ConfigurationProperties(prefix = "security")
public class SaTokenSecurityProperties {
    /**
     * 排除路径
     */
    private String[] excludes;
}