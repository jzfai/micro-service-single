package top.hugo.admin.service;

import top.hugo.admin.entity.SysDictype;
import top.hugo.admin.query.SysDictypeQuery;
import top.hugo.admin.vo.SysDictypeVo;
import top.hugo.domain.TableDataInfo;

import java.util.List;

/**
 * 字典类型表 服务层处理
 *
 * @author kuanghua
 * @since 2023-11-13 10:58:29
 */
interface ISysDictypeService {


    TableDataInfo<SysDictypeVo> selectPageSysDictypeList();


    /**
     * 查询字典类型表集合
     *
     * @param sysDictype 字典类型表
     * @return 字典类型表集合
     */

    List<SysDictypeVo> selectSysDictypeList(SysDictypeQuery sysDictype);

    /**
     * 查询所有平台
     *
     * @return 平台列表
     */
    List<SysDictypeVo> selectSysDictypeAll();

    /**
     * 通过平台ID查询字典类型表
     *
     * @param sysDictypeId 平台ID
     * @return 角色对象信息
     */

    SysDictype selectSysDictypeById(Long sysDictypeId);


    /**
     * 删除字典类型表
     *
     * @param sysDictypeId 平台ID
     * @return 结果
     */

    int deleteSysDictypeById(Long sysDictypeId);

    /**
     * 批量删除字典类型表
     *
     * @param sysDictypeIds 需要删除的平台ID
     * @return 结果
     */
    int deleteSysDictypeByIds(Long[] sysDictypeIds);

    /**
     * 新增保存字典类型表
     *
     * @param sysDictype 字典类型表
     * @return 结果
     */

    int insertSysDictype(SysDictype sysDictype);


    /**
     * 修改保存字典类型表
     *
     * @param sysDictype 字典类型表
     * @return 结果
     */
    int updateSysDictype(SysDictype sysDictype);

}