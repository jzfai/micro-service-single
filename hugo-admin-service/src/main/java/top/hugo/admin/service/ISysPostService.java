package top.hugo.admin.service;

import top.hugo.admin.entity.SysPost;
import top.hugo.admin.query.SysPostQuery;
import top.hugo.admin.vo.SysPostVo;
import top.hugo.domain.TableDataInfo;

import java.util.List;

/**
 * 岗位信息表 服务层处理
 *
 * @author kuanghua
 * @since 2023-11-20 09:39:53
 */
interface ISysPostService {


    TableDataInfo<SysPostVo> selectPageSysPostList();


    /**
     * 查询岗位信息表集合
     *
     * @param sysPost 岗位信息表
     * @return 岗位信息表集合
     */

    List<SysPostVo> selectSysPostList(SysPostQuery sysPost);

    /**
     * 查询所有平台
     *
     * @return 平台列表
     */
    List<SysPostVo> selectSysPostAll();

    /**
     * 通过平台ID查询岗位信息表
     *
     * @param sysPostId 平台ID
     * @return 角色对象信息
     */

    SysPost selectSysPostById(Long sysPostId);


    /**
     * 删除岗位信息表
     *
     * @param sysPostId 平台ID
     * @return 结果
     */

    int deleteSysPostById(Long sysPostId);

    /**
     * 批量删除岗位信息表
     *
     * @param sysPostIds 需要删除的平台ID
     * @return 结果
     */
    int deleteSysPostByIds(Long[] sysPostIds);

    /**
     * 新增保存岗位信息表
     *
     * @param sysPost 岗位信息表
     * @return 结果
     */

    int insertSysPost(SysPost sysPost);


    /**
     * 修改保存岗位信息表
     *
     * @param sysPost 岗位信息表
     * @return 结果
     */
    int updateSysPost(SysPost sysPost);

}