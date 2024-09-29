


package top.hugo.admin.dto;

import lombok.Data;

import java.util.Date;
/**
*  菜单权限表请求接收类
*
* @author kuanghua
* @since 2023-11-16 10:14:25
*/
@Data
public class SysMenuDto {
  /**
   * 当前路由需要激活的菜单
  */
    private String activeMenu;
  /**
   * 当一条数据时是否显示子菜单
  */
    private Integer alwaysShow;
  /**
   * 组件路径
  */
    private String component;
  /**
   * 创建者
  */
    private String createBy;
  /**
   * 创建时间
  */
    private Date createTime;
  /**
   * 菜单图标
  */
    private String icon;
  /**
   * 是否为外链（0是 1否）
  */
    private Integer isFrame;
  /**
   * 菜单ID
  */
    private Long menuId;
  /**
   * 菜单名称
  */
    private String menuName;
  /**
   * 菜单类型（M目录 C菜单 F按钮）
  */
    private String menuType;
  /**
   * meta中的配置参数
  */
    private String metaExtra;
  /**
   * 显示顺序
  */
    private Integer orderNum;
  /**
   * 父菜单ID
  */
    private Long parentId;
  /**
   * 路由地址
  */
    private String path;
  /**
   * 权限标识
  */
    private String perms;
  /**
   * 平台id
  */
    private Integer platformId;
  /**
   * 重定向地址
  */
    private String redirect;
  /**
   * 备注
  */
    private String remark;
  /**
   * 路由名字
  */
    private String routeName;
  /**
   * 菜单状态（0正常 1停用）
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
  /**
   * 显示状态（0显示 1隐藏）
  */
    private String visible;
}