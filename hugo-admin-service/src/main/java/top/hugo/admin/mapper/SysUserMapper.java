package top.hugo.admin.mapper;

import org.apache.ibatis.annotations.Param;
import top.hugo.admin.entity.SysPost;
import top.hugo.admin.entity.SysUser;
import top.hugo.admin.vo.SysUserVo;
import top.hugo.db.mapper.BaseMapperPlus;

import java.util.List;

/**
 * 用户信息表Mapper
 *
 * @author kuanghua
 * @since 2023-11-20 14:48:14
 */

public interface SysUserMapper extends BaseMapperPlus<SysUserMapper, SysUser, SysUserVo> {
    List<SysPost> selectPostByUserId(@Param("userId") Long userId);
}
