package top.hugo.admin.service;


import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.hugo.admin.dto.SysUserDto;
import top.hugo.admin.entity.SysUser;
import top.hugo.admin.entity.SysUserPost;
import top.hugo.admin.entity.SysUserRole;
import top.hugo.admin.mapper.SysUserMapper;
import top.hugo.admin.mapper.SysUserPostMapper;
import top.hugo.admin.mapper.SysUserRoleMapper;
import top.hugo.admin.query.SysUserQuery;
import top.hugo.admin.vo.SysUserDetailVo;
import top.hugo.admin.vo.SysUserVo;
import top.hugo.common.utils.BeanCopyUtils;
import top.hugo.domain.TableDataInfo;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 用户 服务层处理
 *
 * @author kuanghua
 */
@RequiredArgsConstructor
@Service
public class SysUserService {
    private final SysUserMapper sysUserMapper;

    private final SysUserPostMapper sysUserPostMapper;
    private final SysUserRoleMapper sysUserRoleMapper;

    public TableDataInfo<SysUserVo> selectPageSysUserList(SysUserQuery sysUserQuery) {
        LambdaQueryWrapper<SysUser> lqw = getQueryWrapper(sysUserQuery);
        IPage<SysUserVo> page = sysUserMapper.selectVoPage(sysUserQuery.build(), lqw);
        return TableDataInfo.build(page);
    }


    /**
     * 查询用户集合
     *
     * @param sysUserQuery 用户
     * @return 用户集合
     */

    public List<SysUserVo> selectSysUserList(SysUserQuery sysUserQuery) {
        LambdaQueryWrapper<SysUser> lqw = getQueryWrapper(sysUserQuery);
        return sysUserMapper.selectVoList(lqw);
    }


    /**
     * 查询wrapper封装
     *
     * @return
     */
    private static LambdaQueryWrapper<SysUser> getQueryWrapper(SysUserQuery sysUserQuery) {
        LambdaQueryWrapper<SysUser> lqw = new LambdaQueryWrapper<SysUser>();
        lqw.like(ObjectUtil.isNotEmpty(sysUserQuery.getNickName()), SysUser::getNickName, sysUserQuery.getNickName());
        lqw.like(ObjectUtil.isNotEmpty(sysUserQuery.getPhonenumber()), SysUser::getPhonenumber, sysUserQuery.getPhonenumber());
        lqw.like(ObjectUtil.isNotEmpty(sysUserQuery.getStatus()), SysUser::getStatus, sysUserQuery.getStatus());
        lqw.like(ObjectUtil.isNotEmpty(sysUserQuery.getUserType()), SysUser::getUserType, sysUserQuery.getUserType());
        return lqw;
    }

    /**
     * 查询所有平台
     *
     * @return 平台列表
     */
    public List<SysUserVo> selectSysUserAll() {
        return sysUserMapper.selectVoList();
    }

    /**
     * 通过平台ID查询用户
     *
     * @param sysUserId 平台ID
     * @return 角色对象信息
     */

    public SysUserDetailVo selectSysUserById(Long sysUserId) {
        LambdaQueryWrapper<SysUserPost> lqw = new LambdaQueryWrapper<>();
        lqw.eq(ObjectUtil.isNotEmpty(sysUserId), SysUserPost::getUserId, sysUserId);

        //查询用户岗位表
        Set<Long> postIds = this.sysUserPostMapper.selectList(lqw).stream().map(m -> m.getPostId()).collect(Collectors.toSet());

        //查询用户角色
        LambdaQueryWrapper<SysUserRole> lq = new LambdaQueryWrapper<>();
        lq.eq(ObjectUtil.isNotEmpty(sysUserId), SysUserRole::getUserId, sysUserId);
        Set<Long> roleIds = this.sysUserRoleMapper.selectList(lq).stream().map(m -> m.getRoleId()).collect(Collectors.toSet());
        //查询用户
        SysUser sysUser = sysUserMapper.selectById(sysUserId);
        //拼接数据
        SysUserDetailVo sysUserDetailVo = BeanCopyUtils.copy(sysUser, SysUserDetailVo.class);
        sysUserDetailVo.setPostIds(postIds);
        sysUserDetailVo.setRoleIds(roleIds);
        return sysUserDetailVo;
    }


    /**
     * 删除用户
     *
     * @param sysUserId 平台ID
     * @return 结果
     */

    public int deleteSysUserById(Long sysUserId) {
        return sysUserMapper.deleteById(sysUserId);
    }

    /**
     * 批量删除用户
     *
     * @param sysUserIds 需要删除的平台ID
     * @return 结果
     */
    public int deleteSysUserByIds(Long[] sysUserIds) {
        return sysUserMapper.deleteBatchIds(Arrays.asList(sysUserIds));
    }

    /**
     * 新增保存用户
     *
     * @param sysUserDto 用户
     * @return 结果
     */

    @Transactional(rollbackFor = Exception.class)
    public int insertSysUser(SysUserDto sysUserDto) {
        //检验手机号是否唯一
        LambdaQueryWrapper<SysUser> lqw = new LambdaQueryWrapper<>();
        lqw.eq(ObjectUtil.isNotEmpty(sysUserDto.getPhonenumber()), SysUser::getPhonenumber, sysUserDto.getPhonenumber());
        if (sysUserMapper.exists(lqw)) {
            throw new RuntimeException("手机号已存在");
        }
        //新增用户表
        SysUser sysUser = BeanCopyUtils.copy(sysUserDto, SysUser.class);
        int insert = sysUserMapper.insert(sysUser);
        //新增用户岗位表
        List<SysUserPost> userPostList = sysUserDto.getUserPostList();
        userPostList.forEach(f -> f.setUserId(sysUser.getUserId()));
        sysUserPostMapper.insertBatch(userPostList, 10);
        //新增用户角色表
        List<SysUserRole> userRoleList = sysUserDto.getUserRoleList();
        userRoleList.forEach(f -> f.setUserId(sysUser.getUserId()));
        sysUserRoleMapper.insertBatch(userRoleList, 10);
        return insert;
    }


    /**
     * 修改保存用户
     *
     * @param sysUserDto 用户
     * @return 结果
     */

    @Transactional(rollbackFor = Exception.class)
    public int updateSysUser(SysUserDto sysUserDto) {
        //删除用户岗位表
        sysUserPostMapper.deleteById(sysUserDto.getUserId());
        //删除用户角色表
        sysUserRoleMapper.deleteById(sysUserDto.getUserId());
        //新增用户岗位表
        List<SysUserPost> userPostList = sysUserDto.getUserPostList();
        userPostList.forEach(f -> f.setUserId(sysUserDto.getUserId()));
        sysUserPostMapper.insertBatch(userPostList, 10);
        //新增用户角色表
        List<SysUserRole> userRoleList = sysUserDto.getUserRoleList();
        userRoleList.forEach(f -> f.setUserId(sysUserDto.getUserId()));
        sysUserRoleMapper.insertBatch(userRoleList, 10);
        //更新用户
        return sysUserMapper.updateById(BeanCopyUtils.copy(sysUserDto, SysUser.class));
    }

    /**
     * 修改用户状态
     *
     * @param user 用户信息
     * @return 结果
     */
    public int updateUserStatus(SysUser user) {
        return sysUserMapper.updateById(user);
    }
}