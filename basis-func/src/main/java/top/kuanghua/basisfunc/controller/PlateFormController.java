


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
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import top.kuanghua.basisfunc.entity.PlateForm;
import top.kuanghua.basisfunc.service.PlateFormService;
import top.kuanghua.commonpom.entity.ResResult;
import top.kuanghua.commonpom.entity.SelfCommonParams;
import top.kuanghua.commonpom.utils.ObjSelfUtils;

import java.util.List;

/**
 * 平台Controller
 *
 * @author 熊猫哥
 * @since 2022-10-07 16:33:13
 */
@Api(tags = "平台(PlateForm)")
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
    @ApiOperation(value = "分页查询所有数据")

    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "主键", paramType = "query"),
            @ApiImplicitParam(name = "name", value = "平台名字", paramType = "query"),
    })
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
    @ApiOperation(value = "通过id主键查询单条数据")
    public ResResult<PlateForm> selectById(@RequestParam("id") Integer id) {
        return new ResResult().success(this.plateFormService.selectById(id));
    }

    /**
     * @Description: 根据id数组查询列表
     * @Param: idList id数组
     * @return: ids列表数据
     */
    @ApiOperation(value = "根据id数组查询列表")
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
    @ApiOperation(value = "新增数据")
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
    @ApiOperation(value = "根据id修改数据")
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
    @ApiOperation(value = "根据id数组删除数据")
    @DeleteMapping("deleteBatchIds")
    public ResResult deleteBatchIds(@RequestBody List<Integer> idList) {
        return new ResResult().success(this.plateFormService.deleteBatchIds(idList));
    }

    @ApiOperation("根据id删除数据")
    @DeleteMapping("deleteById")
    public ResResult deleteById(@RequestParam("id") Integer id) {
        return new ResResult().success(this.plateFormService.deleteById(id));
    }
}
