package top.hugo.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.web.bind.annotation.*;
import top.hugo.domain.ResResult;
import top.hugo.domain.SelfCommonParams;
import top.hugo.entity.ConfigSave;
import top.hugo.service.ConfigSaveService;
import top.hugo.utils.ObjSelfUtils;


import javax.annotation.Resource;
import java.util.List;

/**
 * 代码生成配置保存Controller
 *
 * @author 熊猫哥
 * @since 2022-07-20 10:10:28
 */
@RestController
@RequestMapping("configSave")
public class ConfigSaveController {

    @Resource
    private ConfigSaveService generatorConfigSaveService;

    /**
     * 分页查询所有数据
     *
     * @param generatorConfigSave 查询实体
     * @return ResResult
     */
    @GetMapping("selectPage")

    public ResResult selectPage(ConfigSave generatorConfigSave, SelfCommonParams commonParams) {
        QueryWrapper<ConfigSave> queryWrapper = new QueryWrapper<>();
        //选中的字段配置
        if (ObjSelfUtils.isNotEmpty(generatorConfigSave.getName())) {
            queryWrapper.like("name", generatorConfigSave.getName());
        }
        //生成的配置
        queryWrapper.orderByDesc("id");
        queryWrapper.select("id,name,generator_config");
        //创建时间
        queryWrapper.orderByDesc("create_time");

        Page<ConfigSave> generatorConfigSavePage = this.generatorConfigSaveService.selectPage(commonParams.getPageNum(), commonParams.getPageSize(), queryWrapper);
        return new ResResult().success(generatorConfigSavePage);
    }

    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
    @GetMapping("selectById")
    public ResResult selectById(@RequestParam("id") Integer id) {
        return new ResResult().success(this.generatorConfigSaveService.selectById(id));
    }

    /**
     * @Description: 根据id数组查询品牌列表
     * @Param: idList id数组
     * @return: ids列表数据
     */
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
    @PostMapping("insert")
    public ResResult<Integer> insert(@RequestBody ConfigSave generatorConfigSave) {
        return new ResResult().success(this.generatorConfigSaveService.insert(generatorConfigSave));
    }

    /**
     * 修改数据
     *
     * @param generatorConfigSave 实体对象
     * @return 修改结果
     */
    @PutMapping("updateById")
    public ResResult updateById(@RequestBody ConfigSave generatorConfigSave) {
        return new ResResult().success(this.generatorConfigSaveService.updateById(generatorConfigSave));
    }

    /**
     * 删除数据
     *
     * @param idList 主键结合
     * @return 删除结果
     */
    @DeleteMapping("deleteBatchIds")
    public ResResult deleteBatchIds(@RequestBody List<Integer> idList) {
        return new ResResult().success(this.generatorConfigSaveService.deleteBatchIds(idList));
    }

    @DeleteMapping("deleteById")
    public ResResult deleteById(@RequestParam("id") Integer id) {
        return new ResResult().success(this.generatorConfigSaveService.deleteById(id));
    }
}
