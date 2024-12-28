package top.hugo.generator.service;

import cn.hutool.core.util.ObjectUtil;
import top.hugo.domain.TableDataInfo;
import top.hugo.generator.entity.TemplateFile;
import top.hugo.generator.mapper.TemplateFileMapper;
import top.hugo.generator.query.TemplateFileQuery;
import top.hugo.generator.vo.TemplateFileVo;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

/**
 * @author kuanghua
 */
@RequiredArgsConstructor
@Service
public class TemplateFileService {
    private final TemplateFileMapper templateFileMapper;

    public TableDataInfo<TemplateFileVo> selectPageTemplateFileList(TemplateFileQuery templateFileQuery) {
        LambdaQueryWrapper<TemplateFile> lqw = getQueryWrapper(templateFileQuery);
        IPage<TemplateFileVo> page = templateFileMapper.selectVoPage(templateFileQuery.build(), lqw);
        return TableDataInfo.build(page);
    }


    /**
     * 查询分页
     *
     * @return
     */

    public List<TemplateFileVo> selectTemplateFileList(TemplateFileQuery templateFileQuery) {
        LambdaQueryWrapper<TemplateFile> lqw = getQueryWrapper(templateFileQuery);
        return templateFileMapper.selectVoList(lqw);
    }


    /*查询条件抽取*/
    private static LambdaQueryWrapper<TemplateFile> getQueryWrapper(TemplateFileQuery templateFileQuery) {
        LambdaQueryWrapper<TemplateFile> lqw = new LambdaQueryWrapper<>();
        lqw.like(ObjectUtil.isNotEmpty(templateFileQuery.getName()), TemplateFile::getName, templateFileQuery.getName());
        lqw.eq(ObjectUtil.isNotEmpty(templateFileQuery.getProjectType()), TemplateFile::getProjectType, templateFileQuery.getProjectType());
        //用户查询
        //lqw.eq(!LoginHelper.isAdmin(), TemplateFile::getCreateBy, LoginHelper.getUsername());

        lqw.orderByDesc(TemplateFile::getCreateTime).orderByDesc(TemplateFile::getUpdateTime);
        return lqw;
    }

    /**
     * 查询所有平台
     *
     * @return 平台列表
     */
    public List<TemplateFileVo> selectTemplateFileAll() {
        return templateFileMapper.selectVoList();
    }

    /**
     * 通过ID查询
     *
     * @param templateFileId ID
     * @return 角色对象信息
     */

    public TemplateFileVo selectTemplateFileById(Long templateFileId) {
        return templateFileMapper.selectVoById(templateFileId);
    }


    /**
     * 删除
     *
     * @param templateFileId 平台ID
     * @return 结果
     */

    public int deleteTemplateFileById(Long templateFileId) {
        return templateFileMapper.deleteById(templateFileId);
    }

    /**
     * 批量删除
     *
     * @param templateFileIds 需要删除的平台ID
     * @return 结果
     */
    public int deleteTemplateFileByIds(Long[] templateFileIds) {
        return templateFileMapper.deleteBatchIds(Arrays.asList(templateFileIds));
    }

    /**
     * 新增保存
     *
     * @param templateFile 平台信息
     * @return 结果
     */

    public int insertTemplateFile(TemplateFile templateFile) {
        return templateFileMapper.insert(templateFile);
    }


    /**
     * 修改保存
     *
     * @param templateFile
     * @return 结果
     */

    public int updateTemplateFile(TemplateFile templateFile) {
        return templateFileMapper.updateById(templateFile);
    }

}