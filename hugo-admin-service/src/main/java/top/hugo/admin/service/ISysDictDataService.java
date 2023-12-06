package top.hugo.admin.service;

import top.hugo.admin.entity.SysDictData;
import top.hugo.admin.query.SysDictDataQuery;
import top.hugo.admin.vo.SysDictDataVo;
import top.hugo.domain.TableDataInfo;

import java.util.List;

/**
 * 字典数据表 服务层处理
 *
 * @author kuanghua
 * @since 2023-11-13 15:52:45
 */
interface ISysDictDataService {


    TableDataInfo<SysDictDataVo> selectPageSysDictDataList();


    /**
     * 查询字典数据表集合
     *
     * @param sysDictData 字典数据表
     * @return 字典数据表集合
     */

    List<SysDictDataVo> selectSysDictDataList(SysDictDataQuery sysDictData);

    /**
     * 查询所有平台
     *
     * @return 平台列表
     */
    List<SysDictDataVo> selectSysDictDataAll();

    /**
     * 通过平台ID查询字典数据表
     *
     * @param sysDictDataId 平台ID
     * @return 角色对象信息
     */

    SysDictData selectSysDictDataById(Long sysDictDataId);


    /**
     * 删除字典数据表
     *
     * @param sysDictDataId 平台ID
     * @return 结果
     */

    int deleteSysDictDataById(Long sysDictDataId);

    /**
     * 批量删除字典数据表
     *
     * @param sysDictDataIds 需要删除的平台ID
     * @return 结果
     */
    int deleteSysDictDataByIds(Long[] sysDictDataIds);

    /**
     * 新增保存字典数据表
     *
     * @param sysDictData 字典数据表
     * @return 结果
     */

    int insertSysDictData(SysDictData sysDictData);


    /**
     * 修改保存字典数据表
     *
     * @param sysDictData 字典数据表
     * @return 结果
     */
    int updateSysDictData(SysDictData sysDictData);

}