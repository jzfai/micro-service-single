package top.hugo.generator.controller;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import top.hugo.common.domain.R;
import top.hugo.common.utils.DateUtils;
import top.hugo.common.utils.JsonUtils;
import top.hugo.generator.bo.TemplateChangeBo;
import top.hugo.generator.domain.SelfCommonParams;
import top.hugo.generator.entity.TemplateFile;
import top.hugo.generator.service.TemplateFileService;
import top.hugo.generator.utils.FileSelfUtils;
import top.hugo.generator.utils.GeneratorTempUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.util.List;
import java.util.Map;

/**
 * 模板模块及生成文件
 *
 * @author 邝华
 * @since 2022-12-07 13:51:06
 */
@RestController
@RequestMapping("basis-func/templateFile")
@Validated
public class TemplateFileController {
    @Resource
    private TemplateFileService templateFileService;
    /**
     * 分页查询所有数据
     *
     * @param id   主键id
     * @param name 文件存储名
     */
    @GetMapping("selectPage")
    public R<Page<TemplateFile>> selectPage(String id, String name, SelfCommonParams commonParams) {
        QueryWrapper<TemplateFile> queryWrapper = new QueryWrapper<>();
        //
        if (ObjectUtil.isNotEmpty(id)) {
            queryWrapper.like("id", id);
        }
        //
        if (ObjectUtil.isNotEmpty(name)) {
            queryWrapper.like("name", name);
        }
        queryWrapper.select("*");
        queryWrapper.orderByDesc("create_time");
        queryWrapper.orderByDesc("update_time");
        Page<TemplateFile> templateFilePage = this.templateFileService.selectPage(commonParams.getPageNum(), commonParams.getPageSize(), queryWrapper);
        return R.ok(templateFilePage);
    }

    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     */
    @GetMapping("selectById")
    public R<TemplateFile> selectById(@RequestParam("id") Integer id) {
        return R.ok(this.templateFileService.selectById(id));
    }

    /**
     * 根据id数组查询列表
     *
     * @param idList id数组
     */
    @PostMapping("selectBatchIds")
    public R<List<TemplateFile>> selectBatchIds(@RequestParam("idList") List<Integer> idList) {
        return R.ok(this.templateFileService.selectBatchIds(idList));
    }


    /**
     * 修改数据
     *
     * @param templateFile 实体对象
     */
    @PutMapping("updateById")
    public R<Integer> updateById(@Validated @RequestBody TemplateFile templateFile) {
        return R.ok(this.templateFileService.updateById(templateFile));
    }



    /**
     * 修改数据
     *
     * @param templateFile 实体对象 
     * @return
     */
    @PutMapping("updateFileArr")
    public R<Integer> updateFileArr(@Validated @RequestBody TemplateFile templateFile) {
        return R.ok(this.templateFileService.updateFileArr(templateFile));
    }
    /**
     * 删除数据
     *
     * @param idList 主键数组
     */
    @DeleteMapping("deleteBatchIds")
    public R<Integer> deleteBatchIds(@RequestBody List<Integer> idList) {
        return R.ok(this.templateFileService.deleteBatchIds(idList));
    }

    /**
     * 根据id删除数据
     *
     * @param id 主键id
     */
    @DeleteMapping("deleteById")
    public R<Integer> deleteById(@RequestParam("id") Integer id) {
        return R.ok(this.templateFileService.deleteById(id));
    }

    /**
     * 根据文件名读取文件内容
     *
     * @param id       模板id
     * @param fileName 文件名
     */
    @PostMapping("readFileToStringByFileName")
    public R<Object> readFileToStringByFileName(Integer id, String fileName) {
        String fileAbsPath = FileSelfUtils.getTemplateSaveRootDir() + id + File.separator + fileName;
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
        this.templateFileService.saveMultiTemplateFile(file, name);
        return R.ok();
    }

    /**
     * 更新模版文件
     *
     * @param name 模板组名
     * @param file 文件数组
     */
    @PostMapping("saveTemplateFileUpdate")
    public R<Object> saveMultiTemplateFile(List<MultipartFile> file,@RequestParam("fileArr") String fileArr, @RequestParam("name") String name,@RequestParam("id") Integer id) {
        this.templateFileService.saveTemplateFileUpdate(file,fileArr,name,id);
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
        String codeString = this.templateFileService.changeInputCode(JsonUtils.parseObject(jsonData, Map.class), id, name, code);
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
        String exportFilePath = this.templateFileService.generatorTemplateFileByConfig(JsonUtils.parseObject(jsonData, Map.class), id, fileNamePre);
        //你压缩包路径
        FileSelfUtils.exportFile(response, exportFilePath, DateUtils.dateTimeNow() + ".zip");
        FileSelfUtils.deleteFile(exportFilePath);
        FileSelfUtils.deleteDir(GeneratorTempUtils.getNeedZipDir());
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
        String exportFilePath = this.templateFileService.generatorTemplateFileByConfigId(configId, templateId, fileNamePre);
        //你压缩包路径
        FileSelfUtils.exportFile(response, exportFilePath, DateUtils.dateTimeNow() + ".zip");
        FileSelfUtils.deleteFile(exportFilePath);
        FileSelfUtils.deleteDir(GeneratorTempUtils.getNeedZipDir());
    }

    /**
     * 下载模板文件
     *
     * @param id 模板组Id
     */
    @PostMapping("downZipByTemplateFileId")
    public void downZipByTemplateFileId(HttpServletResponse response, HttpServletRequest request, @RequestParam("id") Integer id) {
        String exportFilePath = this.templateFileService.downZipByTemplateFileId(id);
        //导出文件并删除响应文件和目录
        FileSelfUtils.exportFile(response, exportFilePath, DateUtils.dateTimeNow() + ".zip");
        FileSelfUtils.deleteFile(exportFilePath);
        FileSelfUtils.deleteDir(GeneratorTempUtils.getNeedZipDir());
    }

    /**
     * 修改文件信息
     *
     * @param templateChangeBo
     */
    @PostMapping("changeTemplateFile")
    public R<Object> changeTemplateFile(@RequestBody TemplateChangeBo templateChangeBo) {
        templateFileService.changeTemplateFile(templateChangeBo);
        return R.ok();
    }
}
