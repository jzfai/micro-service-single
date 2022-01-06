package top.kuanghua.tyuser.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.extension.api.ApiController;
import com.baomidou.mybatisplus.extension.api.R;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import top.kuanghua.khcomomon.entity.KHCommonParams;
import top.kuanghua.khcomomon.entity.ResResult;
import top.kuanghua.tyuser.entity.ErrorCollection;
import top.kuanghua.tyuser.service.ErrorCollectionService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.*;

@Api(tags = "日志相关")
@RestController
@RequestMapping("errorCollection")
public class ErrorCollectionController {

    @Resource
    private ErrorCollectionService errorCollectionService;

    /**
     * 分页查询所有数据
     *
     * @param errorCollection 查询实体
     * @return 所有数据
     */
    @GetMapping("selectPage")
    @ApiOperation(value = "分页查询所有数据")
    public ResResult selectPage(ErrorCollection errorCollection, KHCommonParams khCommonParams) {
        QueryWrapper<ErrorCollection> queryWrapper = new QueryWrapper<>();
        if (ObjectUtils.isNotEmpty(errorCollection.getErrorLog())) {
            queryWrapper.like("error_log", errorCollection.getErrorLog());
        }
        if (ObjectUtils.isNotEmpty(errorCollection.getPageUrl())) {
            queryWrapper.like("page_url", errorCollection.getPageUrl());
        }
        if (ObjectUtils.isNotEmpty(errorCollection.getVersion())) {
            queryWrapper.like("version", errorCollection.getVersion());
        }
        if (ObjectUtils.isNotEmpty(errorCollection.getCreateTime())) {
            queryWrapper.like("create_time", errorCollection.getCreateTime());
        }

        if(StringUtils.isNotEmpty(khCommonParams.getStartTime())) {
            queryWrapper.between("create_time",khCommonParams.getStartTime(),khCommonParams.getEndTime());
        }
        queryWrapper.orderByDesc("create_time");
        Page<ErrorCollection> errorCollectionPage = this.errorCollectionService.selectPage(khCommonParams.getPageNum(), khCommonParams.getPageSize(), queryWrapper);
        return new ResResult().success(errorCollectionPage);
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
        return new ResResult().success(this.errorCollectionService.selectById(id));
    }

    /**
     * @Description: 根据id数组查询品牌列表
     * @Param: idList id数组
     * @return: ids列表数据
     */
    @ApiOperation(value = "根据id数组查询品牌列表")
    @PostMapping("selectBatchIds")
    public ResResult selectBatchIds(@RequestParam("idList") List<Integer> idList) {
        return new ResResult().success(this.errorCollectionService.selectBatchIds(idList));
    }


    /**
     * 新增数据
     *
     * @param errorCollection 实体对象
     * @return 新增结果
     */
    @ApiOperation(value = "新增数据")
    @PostMapping("insert")
    public ResResult insert(@RequestBody ErrorCollection errorCollection) {
        return new ResResult().success(this.errorCollectionService.insert(errorCollection));
    }

    /**
     * 修改数据
     *
     * @param errorCollection 实体对象
     * @return 修改结果
     */
    @ApiOperation(value = "根据id修改数据")
    @PutMapping("updateById")
    public ResResult updateById(@RequestBody ErrorCollection errorCollection) {
        return new ResResult().success(this.errorCollectionService.updateById(errorCollection));
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
        return new ResResult().success(this.errorCollectionService.deleteBatchIds(idList));
    }

    @DeleteMapping("deleteById")
    @ApiOperation(value = "根据id数组删除数据")
    public ResResult deleteById(@RequestParam("id") Integer id) {
        return new ResResult().success(this.errorCollectionService.deleteById(id));
    }
}
