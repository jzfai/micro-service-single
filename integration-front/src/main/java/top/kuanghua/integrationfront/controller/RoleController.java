package top.kuanghua.integrationfront.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;
import top.kuanghua.commonpom.entity.ResResult;
import top.kuanghua.commonpom.entity.SelfCommonParams;
import top.kuanghua.commonpom.utils.ObjSelfUtils;
import top.kuanghua.integrationfront.entity.Role;
import top.kuanghua.integrationfront.service.RoleService;

import javax.annotation.Resource;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import java.util.List;

/**
 * 角色Controller
 *
 * @author 熊猫哥
 * @since 2022-10-07 16:33:13
 */
@Api(tags = "角色(Role)")
@RestController
@RequestMapping("role")
@Validated
public class RoleController {

    @Resource
    private RoleService roleService;

    /**
     * 分页查询所有数据
     *
     * @return ResResult
     */
    @GetMapping("selectPage")
    @ApiOperation(value = "分页查询所有数据")

    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "角色ID", paramType = "query", required = true),
            @ApiImplicitParam(name = "name", value = "角色名称", paramType = "query", required = true),
    })
    public ResResult selectPage(@NotEmpty(message = "id不能为空}")
                                        String id,
                                @ApiIgnore() String code,
                                @ApiIgnore() String editor,
                                @NotEmpty(message = "name不能为空}")
                                @Pattern(regexp = "^0?1[0-9]{10}$", message = "name输入有误-手机号}")
                                        String name,
                                @ApiIgnore() String createTime,
                                SelfCommonParams commonParams) {
        QueryWrapper<Role> queryWrapper = new QueryWrapper<>();
        //角色ID
        if (ObjSelfUtils.isNotEmpty(id)) {
            queryWrapper.like("id", id);
        }
        //角色唯一CODE代码
        if (ObjSelfUtils.isNotEmpty(code)) {
            queryWrapper.like("code", code);
        }
        //修改人
        if (ObjSelfUtils.isNotEmpty(editor)) {
            queryWrapper.like("editor", editor);
        }
        //角色名称
        if (ObjSelfUtils.isNotEmpty(name)) {
            queryWrapper.like("name", name);
        }
        //创建时间
        queryWrapper.orderByDesc("create_time");
        if (ObjSelfUtils.isNotEmpty(commonParams.getStartTime())) {
            queryWrapper.between("create_time", commonParams.getStartTime(), commonParams.getEndTime());
        }

        queryWrapper.select("id,parent_id,code,intro,create_time,creator,update_time,editor,deleted");

        Page<Role> rolePage = this.roleService.selectPage(commonParams.getPageNum(), commonParams.getPageSize(), queryWrapper);
        return new ResResult().success(rolePage);
    }

    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
    @GetMapping("selectById")
    @ApiOperation(value = "通过id主键查询单条数据")
    public ResResult selectById(@RequestParam("id") Long id) {
        return new ResResult().success(this.roleService.selectById(id));
    }

    /**
     * @Description: 根据id数组查询品牌列表
     * @Param: idList id数组
     * @return: ids列表数据
     */
    @ApiOperation(value = "根据id数组查询品牌列表")
    @PostMapping("selectBatchIds")
    public ResResult selectBatchIds(@RequestParam("idList") List<Long> idList) {
        return new ResResult().success(this.roleService.selectBatchIds(idList));
    }

    /**
     * 新增数据
     *
     * @param role 实体对象
     * @return 新增结果
     */
    @ApiOperation(value = "新增数据")
    @PostMapping("insert")
    public ResResult insert(@Validated @RequestBody Role role) {
        return new ResResult().success(this.roleService.insert(role));
    }

    /**
     * 修改数据
     *
     * @param role 实体对象
     * @return 修改结果
     */
    @ApiOperation(value = "根据id修改数据")
    @PutMapping("updateById")
    public ResResult updateById(@Validated @RequestBody Role role) {
        return new ResResult().success(this.roleService.updateById(role));
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
        return new ResResult().success(this.roleService.deleteBatchIds(idList));
    }

    @DeleteMapping("根据id删除数据")
    public ResResult deleteById(@RequestParam("id") Long id) {
        return new ResResult().success(this.roleService.deleteById(id));
    }
}
