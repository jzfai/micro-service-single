package top.hugo.admin.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.util.Date;

/**
 * 字典数据表实体类
 *
 * @author kuanghua
 * @since 2023-11-13 16:23:36
 */
@Data
@TableName(value = "sys_dict_data")
public class SysDictData {
    //创建者
    @TableField(fill = FieldFill.INSERT)
    private String createBy;

    //创建时间
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    //样式属性（其他样式扩展）
    private String cssClass;
    //字典编码
    @TableId(type = IdType.ASSIGN_ID)
    private Long dictCode;
    //字典标签
    private String dictLabel;
    //字典排序
    private Integer dictSort;
    //字典类型
    private String dictType;
    //字典键值
    private String dictValue;

    //是否默认（Y是 N否）
    private String isDefault;
    //表格回显样式
    private String listClass;
    //备注
    private String remark;
    //状态（0正常 1停用）
    private String status;
    //更新者
    @TableField(fill = FieldFill.UPDATE)
    private String updateBy;

    //更新时间
    @TableField(fill = FieldFill.UPDATE)
    private Date updateTime;
}
