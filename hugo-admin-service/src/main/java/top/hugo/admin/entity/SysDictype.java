package top.hugo.admin.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.util.Date;

/**
 * 字典类型表实体类
 *
 * @author kuanghua
 * @since 2023-11-13 10:58:29
 */
@Data
@TableName(value = "sys_dict_type")
public class SysDictype {
    //字典主键
    @TableId(type = IdType.ASSIGN_ID)
    private Long dictId;
    //字典名称
    private String dictName;
    //字典类型
    private String dictType;
    //状态（0正常 1停用）
    private String status;
    //创建者
    @TableField(fill = FieldFill.INSERT)
    private String createBy;

    //创建时间
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    //更新者
    @TableField(fill = FieldFill.UPDATE)
    private String updateBy;

    //更新时间
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;

    //备注
    private String remark;

}
