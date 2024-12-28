package top.hugo.admin.service;


import cn.hutool.core.util.ObjectUtil;
import top.hugo.admin.entity.SysPost;
import top.hugo.admin.mapper.SysPostMapper;
import top.hugo.admin.query.SysPostQuery;
import top.hugo.admin.vo.SysPostVo;
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
public class SysPostService {
    private final SysPostMapper sysPostMapper;
 
    public TableDataInfo<SysPostVo> selectPageSysPostList(SysPostQuery sysPostQuery) {
        LambdaQueryWrapper<SysPost> lqw = getQueryWrapper(sysPostQuery);
        IPage<SysPostVo> page = sysPostMapper.selectVoPage(sysPostQuery.build(), lqw);
        return TableDataInfo.build(page);
    }


    /**
     * 查询平台信息集合
     *
     * @param sysPostQuery 平台信息
     * @return 平台信息集合
     */

    public List<SysPostVo> selectSysPostList(SysPostQuery sysPostQuery) {
        LambdaQueryWrapper<SysPost> lqw = getQueryWrapper(sysPostQuery);
        return sysPostMapper.selectVoList(lqw);
    }


    /**
     * 查询wrapper封装
     *
     * @return
     */
    private static LambdaQueryWrapper<SysPost> getQueryWrapper(SysPostQuery sysPostQuery) {
        LambdaQueryWrapper<SysPost> lqw = new LambdaQueryWrapper<SysPost>();
        lqw.like(ObjectUtil.isNotEmpty(sysPostQuery.getPostName()), SysPost::getPostName, sysPostQuery.getPostName());
        lqw.like(ObjectUtil.isNotEmpty(sysPostQuery.getStatus()), SysPost::getStatus, sysPostQuery.getStatus());
        return lqw;
    }

    /**
     * 查询所有平台
     *
     * @return 平台列表
     */
    public List<SysPostVo> selectSysPostAll() {
        return sysPostMapper.selectVoList();
    }

    /**
     * 通过平台ID查询平台信息
     *
     * @param sysPostId 平台ID
     * @return 角色对象信息
     */

    public SysPost selectSysPostById(Long sysPostId) {
        return sysPostMapper.selectById(sysPostId);
    }


    /**
     * 删除平台信息
     *
     * @param sysPostId 平台ID
     * @return 结果
     */

    public int deleteSysPostById(Long sysPostId) {
        return sysPostMapper.deleteById(sysPostId);
    }

    /**
     * 批量删除平台信息
     *
     * @param sysPostIds 需要删除的平台ID
     * @return 结果
     */
    public int deleteSysPostByIds(Long[] sysPostIds) {
        return sysPostMapper.deleteBatchIds(Arrays.asList(sysPostIds));
    }

    /**
     * 新增保存平台信息
     *
     * @param sysPost 平台信息
     * @return 结果
     */

    public int insertSysPost(SysPost sysPost) {
        return sysPostMapper.insert(sysPost);
    }


    /**
     * 修改保存平台信息
     *
     * @param sysPost 平台信息
     * @return 结果
     */

    public int updateSysPost(SysPost sysPost) {
        return sysPostMapper.updateById(sysPost);
    }

}