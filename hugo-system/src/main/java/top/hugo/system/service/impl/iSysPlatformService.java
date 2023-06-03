package top.hugo.system.service.impl;


import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import top.hugo.common.helper.LoginHelper;
import top.hugo.common.page.TableDataInfo;
import top.hugo.common.utils.JsonUtils;
import top.hugo.system.entity.SysPlatform;
import top.hugo.system.entity.SysRole;
import top.hugo.system.mapper.SysPlatformMapper;
import top.hugo.system.query.SysPlatformQuery;
import top.hugo.system.service.SysRoleService;
import top.hugo.system.vo.SysPlatformVo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

public interface iSysPlatformService {
     TableDataInfo<SysPlatformVo> selectPagePlatformList(SysPlatformQuery platform) ;


    /**
     * 查询平台信息集合
     *
     * @param platform 平台信息
     * @return 平台信息集合
     */

    public List<SysPlatformVo> selectPlatformList(SysPlatformQuery platform);
    /**
     * 查询所有平台
     *
     * @return 平台列表
     */
    public List<SysPlatformVo> selectPlatformAll();

    /**
     * 通过平台ID查询平台信息
     *
     * @param platformId 平台ID
     * @return 角色对象信息
     */

    public SysPlatform selectPlatformById(Long platformId);


    /**
     * 删除平台信息
     *
     * @param platformId 平台ID
     * @return 结果
     */

    public int deletePlatformById(Long platformId);

    /**
     * 批量删除平台信息
     *
     * @param platformIds 需要删除的平台ID
     * @return 结果
     */
    public int deletePlatformByIds(Long[] platformIds);

    /**
     * 新增保存平台信息
     *
     * @param platform 平台信息
     * @return 结果
     */

    public int insertPlatform(SysPlatform platform);


    /**
     * 修改保存平台信息
     *
     * @param platform 平台信息
     * @return 结果
     */

    public int updatePlatform(SysPlatform platform);

}
