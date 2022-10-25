package top.kuanghua.basisfunc.service;

import org.apache.velocity.Template;
import org.apache.velocity.context.Context;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import top.kuanghua.basisfunc.utils.GeneratorTempUtils;
import top.kuanghua.commonpom.utils.ObjSelfUtils;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
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
            String packagePath = basicConfig.get("packageName").toString().replaceAll("\\.", Matcher.quoteReplacement(File.separator));

            //entity
            Template entityTemp = GeneratorTempUtils.getMybatisPlusTemp("entity.vm");
            FileWriter entityWriter = new FileWriter(GeneratorTempUtils.getExportMybatisPlusDir(packagePath, "entity") + tbName + ".java");
            entityTemp.merge(context, entityWriter);
            entityWriter.close();

            //vo
            Template voTemp = GeneratorTempUtils.getMybatisPlusTemp("vo.vm");
            FileWriter voWriter = new FileWriter(GeneratorTempUtils.getExportMybatisPlusDir(packagePath, "vo") + tbName + "Vo.java");
            voTemp.merge(context, voWriter);
            voWriter.close();

            //controller
            Template controllerTemp = GeneratorTempUtils.getMybatisPlusTemp("controller.vm");
            FileWriter controllerWriter = new FileWriter(GeneratorTempUtils.getExportMybatisPlusDir(packagePath, "controller") + tbName + "Controller.java");
            controllerTemp.merge(context, controllerWriter);
            controllerWriter.close();

            //service
            Template serviceTemp = GeneratorTempUtils.getMybatisPlusTemp("service.vm");
            FileWriter serviceWriter = new FileWriter(GeneratorTempUtils.getExportMybatisPlusDir(packagePath, "service") + tbName + "Service.java");
            serviceTemp.merge(context, serviceWriter);
            serviceWriter.close();

            //mapper
            Template mapperTemp = GeneratorTempUtils.getMybatisPlusTemp("mapper.vm");
            FileWriter mapperWriter = new FileWriter(GeneratorTempUtils.getExportMybatisPlusDir(packagePath, "mapper") + tbName + "Mapper.java");
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
     * Mybatis-plus  多表生成
     *
     * @return
     */
    public String generatorMybatisPlusMulTemp(Map<String, Object> jsonData) {
        Context context = GeneratorTempUtils.getVelocityContext();
        context.put("totalData", jsonData);
        context.put("basicConfig", jsonData.get("basicConfig"));
        context.put("multiTableConfig", jsonData.get("multiTableConfig"));
        context.put("dbTableConfig", jsonData.get("dbTableConfig"));
        context.put("apiConfig", jsonData.get("apiConfig"));
        context.put("queryConfig", jsonData.get("queryConfig"));
        context.put("tableConfig", jsonData.get("tableConfig"));
        context.put("formConfig", jsonData.get("formConfig"));
        Map<String, Object> dbTableConfig = ObjSelfUtils.changeToMap(jsonData.get("dbTableConfig"));
        ArrayList<Map<String, Object>> multiTableConfig = ObjSelfUtils.changeToArrayMap(jsonData.get("multiTableConfig"));
        Map<String, Object> basicConfig = ObjSelfUtils.changeToMap(jsonData.get("basicConfig"));
        String packagePath = basicConfig.get("packageName").toString().replaceAll("\\.", Matcher.quoteReplacement(File.separator));
        multiTableConfig.forEach((fItem) -> {
            //entity
            FileWriter entityWriter = null;
            try {
                Template entityTemp = GeneratorTempUtils.getMybatisPlusMulTbTemp("entity.vm");
                context.put("currentTbConfig", fItem);
//                if (multiTableConfig.get(multiTableConfig.size() - 1).get("originTableName").equals(fItem.get("originTableName"))) {
//                    context.put("tableFormArr", fItem.get("tableFieldArr"));
//                } else {
//                    context.put("tableFormArr", fItem.get("tableFormArr"));
//                }
                context.put("tableFormArr", fItem.get("tableFieldArr"));
                entityWriter = new FileWriter(GeneratorTempUtils.getExportMybatisPlusDir(packagePath, "entity") + fItem.get("tableNameCase") + ".java");
                entityTemp.merge(context, entityWriter);
                entityWriter.close();

                //single-mapper
                Template mapperTemp = GeneratorTempUtils.getMybatisPlusMulTbTemp("mapper.vm");
                FileWriter mapperWriter = new FileWriter(GeneratorTempUtils.getExportMybatisPlusDir(packagePath, "mapper") + fItem.get("tableNameCase") + "Mapper.java");
                mapperTemp.merge(context, mapperWriter);
                mapperWriter.close();
            } catch (IOException e) {
                throw new RuntimeException(" mybatis-plus多表生成模版报错" + e);
            }
        });
        String tbName = dbTableConfig.get("multiTableNameCase").toString();
        try {
            //controller
            Template controllerTemp = GeneratorTempUtils.getMybatisPlusMulTbTemp("controllerMul.vm");
            FileWriter controllerWriter = new FileWriter(GeneratorTempUtils.getExportMybatisPlusDir(packagePath, "controller") + tbName + "Controller.java");
            controllerTemp.merge(context, controllerWriter);
            controllerWriter.close();
            //service
            Template serviceTemp = GeneratorTempUtils.getMybatisPlusMulTbTemp("serviceMul.vm");
            FileWriter serviceWriter = new FileWriter(GeneratorTempUtils.getExportMybatisPlusDir(packagePath, "service") + tbName + "Service.java");
            serviceTemp.merge(context, serviceWriter);
            serviceWriter.close();

            //mul-entity
            Template mapperMulTemp = GeneratorTempUtils.getMybatisPlusMulTbTemp("mapperMul.vm");
            FileWriter mapperMulWriter = new FileWriter(GeneratorTempUtils.getExportMybatisPlusDir(packagePath, "mapper") + tbName + "Mapper.java");
            mapperMulTemp.merge(context, mapperMulWriter);
            mapperMulWriter.close();

            //query
            Template queryTemp = GeneratorTempUtils.getMybatisPlusMulTbTemp("query.vm");
            FileWriter queryWriter = new FileWriter(GeneratorTempUtils.getExportMybatisPlusDir(packagePath, "query") + tbName + "Query.java");
            queryTemp.merge(context, queryWriter);
            queryWriter.close();

            //vo
            Template voTemp = GeneratorTempUtils.getMybatisPlusTemp("vo.vm");
            FileWriter voWriter = new FileWriter(GeneratorTempUtils.getExportMybatisPlusDir(packagePath, "vo") + tbName + "Vo.java");
            voTemp.merge(context, voWriter);
            voWriter.close();

            //entity-mul
            Template entityMulTemp = GeneratorTempUtils.getMybatisPlusMulTbTemp("entityMul.vm");
            FileWriter entityMulWriter = new FileWriter(GeneratorTempUtils.getExportMybatisPlusDir(packagePath, "entity") + tbName + ".java");
            entityMulTemp.merge(context, entityMulWriter);
            entityMulWriter.close();
            //xml
            Template xmlTemp = GeneratorTempUtils.getMybatisPlusMulTbTemp("xmlMul.vm");
            FileWriter xmlWriter = new FileWriter(GeneratorTempUtils.getExportMybatisPlusDir(packagePath, "xml") + tbName + "Mapper.xml");
            xmlTemp.merge(context, xmlWriter);
            xmlWriter.close();

            String exportFilePath = GeneratorTempUtils.getOutputZipPath() + ObjSelfUtils.getCurrentDateTimeTrim() + ".zip";
            //生成zip包
            GeneratorTempUtils.createZipFile(exportFilePath, GeneratorTempUtils.getNeedZipDir());


            return exportFilePath;
        } catch (IOException e) {
            throw new RuntimeException("生成实体类报错" + e);
        }
    }

    /**
     * mybatis-plus多表生成模版-查询
     */
    public void generatorMybatisPlusMulQueryTmp(Map jsonData) {
        Context context = GeneratorTempUtils.getVelocityContext();
        context.put("totalData", jsonData);
        context.put("basicConfig", jsonData.get("basicConfig"));
        context.put("multiTableConfig", jsonData.get("multiTableConfig"));
        context.put("dbTableConfig", jsonData.get("dbTableConfig"));
        context.put("apiConfig", jsonData.get("apiConfig"));
        context.put("queryConfig", jsonData.get("queryConfig"));
        context.put("tableConfig", jsonData.get("tableConfig"));
        context.put("formConfig", jsonData.get("formConfig"));
        Map<String, Object> dbTableConfig = ObjSelfUtils.changeToMap(jsonData.get("dbTableConfig"));
//        ArrayList<Map<String, Object>> multiTableConfig = ObjSelfUtils.changeToArrayMap(jsonData.get("multiTableConfig"));
//        multiTableConfig.forEach((fItem) -> {
//            //entity
//            FileWriter entityWriter = null;
//            try {
//                Template entityTemp = GeneratorTempUtils.getMybatisPlusMulTbTemp("entity.vm");
//                context.put("currentTbConfig", fItem);
//                context.put("tableFieldArr", fItem.get("tableFieldArr"));
//                entityWriter = new FileWriter(GeneratorTempUtils.getExportMybatisPlusMulQueryDir("entity") + fItem.get("tableNameCase") + ".java");
//                entityTemp.merge(context, entityWriter);
//                entityWriter.close();
//
//                //single-mapper
//                Template mapperTemp = GeneratorTempUtils.getMybatisPlusMulTbTemp("mapper.vm");
//                FileWriter mapperWriter = new FileWriter(GeneratorTempUtils.getExportMybatisPlusMulQueryDir("mapper") + fItem.get("tableNameCase") + "Mapper.java");
//                mapperTemp.merge(context, mapperWriter);
//                mapperWriter.close();
//            } catch (IOException e) {
//                throw new RuntimeException(" mybatis-plus多表生成模版报错" + e);
//            }
//        });
        String tbName = dbTableConfig.get("multiTableNameCase").toString();
        try {
            //controller
            Template controllerTemp = GeneratorTempUtils.getMybatisPlusMulTbTemp("controllerMul.vm");
            FileWriter controllerWriter = new FileWriter(GeneratorTempUtils.getExportMybatisPlusMulQueryDir("controller") + tbName + "Controller.java");
            controllerTemp.merge(context, controllerWriter);
            controllerWriter.close();
            //service
            Template serviceTemp = GeneratorTempUtils.getMybatisPlusMulTbTemp("serviceMul.vm");
            FileWriter serviceWriter = new FileWriter(GeneratorTempUtils.getExportMybatisPlusMulQueryDir("service") + tbName + "Service.java");
            serviceTemp.merge(context, serviceWriter);
            serviceWriter.close();

            //mul-entity
            Template mapperMulTemp = GeneratorTempUtils.getMybatisPlusMulTbTemp("mapperMul.vm");
            FileWriter mapperMulWriter = new FileWriter(GeneratorTempUtils.getExportMybatisPlusMulQueryDir("mapper") + tbName + "Mapper.java");
            mapperMulTemp.merge(context, mapperMulWriter);
            mapperMulWriter.close();

            //entity-vo
            Template entityVoTemp = GeneratorTempUtils.getMybatisPlusMulTbTemp("entityVo.vm");
            FileWriter entityVoWriter = new FileWriter(GeneratorTempUtils.getExportMybatisPlusMulQueryDir("vo") + tbName + "Vo.java");
            entityVoTemp.merge(context, entityVoWriter);
            entityVoWriter.close();

            //entity-mul
            Template entityMulTemp = GeneratorTempUtils.getMybatisPlusMulTbTemp("entityMul.vm");
            FileWriter entityMulWriter = new FileWriter(GeneratorTempUtils.getExportMybatisPlusMulQueryDir("entity") + tbName + ".java");
            entityMulTemp.merge(context, entityMulWriter);
            entityMulWriter.close();
            //xml
            Template xmlTemp = GeneratorTempUtils.getMybatisPlusMulTbTemp("xmlMul.vm");
            FileWriter xmlWriter = new FileWriter(GeneratorTempUtils.getExportMybatisPlusMulQueryDir("xml") + tbName + "Mapper.xml");
            xmlTemp.merge(context, xmlWriter);
            xmlWriter.close();
            String exportFilePath = GeneratorTempUtils.getOutputZipPath() + ObjSelfUtils.getCurrentDateTimeTrim() + ".zip";
            //生成zip包
            GeneratorTempUtils.createZipFile(exportFilePath, GeneratorTempUtils.getNeedZipDir());
        } catch (IOException e) {
            throw new RuntimeException("生成实体类报错" + e);
        }
    }


    /**
     * 测试模板
     *
     * @param files 传入的文件数组
     * @return
     * @author 猫哥
     * @date 2022-10-25 9:51
     */
    public String generatorTmpDirTemp(List<MultipartFile> files, Map jsonData, String tmpSaveDir, String code) {

        Context context = GeneratorTempUtils.getVelocityContext();
        context.put("configData", jsonData);
        context.put("basicConfig", jsonData.get("basicConfig"));
        context.put("apiConfig", jsonData.get("apiConfig"));
        context.put("tableList", jsonData.get("tableList"));
        context.put("tableConfigArr", jsonData.get("tableConfigArr"));
        context.put("queryConfig", jsonData.get("queryConfig"));
        context.put("tableConfig", jsonData.get("tableConfig"));
        try {
            String codeName = "tmp-code";
            FileWriter fw = new FileWriter(tmpSaveDir + codeName);
            fw.write(code);
            fw.close();
            Template xmlTemp = GeneratorTempUtils.getTmpSaveDirTemp(tmpSaveDir, codeName);

            Writer xmlWriter = new StringWriter();
            xmlTemp.merge(context, xmlWriter);
            return xmlWriter.toString();
        } catch (IOException e) {
            throw new RuntimeException("生成临时模版报错" + e);
        }
    }
}
