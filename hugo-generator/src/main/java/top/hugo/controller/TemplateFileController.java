package top.hugo.controller;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import top.hugo.domain.ResResult;
import top.hugo.domain.SelfCommonParams;
import top.hugo.entity.TemplateFile;
import top.hugo.service.TemplateFileService;
import top.hugo.utils.FileSelfUtils;
import top.hugo.utils.GeneratorTempUtils;
import top.hugo.utils.ObjSelfUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.util.List;
import java.util.Map;

/**
 * Controller
 *
 * @author 邝华
 * @since 2022-12-07 13:51:06
 */
@RestController
@RequestMapping("templateFile")
@Validated
public class TemplateFileController {

    @Resource
    private TemplateFileService templateFileService;

    /**
     * 分页查询所有数据
     *
     * @return ResResult
     */
    @GetMapping("selectPage")
    public ResResult<List<TemplateFile>> selectPage(String id, String name, SelfCommonParams commonParams) {
        QueryWrapper<TemplateFile> queryWrapper = new QueryWrapper<>();
        //主键id
        if (ObjSelfUtils.isNotEmpty(id)) {
            queryWrapper.like("id", id);
        }
        //文件存储名
        if (ObjSelfUtils.isNotEmpty(name)) {
            queryWrapper.like("name", name);
        }

        queryWrapper.select("id,name,file_arr,create_time,update_time,creator");
        Page<TemplateFile> templateFilePage = this.templateFileService.selectPage(commonParams.getPageNum(), commonParams.getPageSize(), queryWrapper);
        return new ResResult().success(templateFilePage);
    }

    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
    @GetMapping("selectById")
    public ResResult<TemplateFile> selectById(@RequestParam("id") Integer id) {
        return new ResResult().success(this.templateFileService.selectById(id));
    }

    /**
     * @Description: 根据id数组查询列表
     * @Param: idList id数组
     * @return: ids列表数据
     */
    @PostMapping("selectBatchIds")
    public ResResult<List<TemplateFile>> selectBatchIds(@RequestParam("idList") List<Integer> idList) {
        return new ResResult().success(this.templateFileService.selectBatchIds(idList));
    }


    /**
     * 修改数据
     *
     * @param templateFile 实体对象
     * @return 修改结果
     */
    @PutMapping("updateById")
    public ResResult updateById(@Validated @RequestBody TemplateFile templateFile) {
        return new ResResult().success(this.templateFileService.updateById(templateFile));
    }

    /**
     * 删除数据
     *
     * @param idList 主键结合
     * @return 删除结果
     */
    @DeleteMapping("deleteBatchIds")
    public ResResult deleteBatchIds(@RequestBody List<Integer> idList) {
        return new ResResult().success(this.templateFileService.deleteBatchIds(idList));
    }
    /**
     * 根据id删除数据
     *
     * @param id
     * @return 删除结果
     */
    @DeleteMapping("deleteById")
    public ResResult deleteById(@RequestParam("id") Integer id) {
        return new ResResult().success(this.templateFileService.deleteById(id));
    }


    /*
     * 根据文件名读取文件内容
     * */
    @PostMapping("readFileToStringByFileName")
    public ResResult<String> readFileToStringByFileName(Integer id, String fileName) {
        String fileAbsPath = FileSelfUtils.getTemplateSaveRootDir() + id + File.separator + fileName;
        String fileToString = FileSelfUtils.readFileToString(fileAbsPath);
        return new ResResult().success(fileToString);
    }

    /*
     * 保存模版文件
     * */
    @PostMapping("saveTemplateFile")
    public ResResult saveMultiTemplateFile(@RequestParam("file") List<MultipartFile> files, @RequestParam("name") String name) {
        this.templateFileService.saveMultiTemplateFile(files, name);
        return new ResResult().success();
    }


    /*
     * 保存模版文件
     * */
    @PostMapping("changeInputCode")
    public ResResult<String> changeInputCode(@RequestParam("jsonData") String jsonData, @RequestParam("id") Integer id,
                                             @RequestParam("name") String name, @RequestParam("code") String code) {
        Map<String, Object> JsonMap = JSON.parseObject(jsonData, Map.class);
        String codeString = this.templateFileService.changeInputCode(JsonMap, id, name, code);
        return new ResResult().success(codeString);
    }

    /*
     * 生成模版文件通过配置
     * */
    @PostMapping("generatorTemplateFileByConfig")
    public void generatorTemplateFileByConfig(HttpServletResponse response, @RequestParam("jsonData") String jsonData, @RequestParam("id") Integer id, @RequestParam("fileNamePre") String fileNamePre) {
        //生成模板
        Map<String, Object> JsonMap = JSON.parseObject(jsonData, Map.class);
        String exportFilePath = this.templateFileService.generatorTemplateFileByConfig(JsonMap, id, fileNamePre);
        response.setContentType("application/zip");
        response.setHeader("Access-Control-Expose-Headers", "exportFileName");
        response.setHeader("exportFileName", "back-temp-" + ObjSelfUtils.getCurrentDateTimeTrim() + ".zip");
        //你压缩包路径
        GeneratorTempUtils.downloadZip(response, exportFilePath);

    }

    @PostMapping("downZipByTemplateFileId")
    public void downZipByTemplateFileId(HttpServletResponse response, HttpServletRequest request, @RequestParam("id") Integer id) {
        String exportFilePath = this.templateFileService.downZipByTemplateFileId(id);
        response.setContentType("application/zip");
        response.setHeader("Access-Control-Expose-Headers", "exportFileName");
        response.setHeader("exportFileName", "back-temp-" + ObjSelfUtils.getCurrentDateTimeTrim() + ".zip");
        //你压缩包路径
        GeneratorTempUtils.downloadZip(response, exportFilePath);
    }
}
