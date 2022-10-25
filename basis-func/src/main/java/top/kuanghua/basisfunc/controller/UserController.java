


package top.kuanghua.basisfunc.controller;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import javax.annotation.Resource;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;
import top.kuanghua.basisfunc.entity.User;
import top.kuanghua.basisfunc.service.UserService;
import top.kuanghua.basisfunc.vo.PermissionVo;
import top.kuanghua.commonpom.entity.ResResult;
import top.kuanghua.commonpom.entity.SelfCommonParams;
import top.kuanghua.commonpom.utils.ObjSelfUtils;
import top.kuanghua.commonpom.utils.SelfObjUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

/**
 * 用户Controller
 *
 * @author 熊猫哥
 * @since 2022-10-07 16:33:13
 */
@Api(tags = "用户(User)")
@RestController
@RequestMapping("user")
@Validated
public class UserController {

    @Resource
    private UserService userService;

    /**
     * 分页查询所有数据
     *
     * @return ResResult
     */
    @GetMapping("selectPage")
    @ApiOperation(value = "分页查询所有数据")

    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "用户ID", paramType = "query"),
            @ApiImplicitParam(name = "name", value = "姓名", paramType = "query", required = true),
            @ApiImplicitParam(name = "phone", value = "手机号码", paramType = "query", required = true),
            @ApiImplicitParam(name = "createTime", value = "创建时间", paramType = "query"),
    })
    public ResResult<List<User>> selectPage(String id, String name,
                                            @Pattern(regexp = "^0?1[0-9]{10}$", message = "phone输入有误-手机号") String phone,
                                            @ApiIgnore() String creator, String createTime, @ApiIgnore() String updateTime,
                                            SelfCommonParams commonParams) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        //用户ID
        if (ObjSelfUtils.isNotEmpty(id)) {
            queryWrapper.like("id", id);
        }
        //姓名
        if (ObjSelfUtils.isNotEmpty(name)) {
            queryWrapper.like("name", name);
        }
        //手机号码
        if (ObjSelfUtils.isNotEmpty(phone)) {
            queryWrapper.like("phone", phone);
        }
        //创建人
        if (ObjSelfUtils.isNotEmpty(creator)) {
            queryWrapper.like("creator", creator);
        }
        //创建时间
        queryWrapper.orderByDesc("create_time");
        if (ObjSelfUtils.isNotEmpty(commonParams.getStartTime())) {
            queryWrapper.between("create_time", commonParams.getStartTime(), commonParams.getEndTime());
        }
        //修改时间
        if (ObjSelfUtils.isNotEmpty(updateTime)) {
            queryWrapper.like("update_time", updateTime);
        }

        queryWrapper.select("id,name,head_img_url,phone,salt,password,role_id,create_time,creator,update_time,editor,deleted");

        Page<User> userPage = this.userService.selectPage(commonParams.getPageNum(), commonParams.getPageSize(), queryWrapper);
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
    public ResResult<User> selectById(@RequestParam("id") Long id) {
        return new ResResult().success(this.userService.selectById(id));
    }

    /**
     * @Description: 根据id数组查询列表
     * @Param: idList id数组
     * @return: ids列表数据
     */
    @ApiOperation(value = "根据id数组查询列表")
    @PostMapping("selectBatchIds")
    public ResResult<List<User>> selectBatchIds(@RequestParam("idList") List<Long> idList) {
        return new ResResult().success(this.userService.selectBatchIds(idList));
    }

    /**
     * 新增数据
     *
     * @param user 实体对象
     * @return 新增结果
     */
    @ApiOperation(value = "新增数据")
    @PostMapping("insert")
    public ResResult insert(@Validated @RequestBody User user) {
        return new ResResult().success(this.userService.insert(user));
    }

    /**
     * 修改数据
     *
     * @param user 实体对象
     * @return 修改结果
     */
    @ApiOperation(value = "根据id修改数据")
    @PutMapping("updateById")
    public ResResult updateById(@Validated @RequestBody User user) {
        return new ResResult().success(this.userService.updateById(user));
    }

    /**
     * 删除数据
     *
     * @param idList 主键结合
     * @return 删除结果
     */
    @ApiOperation(value = "根据id数组删除数据")
    @DeleteMapping("deleteBatchIds")
    public ResResult deleteBatchIds(@RequestBody List<Long> idList) {
        return new ResResult().success(this.userService.deleteBatchIds(idList));
    }

    @ApiOperation("根据id删除数据")
    @DeleteMapping("deleteById")
    public ResResult deleteById(@RequestParam("id") Long id) {
        return new ResResult().success(this.userService.deleteById(id));
    }


    @PostMapping("loginValid")
    @ApiOperation(value = "登录校验")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "keyword", value = "用户名或手机号", paramType = "query", required = true),
            @ApiImplicitParam(name = "password", value = "密码", paramType = "query", required = true),
    })
    public ResResult loginValid(String keyword, String password) {
        HashMap<String, Object> hm = this.userService.loginValid(keyword, password);
        return new ResResult().success(hm);
    }

    @PostMapping("loginOut")
    @ApiOperation(value = "退出登录")
    public ResResult<Boolean> loginOut() {
        this.userService.loginOut();
        return new ResResult().success();
    }

    @PostMapping("changePassword")
    @ApiOperation(value = "修改密码")
    public ResResult<Boolean> changePassword(
            @ApiIgnore @RequestHeader("TOKEN_INFO") String TOKEN_INFO,
            String oldPassword, String newPassword) throws UnsupportedEncodingException {
        Map tokenInfo = JSON.parseObject(URLDecoder.decode(TOKEN_INFO, "utf-8"));
        this.userService.changePassword(tokenInfo.get("username").toString(), oldPassword, newPassword);
        return new ResResult().success();
    }

    @PostMapping("getUserInfo")
    @ApiOperation(value = "获取用户信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", value = "用户ID", paramType = "query", required = true),
            @ApiImplicitParam(name = "plateFormId", value = "平台id", paramType = "query", required = true),
    })
    public ResResult<Map> getUserInfo(@ApiIgnore @RequestHeader("TOKEN_INFO") String TOKEN_INFO, Integer plateFormId) {
        Map tokenInfo = null;
        try {
            tokenInfo = JSON.parseObject(URLDecoder.decode(TOKEN_INFO, "utf-8"));
            User userInfo = SelfObjUtils.parseObject(tokenInfo.get("userInfo"), User.class);
            List<String> roles = this.userService.getRoleCodeArrByUserId(userInfo.getId());
            HashSet<Long> codes = this.userService.getCodesByUserId(userInfo.getId(), plateFormId);
            tokenInfo.put("roles", roles);
            tokenInfo.put("codes", codes);
            tokenInfo.put("menuList", this.userService.getPermissionByUserId(userInfo.getId(), plateFormId));
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("获取用户信息有误");
        }
        return new ResResult().success(tokenInfo);
    }

    /**
     * 重置用用户名：如果用用户存在先删除原有用户在新增一个用户，没有则新建用户
     */
    @PostMapping("resetUser")
    @ApiOperation(value = "重置用户(有则删除在新增)")
    public ResResult<Boolean> resetUser(String username) {
        this.userService.resetUser(username);
        return new ResResult().success();
    }


    @PostMapping("insertUser")
    @ApiOperation(value = "插入用户")
    public ResResult<Boolean> insertUser(String username) {
        this.userService.insertUser(username);
        return new ResResult().success();
    }

    /**
     * 根据用户id获取权限
     *
     * @param userId      用户id
     * @param plateFormId 平台id
     * @author 猫哥
     * @date 2022/10/22 16:27
     */
    @PostMapping("getPermissionByUserId")
    @ApiOperation(value = "根据用户id获取权限")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", value = "用户ID", paramType = "query", required = true),
            @ApiImplicitParam(name = "plateFormId", value = "平台id", paramType = "query", required = true),
    })
    public ResResult<List<PermissionVo>> getPermissionByUserId(Long userId, Integer plateFormId) {
        return new ResResult().success(this.userService.getPermissionByUserId(userId, plateFormId));
    }
}
