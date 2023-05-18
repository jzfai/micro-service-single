package top.hugo.generator.controller;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.web.bind.annotation.*;
import top.hugo.common.domain.R;
import top.hugo.generator.domain.SelfCommonParams;
import top.hugo.generator.entity.ConfigSave;
import top.hugo.generator.service.ConfigSaveService;

import javax.annotation.Resource;
import java.util.List;

/**
 * 配置数据模块
 *
 * @author 熊猫哥
 * @since 2022-07-20 10:10:28
 */
@RestController
@RequestMapping("basis-func/configSave")
public class ConfigSaveController {

    @Resource
    private ConfigSaveService generatorConfigSaveService;

    /**
     * 分页查询所有数据
     *
     * @param generatorConfigSave 查询实体
     * @param commonParams        分页参数
     */
    @GetMapping("selectPage")
    public R<Page<ConfigSave>> selectPage(ConfigSave generatorConfigSave, SelfCommonParams commonParams) {
        QueryWrapper<ConfigSave> queryWrapper = new QueryWrapper<>();
        //选中的字段配置
        if (ObjectUtil.isNotEmpty(generatorConfigSave.getName())) {
            queryWrapper.like("name", generatorConfigSave.getName());
        }
        //生成的配置
        queryWrapper.orderByDesc("id");
        queryWrapper.select("id,name,generator_config");
        //创建时间
        queryWrapper.orderByDesc("create_time");

        Page<ConfigSave> generatorConfigSavePage = this.generatorConfigSaveService.selectPage(commonParams.getPageNum(), commonParams.getPageSize(), queryWrapper);
        return R.ok(generatorConfigSavePage);
    }

    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     */
    @GetMapping("selectById")
    public R<ConfigSave> selectById(@RequestParam("id") Integer id) {
        return R.ok(this.generatorConfigSaveService.selectById(id));
    }

    /**
     * 根据数组查询品牌列表
     *
     * @Param: idList id数组
     */
    @PostMapping("selectBatchIds")
    public R<List<ConfigSave>> selectBatchIds(@RequestParam("idList") List<Integer> idList) {
        return R.ok(this.generatorConfigSaveService.selectBatchIds(idList));
    }

    /**
     * 新增数据
     *
     * @param generatorConfigSave 实体对象
     */
    @PostMapping("insert")
    public R<Integer> insert(@RequestBody ConfigSave generatorConfigSave) {
        return R.ok(this.generatorConfigSaveService.insert(generatorConfigSave));
    }

    /**
     * 修改数据
     *
     * @param generatorConfigSave 实体对象
     */
    @PutMapping("updateById")
    public R<Integer> updateById(@RequestBody ConfigSave generatorConfigSave) {
        return R.ok(this.generatorConfigSaveService.updateById(generatorConfigSave));
    }

    /**
     * 删除数据
     *
     * @param idList 主键数组
     */
    @DeleteMapping("deleteBatchIds")
    public R<Integer> deleteBatchIds(@RequestBody List<Integer> idList) {
        return R.ok(this.generatorConfigSaveService.deleteBatchIds(idList));
    }


    /**
     * 根据主键删除数据
     *
     * @param id 主键id
     */
    @DeleteMapping("deleteById")
    public R<Integer> deleteById(@RequestParam("id") Integer id) {
        return R.ok(this.generatorConfigSaveService.deleteById(id));
    }
}
