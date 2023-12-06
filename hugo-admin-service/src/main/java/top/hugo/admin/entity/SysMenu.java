package top.hugo.admin.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.util.Date;

/**
 * 菜单权限表实体类
 *
 * @author kuanghua
 * @since 2023-11-16 10:14:25
 */
@Data
@TableName(value = "sys_menu")
public class SysMenu {
    //当前路由需要激活的菜单
    private String activeMenu;
    //当一条数据时是否显示子菜单:0=不显示,1=显示
    private Integer alwaysShow;
    //组件路径
    private String component;
    //创建者
    @TableField(fill = FieldFill.INSERT)
    private String createBy;

    //创建时间
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    //菜单图标
    private String icon;
    //是否为外链（0是 1否）
    private Integer isFrame;
    //菜单ID
    @TableId(type = IdType.ASSIGN_ID)
    private Long menuId;
    //菜单名称
    private String menuName;
    //菜单类型（M目录 C菜单 F按钮）
    private String menuType;
    //meta中的配置参数
    private String metaExtra;
    //显示顺序
    private Integer orderNum;
    //父菜单ID
    private Long parentId;
    //路由地址
    private String path;
    //权限标识
    private String perms;
    //平台id
    private Integer platformId;
    //重定向地址
    private String redirect;
    //备注
    private String remark;
    //路由名字
    private String routeName;
    //菜单状态（0正常 1停用）
    private String status;
    //更新者
    @TableField(fill = FieldFill.UPDATE)
    private String updateBy;

    //更新时间
    @TableField(fill = FieldFill.UPDATE)
    private Date updateTime;

    //显示状态（0显示 1隐藏）
    private String visible;

}
