


package top.hugo.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import top.hugo.domain.ResResult;
import top.hugo.domain.SelfCommonParams;
import top.hugo.entity.Permission;
import top.hugo.service.PermissionService;
import top.hugo.utils.ObjSelfUtils;
import top.hugo.vo.PermissionVo;


import javax.annotation.Resource;
import java.util.List;

/**
 * 权限Controller
 *
 * @author 熊猫哥
 * @since 2022-10-07 16:33:13
 */
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
    public ResResult<List<PermissionVo>> selectPage( String id,  String parentId, String code,
                                                     String creator, String editor, String name,
                                                    String category, Integer plateFormId, String title, String createTime, SelfCommonParams commonParams) {
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

        Page<PermissionVo> permissionPage = this.permissionService.selectPage(commonParams.getPageNum(), commonParams.getPageSize(), queryWrapper, plateFormId);
        return new ResResult().success(permissionPage);
    }

    /**
     * 通过主键查询单条数据
     *
     * @param parentId 父元素id
     * @return List
     */
    @GetMapping("getPermissionList")
    public ResResult<List<PermissionVo>> getPermissionList(@RequestParam("parentId") Long parentId, Integer plateFormId) {
        return new ResResult().success(this.permissionService.getPermissionList(parentId, plateFormId));
    }

    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
    @GetMapping("selectById")
    public ResResult<Permission> selectById(@RequestParam("id") Long id) {
        return new ResResult().success(this.permissionService.selectById(id));
    }

    /**
     * @Description: 根据id数组查询列表
     * @Param: idList id数组
     * @return: ids列表数据
     */
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
    @DeleteMapping("deleteBatchIds")
    public ResResult deleteBatchIds(@RequestBody List<Long> idList) {
        return new ResResult().success(this.permissionService.deleteBatchIds(idList));
    }

    @DeleteMapping("deleteById")
    public ResResult deleteById(@RequestParam("id") Long id) {
        return new ResResult().success(this.permissionService.deleteById(id));
    }
}
