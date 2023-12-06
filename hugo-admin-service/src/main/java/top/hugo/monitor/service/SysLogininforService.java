package top.hugo.monitor.service;


import cn.hutool.core.util.ObjectUtil;
import cn.hutool.extra.servlet.ServletUtil;
import cn.hutool.http.useragent.UserAgent;
import cn.hutool.http.useragent.UserAgentUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import top.hugo.common.event.LogininforEvent;
import top.hugo.common.utils.AddressUtils;
import top.hugo.domain.TableDataInfo;
import top.hugo.monitor.entity.SysLogininfor;
import top.hugo.monitor.mapper.SysLogininforMapper;
import top.hugo.monitor.query.SysLogininforQuery;
import top.hugo.monitor.vo.SysLogininforVo;

import javax.servlet.http.HttpServletRequest;
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
@Slf4j
public class SysLogininforService {
    private final SysLogininforMapper sysLogininforMapper;

    public TableDataInfo<SysLogininforVo> selectPageSysLogininforList(SysLogininforQuery sysLogininforQuery) {
        LambdaQueryWrapper<SysLogininfor> lqw = getQueryWrapper(sysLogininforQuery);
        IPage<SysLogininforVo> page = sysLogininforMapper.selectVoPage(sysLogininforQuery.build(), lqw);
        return TableDataInfo.build(page);
    }


    /**
     * 查询平台信息集合
     *
     * @param sysLogininforQuery 平台信息
     * @return 平台信息集合
     */

    public List<SysLogininforVo> selectSysLogininforList(SysLogininforQuery sysLogininforQuery) {
        LambdaQueryWrapper<SysLogininfor> lqw = getQueryWrapper(sysLogininforQuery);
        return sysLogininforMapper.selectVoList(lqw);
    }


    /**
     * 查询wrapper封装
     *
     * @return
     */
    private static LambdaQueryWrapper<SysLogininfor> getQueryWrapper(SysLogininforQuery sysLogininforQuery) {
        LambdaQueryWrapper<SysLogininfor> lqw = new LambdaQueryWrapper<SysLogininfor>();
        lqw.like(ObjectUtil.isNotEmpty(sysLogininforQuery.getBrowser()), SysLogininfor::getBrowser, sysLogininforQuery.getBrowser());
        lqw.like(ObjectUtil.isNotEmpty(sysLogininforQuery.getIpaddr()), SysLogininfor::getIpaddr, sysLogininforQuery.getIpaddr());
        lqw.like(ObjectUtil.isNotEmpty(sysLogininforQuery.getStatus()), SysLogininfor::getStatus, sysLogininforQuery.getStatus());
        lqw.like(ObjectUtil.isNotEmpty(sysLogininforQuery.getUserName()), SysLogininfor::getUserName, sysLogininforQuery.getUserName());
        //时间范围
        lqw.between(ObjectUtil.isNotEmpty(sysLogininforQuery.getBeginTime()) && ObjectUtil.isNotEmpty(sysLogininforQuery.getEndTime()),
                SysLogininfor::getLoginTime, sysLogininforQuery.getBeginTime(), sysLogininforQuery.getEndTime());
        lqw.orderByDesc(SysLogininfor::getLoginTime);
        return lqw;
    }

    /**
     * 查询所有平台
     *
     * @return 平台列表
     */
    public List<SysLogininforVo> selectSysLogininforAll() {
        return sysLogininforMapper.selectVoList();
    }

    /**
     * 通过平台ID查询平台信息
     *
     * @param sysLogininforId 平台ID
     * @return 角色对象信息
     */

    public SysLogininfor selectSysLogininforById(Long sysLogininforId) {
        return sysLogininforMapper.selectById(sysLogininforId);
    }


    /**
     * 删除平台信息
     *
     * @param sysLogininforId 平台ID
     * @return 结果
     */

    public int deleteSysLogininforById(Long sysLogininforId) {
        return sysLogininforMapper.deleteById(sysLogininforId);
    }

    /**
     * 批量删除平台信息
     *
     * @param sysLogininforIds 需要删除的平台ID
     * @return 结果
     */
    public int deleteSysLogininforByIds(Long[] sysLogininforIds) {
        return sysLogininforMapper.deleteBatchIds(Arrays.asList(sysLogininforIds));
    }

    /**
     * 新增保存平台信息
     *
     * @param sysLogininfor 平台信息
     * @return 结果
     */

    public int insertSysLogininfor(SysLogininfor sysLogininfor) {
        return sysLogininforMapper.insert(sysLogininfor);
    }


    /**
     * 修改保存平台信息
     *
     * @param sysLogininfor 平台信息
     * @return 结果
     */

    public int updateSysLogininfor(SysLogininfor sysLogininfor) {
        return sysLogininforMapper.updateById(sysLogininfor);
    }


    /*
     * 清空系统登录日志
     * */
    public void delete(LambdaQueryWrapper<Object> objectLambdaQueryWrapper) {
        sysLogininforMapper.delete(new LambdaQueryWrapper<>());
    }


    /**
     * 记录登录信息
     */
    @Async
    @EventListener
    public void recordLogininfor(LogininforEvent logininforEvent) {
        HttpServletRequest request = logininforEvent.getRequest();
        final UserAgent userAgent = UserAgentUtil.parse(request.getHeader("User-Agent"));
        //ip
        final String ip = ServletUtil.getClientIP(request);
        //address
        String address = AddressUtils.getRealAddressByIP(ip);
        StringBuilder s = new StringBuilder();
        s.append(getBlock(ip));
        s.append(address);
        s.append(getBlock(logininforEvent.getUsername()));
        s.append(getBlock(logininforEvent.getStatus()));
        s.append(getBlock(logininforEvent.getMessage()));
        // 打印信息到日志
        log.info(s.toString(), logininforEvent.getArgs());
        // 获取客户端操作系统
        String os = userAgent.getOs().getName();
        // 获取客户端浏览器
        String browser = userAgent.getBrowser().getName();
        // 封装对象
        SysLogininfor logininfor = new SysLogininfor();
        logininfor.setUserName(logininforEvent.getUsername());
        logininfor.setIpaddr(ip);
        logininfor.setLoginLocation(address);
        logininfor.setBrowser(browser);
        logininfor.setOs(os);
        logininfor.setStatus(logininforEvent.getStatus());
        logininfor.setLoginTime(new Date());
        logininfor.setMsg(logininforEvent.getMessage());

        // 插入数据
        insertSysLogininfor(logininfor);
    }


    /*
     * 处理空字段
     * */
    private String getBlock(Object msg) {
        if (msg == null) msg = "";
        return "[" + msg.toString() + "]";
    }
}