


package top.hugo.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import top.hugo.domain.ResResult;
import top.hugo.domain.SelfCommonParams;
import top.hugo.entity.Role;
import top.hugo.service.RoleService;
import top.hugo.utils.ObjSelfUtils;

import javax.annotation.Resource;
import java.util.List;

/**
 * 角色Controller
 *
 * @author 熊猫哥
 * @since 2022-10-07 16:33:13
 */
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
    public ResResult<List<Role>> selectPage(String id,
                                            String name,
                                            SelfCommonParams commonParams) {
        QueryWrapper<Role> queryWrapper = new QueryWrapper<>();
        //角色ID
        if (ObjSelfUtils.isNotEmpty(id)) {
            queryWrapper.like("id", id);
        }
        //角色名称
        if (ObjSelfUtils.isNotEmpty(name)) {
            queryWrapper.like("name", name);
        }

        queryWrapper.select("id,parent_id,code,name,intro,permission_id,deleted");

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
    public ResResult<Role> selectById(@RequestParam("id") Long id) {
        return new ResResult().success(this.roleService.selectById(id));
    }

    /**
     * @Description: 根据id数组查询列表
     * @Param: idList id数组
     * @return: ids列表数据
     */
    @PostMapping("selectBatchIds")
    public ResResult<List<Role>> selectBatchIds(@RequestParam("idList") List<Long> idList) {
        return new ResResult().success(this.roleService.selectBatchIds(idList));
    }

    /**
     * 新增数据
     *
     * @param role 实体对象
     * @return 新增结果
     */
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
    @DeleteMapping("deleteBatchIds")
    public ResResult deleteBatchIds(@RequestBody List<Long> idList) {
        return new ResResult().success(this.roleService.deleteBatchIds(idList));
    }
    /**
     * 根据id删除数据
     * @return 删除结果
     */
    @DeleteMapping("deleteById")
    public ResResult deleteById(@RequestParam("id") Long id) {
        return new ResResult().success(this.roleService.deleteById(id));
    }
}
