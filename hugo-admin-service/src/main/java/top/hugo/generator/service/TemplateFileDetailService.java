package top.hugo.generator.service;

import cn.hutool.core.util.ObjectUtil;
import top.hugo.domain.TableDataInfo;
import top.hugo.generator.entity.TemplateFileDetail;
import top.hugo.generator.mapper.TemplateFileDetailMapper;
import top.hugo.generator.query.TemplateFileDetailQuery;
import top.hugo.generator.vo.TemplateFileDetailVo;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
/**
 * 平台信息 服务层处理
 *
 * @author kuanghua
 */
@RequiredArgsConstructor
@Service
public class TemplateFileDetailService {
    private final TemplateFileDetailMapper templateFileDetailMapper;
    public TableDataInfo<TemplateFileDetailVo> selectPageTemplateFileDetailList(TemplateFileDetailQuery templateFileDetailQuery) {
        LambdaQueryWrapper< TemplateFileDetail> lqw = getQueryWrapper(templateFileDetailQuery);
        IPage< TemplateFileDetailVo > page = templateFileDetailMapper.selectVoPage(templateFileDetailQuery.build(), lqw);
        return TableDataInfo.build(page);
    }


    /**
     * 查询平台信息集合
     *
     * @param templateFileDetailQuery 平台信息
     * @return 平台信息集合
     */

    public List<TemplateFileDetailVo> selectTemplateFileDetailList(TemplateFileDetailQuery templateFileDetailQuery) {
        LambdaQueryWrapper<TemplateFileDetail> lqw = getQueryWrapper(templateFileDetailQuery);
        return templateFileDetailMapper.selectVoList(lqw);
    }


    /**
     * 查询wrapper封装
     * @return
     */
    private static LambdaQueryWrapper<TemplateFileDetail> getQueryWrapper(TemplateFileDetailQuery templateFileDetailQuery) {
        LambdaQueryWrapper< TemplateFileDetail> lqw = new LambdaQueryWrapper<TemplateFileDetail>();
        lqw.eq(ObjectUtil.isNotEmpty(templateFileDetailQuery.getId()), TemplateFileDetail::getId, templateFileDetailQuery.getId());
        lqw.like(ObjectUtil.isNotEmpty(templateFileDetailQuery.getName()), TemplateFileDetail::getName, templateFileDetailQuery.getName());
        lqw.eq(ObjectUtil.isNotEmpty(templateFileDetailQuery.getModelType()), TemplateFileDetail::getModelType, templateFileDetailQuery.getModelType());
        lqw.like(ObjectUtil.isNotEmpty(templateFileDetailQuery.getPackageSuffix()), TemplateFileDetail::getPackageSuffix, templateFileDetailQuery.getPackageSuffix());
        lqw.eq(ObjectUtil.isNotEmpty(templateFileDetailQuery.getTemplateId()), TemplateFileDetail::getTemplateId, templateFileDetailQuery.getTemplateId());
        lqw.like(ObjectUtil.isNotEmpty(templateFileDetailQuery.getModelName()), TemplateFileDetail::getModelName, templateFileDetailQuery.getModelName());

        lqw.orderByAsc(TemplateFileDetail::getModelName);
        lqw.orderByAsc(TemplateFileDetail::getModelType);
        lqw.orderByAsc(TemplateFileDetail::getPackageSuffix);
        //lqw.orderByAsc(TemplateFileDetail::getUpdateTime);
        return lqw;
    }

    /**
     * 查询所有平台
     *
     * @return 平台列表
     */
    public List< TemplateFileDetailVo > selectTemplateFileDetailAll(TemplateFileDetailQuery templateFileDetailQuery) {
        LambdaQueryWrapper<TemplateFileDetail> lqw = getQueryWrapper(templateFileDetailQuery);
        return templateFileDetailMapper.selectVoList(lqw);
    }

    /**
     * 通过平台ID查询平台信息
     *
     * @param templateFileDetailId 平台ID
     * @return 角色对象信息
     */

    public TemplateFileDetail selectTemplateFileDetailById(Long templateFileDetailId) {
        return templateFileDetailMapper.selectById(templateFileDetailId);
    }


    /**
     * 删除平台信息
     *
     * @param templateFileDetailId 平台ID
     * @return 结果
     */

    public int deleteTemplateFileDetailById(Long templateFileDetailId) {
        return templateFileDetailMapper.deleteById(templateFileDetailId);
    }

    /**
     * 批量删除平台信息
     *
     * @param templateFileDetailIds 需要删除的平台ID
     * @return 结果
     */
    public int deleteTemplateFileDetailByIds(Long[] templateFileDetailIds) {

        return templateFileDetailMapper.deleteBatchIds(Arrays.asList(templateFileDetailIds));
    }

    /**
     * 新增保存平台信息
     *
     * @param templateFileDetail 平台信息
     * @return 结果
     */

    public int insertTemplateFileDetail(TemplateFileDetail templateFileDetail) {
        return templateFileDetailMapper.insert(templateFileDetail);
    }


    /**
     * 修改保存平台信息
     *
     * @param templateFileDetail 平台信息
     * @return 结果
     */

    public int updateTemplateFileDetail(TemplateFileDetail templateFileDetail) {
        return templateFileDetailMapper.updateById(templateFileDetail);
    }

}
