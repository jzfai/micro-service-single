package top.kuanghua.basisfunc.service;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.stereotype.Service;
import top.kuanghua.authpom.service.TokenService;
import top.kuanghua.basisfunc.entity.Role;
import top.kuanghua.basisfunc.entity.User;
import top.kuanghua.basisfunc.mapper.RoleMapper;
import top.kuanghua.basisfunc.mapper.UserMapper;
import top.kuanghua.basisfunc.utils.CodecUtils;
import top.kuanghua.basisfunc.vo.PermissionVo;
import top.kuanghua.commonpom.utils.ObjSelfUtils;
import top.kuanghua.commonpom.utils.SelfObjUtils;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 用户Service
 *
 * @author 熊猫哥
 * @since 2022-10-07 16:33:13
 */
@Service
public class UserService {

    @Resource
    private UserMapper userMapper;

    @Resource
    private RoleMapper roleMapper;

    @Resource
    private PermissionService permissionService;

    @Resource
    private TokenService tokenService;

    public Page<User> selectPage(Integer pageNum, Integer pageSize, QueryWrapper<User> queryWrapper) {
        return this.userMapper.selectPage(new Page<User>(pageNum, pageSize), queryWrapper);
    }

    public User selectById(Long id) {
        return this.userMapper.selectById(id);
    }

    public List<User> selectBatchIds(List<Long> idList) {
        return this.userMapper.selectBatchIds(idList);
    }

    /**
     * 根据用户id获取权限
     *
     * @param userId      用户id
     * @param plateFormId 平台id
     * @author 猫哥
     * @date 2022/10/22 16:27
     */
    public List<PermissionVo> getPermissionByUserId(Long userId, Integer plateFormId) {
        User user = this.userMapper.selectById(userId);
        String roleId = user.getRoleId();
        List<Long> parseArray = JSON.parseArray(roleId, Long.class);
        //定义一个list用于收集permission id
        HashSet<Long> userPermissionSet = new HashSet<>();
        List<Role> roleList = roleMapper.selectBatchIds(parseArray);
        roleList.forEach(fItem -> {
            Map<Integer, List<Long>> permissionMap = JSON.parseObject(fItem.getPermissionId(), Map.class);
            permissionMap.entrySet().stream().forEach(mapEntry -> {
                if (ObjSelfUtils.toInt(mapEntry.getKey()) == plateFormId) {
                    userPermissionSet.addAll(mapEntry.getValue());
                }
            });
        });

        List<PermissionVo> permissionVoList = permissionService.selectList(plateFormId);

        return getPermissionVoList(permissionVoList, userPermissionSet);
    }

    private List<PermissionVo> getPermissionVoList(List<PermissionVo> permissionVoList, HashSet<Long> userPermissionSet) {
        //筛选权限
        List<PermissionVo> listStreamFilter = permissionVoList.stream().filter((ftItem) -> {
            if (SelfObjUtils.isNotEmpty(ftItem.getParentNode()) && ftItem.getParentNode() == 1) {
                ftItem.setChildren(getPermissionVoList(ftItem.getChildren(), userPermissionSet));
            }
            boolean contains = userPermissionSet.contains(ObjSelfUtils.toInt(ftItem.getId()));
            return contains;
        }).collect(Collectors.toList());
        return listStreamFilter;
    }

    /**
     * 根据用户id获取codes
     *
     * @param userId      用户id
     * @param plateFormId 平台id
     * @author 猫哥
     * @date 2022/10/22 16:27
     */
    public HashSet<Long> getCodesByUserId(Long userId, Integer plateFormId) {
        User user = this.userMapper.selectById(userId);
        String roleId = user.getRoleId();
        List<Long> parseArray = JSON.parseArray(roleId, Long.class);
        //定义一个list用于收集permission id
        HashSet<Long> userPermissionSet = new HashSet<>();
        List<Role> roleList = roleMapper.selectBatchIds(parseArray);
        roleList.forEach(fItem -> {
            Map<Integer, List<Long>> permissionMap = JSON.parseObject(fItem.getPermissionId(), Map.class);
            permissionMap.entrySet().stream().forEach(mapEntry -> {
                if (ObjSelfUtils.toInt(mapEntry.getKey()) == plateFormId) {
                    userPermissionSet.addAll(mapEntry.getValue());
                }
            });
        });

        return userPermissionSet;
    }

    public int insert(User user) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<User>().eq("phone", user.getPhone());
        if (this.userMapper.selectCount(queryWrapper) == 1) {
            throw new RuntimeException("手机号已存在");
        }
        String salt = CodecUtils.generateSalt();
        user.setSalt(salt);
        user.setPassword(CodecUtils.md5Hex("123456", salt));
        return this.userMapper.insert(user);
    }

    public int updateById(User user) {
        return this.userMapper.updateById(user);
    }

    public int deleteById(Long id) {
        return this.userMapper.deleteById(id);
    }

    public int deleteBatchIds(List<Long> idList) {
        return this.userMapper.deleteBatchIds(idList);
    }

    /**
     * 用户登录
     *
     * @param keyword 用户名或手机号
     * @return
     */
    public HashMap<String, Object> loginValid(String keyword, String password) {
        QueryWrapper<User> phoneQW = new QueryWrapper<User>().like("phone", keyword).or().like("name", keyword);
        User resUser = this.userMapper.selectOne(phoneQW);
        if (SelfObjUtils.isEmpty(resUser)) {
            throw new RuntimeException("用户不存在");
        }
        if (!resUser.getPassword().equals(CodecUtils.md5Hex(password, resUser.getSalt()))) {
            throw new RuntimeException("用户名或密码错误");
        }
        //生成jwt token
        HashMap<String, Object> hm = new HashMap<>();
        resUser.setPassword(null);
        resUser.setSalt(null);
        hm.put("userInfo", resUser);
        String generateToken = tokenService.generateToken(hm);
        hm.put("jwtToken", generateToken);
        return hm;
    }

    /**
     * 修改用户密码
     *
     * @author 熊猫哥
     * @date 2022/10/22 16:21
     */
    public void changePassword(String username, String oldPassword, String newPassword) {
        //校验oldPassword是否正确
        QueryWrapper<User> qw = new QueryWrapper<User>()
                .eq("username", username);

        User resUser = this.userMapper.selectOne(qw);
        if (SelfObjUtils.isEmpty(resUser)) {
            throw new RuntimeException("用户不存在");
        }
        if (!resUser.getPassword().equals(CodecUtils.md5Hex(oldPassword, resUser.getSalt()))) {
            throw new RuntimeException("密码错误");
        }

        //设置新的密码
        String salt = CodecUtils.generateSalt();
        resUser.setSalt(salt);
        resUser.setPassword(CodecUtils.md5Hex(newPassword, salt));
        boolean insertBl = this.userMapper.updateById(resUser) == 1;
        if (!insertBl) {
            throw new RuntimeException("修改密码失败");
        }
    }

    /**
     * 重置用用户名：如果用用户存在先删除原有用户在新增一个用户，没有则新建用户
     */
    public void resetUser(String username) {
        QueryWrapper<User> userWrap = new QueryWrapper<User>().eq("username", username);
        List<User> users = userMapper.selectList(userWrap);
        //用户存在,先删除用户
        if (users.size() == 1) {
            userMapper.delete(userWrap);
        } else {
            //新增用户
            this.insertUser(username);
        }
    }

    /**
     * 插入用户
     *
     * @param username 用户名
     */
    public void insertUser(String username) {
        //先查询当前用户是否存在不存在则导入
        QueryWrapper<User> qw = new QueryWrapper<User>().eq("username", username);
        User resUser = this.userMapper.selectOne(qw);
        if (SelfObjUtils.isEmpty(resUser)) {
            //添加盐设置密码为md5
            User user = new User();
            user.setPassword("123456");
            user.setName(username);
            String salt = CodecUtils.generateSalt();
            user.setSalt(salt);
            user.setPassword(CodecUtils.md5Hex(user.getPassword(), salt));
            userMapper.insert(user);
        }
    }


    /**
     * 获取角色code数组
     *
     * @param userId 用户id
     * @return
     */
    public List<String> getRoleCodeArrByUserId(Long userId) {
        //先查询当前用户是否存在不存在则导入
        User user = this.userMapper.selectById(userId);
        if (SelfObjUtils.isNotEmpty(user)) {
            List<Long> parseArray = JSON.parseArray(user.getRoleId(), Long.class);
            List<Role> roleList = this.roleMapper.selectBatchIds(parseArray);
            return roleList.stream().map(mItem -> {
                return mItem.getCode();
            }).collect(Collectors.toList());

        }
        return null;
    }

    /**
     * 登出
     */
    public void loginOut() {
        return;
    }
}
