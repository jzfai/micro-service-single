package top.kuanghua.basisfunc.mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import top.kuanghua.basisfunc.entity.Permission;
import top.kuanghua.basisfunc.vo.PermissionVo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
*  权限Mapper
*
* @author 熊猫哥
* @since 2022-10-07 16:33:13
*/

public interface PermissionMapper extends BaseMapper< Permission > {

}
