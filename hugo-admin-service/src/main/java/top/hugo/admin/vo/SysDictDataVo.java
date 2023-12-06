package top.hugo.admin.vo;

import lombok.Data;

import java.util.Date;

/**
 * 字典数据表返回实体类
 *
 * @author kuanghua
 * @since 2023-11-13 15:53:03
 */
@Data
public class SysDictDataVo {
    /**
     * 创建者
     */
    private String createBy;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 样式属性（其他样式扩展）
     */
    private String cssClass;
    /**
     * 字典编码
     */
    private Long dictCode;
    /**
     * 字典标签
     */
    private String dictLabel;
    /**
     * 字典排序
     */
    private Integer dictSort;
    /**
     * 字典类型
     */
    private String dictType;
    /**
     * 字典键值
     */
    private String dictValue;
    /**
     * 是否默认（Y是 N否）
     */
    private String isDefault;
    /**
     * 表格回显样式
     */
    private String listClass;
    /**
     * 备注
     */
    private String remark;
    /**
     * 状态（0正常 1停用）
     */
    private String status;
    /**
     * 更新者
     */
    private String updateBy;
    /**
     * 更新时间
     */
    private Date updateTime;
}
