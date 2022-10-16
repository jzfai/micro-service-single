


package top.kuanghua.basisfunc.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import javax.annotation.Resource;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.models.auth.In;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;
import top.kuanghua.basisfunc.entity.Permission;
import top.kuanghua.basisfunc.service.PermissionService;
import top.kuanghua.basisfunc.vo.PermissionVo;
import top.kuanghua.commonpom.entity.ResResult;
import top.kuanghua.commonpom.entity.SelfCommonParams;
import top.kuanghua.commonpom.utils.ObjSelfUtils;

import java.util.List;
import java.util.Map;

/**
 * 权限Controller
 *
 * @author 熊猫哥
 * @since 2022-10-07 16:33:13
 */
@Api(tags = "权限(Permission)")
@RestController
@RequestMapping("permission")
@Validated
public class PermissionController {

    @Resource
    private PermissionService permissionService;

    /**
     * 分页查询所有数据
     *
     * @return ResResult
     */
    @GetMapping("selectPage")
    @ApiOperation(value = "分页查询所有数据")

    @ApiImplicitParams({
            @ApiImplicitParam(name = "code", value = "权限唯一CODE代码", paramType = "query"),
            @ApiImplicitParam(name = "name", value = "权限名称", paramType = "query"),
            @ApiImplicitParam(name = "plateFormId", value = "平台id", paramType = "quebn" +
                    "ry"),
            @ApiImplicitParam(name = "title", value = "页面标题", paramType = "query"),
            @ApiImplicitParam(name = "createTime", value = "创建时间", paramType = "query"),
    })
    public ResResult<List<PermissionVo>> selectPage(@ApiIgnore() String id, @ApiIgnore() String parentId, String code,
                                                  @ApiIgnore() String creator, @ApiIgnore() String editor, String name,
                                                  @ApiIgnore() String category,Integer plateFormId, String title, String createTime, SelfCommonParams commonParams) {
        QueryWrapper<Permission> queryWrapper = new QueryWrapper<>();
        //权限ID
        if (ObjSelfUtils.isNotEmpty(id)) {
            queryWrapper.like("id", id);
        }
        //所属父级权限ID
        if (ObjSelfUtils.isNotEmpty(parentId)) {
            queryWrapper.like("parent_id", parentId);
        }
        //权限唯一CODE代码
        if (ObjSelfUtils.isNotEmpty(code)) {
            queryWrapper.like("code", code);
        }
        //创建人
        if (ObjSelfUtils.isNotEmpty(creator)) {
            queryWrapper.like("creator", creator);
        }
        //修改人
        if (ObjSelfUtils.isNotEmpty(editor)) {
            queryWrapper.like("editor", editor);
        }
        //权限名称
        if (ObjSelfUtils.isNotEmpty(name)) {
            queryWrapper.like("name", name);
        }
        //权限类别;1:路由,2:内页,3:按钮
        if (ObjSelfUtils.isNotEmpty(category)) {
            queryWrapper.like("category", category);
        }
        //页面标题
        if (ObjSelfUtils.isNotEmpty(title)) {
            queryWrapper.like("title", title);
        }
        //页面标题
        if (ObjSelfUtils.isNotEmpty(plateFormId)) {
            queryWrapper.like("plate_form_id", plateFormId);
        }
        //创建时间
        queryWrapper.orderByDesc("create_time");
        if (ObjSelfUtils.isNotEmpty(commonParams.getStartTime())) {
            queryWrapper.between("create_time", commonParams.getStartTime(), commonParams.getEndTime());
        }

        queryWrapper.select("id,parent_id,code,intro,creator,editor,path,name,category,component,title,el_svg_icon,icon,redirect,update_time,create_time,plate_form_id,deleted");

        Page<PermissionVo> permissionPage = this.permissionService.selectPage(commonParams.getPageNum(), commonParams.getPageSize(), queryWrapper);
        return new ResResult().success(permissionPage);
    }

    /**
     * 通过主键查询单条数据
     *
     * @param parentId 父元素id
     * @return List
     */
    @GetMapping("getPermissionList")
    @ApiOperation(value = "递归查询children数据")
    public ResResult<List<PermissionVo>> getPermissionList(@RequestParam("parentId") Long parentId) {
        return new ResResult().success(this.permissionService.getPermissionList(parentId));
    }

    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
    @GetMapping("selectById")
    @ApiOperation(value = "通过id主键查询单条数据")
    public ResResult<Permission> selectById(@RequestParam("id") Long id) {
        return new ResResult().success(this.permissionService.selectById(id));
    }
    /**
     * @Description: 根据id数组查询列表
     * @Param: idList id数组
     * @return: ids列表数据
     */
    @ApiOperation(value = "根据id数组查询列表")
    @PostMapping("selectBatchIds")
    public ResResult<List<Permission>> selectBatchIds(@RequestParam("idList") List<Long> idList) {
        return new ResResult().success(this.permissionService.selectBatchIds(idList));
    }

    /**
     * 新增数据
     *
     * @param permission 实体对象
     * @return 新增结果
     */
    @ApiOperation(value = "新增数据")
    @PostMapping("insert")
    public ResResult insert(@Validated @RequestBody Permission permission) {
        return new ResResult().success(this.permissionService.insert(permission));
    }

    /**
     * 修改数据
     *
     * @param permission 实体对象
     * @return 修改结果
     */
    @ApiOperation(value = "根据id修改数据")
    @PutMapping("updateById")
    public ResResult updateById(@Validated @RequestBody Permission permission) {
        return new ResResult().success(this.permissionService.updateById(permission));
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
        return new ResResult().success(this.permissionService.deleteBatchIds(idList));
    }

    @ApiOperation("根据id删除数据")
    @DeleteMapping("deleteById")
    public ResResult deleteById(@RequestParam("id") Long id) {
        return new ResResult().success(this.permissionService.deleteById(id));
    }
}
