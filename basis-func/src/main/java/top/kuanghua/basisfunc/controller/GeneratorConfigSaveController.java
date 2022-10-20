package top.kuanghua.basisfunc.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;
import top.kuanghua.basisfunc.entity.GeneratorConfigSave;
import top.kuanghua.basisfunc.service.GeneratorConfigSaveService;
import top.kuanghua.commonpom.entity.ResResult;
import top.kuanghua.commonpom.entity.SelfCommonParams;
import top.kuanghua.commonpom.utils.ObjSelfUtils;

import javax.annotation.Resource;
import java.util.List;

/**
 * 代码生成配置保存Controller
 *
 * @author 熊猫哥
 * @since 2022-07-20 10:10:28
 */
@Api(tags = "配置信息")
@RestController
@RequestMapping("generatorConfigSave")
public class GeneratorConfigSaveController {

    @Resource
    private GeneratorConfigSaveService generatorConfigSaveService;

    /**
     * 分页查询所有数据
     *
     * @param generatorConfigSave 查询实体
     * @return ResResult
     */
    @GetMapping("selectPage")
    @ApiOperation(value = "分页查询所有数据")

    public ResResult selectPage(GeneratorConfigSave generatorConfigSave, SelfCommonParams commonParams) {
        QueryWrapper<GeneratorConfigSave> queryWrapper = new QueryWrapper<>();
        //选中的字段配置
        if (ObjSelfUtils.isNotEmpty(generatorConfigSave.getName())) {
            queryWrapper.like("name", generatorConfigSave.getName());
        }
        //生成的配置
        queryWrapper.orderByDesc("id");
        queryWrapper.select("id,name,generator_config");

        Page<GeneratorConfigSave> generatorConfigSavePage = this.generatorConfigSaveService.selectPage(commonParams.getPageNum(), commonParams.getPageSize(), queryWrapper);
        return new ResResult().success(generatorConfigSavePage);
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
        return new ResResult().success(this.generatorConfigSaveService.selectById(id));
    }

    /**
     * @Description: 根据id数组查询品牌列表
     * @Param: idList id数组
     * @return: ids列表数据
     */
    @ApiOperation(value = "根据id数组查询品牌列表")
    @PostMapping("selectBatchIds")
    public ResResult selectBatchIds(@RequestParam("idList") List<Integer> idList) {
        return new ResResult().success(this.generatorConfigSaveService.selectBatchIds(idList));
    }


    /**
     * 新增数据
     *
     * @param generatorConfigSave 实体对象
     * @return 新增结果
     */
    @ApiOperation(value = "新增数据")
    @PostMapping("insert")
    public ResResult insert(@RequestBody GeneratorConfigSave generatorConfigSave) {
        return new ResResult().success(this.generatorConfigSaveService.insert(generatorConfigSave));
    }

    /**
     * 修改数据
     *
     * @param generatorConfigSave 实体对象
     * @return 修改结果
     */
    @ApiOperation(value = "根据id修改数据")
    @PutMapping("updateById")
    public ResResult updateById(@RequestBody GeneratorConfigSave generatorConfigSave) {
        return new ResResult().success(this.generatorConfigSaveService.updateById(generatorConfigSave));
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
        return new ResResult().success(this.generatorConfigSaveService.deleteBatchIds(idList));
    }

    @DeleteMapping("deleteById")
    public ResResult deleteById(@RequestParam("id") Integer id) {
        return new ResResult().success(this.generatorConfigSaveService.deleteById(id));
    }
}
