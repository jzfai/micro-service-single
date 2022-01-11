package top.kuanghua.integrationfront.controller;


import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;
import top.kuanghua.feign.tyauth.feign.TokenFeign;
import top.kuanghua.khcomomon.entity.KHCommonParams;
import top.kuanghua.khcomomon.entity.ResResult;
import top.kuanghua.integrationfront.entity.User;
import top.kuanghua.integrationfront.mapper.MultiPageMapper;
import top.kuanghua.integrationfront.mapper.UserMapper;
import top.kuanghua.integrationfront.service.UserService;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Api(tags = "用户相关")
@RestController
@RequestMapping("user")
@Transactional
public class UserController {
    @Resource
    private UserService userService;
    @Resource
    private UserMapper userMapper;

    @Resource
    private MultiPageMapper multiPageMapper;


    @Resource
    private TokenFeign tokenFeign;
    /**
     * 分页查询所有数据
     * @param user     查询实体
     * @return 所有数据
     */
    @GetMapping("selectPage")
    @ApiOperation(value = "分页查询数据")
    public ResResult selectPage(User user, KHCommonParams commonParams) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        if(StringUtils.isNotEmpty(user.getUsername())) {
            queryWrapper.like("username",user.getUsername());
        }
        if(StringUtils.isNotEmpty(user.getPhone())) {
            queryWrapper.like("phone",user.getPhone());
        }
        if(StringUtils.isNotEmpty(commonParams.getStartTime())) {
            queryWrapper.between("create_time",commonParams.getStartTime(),commonParams.getEndTime());
        }
        queryWrapper.select("username","phone","create_time");
        Page<User> userPage = this.userMapper.selectPage(new Page<>(commonParams.getPageNum(), commonParams.getPageSize()), queryWrapper);
        return new ResResult().success(userPage);
    }

    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
    @GetMapping("selectById")
    @ApiOperation(value = "通过id主键查询单条数据")
    public ResResult selectById(@RequestParam("id") Integer id) {
        return new ResResult().success(this.userMapper.selectById(id));
    }
//
//    /**
//     * @Description: 根据id数组查询品牌列表
//     * @Param: idList id数组
//     * @return: ids列表数据
//     */
//    @ApiOperation(value = "根据id数组查询品牌列表")
//    @PostMapping("selectBatchIds")
//    public ResResult selectBatchIds(@RequestParam("idList") List<Integer> idList) {
//        return new ResResult().success(this.userMapper.selectBatchIds(idList));
//    }

    /**
     * 新增数据
     *
     * @param user 实体对象
     * @return 新增结果
     */
    @ApiOperation(value = "新增数据")
    @PostMapping("insert")

    public ResResult insert(@Valid @RequestBody User user) {
        //List<UserInfo> userInfos = userInfoMapper.selectList(new EntityWrapper<>(userInfo).like("firstname", name)
        //                .like("lastname", name));52
        return new ResResult().success(this.userService.insert(user));
    }
    /**
     * 修改数据
     *
     * @return 修改结果
     */

//    @ApiOperation(value = "根据id修改数据")
//    @PutMapping("updateById")
//    public ResResult updateById(@RequestBody User user) {
//        return new ResResult().success(this.userService.updateById(user));
//    }
//    /**
//     * 删除数据
//     *
//     * @param idList 主键结合
//     * @return 删除结果
//     */
    @ApiOperation(value = "根据id数组删除数据")
    @DeleteMapping("deleteBatchIds")
    public ResResult deleteBatchIds(@RequestBody List<Long> idList) {
        return new ResResult().success(this.userMapper.deleteBatchIds(idList));
    }
    @DeleteMapping("deleteById")
    @ApiOperation(value = "根据id删除数据")
    public ResResult deleteById(@RequestParam("id") Integer id) {
        return new ResResult().success(this.userMapper.deleteById(id));
    }

    @PostMapping("registry")
    @ApiOperation(value = "用户注册")
    public ResResult registry(User user,String code,String password) throws Exception {
        this.userService.registry(user,code);
        return new ResResult().success();
    }

    @PostMapping("loginValid")
    @ApiOperation(value = "登录校验")
    public ResResult loginValid(User user) throws Exception {
        HashMap<String, Object> hm = this.userService.loginValid(user);
        return  new ResResult().success(hm);
    }

    @PostMapping("loginOut")
    @ApiOperation(value = "退出登录")

    public ResResult loginOut() {
        this.userService.loginOut();
        return new ResResult().success();
    }
    @PostMapping("multiPageTest")
    @ApiOperation(value = "多分页测试")
    public Page<Map<String, Object>> multiPageTest() {
        Page<Map<String, Object>> mapPage = multiPageMapper.queryAllUsers(new Page<>(1, 5));
        return mapPage;
    }
//
//    @PostMapping("userRegister")
//    @ApiOperation(value = "用户注册")
//    public ResResult userRegister(String email,String code,String username,String password){
//        this.userService.userRegister(email,code,username,password);
//        return new ResResult().success();
//    }

    @PostMapping("changePassword")
    @ApiOperation(value = "修改密码")
    public ResResult changePassword(
            @ApiIgnore @RequestHeader("TOKEN_INFO") String TOKEN_INFO,
            String oldPassword,String newPassword) throws UnsupportedEncodingException {
        Map tokenInfo = JSON.parseObject(URLDecoder.decode(TOKEN_INFO, "utf-8"));
        this.userService.changePassword(tokenInfo.get("username").toString(),oldPassword,newPassword);
        return new ResResult().success();
    }
    @PostMapping("getUserInfo")
    @ApiOperation(value = "获取用户信息")
    public ResResult changePassword(@ApiIgnore @RequestHeader("TOKEN_INFO") String TOKEN_INFO) throws UnsupportedEncodingException {
        Map tokenInfo = JSON.parseObject(URLDecoder.decode(TOKEN_INFO, "utf-8"));
        return new ResResult().success(tokenInfo);
    }


    @PostMapping("insertUser")
    @ApiOperation(value = "插入用户")
    public ResResult insertUser(String username){
        this.userService.insertUser(username);
        return new ResResult().success();
    }


    /**
     * 重置用用户名：如果用用户存在先删除原有用户在新增一个用户，没有则新建用户
     */
    @PostMapping("resetUser")
    @ApiOperation(value = "重置用户(有则删除在新增)")
    public ResResult resetUser(String username) {
        this.userService.resetUser(username);
        return  new ResResult().success();
    }
}