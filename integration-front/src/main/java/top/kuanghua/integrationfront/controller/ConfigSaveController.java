


package top.kuanghua.integrationfront.controller;

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
import top.kuanghua.commonpom.entity.ResResult;
import top.kuanghua.commonpom.entity.SelfCommonParams;
import top.kuanghua.commonpom.utils.ObjSelfUtils;
import top.kuanghua.integrationfront.entity.ConfigSave;
import top.kuanghua.integrationfront.service.ConfigSaveService;
import top.kuanghua.integrationfront.vo.ConfigSaveVo;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

/**
 * 代码生成配置保存Controller
 *
 * @author 熊猫哥
 * @since 2022-10-07 16:33:13
 */
@Api(tags = "代码生成配置保存(ConfigSave)")
@RestController
@RequestMapping("configSave")
@Validated
public class ConfigSaveController {

    @Resource
    private ConfigSaveService configSaveService;

    /**
     * 分页查询所有数据
     *
     * @return ResResult
     */
    @GetMapping("selectPage")
    @ApiOperation(value = "分页查询所有数据")

    @ApiImplicitParams({
            @ApiImplicitParam(name = "name", value = "选中的字段配置", paramType = "query"),
    })
    public ResResult<List<ConfigSaveVo>> selectPage(String name,
                                                    SelfCommonParams commonParams) {
        QueryWrapper<ConfigSave> queryWrapper = new QueryWrapper<>();
        //选中的字段配置
        if (ObjSelfUtils.isNotEmpty(name)) {
            queryWrapper.like("name", name);
        }

        queryWrapper.select("name,generator_config,id");
        queryWrapper.orderByDesc("id");
        Page<ConfigSave> configSavePage = this.configSaveService.selectPage(commonParams.getPageNum(), commonParams.getPageSize(), queryWrapper);
        return new ResResult().success(configSavePage);
    }

    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
    @GetMapping("selectById")
    @ApiOperation(value = "通过id主键查询单条数据")
    public ResResult<ConfigSave> selectById(@RequestParam("id") Integer id) {
        return new ResResult().success(this.configSaveService.selectById(id));
    }

    /**
     * @Description: 根据id数组查询列表
     * @Param: idList id数组
     * @return: ids列表数据
     */
    @ApiOperation(value = "根据id数组查询列表")
    @PostMapping("selectBatchIds")
    public ResResult<List<ConfigSave>> selectBatchIds(@RequestParam("idList") List<Integer> idList) {
        return new ResResult().success(this.configSaveService.selectBatchIds(idList));
    }

    /**
     * 新增数据
     *
     * @param configSave 实体对象
     * @return 新增结果
     */
    @ApiOperation(value = "新增数据")
    @PostMapping("insert")
    public ResResult insert(@Validated @RequestBody ConfigSave configSave) {
        return new ResResult().success(this.configSaveService.insert(configSave));
    }

    /**
     * 修改数据
     *
     * @param configSave 实体对象
     * @return 修改结果
     */
    @ApiOperation(value = "根据id修改数据")
    @PutMapping("updateById")
    public ResResult updateById(@Validated @RequestBody ConfigSave configSave) {
        return new ResResult().success(this.configSaveService.updateById(configSave));
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
        return new ResResult().success(this.configSaveService.deleteBatchIds(idList));
    }

    @ApiOperation("根据id删除数据")
    @DeleteMapping("deleteById")
    public ResResult deleteById(@RequestParam("id") Integer id) {
        return new ResResult().success(this.configSaveService.deleteById(id));
    }
}
