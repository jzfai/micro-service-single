package top.kuanghua.integrationfront.controller;


import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import top.kuanghua.integrationfront.entity.Vci;
import top.kuanghua.integrationfront.excel.imp.VciExcelImp;
import top.kuanghua.integrationfront.service.VciService;
import top.kuanghua.integrationfront.vo.ExcelCheckResult;
import top.kuanghua.khcomomon.entity.CommonParamsSelf;
import top.kuanghua.khcomomon.entity.ResResult;
import top.kuanghua.khcomomon.utils.ObjectUtilsSelf;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.List;


@Api(tags = "VCI设备表(Vci)")
@RestController
@RequestMapping("vci")
public class VciController {

    @Resource
    private VciService vciService;

    /**
     * 分页查询所有数据
     *
     * @param vci 查询实体
     * @return 所有数据
     */
    @GetMapping("selectPage")
    @ApiOperation(value = "分页查询所有数据")
    public ResResult selectPage(Vci vci, CommonParamsSelf commonParams) {
        QueryWrapper<Vci> queryWrapper = new QueryWrapper<>();
        if (ObjectUtils.isNotEmpty(vci.getSn())) {
            queryWrapper.like("sn", vci.getSn());
        }
        if (ObjectUtils.isNotEmpty(vci.getHardVersion())) {
            queryWrapper.like("hard_version", vci.getHardVersion());
        }
        if (ObjectUtils.isNotEmpty(vci.getStatus())) {
            queryWrapper.like("status", vci.getStatus());
        }
        //根据创建时间查询
        queryWrapper.orderByDesc("create_time");
        if (StringUtils.isNotEmpty(commonParams.getStartTime())) {
            queryWrapper.between("create_time", commonParams.getStartTime(), commonParams.getEndTime());
        }

        queryWrapper.select("id,sn,hard_version,create_time,status,supplier,receipt_no,product_spec");
        Page<Vci> vciPage = this.vciService.selectPage(commonParams.getPageNum(), commonParams.getPageSize(), queryWrapper);
        return new ResResult().success(vciPage);
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
        return new ResResult().success(this.vciService.selectById(id));
    }

    /**
     * @Description: 根据id数组查询品牌列表
     * @Param: idList id数组
     * @return: ids列表数据
     */
    @ApiOperation(value = "根据id数组查询品牌列表")
    @PostMapping("selectBatchIds")
    public ResResult selectBatchIds(@RequestParam("idList") List<Integer> idList) {
        return new ResResult().success(this.vciService.selectBatchIds(idList));
    }

    /**
     * 新增数据
     *
     * @param vci 实体对象
     * @return 新增结果
     */
    @ApiOperation(value = "新增数据")
    @PostMapping("insert")
    public ResResult insert(@RequestBody Vci vci) {
        return new ResResult().success(this.vciService.insert(vci));
    }

    /**
     * 修改数据
     *
     * @param vci 实体对象
     * @return 修改结果
     */
    @ApiOperation(value = "根据id修改数据")
    @PutMapping("updateById")
    public ResResult updateById(@RequestBody Vci vci) {
        return new ResResult().success(this.vciService.updateById(vci));
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
        return new ResResult().success(this.vciService.deleteBatchIds(idList));
    }

    @DeleteMapping("deleteById")
    @ApiOperation(value = "根据id数组删除数据")
    public ResResult deleteById(@RequestParam("id") Integer id) {
        return new ResResult().success(this.vciService.deleteById(id));
    }

    @ApiOperation("检查excel")
    @PostMapping("validExcel")
    public ResResult validExcel(@RequestParam("file") MultipartFile file) throws IOException {
        ExcelCheckResult<VciExcelImp> checkResult = vciService.validExcel(file.getInputStream());
        return new ResResult().success(checkResult);
    }

    @ApiOperation("导入excel")
    @PostMapping("importExcel")
    public ResResult importExcel(@RequestBody List<VciExcelImp> vciExcelBo) {
        vciService.importExcel(vciExcelBo);
        return new ResResult().success();
    }

    @ApiOperation("导出excel")
    @GetMapping("exportExcel")
    public void exportExcel(HttpServletResponse response, Vci vci, CommonParamsSelf commonParams) throws IOException {
        QueryWrapper<Vci> queryWrapper = new QueryWrapper<>();
        if (ObjectUtils.isNotEmpty(vci.getSn())) {
            queryWrapper.like("sn", vci.getSn());
        }
        if (ObjectUtils.isNotEmpty(vci.getHardVersion())) {
            queryWrapper.like("hard_version", vci.getHardVersion());
        }
        if (ObjectUtils.isNotEmpty(vci.getStatus())) {
            queryWrapper.like("status", vci.getStatus());
        }
        //根据创建时间查询
        queryWrapper.orderByDesc("create_time");
        if (StringUtils.isNotEmpty(commonParams.getStartTime())) {
            queryWrapper.between("create_time", commonParams.getStartTime(), commonParams.getEndTime());
        }

        queryWrapper.select("sn,hard_version,create_time,status,supplier,receipt_no,product_spec");
        Page<Vci> vciPage = this.vciService.selectPage(commonParams.getPageNum(), commonParams.getPageSize(), queryWrapper);
        // 这里注意 有同学反应使用swagger 会导致各种问题，请直接用浏览器或者用postman
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setCharacterEncoding("utf-8");

        String fileName = URLEncoder.encode("Vci导出数据", "UTF-8") + "-" + ObjectUtilsSelf.getCurrentDateTime();
        response.setHeader("Access-Control-Expose-Headers", "exportFileName");
        response.setHeader("exportFileName", fileName + ".xlsx");
        EasyExcel.write(response.getOutputStream(), VciExcelImp.class).sheet("模板").doWrite(vciPage.getRecords());
    }

}
