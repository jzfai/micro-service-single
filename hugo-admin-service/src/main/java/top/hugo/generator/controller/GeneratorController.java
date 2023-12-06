package top.hugo.generator.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import top.hugo.common.domain.R;
import top.hugo.common.utils.JacksonUtils;
import top.hugo.generator.dto.TemplateChangeDto;
import top.hugo.generator.service.GeneratorService;
import top.hugo.generator.utils.DateUtils;
import top.hugo.generator.utils.FileSelfUtils;
import top.hugo.generator.utils.GeneratorTempUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.util.List;
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
@RequestMapping("/generator")
public class GeneratorController {
    private final GeneratorService generatorService;

    /**
     * 根据文件名读取文件内容
     *
     * @param id       模板id
     * @param fileName 文件名
     */
    @PostMapping("readFileToStringByFileName")
    public R<Object> readFileToStringByFileName(Integer id, String fileName) {
        String fileAbsPath = GeneratorTempUtils.getDirByEnv() + File.separator + id + File.separator + fileName;
        String fileToString = FileSelfUtils.readFileToString(fileAbsPath);
        return R.ok(fileToString);
    }

    /**
     * 保存模版文件
     *
     * @param name 模板组名
     * @param file 文件数组
     */
    @PostMapping("saveTemplateFile")
    public R<Object> saveMultiTemplateFile(List<MultipartFile> file, @RequestParam("name") String name) {
        generatorService.saveTemplateFile(file, name);
        return R.ok();
    }


    /**
     * 修改模板文件
     *
     * @param jsonData 数据（json）
     * @param id       模板组Id
     * @param name     文件名
     * @param code     文件内容
     */
    @PostMapping("changeInputCode")
    public R<Object> changeInputCode(@RequestParam("jsonData") String jsonData, @RequestParam("id") Integer id,
                                     @RequestParam("name") String name, @RequestParam("code") String code) {
        String codeString = generatorService.updateFileReturnCode(JacksonUtils.parseObject(jsonData, Map.class), id, name, code);
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
        FileSelfUtils.exportFile(response, exportFilePath, DateUtils.dateTimeNow() + ".zip");
    }


    /**
     * 生成模版文件通过配置(ConfigId)
     *
     * @param configId    配置Id
     * @param templateId  模板组Id
     * @param fileNamePre 文件名前缀
     */
    @PostMapping("generatorTemplateFileByConfigId")
    public void generatorTemplateFileByConfigId(HttpServletResponse response, @RequestParam("configId") Integer configId, @RequestParam("templateId") Integer templateId, @RequestParam("fileNamePre") String fileNamePre) {
        //生成模板
        String exportFilePath = generatorService.generatorTemplateFileByConfigId(configId, templateId, fileNamePre);
        //你压缩包路径
        FileSelfUtils.exportFile(response, exportFilePath, DateUtils.dateTimeNow() + ".zip");
    }

    /**
     * 下载模板文件
     *
     * @param id 模板组Id
     */
    @PostMapping("downZipByTemplateFileId")
    public void downZipByTemplateFileId(HttpServletResponse response, HttpServletRequest request, @RequestParam("id") Integer id) {
        String exportFilePath = generatorService.downZipByTemplateFileId(id);
        //导出文件并删除响应文件和目录
        FileSelfUtils.exportFile(response, exportFilePath, DateUtils.dateTimeNow() + ".zip");
    }

    /**
     * 修改模板文件
     *
     * @param templateChangeBo
     */
    @PostMapping("changeTemplateFile")
    public R<Object> changeTemplateFile(@RequestBody TemplateChangeDto templateChangeBo) {
        generatorService.changeTemplateFile(templateChangeBo);
        return R.ok();
    }
}
