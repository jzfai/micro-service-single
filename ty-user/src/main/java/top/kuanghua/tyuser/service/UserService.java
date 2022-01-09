package top.kuanghua.tyuser.service;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import top.kuanghua.feign.tyauth.feign.TokenFeign;
import top.kuanghua.feign.tyexecl.feign.ExcelFeign;
import top.kuanghua.khcomomon.entity.ResResult;
import top.kuanghua.khcomomon.utils.CodecUtils;
import top.kuanghua.khcomomon.utils.ObjectUtilsSelf;
import top.kuanghua.tyuser.entity.User;
import top.kuanghua.tyuser.mapper.UserMapper;

import javax.annotation.Resource;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * (User)
 *
 * @author kuanghua
 * @since 2020-10-27 20:54:24
 */
@Service
@Slf4j
public class UserService {
    @Resource
    private UserMapper userMapper;

    @Resource
    private ExcelFeign excelFeign;

    @Resource
    private TokenFeign tokenFeign;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    public void registry(User user, String code) throws Exception {
        QueryWrapper<User> queryWrapper = new QueryWrapper<User>()
                .select("phone")
                .eq("phone", user.getPhone());
        Boolean bl = this.userMapper.selectCount(queryWrapper) == 1;
        if (bl) {
            throw new Exception("手机号已存在");
        }
        String codeStr = stringRedisTemplate.opsForValue().get("user:code:phone:" + user.getPhone());
        if (StringUtils.isEmpty(codeStr)) {
            throw new Exception("验证码失效，请重新获取");
        }
        if (!Objects.equals(codeStr, code)) {
            throw new Exception("验证码输入错误");
        }
        String salt = CodecUtils.generateSalt();
        user.setSalt(salt);
        user.setPassword(CodecUtils.md5Hex(user.getPassword(), salt));
        boolean insertBl = this.userMapper.insert(user) == 1;
        if (insertBl) {
            //删除redis中的code
            this.stringRedisTemplate.delete("user:code:phone:" + user.getPhone());
        } else {
            throw new Exception("用户注册失败");
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public Object insert(User user) {
        return this.userMapper.insert(user);
    }


    @Transactional(rollbackFor = Exception.class)
    public Object updateById(User user) {
        int updateById = this.userMapper.updateById(user);
        int i = 10 / 0;
        return updateById;
    }

    /**
     * 登出
     */
    public void loginOut() {
        return;
    }

    /**
     * 用户注册
     *
     * @param code
     * @param username
     * @param password
     */
    public void userRegister(String email, String code, String username, String password) {

        //user表中查询用户是否重复
        QueryWrapper<User> queryWrapper = new QueryWrapper<User>()
                .eq("username", username);
        if (this.userMapper.selectCount(queryWrapper) == 1) {
            throw new RuntimeException(MessageFormat.format("账号【{0}】已存在，请误重复注册", username));
        }

        //去ty-execl中查询用户名是否存在
        ResResult resResult = excelFeign.selectExcelByUser(username);
        List list = ObjectUtilsSelf.parseResToList(resResult);
        if (list.size() == 0) {
            throw new RuntimeException(MessageFormat.format("考勤表中不存在用户名【{0}】", username));
        }

        //校验验证码
        String codeInfo = stringRedisTemplate.opsForValue().get("email.code.routing.name" + email);
        Map map = JSON.parseObject(codeInfo);
        if (ObjectUtilsSelf.isEmpty(map)) {
            throw new RuntimeException("验证码失效，请重新获取");
        }
        if (!Objects.equals(map.get("code"), code)) {
            throw new RuntimeException("验证码不正确");
        }
        //插入数据，用户添加成功
        User userEt = new User();
        userEt.setPassword(password);
        userEt.setEmail(email);
        userEt.setUsername(username);
        //添加盐设置密码为md5
        String salt = CodecUtils.generateSalt();
        userEt.setSalt(salt);
        userEt.setPassword(CodecUtils.md5Hex(userEt.getPassword(), salt));
        boolean inResult = userMapper.insert(userEt) == 1;
        if (!inResult) {
            throw new RuntimeException("插入失败");
        }
    }

    /**
     * 用户登录
     *
     * @param user
     * @return
     * @throws Exception
     */
    public HashMap<String, Object> loginValid(User user) {
        QueryWrapper<User> phoneQW = new QueryWrapper<User>().like("email", user.getEmail())
                .or().eq("username", user.getUsername());
        User resUser = this.userMapper.selectOne(phoneQW);
        if (ObjectUtils.isEmpty(resUser)) {
            throw new RuntimeException("用户不存在");
        }

        if (!resUser.getPassword().equals(CodecUtils.md5Hex(user.getPassword(), resUser.getSalt()))) {
            throw new RuntimeException("用户名或密码错误");
        }
        //生成jwt token
        HashMap<String, Object> hm = new HashMap<>();
        hm.put("username", resUser.getUsername());
        hm.put("email", resUser.getEmail());
        ResResult resResult = tokenFeign.generateToken(hm);

        hm.put("jwtToken", resResult.getData());
        return hm;
    }

    /*修改用户密码*/
    public void changePassword(String username, String oldPassword, String newPassword) {
        //校验oldPassword是否正确
        QueryWrapper<User> qw = new QueryWrapper<User>()
                .eq("username", username);

        User resUser = this.userMapper.selectOne(qw);
        if (ObjectUtilsSelf.isEmpty(resUser)) {
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
     * 插入用户
     *
     * @param username 用户名
     */
    public void insertUser(String username) {
        //先查询当前用户是否存在不存在则导入
        QueryWrapper<User> qw = new QueryWrapper<User>()
                .eq("username", username);

        User resUser = this.userMapper.selectOne(qw);
        if (ObjectUtilsSelf.isEmpty(resUser)) {
            //添加盐设置密码为md5
            User user = new User();
            user.setPassword("123456");
            user.setUsername(username);
            String salt = CodecUtils.generateSalt();
            user.setSalt(salt);
            user.setPassword(CodecUtils.md5Hex(user.getPassword(), salt));
            userMapper.insert(user);
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
}