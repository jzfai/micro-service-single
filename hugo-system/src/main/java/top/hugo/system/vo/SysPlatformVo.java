package top.hugo.system.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;
import top.hugo.common.domain.BaseEntity;

import javax.validation.constraints.NotBlank;

/**
 * 平台管理
 *
 * @author kuanghua
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class SysPlatformVo extends BaseEntity {
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
