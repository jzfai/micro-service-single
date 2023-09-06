package top.hugo.oss.service;


import top.hugo.common.page.TableDataInfo;
import top.hugo.oss.entity.SysOss;
import top.hugo.oss.query.SysOssQuery;
import top.hugo.oss.vo.SysOssVo;

import java.util.List;

/**
 * OSS对象存储表 服务层处理
 *
 * @author kuanghua
 * @since 2023-09-06 11:14:58
 */
interface ISysOssService {


    TableDataInfo<SysOssVo> selectPageSysOssList();


    /**
     * 查询OSS对象存储表集合
     *
     * @param sysOss OSS对象存储表
     * @return OSS对象存储表集合
     */

    List<SysOssVo> selectSysOssList(SysOssQuery sysOss);

    /**
     * 查询所有平台
     *
     * @return 平台列表
     */
    List<SysOssVo> selectSysOssAll();

    /**
     * 通过平台ID查询OSS对象存储表
     *
     * @param sysOssId 平台ID
     * @return 角色对象信息
     */

    SysOss selectSysOssById(Long sysOssId);


    /**
     * 删除OSS对象存储表
     *
     * @param sysOssId 平台ID
     * @return 结果
     */

    int deleteSysOssById(Long sysOssId);

    /**
     * 批量删除OSS对象存储表
     *
     * @param sysOssIds 需要删除的平台ID
     * @return 结果
     */
    int deleteSysOssByIds(Long[] sysOssIds);

    /**
     * 新增保存OSS对象存储表
     *
     * @param sysOss OSS对象存储表
     * @return 结果
     */

    int insertSysOss(SysOss sysOss);


    /**
     * 修改保存OSS对象存储表
     *
     * @param sysOss OSS对象存储表
     * @return 结果
     */
    int updateSysOss(SysOss sysOss);

}