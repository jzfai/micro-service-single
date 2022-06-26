package top.kuanghua.basisfunc.service;

import com.alibaba.fastjson.JSON;
import org.apache.velocity.Template;
import org.apache.velocity.context.Context;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import top.kuanghua.basisfunc.utils.GeneratorTempUtils;
import top.kuanghua.commonpom.utils.ObjSelfUtils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author 猫哥
 * @date 2022-06-06 13:31
 **/
@Service
public class GeneratorCustromService {

    /**
     * mybatis-plus多表生成模版
     */
    public void generatorMybatisPlusMulTemp(Map jsonData, String tmpSaveDir) {
        Context context = getContext(jsonData);
        Map<String, Object> dbTableConfig = ObjSelfUtils.changeToMap(jsonData.get("dbTableConfig"));
        ArrayList<Map<String, Object>> multiTableConfig = ObjSelfUtils.changeToArrayMap(jsonData.get("multiTableConfig"));
        multiTableConfig.forEach((fItem) -> {
            //entity
            FileWriter entityWriter = null;
            try {
                Template entityTemp = GeneratorTempUtils.getTmpSaveDirTemp(tmpSaveDir, "entity.vm");
                context.put("currentTbConfig", fItem);
                context.put("tableFieldArr", fItem.get("tableFieldArr"));
                entityWriter = new FileWriter(GeneratorTempUtils.getExportMybatisPlusMulDir("entity") + fItem.get("tableNameCase") + ".java");
                entityTemp.merge(context, entityWriter);
                entityWriter.close();

                //single-mapper
                Template mapperTemp = GeneratorTempUtils.getTmpSaveDirTemp(tmpSaveDir, "mapper.vm");
                FileWriter mapperWriter = new FileWriter(GeneratorTempUtils.getExportMybatisPlusMulDir("mapper") + fItem.get("tableNameCase") + "Mapper.java");
                mapperTemp.merge(context, mapperWriter);
                mapperWriter.close();
            } catch (IOException e) {
                throw new RuntimeException(" mybatis-plus多表生成模版报错" + e);
            }
        });
        String tbName = dbTableConfig.get("multiTableNameCase").toString();
        try {
            //controller
            Template controllerTemp = GeneratorTempUtils.getTmpSaveDirTemp(tmpSaveDir, "controllerMul.vm");
            FileWriter controllerWriter = new FileWriter(GeneratorTempUtils.getExportMybatisPlusMulDir("controller") + tbName + "Controller.java");
            controllerTemp.merge(context, controllerWriter);
            controllerWriter.close();
            //service
            Template serviceTemp = GeneratorTempUtils.getTmpSaveDirTemp(tmpSaveDir, "serviceMul.vm");
            FileWriter serviceWriter = new FileWriter(GeneratorTempUtils.getExportMybatisPlusMulDir("service") + tbName + "Service.java");
            serviceTemp.merge(context, serviceWriter);
            serviceWriter.close();

            //mul-entity
            Template mapperMulTemp = GeneratorTempUtils.getTmpSaveDirTemp(tmpSaveDir, "mapperMul.vm");
            FileWriter mapperMulWriter = new FileWriter(GeneratorTempUtils.getExportMybatisPlusMulDir("mapper") + tbName + "Mapper.java");
            mapperMulTemp.merge(context, mapperMulWriter);
            mapperMulWriter.close();

            //entity-vo
            Template entityVoTemp = GeneratorTempUtils.getTmpSaveDirTemp(tmpSaveDir, "entityVo.vm");
            FileWriter entityVoWriter = new FileWriter(GeneratorTempUtils.getExportMybatisPlusMulDir("vo") + tbName + "Vo.java");
            entityVoTemp.merge(context, entityVoWriter);
            entityVoWriter.close();

            //entity-mul
            Template entityMulTemp = GeneratorTempUtils.getTmpSaveDirTemp(tmpSaveDir, "entityMul.vm");
            FileWriter entityMulWriter = new FileWriter(GeneratorTempUtils.getExportMybatisPlusMulDir("entity") + tbName + ".java");
            entityMulTemp.merge(context, entityMulWriter);
            entityMulWriter.close();
            //xml
            Template xmlTemp = GeneratorTempUtils.getTmpSaveDirTemp(tmpSaveDir, "xmlMul.vm");
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

    @NotNull
    private Context getContext(Map jsonData) {
        Context context = GeneratorTempUtils.getVelocityContext();
        context.put("totalData", jsonData);
        context.put("projectOrAuthor", jsonData.get("projectOrAuthor"));
        context.put("multiTableConfig", jsonData.get("multiTableConfig"));
        context.put("queryConfig", jsonData.get("queryConfig"));
        context.put("tableConfig", jsonData.get("tableConfig"));
        context.put("formConfig", jsonData.get("formConfig"));
        context.put("dbTableConfig", jsonData.get("dbTableConfig"));
        return context;
    }

    /**
     * Mybatis-plus单表生成模版
     */
    public void generatorMybatisPlusTemp(Map jsonData, String tmpSaveDir) {
        try {
            Context context = getContext(jsonData);
            Map<String, Object> dbTableConfig = JSON.parseObject(JSON.toJSONString(jsonData.get("dbTableConfig")), Map.class);
            String tbName = dbTableConfig.get("tableNameCase").toString();
            //entity
            Template entityTemp = GeneratorTempUtils.getTmpSaveDirTemp(tmpSaveDir, "entity.vm");
            FileWriter entityWriter = new FileWriter(GeneratorTempUtils.getExportMybatisPlusDir("entity") + tbName + ".java");
            entityTemp.merge(context, entityWriter);
            entityWriter.close();
            //controller
            Template controllerTemp = GeneratorTempUtils.getTmpSaveDirTemp(tmpSaveDir, "controller.vm");
            FileWriter controllerWriter = new FileWriter(GeneratorTempUtils.getExportMybatisPlusDir("controller") + tbName + "Controller.java");
            controllerTemp.merge(context, controllerWriter);
            controllerWriter.close();
            //service
            Template serviceTemp = GeneratorTempUtils.getTmpSaveDirTemp(tmpSaveDir, "service.vm");
            FileWriter serviceWriter = new FileWriter(GeneratorTempUtils.getExportMybatisPlusDir("service") + tbName + "Service.java");
            serviceTemp.merge(context, serviceWriter);
            serviceWriter.close();
            //mapper
            Template mapperTemp = GeneratorTempUtils.getTmpSaveDirTemp(tmpSaveDir, "mapper.vm");
            FileWriter mapperWriter = new FileWriter(GeneratorTempUtils.getExportMybatisPlusDir("mapper") + tbName + "Mapper.java");
            mapperTemp.merge(context, mapperWriter);
            mapperWriter.close();
        } catch (IOException e) {
            throw new RuntimeException("Mybatis-plus单表生成模版报错" + e);
        }
    }

    /**
     * element-plus多表生成模版
     */
    public void generatorElementPlusTemp(Map jsonData, String tmpSaveDir) {
        try {
            Context context = getContext(jsonData);
            Template template = GeneratorTempUtils.getTmpSaveDirTemp(tmpSaveDir, "CRUD.vm");
            FileWriter fileWriter = new FileWriter(GeneratorTempUtils.getExportElementPlusDir("") + "CRUD.vue");
            template.merge(context, fileWriter);
            fileWriter.close();
            //第二个模板
            Template addModal = GeneratorTempUtils.getTmpSaveDirTemp(tmpSaveDir, "CRUDForm.vm");
            FileWriter addModalWriter = new FileWriter(GeneratorTempUtils.getExportElementPlusDir("") + "CRUDForm.vue");
            addModal.merge(context, addModalWriter);
            addModalWriter.close();
        } catch (IOException e) {
            throw new RuntimeException("element-plus多表生成模版报错" + e);
        }
    }

    /**
     * 生成my模版
     *
     * @param jsonData
     * @return
     */
    public String generatorBackTempZip(Map jsonData, String tmpSaveDir) {
        Map<String, Object> commonConfig = JSON.parseObject(JSON.toJSONString(jsonData.get("commonConfig")), Map.class);
        if ("true".equals(commonConfig.get("isTableMulChoose").toString())) {
            //生成前端模板
            generatorMybatisPlusMulTemp(jsonData, tmpSaveDir);
        } else {
            /*单表*/
            //生成前端模板
            generatorMybatisPlusTemp(jsonData, tmpSaveDir);
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
    public String generatorFrontTempZip(Map jsonData, String tmpSaveDir) {
        Map<String, Object> commonConfig = JSON.parseObject(JSON.toJSONString(jsonData.get("commonConfig")), Map.class);
        //多表
        if ("true".equals(commonConfig.get("isTableMulChoose").toString())) {
            //生成前端模板
            generatorElementPlusTemp(jsonData, tmpSaveDir);
        } else {
            /*单表*/
            //生成前端模板
            generatorElementPlusTemp(jsonData, tmpSaveDir);
        }
        String exportFilePath = GeneratorTempUtils.getOutputZipPath() + ObjSelfUtils.getCurrentDateTimeTrim() + ".zip";
        //生成zip包
        GeneratorTempUtils.createZipFile(exportFilePath, GeneratorTempUtils.getNeedZipDir());
        return exportFilePath;
    }

//    /**
//     * 生成前后端模版
//     *
//     * @param jsonData
//     * @return
//     */
//    public String generatorFrontBackTempZip(Map jsonData, String tmpSaveDir) {
//        Map<String, Object> commonConfig = JSON.parseObject(JSON.toJSONString(jsonData.get("commonConfig")), Map.class);
//        //多表
//        if ("true".equals(commonConfig.get("isTableMulChoose").toString())) {
//            //生成前端模板
//            generatorElementPlusTemp(jsonData, tmpSaveDir);
//            //生成后端模板
//            generatorMybatisPlusMulTemp(jsonData, tmpSaveDir);
//        } else {
//            /*单表*/
//            //生成前端模板
//            generatorElementPlusTemp(jsonData, tmpSaveDir);
//            //生成后端模板
//            generatorMybatisPlusTemp(jsonData, tmpSaveDir);
//        }
//
//        String exportFilePath = GeneratorTempUtils.getOutputZipPath() + ObjSelfUtils.getCurrentDateTimeTrim() + ".zip";
//        //生成zip包
//        GeneratorTempUtils.createZipFile(exportFilePath, GeneratorTempUtils.getNeedZipDir());
//        return exportFilePath;
//    }

    /**
     * 生成前端自定义模版
     *
     * @param files
     * @param jsonData
     * @return
     */
    public String generatorFrontCustomTemp(List<MultipartFile> files, Map jsonData) {
        String tempSaveDir = getSaveDir(files);
        String tempZip = generatorFrontTempZip(jsonData, tempSaveDir);
        GeneratorTempUtils.deleteDir(tempSaveDir);
        return tempZip;
    }

    /**
     * 生成后端自定义模版
     *
     * @param files
     * @param jsonData
     * @return
     */
    public String generatorBackCustomTemp(List<MultipartFile> files, Map jsonData) {
        String tempSaveDir = getSaveDir(files);
        String tempZip = generatorBackTempZip(jsonData, tempSaveDir);
        GeneratorTempUtils.deleteDir(tempSaveDir);
        return tempZip;
    }


    /**
     * 接收文件并保存文件到本地
     *
     * @param files
     * @return 保存后的文件路径
     */
    private String getSaveDir(List<MultipartFile> files) {
        String tmpSaveDir = GeneratorTempUtils.getTmpSaveDir();
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
        return tmpSaveDir;
    }


    /**
     * 只提供数据源能力，导入的模版是什么则返回什么， 返回后会对模版里的插槽字段进行填充
     *
     * @param files
     * @param jsonData
     * @return
     */
    public String generatorCustomTemp(List<MultipartFile> files, Map jsonData) {
        Context context = getContext(jsonData);
        String tempSaveDir = getSaveDir(files);
        files.forEach((file) -> {
            String originalFilename = file.getOriginalFilename();
            Template xmlTemp = GeneratorTempUtils.getTmpSaveDirTemp(tempSaveDir, originalFilename);
            try {
                FileWriter xmlWriter = new FileWriter(GeneratorTempUtils.getExportFileDir() + originalFilename);
                xmlTemp.merge(context, xmlWriter);
                xmlWriter.close();
            } catch (IOException e) {
                throw new RuntimeException("生成临时模版报错" + e);
            }
        });
        String exportFilePath = GeneratorTempUtils.getOutputZipPath() + ObjSelfUtils.getCurrentDateTimeTrim() + ".zip";
        GeneratorTempUtils.createZipFile(exportFilePath, GeneratorTempUtils.getNeedZipDir());
        GeneratorTempUtils.deleteDir(tempSaveDir);
        return exportFilePath;
    }

}
