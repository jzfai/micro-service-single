package top.kuanghua.basisfunc.controller;

import com.alibaba.fastjson.JSON;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.velocity.Template;
import org.apache.velocity.context.Context;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import top.kuanghua.basisfunc.service.GeneratorCustromService;
import top.kuanghua.basisfunc.service.GeneratorService;
import top.kuanghua.basisfunc.service.MybatisPlusGeneratorService;
import top.kuanghua.basisfunc.utils.GeneratorTempUtils;
import top.kuanghua.commonpom.entity.ResResult;
import top.kuanghua.commonpom.utils.ObjSelfUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
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


    @ApiOperation(value = "实时测试模板")
    @PostMapping("test-tmp-generator")
    public ResResult<String> getUploadFileToVms(@RequestParam("file") List<MultipartFile> files, @RequestParam("jsonData") String jsonData,
                                                @RequestParam("code") String code) {
        String tmpSaveDir = GeneratorTempUtils.getTmpSaveDir();
        //接收文件并保存文件到本地
        for (MultipartFile file : files) {
            String filename = file.getOriginalFilename();
            File f = new File(tmpSaveDir + filename);
            //保存文件
            try {
                file.transferTo(f);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        //获取数据并生成vm模版
        Map<String, Object> JsonMap = JSON.parseObject(jsonData, Map.class);
        //生成模板
        String tmpDirTemp = mybatisPlusGeneratorService.generatorTmpDirTemp(files, JsonMap, tmpSaveDir, code);
        return new ResResult().success(tmpDirTemp);
    }

}
