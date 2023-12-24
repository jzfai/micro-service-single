package top.hugo.admin.service;


import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import top.hugo.admin.entity.SysOperLog;
import top.hugo.admin.mapper.SysOperLogMapper;
import top.hugo.admin.query.SysOperLogQuery;
import top.hugo.admin.vo.SysOperLogVo;
import top.hugo.common.event.OperLogEvent;
import top.hugo.common.utils.AddressUtils;
import top.hugo.domain.TableDataInfo;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * 平台信息 服务层处理
 *
 * @author kuanghua
 */
@RequiredArgsConstructor
@Service
public class SysOperLogService {
    private final SysOperLogMapper sysOperLogMapper;

    public TableDataInfo<SysOperLogVo> selectPageSysOperLogList(SysOperLogQuery sysOperLogQuery) {
        LambdaQueryWrapper<SysOperLog> lqw = getQueryWrapper(sysOperLogQuery);
        IPage<SysOperLogVo> page = sysOperLogMapper.selectVoPage(sysOperLogQuery.build(), lqw);
        return TableDataInfo.build(page);
    }


    /**
     * 查询平台信息集合
     *
     * @param sysOperLogQuery 平台信息
     * @return 平台信息集合
     */

    public List<SysOperLogVo> selectSysOperLogList(SysOperLogQuery sysOperLogQuery) {
        LambdaQueryWrapper<SysOperLog> lqw = getQueryWrapper(sysOperLogQuery);
        return sysOperLogMapper.selectVoList(lqw);
    }


    /**
     * 查询wrapper封装
     *
     * @return
     */
    private static LambdaQueryWrapper<SysOperLog> getQueryWrapper(SysOperLogQuery sysOperLogQuery) {
        LambdaQueryWrapper<SysOperLog> lqw = new LambdaQueryWrapper<SysOperLog>();
        lqw.like(ObjectUtil.isNotEmpty(sysOperLogQuery.getBusinessType()), SysOperLog::getBusinessType, sysOperLogQuery.getBusinessType());
        lqw.like(ObjectUtil.isNotEmpty(sysOperLogQuery.getOperTime()), SysOperLog::getOperTime, sysOperLogQuery.getOperTime());
        lqw.like(ObjectUtil.isNotEmpty(sysOperLogQuery.getOperatorType()), SysOperLog::getOperatorType, sysOperLogQuery.getOperatorType());
        lqw.like(ObjectUtil.isNotEmpty(sysOperLogQuery.getStatus()), SysOperLog::getStatus, sysOperLogQuery.getStatus());
        return lqw;
    }

    /**
     * 查询所有平台
     *
     * @return 平台列表
     */
    public List<SysOperLogVo> selectSysOperLogAll() {
        return sysOperLogMapper.selectVoList();
    }

    /**
     * 通过平台ID查询平台信息
     *
     * @param sysOperLogId 平台ID
     * @return 角色对象信息
     */

    public SysOperLog selectSysOperLogById(Long sysOperLogId) {
        return sysOperLogMapper.selectById(sysOperLogId);
    }


    /**
     * 删除平台信息
     *
     * @param sysOperLogId 平台ID
     * @return 结果
     */

    public int deleteSysOperLogById(Long sysOperLogId) {
        return sysOperLogMapper.deleteById(sysOperLogId);
    }

    /**
     * 批量删除平台信息
     *
     * @param sysOperLogIds 需要删除的平台ID
     * @return 结果
     */
    public int deleteSysOperLogByIds(Long[] sysOperLogIds) {
        return sysOperLogMapper.deleteBatchIds(Arrays.asList(sysOperLogIds));
    }

    /**
     * 新增保存平台信息
     *
     * @param sysOperLog 平台信息
     * @return 结果
     */

    public int insertSysOperLog(SysOperLog sysOperLog) {
        return sysOperLogMapper.insert(sysOperLog);
    }


    /**
     * 修改保存平台信息
     *
     * @param sysOperLog 平台信息
     * @return 结果
     */

    public int updateSysOperLog(SysOperLog sysOperLog) {
        return sysOperLogMapper.updateById(sysOperLog);
    }

    @Async
    @EventListener
    public void recordOper(OperLogEvent operLogEvent) {
        SysOperLog operLog = BeanUtil.toBean(operLogEvent, SysOperLog.class);
        operLog.setOperTime(new Date());
        // 远程查询操作地点
        operLog.setOperLocation(AddressUtils.getRealAddressByIP(operLog.getOperIp()));
        insertSysOperLog(operLog);
    }
}