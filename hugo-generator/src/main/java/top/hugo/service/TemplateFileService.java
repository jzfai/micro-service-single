package top.hugo.service;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.apache.velocity.Template;
import org.apache.velocity.context.Context;
import org.apache.velocity.shaded.commons.io.FilenameUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import top.hugo.entity.TemplateFile;
import top.hugo.mapper.TemplateFileMapper;
import top.hugo.utils.FileSelfUtils;
import top.hugo.utils.GeneratorTempUtils;
import top.hugo.utils.ObjSelfUtils;

import javax.annotation.Resource;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;

/**
 * Service
 *
 * @author 邝华
 * @since 2022-12-07 13:51:06
 */
@Service
@Slf4j
public class TemplateFileService {

    @Resource
    private TemplateFileMapper templateFileMapper;

    public Page<TemplateFile> selectPage(Integer pageNum, Integer pageSize, QueryWrapper<TemplateFile> queryWrapper) {
        return this.templateFileMapper.selectPage(new Page<TemplateFile>(pageNum, pageSize), queryWrapper);
    }

    public TemplateFile selectById(Integer id) {
        return this.templateFileMapper.selectById(id);
    }

    public List<TemplateFile> selectBatchIds(List<Integer> idList) {
        return this.templateFileMapper.selectBatchIds(idList);
    }

    public int updateById(TemplateFile templateFile) {
        return this.templateFileMapper.updateById(templateFile);
    }

    public int deleteById(Integer id) {
        return this.templateFileMapper.deleteById(id);
    }

    public int deleteBatchIds(List<Integer> idList) {
        return this.templateFileMapper.deleteBatchIds(idList);
    }


    /**
     * 批量导入写入上传的模版文件
     *
     * @date 2022/12/10 16:17
     */
    public void saveMultiTemplateFile(List<MultipartFile> files, String name) {
        ArrayList<String> stringArr = new ArrayList<>();
        TemplateFile templateFile = new TemplateFile();
        templateFile.setName(name);
        this.templateFileMapper.insert(templateFile);
        for (MultipartFile file : files) {
            FileSelfUtils.savaFileByMulti(file, templateFile.getId().toString(), file.getOriginalFilename());
            stringArr.add(file.getOriginalFilename());
        }
        templateFile.setFileArr(JSON.toJSONString(stringArr));
        this.templateFileMapper.updateById(templateFile);
    }


    /**
     * 更新修改后的内容
     *
     * @date 2022/12/10 16:17
     */
    public String changeInputCode(Map<String, Object> contextData, Integer id, String fileName, String code) {
        Context context = setContextData(contextData);
        //更新修改的模版文件内容
        FileSelfUtils.savaFileByName(id, fileName, code);
        //利用velocity模版引擎生成转换后的代码，并返回String
        String workPath = FileSelfUtils.getTemplateSaveRootDir() + id.toString();
        Template xmlTemp = GeneratorTempUtils.getTmpSaveDirTemp(workPath, fileName);
        Writer xmlWriter = new StringWriter();
        xmlTemp.merge(context, xmlWriter);
        return xmlWriter.toString();
    }

    public Context setContextData(Map<String, Object> contextData) {
        //设置上下文数据
        Context context = GeneratorTempUtils.getVelocityContext();
        context.put("totalData", contextData);
        contextData.entrySet().stream().forEach(mapEntry -> {
            context.put(mapEntry.getKey(), mapEntry.getValue());
        });
        return context;
    }

    /**
     * 除vm以外的文件生成文件导出
     * 熊猫哥
     *
     * @date 2022/12/10 16:17
     */
    public String generatorTemplateFileByConfig(Map<String, Object> contextData, Integer id, String fileNamePre) {
        Context context = setContextData(contextData);
        Map<String, Object> basicConfig = ObjSelfUtils.changeToMap(contextData.get("basicConfig"));
        //通过id获取文件列表
        TemplateFile templateFile = templateFileMapper.selectById(id);
        List<String> arrString = JSON.parseArray(templateFile.getFileArr(), String.class);
        //筛选非.vm结尾文件生成导出
        String workPath = FileSelfUtils.getTemplateSaveRootDir() + templateFile.getId();
        arrString.forEach(feItem -> {
            String extension = FilenameUtils.getExtension(feItem);
            if (!extension.equals("vm")) {
                Template entityTemp = GeneratorTempUtils.getTmpSaveDirTemp(workPath, feItem);
                //GeneratorTempUtils.getExportMybatisPlusDir(packagePath, "entity") + tbName + ".java"
                String packagePath = "";
                if (ObjSelfUtils.isNotEmpty(basicConfig) && ObjSelfUtils.isNotEmpty(basicConfig.get("packageName"))) {
                    packagePath = basicConfig.get("packageName").toString().replaceAll("\\.", Matcher.quoteReplacement(File.separator));
                }
                String fileName = dillFileName(fileNamePre, feItem, extension);
                try {
                    FileWriter entityWriter = new FileWriter(GeneratorTempUtils.getExportMybatisPlusDir(packagePath, mappingDir(feItem)) + fileName);
                    entityTemp.merge(context, entityWriter);
                    entityWriter.close();
                } catch (IOException e) {
                    throw new RuntimeException("生成文件报错" + e);
                }
            }
        });
        String exportFileName = GeneratorTempUtils.getOutputZipPath() + ObjSelfUtils.getCurrentDateTimeTrim() + ".zip";
        //生成zip包
        GeneratorTempUtils.createZipFile(exportFileName, GeneratorTempUtils.getNeedZipDir());
        return exportFileName;
    }


    /*根据文件配置id,提供下载*/
    public String downZipByTemplateFileId(Integer id) {
        String workPath = FileSelfUtils.getTemplateSaveRootDir() + id;
        log.info("导出的文件路径" + workPath);
        String exportFileName = GeneratorTempUtils.getOutputZipPath() + ObjSelfUtils.getCurrentDateTimeTrim() + ".zip";
        //生成zip包
        GeneratorTempUtils.createZipFile(exportFileName, workPath);
        return exportFileName;
    }

    /*匹配文件名*/
    public String dillFileName(String fileNamePre, String fileName, String extendName) {
        //返回前端名
        List<String> frontExtendList = Arrays.asList("vue", "js", "ts");
        if (frontExtendList.contains(extendName)) {
            return fileNamePre + "-" + fileName;
        }
        //返回后端名
        List<String> backExtendList = Arrays.asList("java", "xml", "yml");
        if (backExtendList.contains(extendName)) {
            return fileNamePre + setFirstWordUpCase(fileName);
        }
        return "nomapping";
    }

    /*
     * 设置首字母大写
     * */
    public String setFirstWordUpCase(String name) {
        return name.substring(0, 1).toUpperCase() + name.substring(1);
    }

    /*根据文件名匹配目录*/
    public String mappingDir(String fileName) {
        List<String> stringList = Arrays.asList("controller", "query", "entity", "mapper", "service", "utils", "vo", "xml", "vue", "js", "ts");
        String mappingString = "nomapping";
        for (String item : stringList) {
            if (fileName.contains(item)) {
                mappingString = item;
            }
        }
        return mappingString;
    }
}
