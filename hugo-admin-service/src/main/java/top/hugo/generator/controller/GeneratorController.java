package top.hugo.generator.controller;

import top.hugo.common.annotation.Log;
import top.hugo.common.domain.R;
import top.hugo.common.enums.BusinessType;
import top.hugo.common.utils.JacksonUtils;
import top.hugo.generator.dto.TemplateChangeDto;
import top.hugo.generator.entity.TemplateFile;
import top.hugo.generator.service.AultonGeneratorService;
import top.hugo.generator.service.GeneratorService;
import top.hugo.generator.service.TemplateFileService;
import top.hugo.generator.utils.DateUtils;
import top.hugo.generator.utils.FileSelfUtils;
import top.hugo.generator.utils.GeneratorTempUtils;
import top.hugo.generator.vo.TemplateFileVo;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.net.URLEncoder;
import java.util.Map;

/**
 * generator核心代码
 *
 * @author kuanghua
 * @since 2023-10-18 17:23:16
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/generator/templateFile")
public class GeneratorController {
    private final GeneratorService generatorService;
    private final AultonGeneratorService aultonGeneratorService;

    private final TemplateFileService templateFileService;

    /**
     * 生成模版文件通过配置
     *
     * @param jsonData    数据（json）
     */
    //    @PostMapping("createProjectByConfig")
    //    public void createProjectByConfig(@RequestParam("jsonData") String jsonData) {
    //        //生成模板
    //        aultonGeneratorService.createProjectByConfig(JacksonUtils.parseObject(jsonData, Map.class));
    //    }


    /**
     * 根据文件名读取文件内容
     *
     * @param fileId       模板id
     * @param fileName 文件名
     */
    @GetMapping("readFileToStringByFileName")
    public R<Object> readFileToStringByFileName(String fileId,String modelId, String fileName) {
        String fileAbsPath = GeneratorTempUtils.getDirByEnv() + File.separator +modelId+File.separator+ fileId + File.separator + fileName;
        String fileToString = FileSelfUtils.readFileToString(fileAbsPath);
        return R.ok(fileToString);
    }



    /**
     * 复制文件
     *
     * @param id 文件id
     */
    @PostMapping("copyFileByFileId")
    public R<Object> copyFileByFileId(@RequestParam("id") Integer id) {
        aultonGeneratorService.copyFileByFileId(id);
        return R.ok();
    }

    /**
     * 复制项目
     *
     * @param modelId 模板id
     */
    @PostMapping("copyProject")
    public R<Object> copyProject(@RequestParam("modelId") Integer modelId) {
        aultonGeneratorService.copyProject(modelId);
        return R.ok();
    }
    /**
     * 保存模版文件
     *
     * @param modelId 模板id
     * @param file 文件数组
     */
//    @PostMapping("saveTemplateFile")
//    public R<Object> saveMultiTemplateFile(MultipartFile file,@RequestParam("modelId") Integer modelId, @RequestParam("templateFileDetail") String TemplateFileDetailString) {
//        TemplateFileDetail  templateFileDetail= JacksonUtils.parseObject(TemplateFileDetailString, TemplateFileDetail.class);
//        aultonGeneratorService.saveTemplateFile(file, modelId,templateFileDetail);
//        return R.ok();
//    }
//

    /**
     * 保存模版文件
     *
     * @param templateFile 模板id
     */
    @Log(title = "保存模版文件", businessType = BusinessType.EXPORT)
    @PostMapping("saveTemplateFile")
    public R<Object> saveMultiTemplateFile(@RequestBody TemplateFile  templateFile) {
        aultonGeneratorService.saveTemplateFile(templateFile);
        return R.ok();
    }
    /**
     * 修改模板文件
     *
     * @param jsonData 数据（json）
     * @param id       模板组Id
     * @param code     文件内容
     */
    @PostMapping("changeInputCode")
    public R<Object> changeInputCode(@RequestParam("jsonData") String jsonData, @RequestParam("modelId") Integer modelId, @RequestParam("id") Integer id,
                                     @RequestParam("fileName") String fileName, @RequestParam("code") String code) {
        String codeString = aultonGeneratorService.updateFileReturnCode(JacksonUtils.parseObject(jsonData, Map.class),modelId, id, fileName, code);
        return R.ok(codeString);
    }

    /**
     * 生成模版文件通过配置
     *
     * @param jsonData    数据（json）
     * @param id          模板组Id
     * @param fileNamePre 文件名前缀
     */
    @PostMapping("generatorTemplateFileByConfig")
    public void generatorTemplateFileByConfig(HttpServletResponse response, @RequestParam("jsonData") String jsonData, @RequestParam("id") Integer id, @RequestParam("fileNamePre") String fileNamePre) {
        //生成模板
        String exportFilePath = generatorService.generatorFileByConfig(JacksonUtils.parseObject(jsonData, Map.class), id, fileNamePre);
        //你压缩包路径
        FileSelfUtils.exportFile(response, exportFilePath, getExportFileName("代码文件",id) + ".zip");
    }

    /**
     * 生成项目
     * @param response
     * @param jsonData
     * @param modelId
     */
    @Log(title = "生成项目", businessType = BusinessType.OTHER)
    @PostMapping("createProjectByConfig")
    public void createProjectByConfig(HttpServletResponse response, @RequestParam("jsonData") String jsonData,@RequestParam("modelId") Integer modelId) {
        //生成模板
        String exportFilePath = aultonGeneratorService.createProjectByConfig(JacksonUtils.parseObject(jsonData, Map.class),modelId);
        //你压缩包路径
        FileSelfUtils.exportFile(response, exportFilePath,  getExportFileName("代码文件",modelId) + ".zip");
    }

    /**
     * 生成模版文件通过配置(ConfigId)
     *
     * @param configId    配置Id
     * @param templateId  模板组Id
     * @param fileNamePre 文件名前缀
     */
    //    @PostMapping("generatorTemplateFileByConfigId")
    //    public void generatorTemplateFileByConfigId(HttpServletResponse response, @RequestParam("configId") Integer configId, @RequestParam("templateId") Integer templateId, @RequestParam("fileNamePre") String fileNamePre) {
    //        //生成模板
    //        String exportFilePath = generatorService.generatorTemplateFileByConfigId(configId, templateId, fileNamePre);
    //        //你压缩包路径
    //        FileSelfUtils.exportFile(response, exportFilePath, fileNamePre + DateUtils.dateTimeNow() + ".zip");
    //    }

    /**
     * 下载模板文件
     *
     * @param id 模板组Id
     */
    @PostMapping("downZipByTemplateFileId")
    public void downZipByTemplateFileId(HttpServletResponse response, HttpServletRequest request, @RequestParam("id") Integer id) {
        String exportFilePath = generatorService.downZipByTemplateFileId(id);
        //导出文件并删除响应文件和目录
        FileSelfUtils.exportFile(response, exportFilePath, "-"+getExportFileName("模板文件",id) + ".zip");
    }


    //拼接导出名字
    public String getExportFileName(String name,Integer id){
        //查询项目名称
        TemplateFileVo templateFileVo = templateFileService.selectTemplateFileById(id.longValue());

        return URLEncoder.encode(name+templateFileVo.getName()+"-"+DateUtils.dateTimeNow());
    }
    /**
     * 修改模板文件
     *
     * @param templateChangeBo
     */
    @PostMapping("changeTemplateFile")
    public R<Object> changeTemplateFile(@RequestBody TemplateChangeDto templateChangeBo) {
        aultonGeneratorService.changeTemplateFile(templateChangeBo);
        return R.ok();
    }
}
