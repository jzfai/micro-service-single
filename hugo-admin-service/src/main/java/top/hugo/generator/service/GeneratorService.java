package top.hugo.generator.service;

import cn.hutool.core.util.ObjectUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.velocity.Template;
import org.apache.velocity.context.Context;
import org.apache.velocity.shaded.commons.io.FilenameUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import top.hugo.common.utils.BeanCopyUtils;
import top.hugo.common.utils.JacksonUtils;
import top.hugo.generator.dto.TemplateChangeDto;
import top.hugo.generator.entity.ConfigSave;
import top.hugo.generator.entity.TemplateFile;
import top.hugo.generator.mapper.ConfigSaveMapper;
import top.hugo.generator.mapper.TemplateFileMapper;
import top.hugo.generator.utils.FileSelfUtils;
import top.hugo.generator.utils.GeneratorTempUtils;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;


@Service
@RequiredArgsConstructor
@Slf4j
public class GeneratorService {
    private final TemplateFileMapper templateFileMapper;

    private final ConfigSaveMapper configSaveMapper;


    /*
     * 修改模板文件
     * */
    public void changeTemplateFile(TemplateChangeDto templateChangeBo) {
        //更新模板文件信息
        this.templateFileMapper.updateById(BeanCopyUtils.copy(templateChangeBo, TemplateFile.class));
        //更新模板内容
        FileSelfUtils.savaFileByName(templateChangeBo.getId(), templateChangeBo.getFileName(), templateChangeBo.getCode());
    }

    /**
     * 保存模版文件
     *
     * @date 2022/12/10 16:17
     */
    @Transactional(rollbackFor = Exception.class)
    public void saveTemplateFile(List<MultipartFile> files, String name) {
        ArrayList<String> stringArr = new ArrayList<>();
        //拼接文件名
        for (MultipartFile file : files) {
            stringArr.add(file.getOriginalFilename());
        }
        //新增模板配置
        TemplateFile templateFile = new TemplateFile();
        templateFile.setName(name);
        templateFile.setFileArr(JacksonUtils.toJsonString(stringArr));
        this.templateFileMapper.insert(templateFile);
        //写入到文件系统
        for (MultipartFile file : files) {
            FileSelfUtils.savaFileByMulti(file, templateFile.getId().toString(), file.getOriginalFilename());
        }
    }

    /*根据文件配置id,提供下载*/
    public String downZipByTemplateFileId(Integer id) {
        String workPath = GeneratorTempUtils.getDirByEnv() + File.separator + id;
        log.info("导出的文件路径" + workPath);
        String exportFileName = GeneratorTempUtils.getZipDirByEnv() + ".zip";
        //生成zip包
        GeneratorTempUtils.createZipFile(exportFileName, workPath);
        return exportFileName;
    }


    /**
     * 更新文件并返回文件内容
     *
     * @date 2022/12/10 16:17
     */
    public String updateFileReturnCode(Map<String, Object> contextData, Integer id, String fileName, String code) {
        Context context = setContextData(contextData);
        //更新修改的模版文件内容
        FileSelfUtils.savaFileByName(id, fileName, code);
        //利用velocity模版引擎生成转换后的代码，并返回String
        String workPath = GeneratorTempUtils.getDirByEnv() + File.separator + id.toString();
        Template xmlTemp = GeneratorTempUtils.getTemplate(workPath, fileName);
        Writer xmlWriter = new StringWriter();
        xmlTemp.merge(context, xmlWriter);
        return xmlWriter.toString();
    }


    /**
     * 除vm以外的文件生成文件导出(根据配置Id)
     * 熊猫哥
     */
    public String generatorTemplateFileByConfigId(Integer configId, Integer templateId, String fileNamePre) {
        ConfigSave configSave = configSaveMapper.selectById(configId);
        return generatorFileByConfig(JacksonUtils.parseObject(configSave.getGeneratorConfig(), Map.class), templateId, fileNamePre);
    }

    /**
     * 生成除vm结尾的文件
     *
     * @param contextData 配置数据
     * @param id          模板id
     * @param fileNamePre 模板前缀
     * @return 文件路径
     */
    public String generatorFileByConfig(Map<String, Object> contextData, Integer id, String fileNamePre) {
        Context context = setContextData(contextData);
        //获取基本信息
        Map<String, Object> basicConfig = JacksonUtils.parseObject(contextData.get("basicConfig"), Map.class);
        //获取该模板文件
        TemplateFile templateFile = templateFileMapper.selectById(id);
        List<String> fileArr = JacksonUtils.parseArray(templateFile.getFileArr(), String.class);
        //文件基础保存目录
        String baseTemplatePath = FileSelfUtils.getTemplateSaveRootDir() + File.separator + templateFile.getId();

        String exportZipDir = GeneratorTempUtils.getZipDirByEnv();
        //批量生成文件写入文件系统
        fileArr.forEach(fe -> {
            String extension = FilenameUtils.getExtension(fe);
            if (!extension.equals("vm")) {
                //velocity模板引擎
                Template templateEngine = GeneratorTempUtils.getTemplate(baseTemplatePath, fe);
                //生成的包路径
                String packagePath = "";
                if (ObjectUtil.isNotEmpty(basicConfig)) {
                    packagePath = StringUtils.defaultString(basicConfig.get("packageName").toString(), "").replaceAll("\\.", Matcher.quoteReplacement(File.separator));
                }
                //文件名
                String fileName = dillFileName(fileNamePre, fe, extension);
                //写入文件系统
                try {
                    String finalExportPath = GeneratorTempUtils.fileMkdir(exportZipDir + File.separator + packagePath + File.separator + mappingDir(fe) + File.separator) + fileName;
                    FileWriter entityWriter = new FileWriter(finalExportPath);
                    templateEngine.merge(context, entityWriter);
                    entityWriter.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        //生成zip包
        GeneratorTempUtils.createZipFile(exportZipDir + ".zip", exportZipDir);
        return exportZipDir + ".zip";
    }


    /**
     * 处理前后端文件名
     *
     * @param fileNamePre
     * @param fileName
     * @param extendName
     * @return
     */
    public String dillFileName(String fileNamePre, String fileName, String extendName) {
        //处理前端文件名
        List<String> frontExtendList = Arrays.asList("vue", "js", "ts");
        if (frontExtendList.contains(extendName)) {
            return fileName;
        }
        //处理后端文件名
        List<String> backExtendList = Arrays.asList("java", "xml", "yml");
        //如果是entity直接返回 “”
        if ("entity.java".equals(fileName)) return fileNamePre + ".java";
        if ("Iservice.java".equals(fileName)) {
            return "I" + fileNamePre + "Service.java";
        }
        //匹配文件前缀
        if (backExtendList.contains(extendName)) {
            if (ObjectUtil.isNotEmpty(fileNamePre)) {
                return fileNamePre + setFirstWordUpCase(fileName);
            } else {
                return setFirstWordUpCase(fileName);
            }
        }
        return "";
    }

    /*
     * 设置首字母大写
     * */
    private String setFirstWordUpCase(String name) {
        return name.substring(0, 1).toUpperCase() + name.substring(1);
    }


    /**
     * 生成文件目录
     *
     * @param fileName
     * @return 匹配后的文件目录
     */
    public String mappingDir(String fileName) {
        List<String> stringList = Arrays.asList("controller", "query", "entity", "mapper", "service", "sql", "utils", "vo", "dto", "bo", "xml", "vue", "js", "ts");
        String mappingString = "nomapping";
        for (String item : stringList) {
            if (fileName.contains(item)) {
                mappingString = item;
            }
        }
        return mappingString;
    }

    /*
     * 设置velocity模板上下文数据
     * */
    public Context setContextData(Map<String, Object> contextData) {
        //设置上下文数据
        Context context = GeneratorTempUtils.getVelocityContext();
        contextData.forEach(context::put);
        return context;
    }
}
