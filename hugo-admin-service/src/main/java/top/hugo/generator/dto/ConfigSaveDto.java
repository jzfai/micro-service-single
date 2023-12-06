package top.hugo.generator.dto;


import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * 代码生成配置保存请求接收类
 *
 * @author kuanghua
 * @since 2023-10-18 11:42:26
 */
@Data
public class ConfigSaveDto {
    /**
     * 配置数据
     */
    private String generatorConfig;
    /**
     * 配置名称
     */
    @NotBlank(message = "name不能为空")
    private String name;
}