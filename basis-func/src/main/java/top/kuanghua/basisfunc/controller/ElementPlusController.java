package top.kuanghua.basisfunc.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;
import top.kuanghua.basisfunc.service.ElementPlusService;
import top.kuanghua.basisfunc.utils.GeneratorTempUtils;
import top.kuanghua.commonpom.utils.ObjSelfUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 *  生成代码控制层
 * @author 邝华
 * @email kuanghua@aulton.com
 * @date 2022-06-06 13:39
 * @Copyright Copyright (c) aulton Inc. All Rights Reserved.
 **/
@Api(tags = "element-plus代码生成")
@RestController
@RequestMapping("element-plus")
public class ElementPlusController {
    @Resource
    private ElementPlusService elementPlusService;

    @PostMapping("querySearchTmp")
    @ApiOperation(value = "查询模板生成")
    public void querySearchTmp(HttpServletResponse response, @RequestBody Map generatorData) {
        //生成模板
        String exportFilePath = this.elementPlusService.generatorTableQuery(generatorData);
        response.setContentType("application/zip");
        response.setCharacterEncoding("utf-8");
        response.setHeader("Access-Control-Expose-Headers", "exportFileName");
        response.setHeader("exportFileName", "querySearchTmp-"+ ObjSelfUtils.getCurrentDateTimeTrim()+".zip");
        //你压缩包路径
        GeneratorTempUtils.downloadZip(response, exportFilePath);
    }


    @PostMapping("addEdit")
    @ApiOperation(value = "添加编辑生成")
    public void addEditTmp(HttpServletResponse response, @RequestBody Map generatorData) {
        //生成模板
        String exportFilePath = this.elementPlusService.generatorAddEdit(generatorData);
        response.setContentType("application/zip");
        response.setCharacterEncoding("utf-8");
        response.setHeader("Access-Control-Expose-Headers", "exportFileName");
        response.setHeader("exportFileName", "addEdit-"+ObjSelfUtils.getCurrentDateTimeTrim()+".zip");
        //你压缩包路径
        GeneratorTempUtils.downloadZip(response, exportFilePath);
    }


    @PostMapping("detail")
    @ApiOperation(value = "详情模板生成")
    public void detailTmp(HttpServletResponse response, @RequestBody Map generatorData) {
        //生成模板
        String exportFilePath = this.elementPlusService.generatorDetail(generatorData);
        response.setContentType("application/zip");
        response.setCharacterEncoding("utf-8");
        response.setHeader("Access-Control-Expose-Headers", "exportFileName");
        response.setHeader("exportFileName", "detail-"+ObjSelfUtils.getCurrentDateTimeTrim()+".zip");
        //你压缩包路径
        GeneratorTempUtils.downloadZip(response, exportFilePath);
    }
}
