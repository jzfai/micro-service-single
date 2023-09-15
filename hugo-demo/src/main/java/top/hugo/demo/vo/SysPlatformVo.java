package top.hugo.demo.vo;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * 平台管理
 *
 * @author kuanghua
 */
@Data
public class SysPlatformVo {
    /**
     * 平台id
     */
    private Integer id;
    /**
     * 平台名字
     */
    @NotBlank(message = "平台名字不能为空")
    private String name;
}