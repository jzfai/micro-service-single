package top.hugo.admin.service;


import top.hugo.admin.entity.SysPlatform;
import top.hugo.admin.query.SysPlatformQuery;
import top.hugo.admin.vo.SysPlatformVo;
import top.hugo.domain.TableDataInfo;

import java.util.List;

/**
 * 平台 服务层处理
 *
 * @author kuanghua
 * @since 2023-11-10 11:54:52
 */
interface ISysPlatformService {


    TableDataInfo<SysPlatformVo> selectPageSysPlatformList();


    /**
     * 查询平台集合
     *
     * @param sysPlatform 平台
     * @return 平台集合
     */

    List<SysPlatformVo> selectSysPlatformList(SysPlatformQuery sysPlatform);

    /**
     * 查询所有平台
     *
     * @return 平台列表
     */
    List<SysPlatformVo> selectSysPlatformAll();

    /**
     * 通过平台ID查询平台
     *
     * @param sysPlatformId 平台ID
     * @return 角色对象信息
     */

    SysPlatform selectSysPlatformById(Long sysPlatformId);


    /**
     * 删除平台
     *
     * @param sysPlatformId 平台ID
     * @return 结果
     */

    int deleteSysPlatformById(Long sysPlatformId);

    /**
     * 批量删除平台
     *
     * @param sysPlatformIds 需要删除的平台ID
     * @return 结果
     */
    int deleteSysPlatformByIds(Long[] sysPlatformIds);

    /**
     * 新增保存平台
     *
     * @param sysPlatform 平台
     * @return 结果
     */

    int insertSysPlatform(SysPlatform sysPlatform);


    /**
     * 修改保存平台
     *
     * @param sysPlatform 平台
     * @return 结果
     */
    int updateSysPlatform(SysPlatform sysPlatform);

}