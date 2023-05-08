package top.hugo.system.service;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.tree.Tree;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import top.hugo.common.utils.TreeBuildUtils;
import top.hugo.system.entity.SysDept;
import top.hugo.system.entity.SysRole;
import top.hugo.system.mapper.SysDeptMapper;
import top.hugo.system.mapper.SysRoleMapper;

import java.util.List;

/**
 * 部门管理 服务实现
 *
 * @author hugo
 */
@RequiredArgsConstructor
@Service
public class SysDeptService {
    private final SysDeptMapper sysDeptMapper;
    private final SysRoleMapper roleMapper;

    /**
     * 查询部门树结构信息
     *
     * @param dept 部门信息
     * @return 部门树信息集合
     */
    public List<Tree<Long>> selectDeptTreeList(SysDept dept) {
        List<SysDept> depts = this.selectDeptList(dept);
        return buildDeptTreeSelect(depts);
    }


    /**
     * 查询部门管理数据
     *
     * @param dept 部门信息
     * @return 部门信息集合
     */
    public List<SysDept> selectDeptList(SysDept dept) {
        LambdaQueryWrapper<SysDept> lqw = new LambdaQueryWrapper<>();
        lqw.eq(SysDept::getDelFlag, "0")
                .eq(ObjectUtil.isNotEmpty(dept.getDeptId()), SysDept::getDeptId, dept.getDeptId())
                .eq(ObjectUtil.isNotEmpty(dept.getParentId()), SysDept::getParentId, dept.getParentId())
                .like(ObjectUtil.isNotEmpty(dept.getDeptName()), SysDept::getDeptName, dept.getDeptName())
                .eq(ObjectUtil.isNotEmpty(dept.getStatus()), SysDept::getStatus, dept.getStatus())
                .orderByAsc(SysDept::getParentId)
                .orderByAsc(SysDept::getOrderNum);
        return sysDeptMapper.selectDeptList(lqw);
    }

    /**
     * 构建前端所需要下拉树结构
     *
     * @param depts 部门列表
     * @return 下拉树结构列表
     */
    public List<Tree<Long>> buildDeptTreeSelect(List<SysDept> depts) {
        if (CollUtil.isEmpty(depts)) {
            return CollUtil.newArrayList();
        }
        return TreeBuildUtils.build(depts, (dept, tree) ->
                tree.setId(dept.getDeptId())
                        .setParentId(dept.getParentId())
                        .setName(dept.getDeptName())
                        .setWeight(dept.getOrderNum()));
    }


    public List<Long> selectDeptListByRoleId(Long roleId) {
        SysRole role = roleMapper.selectById(roleId);
        return sysDeptMapper.selectDeptListByRoleId(roleId, role.getDeptCheckStrictly());
    }
}
