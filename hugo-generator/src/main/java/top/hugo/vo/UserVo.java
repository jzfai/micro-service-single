package top.hugo.vo;

import lombok.Data;

import java.util.Date;
/**
*  用户实体类
*
* @author 熊猫哥
* @since 2022-10-07 16:33:13
*/
@Data
public class UserVo  {
private Long id;
private String name;
private String headImgUrl;
private String phone;
private String roleId;
private Date createTime;
private String creator;
private Date updateTime;
private String editor;
private Integer deleted;
}
