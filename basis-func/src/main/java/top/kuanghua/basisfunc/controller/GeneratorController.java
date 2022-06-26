package top.kuanghua.basisfunc.controller;

import com.alibaba.fastjson.JSON;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import top.kuanghua.basisfunc.service.GeneratorCustromService;
import top.kuanghua.basisfunc.service.GeneratorService;
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
@RequestMapping("generator")
public class GeneratorController {
    @Resource
    private GeneratorService generatorService;
    
    @Resource
    private GeneratorCustromService generatorCustromService;

    @ApiOperation(value = "生成前后端模版")
    @PostMapping("generatorFrontBackTempZip")
    public void generatorFrontBackTempZip(HttpServletResponse response, @RequestBody Map generatorData) {
        //生成模板
        String exportFilePath = this.generatorService.generatorFrontBackTempZip(generatorData);
        response.setContentType("application/zip");
        response.setCharacterEncoding("utf-8");
        response.setHeader("Access-Control-Expose-Headers", "exportFileName");
        response.setHeader("exportFileName", "font-back-temp-" + ObjSelfUtils.getCurrentDateTimeTrim() + ".zip");
        //你压缩包路径
        GeneratorTempUtils.downloadZip(response, exportFilePath);
    }

    @ApiOperation(value = "生成前端模版")
    @PostMapping("generatorFrontTempZip")
    public void generatorFrontTempZip(HttpServletResponse response, @RequestBody Map generatorData) {
        //生成模板
        String exportFilePath = this.generatorService.generatorFrontTempZip(generatorData);
        response.setContentType("application/zip");
        response.setCharacterEncoding("utf-8");
        response.setHeader("Access-Control-Expose-Headers", "exportFileName");
        response.setHeader("exportFileName", "front-temp-" + ObjSelfUtils.getCurrentDateTimeTrim() + ".zip");
        //你压缩包路径
        GeneratorTempUtils.downloadZip(response, exportFilePath);
    }

    @ApiOperation(value = "生成后端模版")
    @PostMapping("generatorBackTempZip")
    public void generatorBackTempZip(HttpServletResponse response, @RequestBody Map generatorData) {
        //生成模板
        String exportFilePath = this.generatorService.generatorBackTempZip(generatorData);
        response.setContentType("application/zip");
        response.setCharacterEncoding("utf-8");
        response.setHeader("Access-Control-Expose-Headers", "exportFileName");
        response.setHeader("exportFileName", "back-temp-" + ObjSelfUtils.getCurrentDateTimeTrim() + ".zip");
        //你压缩包路径
        GeneratorTempUtils.downloadZip(response, exportFilePath);
    }

    /*自定义模版*/
    @ApiOperation(value = "生成前端自定义模版")
    @PostMapping("generatorFrontCustomTemp")
    public void generatorFrontCustomTemp(HttpServletResponse response, @RequestParam("file") List<MultipartFile> files, @RequestParam("jsonData") String jsonData) {
        Map<String, Object> JsonMap = JSON.parseObject(jsonData, Map.class);
        //生成模板
        String exportFilePath = generatorCustromService.generatorFrontCustomTemp(files, JsonMap);
        response.setContentType("application/zip");
        response.setCharacterEncoding("utf-8");
        response.setHeader("Access-Control-Expose-Headers", "exportFileName");
        response.setHeader("exportFileName", "custom-front-temp-" + ObjSelfUtils.getCurrentDateTimeTrim() + ".zip");
        //你压缩包路径
        GeneratorTempUtils.downloadZip(response, exportFilePath);
    }

    @ApiOperation(value = "生成后端自定义模版")
    @PostMapping("generatorBackCustomTemp")
    public void generatorBackCustomTemp(HttpServletResponse response, @RequestParam("file") List<MultipartFile> files, @RequestParam("jsonData") String jsonData) {
        Map<String, Object> JsonMap = JSON.parseObject(jsonData, Map.class);
        //生成模板
        String exportFilePath = generatorCustromService.generatorBackCustomTemp(files, JsonMap);
        response.setContentType("application/zip");
        response.setCharacterEncoding("utf-8");
        response.setHeader("Access-Control-Expose-Headers", "exportFileName");
        response.setHeader("exportFileName", "custom-back-temp-" + ObjSelfUtils.getCurrentDateTimeTrim() + ".zip");
        //你压缩包路径
        GeneratorTempUtils.downloadZip(response, exportFilePath);
    }

    @ApiOperation(value = "自定义生成模版（只提供数据源能力，导入的模版是什么则返回什么， 返回后会对模版里的插槽字段进行填充）")
    @PostMapping("generatorCustomTemp")
    public void generatorCustomTemp(HttpServletResponse response, @RequestParam("file") List<MultipartFile> files, @RequestParam("jsonData") String jsonData) {
        Map<String, Object> JsonMap = JSON.parseObject(jsonData, Map.class);
        //生成模板
        String exportFilePath = generatorCustromService.generatorCustomTemp(files, JsonMap);
        response.setContentType("application/zip");
        response.setCharacterEncoding("utf-8");
        response.setHeader("Access-Control-Expose-Headers", "exportFileName");
        response.setHeader("exportFileName", "custom-temp-" + ObjSelfUtils.getCurrentDateTimeTrim() + ".zip");
        //你压缩包路径
        GeneratorTempUtils.downloadZip(response, exportFilePath);
    }
}
