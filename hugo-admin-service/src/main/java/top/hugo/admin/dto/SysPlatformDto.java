package top.hugo.admin.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * 平台请求接收类
 *
 * @author kuanghua
 * @since 2023-11-10 11:54:52
 */
@Data
public class SysPlatformDto {
    /**
     * 主键
     */
    private Integer id;
    /**
     * 平台的名字
     */
    @NotBlank(message = "name不能为空")
    private String name;
}