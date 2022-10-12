package top.kuanghua.basisfunc.controller;

import com.alibaba.fastjson.JSON;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import top.kuanghua.basisfunc.service.GeneratorCustromService;
import top.kuanghua.basisfunc.service.GeneratorService;
import top.kuanghua.basisfunc.service.MybatisPlusGeneratorService;
import top.kuanghua.basisfunc.utils.GeneratorTempUtils;
import top.kuanghua.commonpom.utils.ObjSelfUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * @author 猫哥
 * @email kuanghua@aulton.com
 * @date 2022-06-06 13:39
 * @Copyright Copyright (c) aulton Inc. All Rights Reserved.
 **/
@Api(tags = "模板生成")
@RestController
@RequestMapping("mybatis-plus")
public class MybatisPlusController {
    @Resource
    private MybatisPlusGeneratorService mybatisPlusGeneratorService;


    @ApiOperation(value = "基础模板生成")
    @PostMapping("generatorMybatisPlusBasicTmp")
    public void generatorMybatisPlusBasicTmp(HttpServletResponse response, @RequestBody Map generatorData) {
        //生成模板
        String exportFilePath = this.mybatisPlusGeneratorService.generatorMybatisPlusBasicTmp(generatorData);
        response.setContentType("application/zip");
        response.setCharacterEncoding("utf-8");
        response.setHeader("Access-Control-Expose-Headers", "exportFileName");
        response.setHeader("exportFileName", "MybatisPlusBasic" + ObjSelfUtils.getCurrentDateTimeTrim() + ".zip");
        //你压缩包路径
        GeneratorTempUtils.downloadZip(response, exportFilePath);
    }

    @ApiOperation(value = "多表生成")
    @PostMapping("generatorMybatisPlusMulTemp")
    public void generatorMybatisPlusMulTemp(HttpServletResponse response, @RequestBody Map generatorData) {
        //生成模板
        String exportFilePath = this.mybatisPlusGeneratorService.generatorMybatisPlusMulTemp(generatorData);
        response.setContentType("application/zip");
        response.setCharacterEncoding("utf-8");
        response.setHeader("Access-Control-Expose-Headers", "exportFileName");
        response.setHeader("exportFileName", "generatorMybatisPlusMulTemp-" + ObjSelfUtils.getCurrentDateTimeTrim() + ".zip");
        //你压缩包路径
        GeneratorTempUtils.downloadZip(response, exportFilePath);
    }

}
