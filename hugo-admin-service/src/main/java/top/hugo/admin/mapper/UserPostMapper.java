package top.hugo.admin.mapper;

import top.hugo.admin.vo.UserPostVo;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

public interface UserPostMapper {
    IPage<UserPostVo> selectPageUserPostList(@Param("page") Page<UserPostVo> page, @Param(Constants.WRAPPER) Wrapper<UserPostVo> queryWrapper);
}
