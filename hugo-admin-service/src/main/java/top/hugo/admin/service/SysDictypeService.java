package top.hugo.admin.service;


import cn.hutool.core.util.ObjectUtil;
import top.hugo.admin.entity.SysDictype;
import top.hugo.admin.mapper.SysDictypeMapper;
import top.hugo.admin.query.SysDictypeQuery;
import top.hugo.admin.vo.SysDictypeVo;
import top.hugo.domain.TableDataInfo;
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
public class SysDictypeService {
    private final SysDictypeMapper sysDictypeMapper;

    public TableDataInfo<SysDictypeVo> selectPageSysDictypeList(SysDictypeQuery sysDictypeQuery) {
        LambdaQueryWrapper<SysDictype> lqw = getQueryWrapper(sysDictypeQuery);
        IPage<SysDictypeVo> page = sysDictypeMapper.selectVoPage(sysDictypeQuery.build(), lqw);
        return TableDataInfo.build(page);
    }


    /**
     * 查询平台信息集合
     *
     * @param sysDictypeQuery 平台信息
     * @return 平台信息集合
     */

    public List<SysDictypeVo> selectSysDictypeList(SysDictypeQuery sysDictypeQuery) {
        LambdaQueryWrapper<SysDictype> lqw = getQueryWrapper(sysDictypeQuery);
        return sysDictypeMapper.selectVoList(lqw);
    }

    /**
     * 查询wrapper封装
     *
     * @return
     */
    private static LambdaQueryWrapper<SysDictype> getQueryWrapper(SysDictypeQuery sysDictypeQuery) {
        LambdaQueryWrapper<SysDictype> lqw = new LambdaQueryWrapper<SysDictype>();
        lqw.like(ObjectUtil.isNotEmpty(sysDictypeQuery.getDictName()), SysDictype::getDictName, sysDictypeQuery.getDictName());
        lqw.like(ObjectUtil.isNotEmpty(sysDictypeQuery.getDictType()), SysDictype::getDictType, sysDictypeQuery.getDictType());
        lqw.like(ObjectUtil.isNotEmpty(sysDictypeQuery.getStatus()), SysDictype::getStatus, sysDictypeQuery.getStatus());
        return lqw;
    }

    /**
     * 查询所有平台
     *
     * @return 平台列表
     */
    public List<SysDictypeVo> selectSysDictypeAll() {
        return sysDictypeMapper.selectVoList();
    }

    /**
     * 通过平台ID查询平台信息
     *
     * @param sysDictypeId 平台ID
     * @return 角色对象信息
     */

    public SysDictype selectSysDictypeById(Long sysDictypeId) {
        return sysDictypeMapper.selectById(sysDictypeId);
    }


    /**
     * 删除平台信息
     *
     * @param sysDictypeId 平台ID
     * @return 结果
     */

    public int deleteSysDictypeById(Long sysDictypeId) {
        return sysDictypeMapper.deleteById(sysDictypeId);
    }

    /**
     * 批量删除平台信息
     *
     * @param sysDictypeIds 需要删除的平台ID
     * @return 结果
     */
    public int deleteSysDictypeByIds(Long[] sysDictypeIds) {
        return sysDictypeMapper.deleteBatchIds(Arrays.asList(sysDictypeIds));
    }

    /**
     * 新增保存平台信息
     *
     * @param sysDictype 平台信息
     * @return 结果
     */

    public int insertSysDictype(SysDictype sysDictype) {
        return sysDictypeMapper.insert(sysDictype);
    }


    /**
     * 修改保存平台信息
     *
     * @param sysDictype 平台信息
     * @return 结果
     */

    public int updateSysDictype(SysDictype sysDictype) {
        return sysDictypeMapper.updateById(sysDictype);
    }

}