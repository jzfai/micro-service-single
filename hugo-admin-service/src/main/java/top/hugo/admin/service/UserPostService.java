package top.hugo.admin.service;


import cn.hutool.core.util.ObjectUtil;
import top.hugo.admin.mapper.UserPostMapper;
import top.hugo.admin.query.UserPostQuery;
import top.hugo.admin.vo.UserPostVo;
import top.hugo.domain.TableDataInfo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * 平台信息 服务层处理
 *
 * @author kuanghua
 */
@RequiredArgsConstructor
@Service
public class UserPostService {
    private final UserPostMapper userPostMapper;

    public TableDataInfo<UserPostVo> selectPageUserPostList(UserPostQuery userPostQuery) {
        QueryWrapper<UserPostVo> wrapper = new QueryWrapper<>();
        wrapper.eq("su.del_flag", "0");
        // 用户账号
        if (ObjectUtil.isNotEmpty(userPostQuery.getUserName())) {
            wrapper.like("su.user_name", userPostQuery.getUserName());
        }
        // 手机号码
        if (ObjectUtil.isNotEmpty(userPostQuery.getPhonenumber())) {
            wrapper.like("su.phonenumber", userPostQuery.getPhonenumber());
        }
        // 帐号状态（0正常 1停用）
        if (ObjectUtil.isNotEmpty(userPostQuery.getStatus())) {
            wrapper.like("su.status", userPostQuery.getStatus());
        }
        // 创建时间
        if (ObjectUtil.isNotEmpty(userPostQuery.getBeginTime()) && ObjectUtil.isNotEmpty(userPostQuery.getEndTime())) {
            wrapper.between("su.create_time", userPostQuery.getBeginTime(), userPostQuery.getEndTime());
        }
        // 部门id
        if (ObjectUtil.isNotEmpty(userPostQuery.getDeptId())) {
            wrapper.eq("sd.dept_id", userPostQuery.getDeptId());
        }
        IPage<UserPostVo> page = userPostMapper.selectPageUserPostList(userPostQuery.build(), wrapper);
        return TableDataInfo.build(page);
    }

}
