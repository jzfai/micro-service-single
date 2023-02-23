package top.hugo.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import top.hugo.domain.SelfCommonParams;
import top.hugo.entity.Brand;
import top.hugo.domain.ResResult;
import top.hugo.query.BrandQuery;
import top.hugo.service.BrandService;
import top.hugo.utils.ObjSelfUtils;


import javax.annotation.Resource;
import java.util.List;

/**
 * 品牌表Controller
 *
 * @author 熊猫哥
 * @since 2022-12-16 14:42:22
 */
@RestController
@RequestMapping("brand")
@Validated
public class BrandController {

    @Resource
    private BrandService brandService;

    /**
     * 分页查询所有数据
     *
     * @return ResResult
     */
    @GetMapping("selectPage")
    public ResResult<List<Brand>> selectPage(@Validated BrandQuery brandQuery, SelfCommonParams commonParams) {
        QueryWrapper<Brand> queryWrapper = new QueryWrapper<>();
        //品牌id
        if (ObjSelfUtils.isNotEmpty(brandQuery.getId())) {
            queryWrapper.like("id", brandQuery.getId());
        }
        //品牌名称
        if (ObjSelfUtils.isNotEmpty(brandQuery.getName())) {
            queryWrapper.like("name", brandQuery.getName());
        }
        //排序
        if (ObjSelfUtils.isNotEmpty(brandQuery.getSeq())) {
            queryWrapper.like("seq", brandQuery.getSeq());
        }

        queryWrapper.select("id,name,image,letter,seq,create_time,update_time");

        Page<Brand> brandPage = this.brandService.selectPage(commonParams.getPageNum(), commonParams.getPageSize(), queryWrapper);
        return new ResResult().success(brandPage);
    }

    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
    @GetMapping("selectById")
    public ResResult<Brand> selectById(@RequestParam("id") Integer id) {
        return new ResResult().success(this.brandService.selectById(id));
    }

    /**
     * @Description: 根据id数组查询列表
     * @Param: idList id数组
     * @return: ids列表数据
     */
    @PostMapping("selectBatchIds")
    public ResResult<List<Brand>> selectBatchIds(@RequestParam("idList") List<Integer> idList) {
        return new ResResult().success(this.brandService.selectBatchIds(idList));
    }


    /**
     * 新增数据
     *
     * @param brand 实体对象
     * @return 新增结果
     */
    @PostMapping("insert")
    public ResResult insert(@Validated @RequestBody Brand brand) {
        return new ResResult().success(this.brandService.insert(brand));
    }

    /**
     * 修改数据
     *
     * @param brand 实体对象
     * @return 修改结果
     */
    @PutMapping("updateById")
    public ResResult updateById(@Validated @RequestBody Brand brand) {
        return new ResResult().success(this.brandService.updateById(brand));
    }

    /**
     * 批量删除数据
     *
     * @param idList 主键结合
     * @return 删除结果
     */
    @DeleteMapping("deleteBatchIds")
    public ResResult deleteBatchIds(@RequestBody List<Integer> idList) {
        return new ResResult().success(this.brandService.deleteBatchIds(idList));
    }
    /**
     * 删除单条数据
     * @return 删除结果
     */
    @DeleteMapping("deleteById")
    public ResResult deleteById(@RequestParam("id") Integer id) {
        return new ResResult().success(this.brandService.deleteById(id));
    }
}
