package top.hugo.monitor.service;

import top.hugo.domain.TableDataInfo;
import top.hugo.monitor.entity.SysLogininfor;
import top.hugo.monitor.query.SysLogininforQuery;
import top.hugo.monitor.vo.SysLogininforVo;

import java.util.List;

/**
 * 系统访问记录 服务层处理
 *
 * @author kuanghua
 * @since 2023-11-17 10:02:07
 */
interface ISysLogininforService {

 
    TableDataInfo<SysLogininforVo> selectPageSysLogininforList();


    /**
     * 查询系统访问记录集合
     *
     * @param sysLogininfor 系统访问记录
     * @return 系统访问记录集合
     */

    List<SysLogininforVo> selectSysLogininforList(SysLogininforQuery sysLogininfor);

    /**
     * 查询所有平台
     *
     * @return 平台列表
     */
    List<SysLogininforVo> selectSysLogininforAll();

    /**
     * 通过平台ID查询系统访问记录
     *
     * @param sysLogininforId 平台ID
     * @return 角色对象信息
     */

    SysLogininfor selectSysLogininforById(Long sysLogininforId);


    /**
     * 删除系统访问记录
     *
     * @param sysLogininforId 平台ID
     * @return 结果
     */

    int deleteSysLogininforById(Long sysLogininforId);

    /**
     * 批量删除系统访问记录
     *
     * @param sysLogininforIds 需要删除的平台ID
     * @return 结果
     */
    int deleteSysLogininforByIds(Long[] sysLogininforIds);

    /**
     * 新增保存系统访问记录
     *
     * @param sysLogininfor 系统访问记录
     * @return 结果
     */

    int insertSysLogininfor(SysLogininfor sysLogininfor);


    /**
     * 修改保存系统访问记录
     *
     * @param sysLogininfor 系统访问记录
     * @return 结果
     */
    int updateSysLogininfor(SysLogininfor sysLogininfor);

}