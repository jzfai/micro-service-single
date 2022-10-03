package top.kuanghua.basisfunc.service;

import com.alibaba.fastjson.JSON;
import org.apache.velocity.Template;
import org.apache.velocity.context.Context;
import org.springframework.stereotype.Service;
import top.kuanghua.basisfunc.utils.GeneratorTempUtils;
import top.kuanghua.commonpom.utils.ObjSelfUtils;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

/**
 * @author 猫哥
 * @date 2022-06-06 13:31
 **/
@Service
public class ElementPlusService {
    /**
     * 生成查询表格和请求api模板
     * @param jsonData  前端传入的json数据
     * @return 导出zip的路径
     * @author 邝华
     * @email kuanghua@aulton.com
     * @date 2022-06-10 9:59
     */
    public String generatorTableQuery(Map<String, Object> jsonData)  {
        try {
            Context context = GeneratorTempUtils.getVelocityContext();
            context.put("configData", jsonData);
            context.put("basicConfig", jsonData.get("basicConfig"));
            context.put("apiConfig", jsonData.get("apiConfig"));
            context.put("queryConfig", jsonData.get("queryConfig"));
            context.put("tableConfig", jsonData.get("tableConfig"));
            context.put("tableList", jsonData.get("tableList"));
            Map<String, Object> basicConfig = ObjSelfUtils.changeToMap(jsonData.get("basicConfig"));
            String pageName=basicConfig.get("apiFileNameFirstCase").toString();
            //table-query
            Template template = GeneratorTempUtils.getElementPlusTemp("tableQuery.vm");
            FileWriter fileWriter = new FileWriter(GeneratorTempUtils.getExportFileDir() +pageName+"TableQuery.vue");
            template.merge(context, fileWriter);
            fileWriter.close();
            //reqApi.vm
            Template reqApiTemplateModal = GeneratorTempUtils.getElementPlusTemp("queryApi.vm");
            FileWriter reqApiTemplateWriter = new FileWriter(GeneratorTempUtils.getExportFileDir() +pageName+".js");
            reqApiTemplateModal.merge(context, reqApiTemplateWriter);
            reqApiTemplateWriter.close();


            //tableQueryIndex.vm
            Template  tableQueryIndexModal = GeneratorTempUtils.getElementPlusTemp("tableQueryIndex.vm");
            FileWriter tableQueryIndexWriter = new FileWriter(GeneratorTempUtils.getExportFileDir() +"index.ts");
            tableQueryIndexModal.merge(context, tableQueryIndexWriter);
            tableQueryIndexWriter.close();

            //routerItem.vm
            Template routerItemModal = GeneratorTempUtils.getElementPlusTemp("routerItem.vm");
            FileWriter routerItemWriter = new FileWriter(GeneratorTempUtils.getExportFileDir() +"routerItem.ts");
            routerItemModal.merge(context, routerItemWriter);
            routerItemWriter.close();

            //导出文件路径
            String exportFilePath=GeneratorTempUtils.getOutputZipPath() + pageName+ ObjSelfUtils.getCurrentDateTimeTrim()+".zip";

            //生成zip包
            GeneratorTempUtils.createZipFile(exportFilePath, GeneratorTempUtils.getNeedZipDir());
            return  exportFilePath;
        } catch (IOException e) {
            throw new RuntimeException("前端导出模板报错"+e);
        }
    }



    //生成新增和编辑模板
    public String generatorAddEdit(Map jsonData)  {
        try {
            Context context = GeneratorTempUtils.getVelocityContext();
            context.put("configData", jsonData);
            context.put("basicConfig", jsonData.get("basicConfig"));
            context.put("apiConfig", jsonData.get("apiConfig"));
            context.put("tableList", jsonData.get("tableList"));
            Map<String, Object> basicConfig = ObjSelfUtils.changeToMap(jsonData.get("basicConfig"));
            String pageName=basicConfig.get("apiFileNameFirstCase").toString();
            //add-edit
            Template template = GeneratorTempUtils.getElementPlusTemp("addEdit.vm");
            FileWriter fileWriter = new FileWriter(GeneratorTempUtils.getExportFileDir() +pageName+"AddEdit.vue");
            template.merge(context, fileWriter);
            fileWriter.close();
            //reqApi.vm
            Template reqApiTemplateModal = GeneratorTempUtils.getElementPlusTemp("AddEditApi.vm");
            FileWriter reqApiTemplateWriter = new FileWriter(GeneratorTempUtils.getExportFileDir() +pageName+"AddEdit.js");
            reqApiTemplateModal.merge(context, reqApiTemplateWriter);
            reqApiTemplateWriter.close();

            //add-edit-table
            ArrayList<Map<String, Object>> mapArrayList = ObjSelfUtils.changeToArrayMap(jsonData.get("tableList"));
            mapArrayList.forEach((listItem) -> {
                if ("table".equals(listItem.get("componentType"))) {
                    context.put("tableListConfig",listItem.get("tableListConfig"));
                    try {
                        Template addTableTemplate= GeneratorTempUtils.getElementPlusTemp("add-edit-table.vm");
                        FileWriter fileWriterAddTable = new FileWriter(GeneratorTempUtils.getExportFileDir() +listItem.get("columnName")+"-add-edit-table.vue");
                        addTableTemplate.merge(context, fileWriterAddTable);
                        fileWriterAddTable.close();

                        //reqApi.vm
                        Template reqApiModal = GeneratorTempUtils.getElementPlusTemp("add-edit-table-api.vm");
                        FileWriter reqApiWriter = new FileWriter(GeneratorTempUtils.getExportFileDir() +pageName+"-add-edit-table.js");
                        reqApiModal.merge(context, reqApiWriter);
                        reqApiWriter.close();
                    } catch (IOException e) {
                        throw  new RuntimeException("生成模板报错"+e);
                    }
                }
            });
            String exportFilePath=GeneratorTempUtils.getOutputZipPath() + pageName+ ObjSelfUtils.getCurrentDateTimeTrim()+".zip";
            //生成zip包
            GeneratorTempUtils.createZipFile(exportFilePath, GeneratorTempUtils.getNeedZipDir());
            return  exportFilePath;
        } catch (IOException e) {
            throw new RuntimeException("前端导出模板报错"+e);
        }
    }

    /**
     * 生成详情数据
     * @param jsonData  前端传入的json数据
     * @return 导出zip的路径
     * @author 邝华
     * @email kuanghua@aulton.com
     * @date 2022-06-10 9:59
     */
    public String generatorDetail(Map jsonData)  {
        try {
            Context context = GeneratorTempUtils.getVelocityContext();
            context.put("configData", jsonData);
            context.put("basicConfig", jsonData.get("basicConfig"));
            context.put("apiConfig", jsonData.get("apiConfig"));
            context.put("tableConfigArr", jsonData.get("tableConfigArr"));
            Map<String, Object> basicConfig = ObjSelfUtils.changeToMap(jsonData.get("basicConfig"));
            String pageName=basicConfig.get("apiFileNameFirstCase").toString();
            //detail
            Template template = GeneratorTempUtils.getElementPlusTemp("detail.vm");
            FileWriter fileWriter = new FileWriter(GeneratorTempUtils.getExportFileDir() +pageName+"Detail.vue");
            template.merge(context, fileWriter);
            fileWriter.close();

            //reqApi.vm
            Template reqApiTemplateModal = GeneratorTempUtils.getElementPlusTemp("detailApi.vm");
            FileWriter reqApiTemplateWriter = new FileWriter(GeneratorTempUtils.getExportFileDir() +pageName+"Detail.js");
            reqApiTemplateModal.merge(context, reqApiTemplateWriter);
            reqApiTemplateWriter.close();

            String exportFilePath=GeneratorTempUtils.getOutputZipPath() + pageName+ ObjSelfUtils.getCurrentDateTimeTrim()+".zip";
            //生成zip包
            GeneratorTempUtils.createZipFile(exportFilePath, GeneratorTempUtils.getNeedZipDir());
            return  exportFilePath;
        } catch (IOException e) {
            throw new RuntimeException("前端导出模板报错"+e);
        }
    }
   
}
