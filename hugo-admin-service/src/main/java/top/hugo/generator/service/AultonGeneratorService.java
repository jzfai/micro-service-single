package top.hugo.generator.service;

import cn.hutool.core.util.ObjectUtil;
import top.hugo.common.utils.JacksonUtils;
import top.hugo.generator.dto.TemplateChangeDto;
import top.hugo.generator.entity.ConfigSave;
import top.hugo.generator.entity.TemplateFile;
import top.hugo.generator.entity.TemplateFileDetail;
import top.hugo.generator.mapper.ConfigSaveMapper;
import top.hugo.generator.mapper.TemplateFileDetailMapper;
import top.hugo.generator.mapper.TemplateFileMapper;
import top.hugo.generator.utils.FileSelfUtils;
import top.hugo.generator.utils.GeneratorTempUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.velocity.Template;
import org.apache.velocity.context.Context;
import org.apache.velocity.shaded.commons.io.FilenameUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;


@Service
@RequiredArgsConstructor
@Slf4j
public class AultonGeneratorService {
    private final TemplateFileMapper templateFileMapper;

    private final ConfigSaveMapper configSaveMapper;
    private final TemplateFileDetailMapper templateFileDetailMapper;

    /*
     * 复制项目
     * */
    @Transactional(rollbackFor = Exception.class)
    public void copyProject(Integer modelId) {
        //新建项目
        TemplateFile templateFile = templateFileMapper.selectById(modelId);
        //新建项目下的文件
        templateFile.setId(null);
        templateFileMapper.insert(templateFile);
        //复制项目下的文件列表
        LambdaQueryWrapper<TemplateFileDetail> detailLambdaQueryWrapper = new LambdaQueryWrapper<>();
        detailLambdaQueryWrapper.eq(TemplateFileDetail::getTemplateId, modelId);
        List<TemplateFileDetail> templateFileDetails = templateFileDetailMapper.selectList(detailLambdaQueryWrapper);

        //插入新增的文件

        HashMap<String, String> sourceDestHashMap = new HashMap<>();

        templateFileDetails.forEach(f -> {
            Integer sourceId = f.getId();
            f.setId(null);
            f.setTemplateId(templateFile.getId());
            templateFileDetailMapper.insert(f);
            sourceDestHashMap.put(sourceId.toString(), f.getId().toString());
        });
        //批量插入
        //templateFileDetailMapper.insertBatch(templateFileDetails,50);
        String sourcePath = GeneratorTempUtils.getDirByEnv() + File.separator + modelId;
        String workPath = GeneratorTempUtils.getDirByEnv() + File.separator + templateFile.getId();

        //复制文件到新项目下
        sourceDestHashMap.forEach((key, value) -> {
            String sourceDir = sourcePath + File.separator + key;
            String destDir = workPath + File.separator + value;
            FileSelfUtils.copyDirectory(new File(sourceDir), new File(destDir));
        });


        //FileSelfUtils.copyDirectory(new File(sourceDir), new File(workPath));

    }


    /*
     * 复制文件
     * */
    @Transactional(rollbackFor = Exception.class)
    public void copyFileByFileId(Integer id) {
        //新建文件
        TemplateFileDetail templateFileDetail = templateFileDetailMapper.selectById(id);

        templateFileDetail.setId(null);

        templateFileDetailMapper.insert(templateFileDetail);

        //新增文件
        String sourcePath = GeneratorTempUtils.getDirByEnv() + File.separator + templateFileDetail.getTemplateId() +
                File.separator + id + File.separator + templateFileDetail.getName();
        String workDir = GeneratorTempUtils.getDirByEnv() + File.separator + templateFileDetail.getTemplateId() +
                File.separator + templateFileDetail.getId();
        FileSelfUtils.copyFileToDir(sourcePath, workDir);
    }

    /*
     * 此方法用于生成项目（基础层和应用层）
     * @param fileNamePre
     * @param fileName
     * @param extendName
     * @return
     * */
    //Map<String, Object> contextData
    public String createProjectByConfig(Map<String, Object> contextData, Integer modelId) {

        TemplateFile templateFile = templateFileMapper.selectById(modelId);

        Map<String, Object> basicConfig = JacksonUtils.parseObject(contextData.get("basicConfig"), Map.class);

        //应用层
        String basicNameCase ;
        if(templateFile.getProjectType()==3){
            basicNameCase= basicConfig.get("dillBasicNameCase").toString();
        }else{
            basicNameCase= basicConfig.get("basicNameCase").toString();
        }

        boolean isGeneratorProject = Boolean.parseBoolean(basicConfig.get("isGeneratorProject").toString());


        //String tableName="BatteryConfig";
        //contextData.put("packageNameDash","aulton-ms-gov-config");
        //Integer modelId=55;

        /*读取模板*/
        //模板id modelId
        /*读取模板文件详情列表*/
        LambdaQueryWrapper<TemplateFileDetail> lqw = new LambdaQueryWrapper<>();
        lqw.like(TemplateFileDetail::getTemplateId, modelId);
        lqw.like(TemplateFileDetail::getStatus, 1);

        List<TemplateFileDetail> templateFileDetailList = templateFileDetailMapper.selectList(lqw);
        /*生成项目名*/
        //String packageName = templateFile.getBasicPackage();
        String originPackage = templateFile.getOriginPackage();
        String projectDir = originPackage.replace("com.", "").replace(".", "-");


        //项目路径
        String basePath = GeneratorTempUtils.getDirByEnv() + File.separator + projectDir;

        /*生成模块名 注：项目路径名和包名一致*/
        List<String> modelList = JacksonUtils.parseArray(templateFile.getModelList(), String.class);
        for (String str : modelList) {
            String dirPath = basePath + File.separator + (projectDir + "-" + str);
            /*创建main和test*/
            //创建model main
            FileSelfUtils.createDir(dirPath + File.separator + FileSelfUtils.changeDiagToDir("src-main-java"));
            //创建model test
            FileSelfUtils.createDir(dirPath + File.separator + FileSelfUtils.changeDiagToDir("src-test-java"));
        }

        /*生成项目，模块，包，的文件*/
        templateFileDetailList.forEach(f -> {
            Integer model = f.getModelType();
            String basePathTemplate = basePath + File.separator;
            //项目生成文件
            if (isGeneratorProject) {
                if (1 == model) {
                    //项目文件
                    generatorFileByTemplate(contextData, modelId, f.getId(), f.getName(), basePathTemplate + f.getName());
                }
                if (2 == model) {
                    String packagePath = basePathTemplate + (projectDir + "-" + f.getModelName());
                    //模块文件
                    /*插入pom.xml文件*/
                    //项目文件
                    generatorFileByTemplate(contextData, modelId, f.getId(), f.getName(), packagePath + File.separator + f.getName());
                }
                //配置文件resource
                if (4 == model) {
                    String sourceString = "src-main-resources";
                    if (1 == f.getResourceType()) {
                        sourceString = "src-main-resources";
                    } else if (2 == f.getResourceType()) {
                        sourceString = "src-test-resources";
                    }
                    //拼接模块包名路径
                    String packagePath = basePathTemplate + (projectDir + "-" + f.getModelName()) + File.separator + FileSelfUtils.changeDiagToDir(sourceString) +
                            File.separator + f.getPackageSuffix();
                    FileSelfUtils.createDir(packagePath);
                    //包名文件
                    String generatorFileName = f.getName();
                    if (f.getInjectBasicName() == 1) {
                        generatorFileName = basicNameCase + generatorFileName;
                    }
                    generatorFileByTemplate(contextData, modelId, f.getId(), f.getName(), packagePath + File.separator + generatorFileName);
                }
            }
            if (3 == model) {
                String sourceString = "src-main-java";
                if (1 == f.getResourceType()) {
                    sourceString = "src-main-java";
                } else if (2 == f.getResourceType()) {
                    sourceString = "src-test-java";
                }
                //拼接模块包名路径
                String packagePath = basePathTemplate + (projectDir + "-" + f.getModelName()) + File.separator + FileSelfUtils.changeDiagToDir(sourceString) +
                        File.separator + FileSelfUtils.changeDotToDir(originPackage) + File.separator + f.getPackageSuffix();
                FileSelfUtils.createDir(packagePath);

                //拼接文件名
                String generatorFileName = f.getName();
                if (f.getInjectBasicName() == 1) {
                    generatorFileName = basicNameCase + generatorFileName;
                }
                if (ObjectUtil.isNotEmpty(f.getFileNamePre())) {
                    generatorFileName = f.getFileNamePre() + generatorFileName;
                }
                generatorFileByTemplate(contextData, modelId, f.getId(), f.getName(), packagePath + File.separator + generatorFileName);
            }
        });

        //应用层
        // String packageName2="aulton-ms-gov-config".replace("-",File.separator);
        // //基础成生产的目录
        // ArrayList<String> list2 = new ArrayList<>();
        // // 使用add方法添加元素
        // list2.add("interface");
        // list2.add("model");
        // list2.add("repository");
        // list2.add("service");
        //
        // // 打印ArrayList内容，以验证赋值是否成功
        // for (String str : list2) {
        //     String dirPath= GeneratorTempUtils.getDirByEnv()+File.separator+packageNameDash+File.separator+str;
        //     FileSelfUtils.createDir(dirPath);
        // }
        //生成zip包
        String exportZipDir = GeneratorTempUtils.getZipDirByEnv();
        String finalFilePath = exportZipDir + ".zip";

        GeneratorTempUtils.createZipFile(finalFilePath, basePath);
        //删除导出的文件夹（清空目录）
        FileSelfUtils.deleteDir(basePath);
        return finalFilePath;
    }

    /**
     * 生成除vm结尾的文件
     *
     * @param contextData 配置数据
     * @param id          模板id
     * @param fileName    模板文件名
     * @param writePath   写入文件路径
     */
    public void generatorFileByTemplate(Map<String, Object> contextData, Integer id, Integer fileId, String fileName, String writePath) {
        Context context = setContextData(contextData);
        //获取基本信息
        //Map<String, Object> basicConfig = JacksonUtils.parseObject(contextData.get("basicConfig"), Map.class);

        //获取该模板文件
        // TemplateFile templateFile = templateFileMapper.selectById(id);
        // List<String> fileArr = JacksonUtils.parseArray(templateFile.getFileArr(), String.class);

        // 文件基础保存目录
        String baseTemplatePath = FileSelfUtils.getTemplateSaveRootDir();
        //String exportZipDir = GeneratorTempUtils.getZipDirByEnv();

        //velocity模板引擎
        Template templateEngine = GeneratorTempUtils.getTemplate(baseTemplatePath, +id + File.separator +
                fileId + File.separator + fileName);
        //String finalExportPath = GeneratorTempUtils.fileMkdir(writePath) ;
        //如果目录不存在则创建
        //FileSelfUtils.createDir(writePath);
        try {
            FileWriter entityWriter = new FileWriter(writePath);
            templateEngine.merge(context, entityWriter);
            entityWriter.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        //批量生成文件写入文件系统
        //  fileArr.forEach(fe -> {
        //      String extension = FilenameUtils.getExtension(fe);
        //      if (!extension.equals("vm")) {
        //          //velocity模板引擎
        //          Template templateEngine = GeneratorTempUtils.getTemplate(baseTemplatePath, fe);
        //          //生成的包路径
        //          String packagePath = "";
        //          if (ObjectUtil.isNotEmpty(basicConfig)) {
        //              packagePath = StringUtils.defaultString(basicConfig.get("packageNameDash").toString(), "").replaceAll("\\.", Matcher.quoteReplacement(File.separator));
        //          }
        //          //文件名
        //          String fileName = dillFileName(fileNamePre, fe, extension);
        //          //写入文件系统
        //          try {
        //              String finalExportPath = Generato
        //              TempUtils.fileMkdir(exportZipDir + File.separator + packagePath + File.separator + mappingDir(fe) + File.separator) + fileName;
        //              FileWriter entityWriter = new FileWriter(finalExportPath);
        //              templateEngine.merge(context, entityWriter);
        //              entityWriter.close();
        //          } catch (IOException e) {
        //              throw new RuntimeException(e);
        //          }
        //      }
        //  });
    }

    /*
     * 修改模板文件
     * */
    public void changeTemplateFile(TemplateChangeDto templateChangeBo) {
        //更新模板文件信息
        TemplateFileDetail templateFile = new TemplateFileDetail();
        templateFile.setName(templateChangeBo.getFileName());
        templateFile.setId(templateChangeBo.getFileId());
        this.templateFileDetailMapper.updateById(templateFile);
        //更新模板内容
        FileSelfUtils.savaFileByNameBack(templateChangeBo.getModelId(), templateChangeBo.getFileId(), templateChangeBo.getFileName(), templateChangeBo.getCode());
    }

    /**
     * 保存模版文件
     *
     * @date 2022/12/10 16:17
     */
    @Transactional(rollbackFor = Exception.class)
    public void saveTemplateFile(TemplateFile templateFile) {
        //新增模板配置
        //templateFile.setFileArr(JacksonUtils.toJsonString(stringArr));
        this.templateFileMapper.insert(templateFile);
//        //写入到文件系统
//        FileSelfUtils.savaFileByMultiBack(file, modelId, templateFileDetail.getId(), templateFileDetail.getName());
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
    public String updateFileReturnCode(Map<String, Object> contextData, Integer modelId, Integer id, String fileName, String code) {
        Context context = setContextData(contextData);
        //更新修改的模版文件内容
        FileSelfUtils.savaFileByNameBack(modelId, id, fileName, code);
        //利用velocity模版引擎生成转换后的代码，并返回String
        String workPath = GeneratorTempUtils.getDirByEnv();
        //文件路径
        String currentFilePath = modelId + File.separator + id + File.separator + fileName;
        Template xmlTemp = GeneratorTempUtils.getTemplate(workPath, currentFilePath);
        Writer xmlWriter = new StringWriter();
        xmlTemp.merge(context, xmlWriter);
        return xmlWriter.toString();
    }


    /**
     * 除vm以外的文件生成文件导出(根据配置Id)
     * 邝华
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
                    packagePath = StringUtils.defaultString(basicConfig.get("packageNameDash").toString(), "").replaceAll("\\.", Matcher.quoteReplacement(File.separator));
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
        String finalFilePath = exportZipDir + ".zip";
        GeneratorTempUtils.createZipFile(finalFilePath, exportZipDir);

        return finalFilePath;
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
