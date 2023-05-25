package top.hugo.system.entity;


import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import top.hugo.common.domain.BaseEntity;

/**
 * 平台表 platform
 *
 * @author kuanghua
 */

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sys_platform")
public class SysPlatform extends BaseEntity {
    private static final long serialVersionUID = 1L;
    /**
     * 平台id
     */
    private Integer id;

    /**
     * 平台名字
     */
    private String name;
}
