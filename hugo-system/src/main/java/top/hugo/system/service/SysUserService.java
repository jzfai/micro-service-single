package top.hugo.system.service;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.hugo.common.constant.UserConstants;
import top.hugo.common.domain.PageQuery;
import top.hugo.common.exception.ServiceException;
import top.hugo.common.page.TableDataInfo;
import top.hugo.common.utils.StringUtils;
import top.hugo.system.entity.SysUser;
import top.hugo.system.entity.SysUserRole;
import top.hugo.system.mapper.SysUserMapper;
import top.hugo.system.mapper.SysUserRoleMapper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Service
public class SysUserService {
    private final SysUserMapper sysUserMapper;
    private final SysUserRoleMapper userRoleMapper;

    public TableDataInfo<SysUser> selectPageUserList(SysUser user, PageQuery pageQuery) {
        List<SysUser> sysUsers = this.selectUserList(user);
//        Page<SysUser> page = sysUserMapper.selectPageUserList(pageQuery.build(), this.buildQueryWrapper(user));
        return TableDataInfo.build(sysUsers);
    }

    public List<SysUser> selectUserList(SysUser user) {
        return sysUserMapper.selectUserList(this.buildQueryWrapper(user));
    }

    private Wrapper<SysUser> buildQueryWrapper(SysUser user) {
        Map<String, Object> params = user.getParams();
        QueryWrapper<SysUser> wrapper = Wrappers.query();
        wrapper.eq("u.del_flag", UserConstants.USER_NORMAL)
                .eq(ObjectUtil.isNotNull(user.getUserId()), "u.user_id", user.getUserId())
                .like(StringUtils.isNotBlank(user.getUserName()), "u.user_name", user.getUserName())
                .eq(StringUtils.isNotBlank(user.getStatus()), "u.status", user.getStatus())
                .like(StringUtils.isNotBlank(user.getPhonenumber()), "u.phonenumber", user.getPhonenumber())
                .between(params.get("beginTime") != null && params.get("endTime") != null,
                        "u.create_time", params.get("beginTime"), params.get("endTime"));
//                .and(ObjectUtil.isNotNull(user.getDeptId()), w -> {
//                    List<SysDept> deptList = deptMapper.selectList(new LambdaQueryWrapper<SysDept>()
//                            .select(SysDept::getDeptId)
//                            .apply(DataBaseHelper.findInSet(user.getDeptId(), "ancestors")));
//                    List<Long> ids = StreamUtils.toList(deptList, SysDept::getDeptId);
//                    ids.add(user.getDeptId());
//                    w.in("u.dept_id", ids);
//                });
        return wrapper;
    }


    /**
     * 校验用户名称是否唯一
     *
     * @param user 用户信息
     * @return 结果
     */
    public String checkUserNameUnique(SysUser user) {
        boolean exist = sysUserMapper.exists(new LambdaQueryWrapper<SysUser>()
                .eq(SysUser::getUserName, user.getUserName())
                .ne(ObjectUtil.isNotNull(user.getUserId()), SysUser::getUserId, user.getUserId()));
        if (exist) {
            return UserConstants.NOT_UNIQUE;
        }
        return UserConstants.UNIQUE;
    }

    /**
     * 校验手机号码是否唯一
     *
     * @param user 用户信息
     * @return
     */
    public String checkPhoneUnique(SysUser user) {
        boolean exist = sysUserMapper.exists(new LambdaQueryWrapper<SysUser>()
                .eq(SysUser::getPhonenumber, user.getPhonenumber())
                .ne(ObjectUtil.isNotNull(user.getUserId()), SysUser::getUserId, user.getUserId()));
        if (exist) {
            return UserConstants.NOT_UNIQUE;
        }
        return UserConstants.UNIQUE;
    }

    /**
     * 校验用户是否允许操作
     *
     * @param user 用户信息
     */
    public void checkUserAllowed(SysUser user) {
        if (ObjectUtil.isNotNull(user.getUserId()) && user.isAdmin()) {
            throw new ServiceException("不允许操作超级管理员用户");
        }
    }

    /**
     * 新增保存用户信息
     *
     * @param user 用户信息
     * @return 结果
     */
    @Transactional(rollbackFor = Exception.class)
    public int insertUser(SysUser user) {
        // 新增用户信息
        int rows = sysUserMapper.insert(user);
        // 新增用户岗位关联
        //insertUserPost(user);
        // 新增用户与角色管理
        insertUserRole(user);
        return rows;
    }

    /**
     * 新增用户角色信息
     *
     * @param user 用户对象
     */
    public void insertUserRole(SysUser user) {
        this.insertUserRole(user.getUserId(), user.getRoleIds());
    }

    /**
     * 新增用户角色信息
     *
     * @param userId  用户ID
     * @param roleIds 角色组
     */
    public void insertUserRole(Long userId, Long[] roleIds) {
        if (ArrayUtil.isNotEmpty(roleIds)) {
            // 新增用户与角色管理
            List<SysUserRole> list = new ArrayList<>(roleIds.length);
            for (Long roleId : roleIds) {
                SysUserRole ur = new SysUserRole();
                ur.setUserId(userId);
                ur.setRoleId(roleId);
                list.add(ur);
            }
            userRoleMapper.insertBatch(list);
        }
    }

    /**
     * 注册用户信息
     *
     * @param user 用户信息
     * @return 结果
     */
    public boolean registerUser(SysUser user) {
        user.setCreateBy(user.getUserName());
        user.setUpdateBy(user.getUserName());
        return sysUserMapper.insert(user) > 0;
    }

    /**
     * 重置用户密码
     *
     * @param userName 用户名
     * @param password 密码
     * @return 结果
     */
    public int resetUserPwd(String userName, String password) {
        return sysUserMapper.update(null,
                new LambdaUpdateWrapper<SysUser>()
                        .set(SysUser::getPassword, password)
                        .eq(SysUser::getUserName, userName));
    }

    /**
     * 通过用户ID删除用户
     *
     * @param userId 用户ID
     * @return 结果
     */
    @Transactional(rollbackFor = Exception.class)
    public int deleteUserById(Long userId) {
        // 删除用户与角色关联
        userRoleMapper.delete(new LambdaQueryWrapper<SysUserRole>().eq(SysUserRole::getUserId, userId));
        // 删除用户与岗位表
        //userPostMapper.delete(new LambdaQueryWrapper<SysUserPost>().eq(SysUserPost::getUserId, userId));
        return sysUserMapper.deleteById(userId);
    }

    /**
     * 批量删除用户信息
     *
     * @param userIds 需要删除的用户ID
     * @return 结果
     */
    @Transactional(rollbackFor = Exception.class)
    public int deleteUserByIds(Long[] userIds) {
        for (Long userId : userIds) {
            checkUserAllowed(new SysUser(userId));
            //checkUserDataScope(userId);
        }
        List<Long> ids = Arrays.asList(userIds);
        // 删除用户与角色关联
        userRoleMapper.delete(new LambdaQueryWrapper<SysUserRole>().in(SysUserRole::getUserId, ids));
        // 删除用户与岗位表
        //userPostMapper.delete(new LambdaQueryWrapper<SysUserPost>().in(SysUserPost::getUserId, ids));
        return sysUserMapper.deleteBatchIds(ids);
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

    /**
     * 重置用户密码
     *
     * @param user 用户信息
     * @return 结果
     */
    public int resetPwd(SysUser user) {
        return sysUserMapper.updateById(user);
    }


    /**
     * 根据条件分页查询已分配用户角色列表
     *
     * @param user 用户信息
     * @return 用户信息集合信息
     */
    public TableDataInfo<SysUser> selectAllocatedList(SysUser user, PageQuery pageQuery) {
        QueryWrapper<SysUser> wrapper = Wrappers.query();
        wrapper.eq("u.del_flag", UserConstants.USER_NORMAL)
                .eq(ObjectUtil.isNotNull(user.getRoleId()), "r.role_id", user.getRoleId())
                .like(StringUtils.isNotBlank(user.getUserName()), "u.user_name", user.getUserName())
                .eq(StringUtils.isNotBlank(user.getStatus()), "u.status", user.getStatus())
                .like(StringUtils.isNotBlank(user.getPhonenumber()), "u.phonenumber", user.getPhonenumber());
        Page<SysUser> page = sysUserMapper.selectAllocatedList(pageQuery.build(), wrapper);
        return TableDataInfo.build(page);
    }

    /**
     * 根据条件分页查询未分配用户角色列表
     *
     * @param user 用户信息
     * @return 用户信息集合信息
     */
    public TableDataInfo<SysUser> selectUnallocatedList(SysUser user, PageQuery pageQuery) {
        List<Long> userIds = userRoleMapper.selectUserIdsByRoleId(user.getRoleId());
        QueryWrapper<SysUser> wrapper = Wrappers.query();
        wrapper.eq("u.del_flag", UserConstants.USER_NORMAL)
                .and(w -> w.ne("r.role_id", user.getRoleId()).or().isNull("r.role_id"))
                .notIn(CollUtil.isNotEmpty(userIds), "u.user_id", userIds)
                .like(StringUtils.isNotBlank(user.getUserName()), "u.user_name", user.getUserName())
                .like(StringUtils.isNotBlank(user.getPhonenumber()), "u.phonenumber", user.getPhonenumber());
        Page<SysUser> page = sysUserMapper.selectUnallocatedList(pageQuery.build(), wrapper);
        return TableDataInfo.build(page);
    }
}