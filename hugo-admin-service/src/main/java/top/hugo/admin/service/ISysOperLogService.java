package top.hugo.admin.service;

import top.hugo.admin.entity.SysOperLog;
import top.hugo.admin.query.SysOperLogQuery;
import top.hugo.admin.vo.SysOperLogVo;
import top.hugo.domain.TableDataInfo;

import java.util.List;

/**
 * 操作日志记录 服务层处理
 *
 * @author kuanghua
 * @since 2023-11-21 13:58:50
 */
interface ISysOperLogService {


    TableDataInfo<SysOperLogVo> selectPageSysOperLogList();


    /**
     * 查询操作日志记录集合
     *
     * @param sysOperLog 操作日志记录
     * @return 操作日志记录集合
     */

    List<SysOperLogVo> selectSysOperLogList(SysOperLogQuery sysOperLog);

    /**
     * 查询所有平台
     *
     * @return 平台列表
     */
    List<SysOperLogVo> selectSysOperLogAll();

    /**
     * 通过平台ID查询操作日志记录
     *
     * @param sysOperLogId 平台ID
     * @return 角色对象信息
     */

    SysOperLog selectSysOperLogById(Long sysOperLogId);


    /**
     * 删除操作日志记录
     *
     * @param sysOperLogId 平台ID
     * @return 结果
     */

    int deleteSysOperLogById(Long sysOperLogId);

    /**
     * 批量删除操作日志记录
     *
     * @param sysOperLogIds 需要删除的平台ID
     * @return 结果
     */
    int deleteSysOperLogByIds(Long[] sysOperLogIds);

    /**
     * 新增保存操作日志记录
     *
     * @param sysOperLog 操作日志记录
     * @return 结果
     */

    int insertSysOperLog(SysOperLog sysOperLog);


    /**
     * 修改保存操作日志记录
     *
     * @param sysOperLog 操作日志记录
     * @return 结果
     */
    int updateSysOperLog(SysOperLog sysOperLog);

}