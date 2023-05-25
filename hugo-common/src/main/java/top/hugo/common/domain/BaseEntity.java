package top.hugo.common.domain;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * Entity基类
 *
 * @author kuanghua
 */

@Data
public class BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 搜索值（不用传）
     */
    @JsonIgnore
    @TableField(exist = false)
    @Schema(hidden = true)
    private String searchValue;

    /**
     * 创建者（不用传）
     */
    @Schema(hidden = true)
    @TableField(fill = FieldFill.INSERT)
    private String createBy;

    /**
     * 创建时间（不用传）
     */
    @Schema(hidden = true)
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    /**
     * 更新者（不用传）
     */
    @Schema(hidden = true)
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private String updateBy;

    /**
     * 更新时间（不用传）
     */
    @Schema(hidden = true)
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;


    /**
     * 开始时间（不用传）
     */
    @Schema(hidden = true)
    @TableField(exist = false)
    private String beginTime;

    /**
     * 结束时间（不用传）
     */
    @Schema(hidden = true)
    @TableField(exist = false)
    private String endTime;
}
