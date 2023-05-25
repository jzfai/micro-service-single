package top.hugo.system.dto;


import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * 平台表 platform dto
 *
 * @author kuanghua
 */

@Data
public class SysPlatformDto {
    /**
     * 平台id(编辑需要传)
     */
    private Integer id;

    /**
     * 平台名字
     */
    @NotBlank(message = "平台名不能为空")
    private String name;
}
