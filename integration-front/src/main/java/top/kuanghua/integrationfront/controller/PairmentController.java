package top.kuanghua.integrationfront.controller;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;
import top.kuanghua.commonpom.entity.ResResult;
import top.kuanghua.commonpom.entity.SelfCommonParams;
import top.kuanghua.integrationfront.entity.Pairment;
import top.kuanghua.integrationfront.service.PairmentService;
import top.kuanghua.integrationfront.vo.PairmentVo;

import javax.annotation.Resource;
import java.util.Map;

/**
 * 多表中实体类的注释Controller
 *
 * @author 熊猫哥
 * @since 2022-06-25 10:32:12
 */
@Api(tags = "维修信息")
@RestController
@RequestMapping("pairment")
public class PairmentController {
    @Resource
    private PairmentService pairmentService;

    @ApiOperation(value = "多表测试")
    @GetMapping("selectPairment")
    public Page<Map> selectPairment(SelfCommonParams commonParams, PairmentVo pairmentVo) {
        Map pairmentMap = JSON.parseObject(JSON.toJSONString(pairmentVo), Map.class);
        return pairmentService.selectPairment(commonParams, pairmentMap);
    }


    /**
     * 新增数据
     *
     * @param pairment 实体对象
     * @return 新增结果
     */
    @ApiOperation(value = "新增数据")
    @PostMapping("insert")
    public ResResult insert(@RequestBody Pairment pairment) {
        this.pairmentService.insert(pairment);
        return new ResResult().success();
    }


    /**
     * 通过主键查询单条数据
     *
     * @param sn 主键
     * @return 单条数据
     */
    @GetMapping("selectByKey")
    @ApiOperation(value = "通过联合key查询连表数据")
    public ResResult selectByKey(@RequestParam("sn") String sn) {
        return new ResResult().success(pairmentService.selectByKey(sn));
    }

    /**
     * 修改数据
     *
     * @param pairment 实体对象
     * @return 修改结果
     */
    @ApiOperation(value = "根据sn修改数据")
    @PutMapping("updateByKey")
    public ResResult updateByKey(@RequestBody Pairment pairment) {
        this.pairmentService.updateByKey(pairment);
        return new ResResult().success();
    }


    /**
     * 删除项根据主键key
     *
     * @param sn
     * @return
     */
    @DeleteMapping("deleteByKey")
    public ResResult deleteByKey(@RequestParam("sn") String sn) {
        this.pairmentService.deleteByKey(sn);
        return new ResResult().success();
    }
}
