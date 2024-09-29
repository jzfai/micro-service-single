package top.hugo.admin.mapper;

import top.hugo.admin.entity.SysUser;
import top.hugo.admin.vo.SysUserVo;
import top.hugo.db.mapper.BaseMapperPlus;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

/**
 * 用户信息表Mapper
 *
 * @author kuanghua
 * @since 2023-11-20 14:48:14
 */

public interface SysUserMapper extends BaseMapperPlus<SysUserMapper, SysUser, SysUserVo> {
    Page<SysUserVo> selectUserAndPostList(@Param("page") Page<SysUser> page, @Param(Constants.WRAPPER) Wrapper<SysUser> queryWrapper);
}
