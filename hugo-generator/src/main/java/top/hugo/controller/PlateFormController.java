


package top.hugo.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import top.hugo.domain.ResResult;
import top.hugo.domain.SelfCommonParams;
import top.hugo.entity.PlateForm;
import top.hugo.service.PlateFormService;
import top.hugo.utils.ObjSelfUtils;


import javax.annotation.Resource;
import java.util.List;

/**
 * 平台Controller
 *
 * @author 熊猫哥
 * @since 2022-10-07 16:33:13
 */
@RestController
@RequestMapping("plateForm")
@Validated
public class PlateFormController {

    @Resource
    private PlateFormService plateFormService;

    /**
     * 分页查询所有数据
     *
     * @return ResResult
     */
    @GetMapping("selectPage")

    public ResResult<List<PlateForm>> selectPage(String id,
                                                 String name,
                                                 SelfCommonParams commonParams) {
        QueryWrapper<PlateForm> queryWrapper = new QueryWrapper<>();
        //主键
        if (ObjSelfUtils.isNotEmpty(id)) {
            queryWrapper.like("id", id);
        }
        //平台名字
        if (ObjSelfUtils.isNotEmpty(name)) {
            queryWrapper.like("name", name);
        }

        queryWrapper.select("id,name");

        Page<PlateForm> plateFormPage = this.plateFormService.selectPage(commonParams.getPageNum(), commonParams.getPageSize(), queryWrapper);
        return new ResResult().success(plateFormPage);
    }

    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
    @GetMapping("selectById")
    public ResResult<PlateForm> selectById(@RequestParam("id") Integer id) {
        return new ResResult().success(this.plateFormService.selectById(id));
    }

    /**
     * @Description: 根据id数组查询列表
     * @Param: idList id数组
     * @return: ids列表数据
     */
    @PostMapping("selectBatchIds")
    public ResResult<List<PlateForm>> selectBatchIds(@RequestParam("idList") List<Integer> idList) {
        return new ResResult().success(this.plateFormService.selectBatchIds(idList));
    }

    /**
     * 新增数据
     *
     * @param plateForm 实体对象
     * @return 新增结果
     */
    @PostMapping("insert")
    public ResResult insert(@Validated @RequestBody PlateForm plateForm) {
        return new ResResult().success(this.plateFormService.insert(plateForm));
    }

    /**
     * 修改数据
     *
     * @param plateForm 实体对象
     * @return 修改结果
     */
    @PutMapping("updateById")
    public ResResult updateById(@Validated @RequestBody PlateForm plateForm) {
        return new ResResult().success(this.plateFormService.updateById(plateForm));
    }

    /**
     * 删除数据
     *
     * @param idList 主键结合
     * @return 删除结果
     */
    @DeleteMapping("deleteBatchIds")
    public ResResult deleteBatchIds(@RequestBody List<Integer> idList) {
        return new ResResult().success(this.plateFormService.deleteBatchIds(idList));
    }

    @DeleteMapping("deleteById")
    public ResResult deleteById(@RequestParam("id") Integer id) {
        return new ResResult().success(this.plateFormService.deleteById(id));
    }
}
