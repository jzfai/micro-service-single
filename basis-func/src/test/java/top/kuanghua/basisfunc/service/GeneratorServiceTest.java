package top.kuanghua.basisfunc.service;

import com.alibaba.fastjson.JSON;
import org.apache.velocity.Template;
import org.apache.velocity.context.Context;
import org.junit.jupiter.api.Test;
import top.kuanghua.basisfunc.utils.GeneratorTempUtils;
import top.kuanghua.commonpom.utils.ObjSelfUtils;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

/**
 * @Title: BackVmsMulTableServiceTest
 * @Description:
 * @Auther: kuanghua
 * @create 2022/6/26 11:11
 */
public class GeneratorServiceTest {

    /**
     * mybatis-plus多表生成模版
     */
    @Test
    public void generatorMybatisPlusMulTemp() throws IOException {
        String string = GeneratorTempUtils.readFileToString("D:\\github\\velocity-tmp-dir\\json-data\\" + "multiTableData.json");
        Map<String, Object> jsonData = JSON.parseObject(string, Map.class);
        Context context = GeneratorTempUtils.getVelocityContext();
        context.put("totalData", jsonData);
        context.put("projectOrAuthor", jsonData.get("projectOrAuthor"));
        context.put("multiTableConfig", jsonData.get("multiTableConfig"));
        context.put("queryConfig", jsonData.get("queryConfig"));
        context.put("tableConfig", jsonData.get("tableConfig"));
        context.put("formConfig", jsonData.get("formConfig"));
        context.put("dbTableConfig", jsonData.get("dbTableConfig"));

        Map<String, Object> dbTableConfig = ObjSelfUtils.changeToMap(jsonData.get("dbTableConfig"));
        ArrayList<Map<String, Object>> multiTableConfig = ObjSelfUtils.changeToArrayMap(jsonData.get("multiTableConfig"));

        multiTableConfig.forEach((fItem) -> {
            //entity
            FileWriter entityWriter = null;
            try {
                Template entityTemp = GeneratorTempUtils.getMybatisPlusMulTbTemp("entity.vm");
                context.put("currentTbConfig", fItem);
                context.put("tableFieldArr", fItem.get("tableFieldArr"));
                entityWriter = new FileWriter(GeneratorTempUtils.getExportMybatisPlusMulDir("entity") + fItem.get("tableNameCase") + ".java");
                entityTemp.merge(context, entityWriter);
                entityWriter.close();

                //single-mapper
                Template mapperTemp = GeneratorTempUtils.getMybatisPlusMulTbTemp("mapper.vm");
                FileWriter mapperWriter = new FileWriter(GeneratorTempUtils.getExportMybatisPlusMulDir("mapper") + fItem.get("tableNameCase") + "Mapper.java");
                mapperTemp.merge(context, mapperWriter);
                mapperWriter.close();
            } catch (IOException e) {
                throw new RuntimeException("生成实体类报错" + e);
            }
        });
        String tbName = dbTableConfig.get("multiTableNameCase").toString();

        try {
            //controller
            Template controllerTemp = GeneratorTempUtils.getMybatisPlusMulTbTemp("controllerMul.vm");
            FileWriter controllerWriter = new FileWriter(GeneratorTempUtils.getExportMybatisPlusMulDir("controller") + tbName + "Controller.java");
            controllerTemp.merge(context, controllerWriter);
            controllerWriter.close();
            //service
            Template serviceTemp = GeneratorTempUtils.getMybatisPlusMulTbTemp("serviceMul.vm");
            FileWriter serviceWriter = new FileWriter(GeneratorTempUtils.getExportMybatisPlusMulDir("service") + tbName + "Service.java");
            serviceTemp.merge(context, serviceWriter);
            serviceWriter.close();

            //mul-entity
            Template mapperMulTemp = GeneratorTempUtils.getMybatisPlusMulTbTemp("mapperMul.vm");
            FileWriter mapperMulWriter = new FileWriter(GeneratorTempUtils.getExportMybatisPlusMulDir("mapper") + tbName + "Mapper.java");
            mapperMulTemp.merge(context, mapperMulWriter);
            mapperMulWriter.close();

            //entity-vo
            Template entityVoTemp = GeneratorTempUtils.getMybatisPlusMulTbTemp("entityVo.vm");
            FileWriter entityVoWriter = new FileWriter(GeneratorTempUtils.getExportMybatisPlusMulDir("vo") + tbName + "Vo.java");
            entityVoTemp.merge(context, entityVoWriter);
            entityVoWriter.close();

            //entity-mul
            Template entityMulTemp = GeneratorTempUtils.getMybatisPlusMulTbTemp("entityMul.vm");
            FileWriter entityMulWriter = new FileWriter(GeneratorTempUtils.getExportMybatisPlusMulDir("entity") + tbName + ".java");
            entityMulTemp.merge(context, entityMulWriter);
            entityMulWriter.close();
            //xml
            Template xmlTemp = GeneratorTempUtils.getMybatisPlusMulTbTemp("xmlMul.vm");
            FileWriter xmlWriter = new FileWriter(GeneratorTempUtils.getExportMybatisPlusMulDir("xml") + tbName + "Mapper.xml");
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
     * element-plus单表生成模版
     */
//    @Test
//    public void generatorMybatisPlusTemp() throws IOException {
//        String string = GeneratorTempUtils.readFileToString("D:\\github\\velocity-tmp-dir\\json-data\\" + "tb_brand.json");
//        Map<String, Object> jsonData = JSON.parseObject(string, Map.class);
//
//        Context context = GeneratorTempUtils.getVelocityContext();
//        context.put("configData", jsonData);
//        context.put("projectOrAuthor", jsonData.get("projectOrAuthor"));
//        context.put("dbTableConfig", jsonData.get("dbTableConfig"));
//        context.put("apiConfig", jsonData.get("apiConfig"));
//        context.put("queryConfig", jsonData.get("queryConfig"));
//        context.put("tableConfig", jsonData.get("tableConfig"));
//        context.put("formConfig", jsonData.get("formConfig"));
//        context.put("commonConfig", jsonData.get("commonConfig"));
//
//
//        Map<String, Object> dbTableConfig = JSON.parseObject(JSON.toJSONString(jsonData.get("dbTableConfig")), Map.class);
//
//        String tbName = dbTableConfig.get("tableNameCase").toString();
//        //entity
//        Template entityTemp = GeneratorTempUtils.getMybatisPlusTemp("entity.vm");
//        FileWriter entityWriter = new FileWriter(GeneratorTempUtils.getExportMybatisPlusDir("entity") + tbName + ".java");
//        entityTemp.merge(context, entityWriter);
//        entityWriter.close();
//        //controller
//        Template controllerTemp = GeneratorTempUtils.getMybatisPlusTemp("controller.vm");
//        FileWriter controllerWriter = new FileWriter(GeneratorTempUtils.getExportMybatisPlusDir("controller") + tbName + "Controller.java");
//        controllerTemp.merge(context, controllerWriter);
//        controllerWriter.close();
//        //service
//        Template serviceTemp = GeneratorTempUtils.getMybatisPlusTemp("service.vm");
//        FileWriter serviceWriter = new FileWriter(GeneratorTempUtils.getExportMybatisPlusDir("service") + tbName + "Service.java");
//        serviceTemp.merge(context, serviceWriter);
//        serviceWriter.close();
//        //mapper
//        Template mapperTemp = GeneratorTempUtils.getMybatisPlusTemp("mapper.vm");
//        FileWriter mapperWriter = new FileWriter(GeneratorTempUtils.getExportMybatisPlusDir("mapper") + tbName + "Mapper.java");
//        mapperTemp.merge(context, mapperWriter);
//        mapperWriter.close();
//    }
//
//
//    /**
//     * element-plus多表生成模版
//     */
//    @Test
//    public void generatorElementPlusTemp() throws IOException {
//        String string = GeneratorTempUtils.readFileToString("D:\\github\\velocity-tmp-dir\\json-data\\" + "tb_brand.json");
//        Map<String, Object> jsonData = JSON.parseObject(string, Map.class);
//        Context context = GeneratorTempUtils.getVelocityContext();
//        context.put("configData", jsonData);
//        context.put("companyOrAuthor", jsonData.get("companyOrAuthor"));
//        context.put("dbTableConfig", jsonData.get("dbTableConfig"));
//        context.put("apiConfig", jsonData.get("apiConfig"));
//        context.put("queryConfig", jsonData.get("queryConfig"));
//        context.put("tableConfig", jsonData.get("tableConfig"));
//        context.put("formConfig", jsonData.get("formConfig"));
//        context.put("commonConfig", jsonData.get("commonConfig"));
//        Template template = GeneratorTempUtils.getElementPlusTemp("CRUD.vm");
//        FileWriter fileWriter = new FileWriter(GeneratorTempUtils.getExportElementPlusDir("") + "CRUD.vue");
//        template.merge(context, fileWriter);
//        fileWriter.close();
//        //第二个模板
//        Template addModal = GeneratorTempUtils.getElementPlusTemp("CRUDForm.vm");
//        FileWriter addModalWriter = new FileWriter(GeneratorTempUtils.getExportElementPlusDir("") + "CRUDForm.vue");
//        addModal.merge(context, addModalWriter);
//        addModalWriter.close();
//    }

}