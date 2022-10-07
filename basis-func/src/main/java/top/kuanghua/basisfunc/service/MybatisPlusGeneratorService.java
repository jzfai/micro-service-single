package top.kuanghua.basisfunc.service;

import com.alibaba.fastjson.JSON;
import org.apache.velocity.Template;
import org.apache.velocity.context.Context;
import org.springframework.stereotype.Service;
import top.kuanghua.basisfunc.utils.GeneratorTempUtils;
import top.kuanghua.commonpom.utils.ObjSelfUtils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.regex.Matcher;

/**
 * @author 猫哥
 * @date 2022-06-06 13:31
 **/
@Service
public class MybatisPlusGeneratorService {
    /**
     * Mybatis-plus单表生成模版
     */
    public String generatorMybatisPlusBasicTmp(Map jsonData) {
        try {
            Context context = GeneratorTempUtils.getVelocityContext();
            context.put("totalData", jsonData);
            context.put("basicConfig", jsonData.get("basicConfig"));
            context.put("queryConfig", jsonData.get("queryConfig"));
            context.put("dbTableConfig", jsonData.get("dbTableConfig"));
            context.put("tableConfig", jsonData.get("tableConfig"));
            context.put("formConfig", jsonData.get("formConfig"));
            Map<String, Object> dbTableConfig = ObjSelfUtils.changeToMap(jsonData.get("dbTableConfig"));
            String tbName = dbTableConfig.get("tableNameCase").toString();

            Map<String, Object> basicConfig = ObjSelfUtils.changeToMap(jsonData.get("basicConfig"));
            //包名转路径
            String packagePath = basicConfig.get("packageName").toString().replaceAll("\\.",  Matcher.quoteReplacement(File.separator));

            //entity
            Template entityTemp = GeneratorTempUtils.getMybatisPlusTemp("entity.vm");
            FileWriter entityWriter = new FileWriter(GeneratorTempUtils.getExportMybatisPlusDir(packagePath,"entity") + tbName + ".java");
            entityTemp.merge(context, entityWriter);
            entityWriter.close();
            //controller
            Template controllerTemp = GeneratorTempUtils.getMybatisPlusTemp("controller.vm");
            FileWriter controllerWriter = new FileWriter(GeneratorTempUtils.getExportMybatisPlusDir(packagePath,"controller") + tbName + "Controller.java");
            controllerTemp.merge(context, controllerWriter);
            controllerWriter.close();
            //service
            Template serviceTemp = GeneratorTempUtils.getMybatisPlusTemp("service.vm");
            FileWriter serviceWriter = new FileWriter(GeneratorTempUtils.getExportMybatisPlusDir(packagePath,"service") + tbName + "Service.java");
            serviceTemp.merge(context, serviceWriter);
            serviceWriter.close();
            //mapper
            Template mapperTemp = GeneratorTempUtils.getMybatisPlusTemp("mapper.vm");
            FileWriter mapperWriter = new FileWriter(GeneratorTempUtils.getExportMybatisPlusDir(packagePath,"mapper") + tbName + "Mapper.java");
            mapperTemp.merge(context, mapperWriter);
            mapperWriter.close();


            String exportFilePath = GeneratorTempUtils.getOutputZipPath() + ObjSelfUtils.getCurrentDateTimeTrim() + ".zip";
            //生成zip包
            GeneratorTempUtils.createZipFile(exportFilePath, GeneratorTempUtils.getNeedZipDir());
            return exportFilePath;
        } catch (IOException e) {
            throw new RuntimeException("Mybatis-plus单表生成模版报错" + e);
        }
    }

    /**
     * element-plus多表生成模版
     */
    public void generatorElementPlusTemp(Map jsonData) {
        try {
            Context context = getContext(jsonData);
            Template template = GeneratorTempUtils.getElementPlusTemp("CRUD.vm");
            FileWriter fileWriter = new FileWriter(GeneratorTempUtils.getExportElementPlusDir("") + "CRUD.vue");
            template.merge(context, fileWriter);
            fileWriter.close();
            //第二个模板
            Template addModal = GeneratorTempUtils.getElementPlusTemp("CRUDForm.vm");
            FileWriter addModalWriter = new FileWriter(GeneratorTempUtils.getExportElementPlusDir("") + "CRUDForm.vue");
            addModal.merge(context, addModalWriter);
            addModalWriter.close();
        } catch (IOException e) {
            throw new RuntimeException("element-plus多表生成模版报错" + e);
        }
    }

    /**
     * 生成后端模版
     *
     * @param jsonData
     * @return
     */
    public String generatorBackTempZip(Map jsonData) {
        Map<String, Object> commonConfig = JSON.parseObject(JSON.toJSONString(jsonData.get("commonConfig")), Map.class);
        if ("true".equals(commonConfig.get("isTableMulChoose").toString())) {
            //多表模版
//            generatorMybatisPlusMulTemp(jsonData);
        } else {
            /*单表*/
//            generatorMybatisPlusTemp(jsonData);
        }
        String exportFilePath = GeneratorTempUtils.getOutputZipPath() + ObjSelfUtils.getCurrentDateTimeTrim() + ".zip";
        //生成zip包
        GeneratorTempUtils.createZipFile(exportFilePath, GeneratorTempUtils.getNeedZipDir());
        return exportFilePath;
    }

    /**
     * 生成前端模版
     *
     * @param jsonData
     * @return
     */
    public String generatorFrontTempZip(Map jsonData) {
        Map<String, Object> commonConfig = JSON.parseObject(JSON.toJSONString(jsonData.get("commonConfig")), Map.class);
        //多表
        if ("true".equals(commonConfig.get("isTableMulChoose").toString())) {
            //生成前端模板
            generatorElementPlusTemp(jsonData);
        } else {
            /*单表*/
            //生成前端模板
            generatorElementPlusTemp(jsonData);
        }
        String exportFilePath = GeneratorTempUtils.getOutputZipPath() + ObjSelfUtils.getCurrentDateTimeTrim() + ".zip";
        GeneratorTempUtils.createZipFile(exportFilePath, GeneratorTempUtils.getNeedZipDir());
        return exportFilePath;
    }


    private Context getContext(Map jsonData) {
        Context context = GeneratorTempUtils.getVelocityContext();
        context.put("totalData", jsonData);
        context.put("projectOrAuthor", jsonData.get("projectOrAuthor"));
        context.put("commonConfig", jsonData.get("commonConfig"));
        context.put("multiTableConfig", jsonData.get("multiTableConfig"));
        context.put("dbTableConfig", jsonData.get("dbTableConfig"));
        context.put("apiConfig", jsonData.get("apiConfig"));
        context.put("queryConfig", jsonData.get("queryConfig"));
        context.put("tableConfig", jsonData.get("tableConfig"));
        context.put("formConfig", jsonData.get("formConfig"));
        return context;
    }

}
