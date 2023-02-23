






package top.hugo.vo;


import lombok.Data;
import top.hugo.entity.Permission;

import java.util.List;
/**
*  权限实体类
*
* @author 熊猫哥
* @since 2022-10-07 16:33:13
*/
@Data
public class PermissionVo  extends Permission {
    private List<PermissionVo> Children;
}
