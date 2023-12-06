package top.hugo.admin.service;

import top.hugo.admin.entity.SysDept;
import top.hugo.admin.query.SysDeptQuery;
import top.hugo.admin.vo.SysDeptVo;
import top.hugo.domain.TableDataInfo;

import java.util.List;

/**
 * 部门表 服务层处理
 *
 * @author kuanghua
 * @since 2023-11-20 09:38:20
 */
interface ISysDeptService {


    TableDataInfo<SysDeptVo> selectPageSysDeptList();


    /**
     * 查询部门表集合
     *
     * @param sysDept 部门表
     * @return 部门表集合
     */

    List<SysDeptVo> selectSysDeptList(SysDeptQuery sysDept);

    /**
     * 查询所有平台
     *
     * @return 平台列表
     */
    List<SysDeptVo> selectSysDeptAll();

    /**
     * 通过平台ID查询部门表
     *
     * @param sysDeptId 平台ID
     * @return 角色对象信息
     */

    SysDept selectSysDeptById(Long sysDeptId);


    /**
     * 删除部门表
     *
     * @param sysDeptId 平台ID
     * @return 结果
     */

    int deleteSysDeptById(Long sysDeptId);

    /**
     * 批量删除部门表
     *
     * @param sysDeptIds 需要删除的平台ID
     * @return 结果
     */
    int deleteSysDeptByIds(Long[] sysDeptIds);

    /**
     * 新增保存部门表
     *
     * @param sysDept 部门表
     * @return 结果
     */

    int insertSysDept(SysDept sysDept);


    /**
     * 修改保存部门表
     *
     * @param sysDept 部门表
     * @return 结果
     */
    int updateSysDept(SysDept sysDept);

}