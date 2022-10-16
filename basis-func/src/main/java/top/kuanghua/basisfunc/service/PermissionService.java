package top.kuanghua.basisfunc.service;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.stereotype.Service;
import top.kuanghua.basisfunc.entity.Permission;
import top.kuanghua.basisfunc.mapper.PermissionMapper;
import top.kuanghua.basisfunc.vo.PermissionVo;
import top.kuanghua.commonpom.utils.ObjSelfUtils;
import top.kuanghua.commonpom.utils.SelfObjUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
*  权限Service
*
* @author 熊猫哥
* @since 2022-10-07 16:33:13
*/
@Service
public class PermissionService {

    @Resource
    private PermissionMapper permissionMapper;

    public Page<PermissionVo> selectPage(Integer pageNum, Integer pageSize, QueryWrapper< Permission > queryWrapper) {
        Page<Permission> permissionPage = this.permissionMapper.selectPage(new Page<Permission>(pageNum, pageSize), queryWrapper);
        List<PermissionVo> arrayList = JSON.parseArray(JSON.toJSONString(permissionPage.getRecords()), PermissionVo.class);
        arrayList.forEach((listItem) -> {
            List<PermissionVo> childrenArr =getPermissionList(listItem.getId());
            listItem.setChildren(childrenArr);
        });
        Page<PermissionVo> newPage = new Page<>();
        newPage.setSize(permissionPage.getSize());
        newPage.setCurrent(permissionPage.getCurrent());
        newPage.setTotal(permissionPage.getTotal());
        newPage.setRecords(arrayList);
        return  newPage;
    }

    /*递归查询children数据*/
    public List<PermissionVo> getPermissionList(Long parentId) {
        QueryWrapper<Permission> queryWrapper = new QueryWrapper<>();
        //权限ID
        queryWrapper.eq("parent_id",parentId);
        List<Permission> parentList = this.permissionMapper.selectList(queryWrapper);
        List<PermissionVo> permissionVoList = parentList.stream().map(mItem -> {
            PermissionVo permissionVo = JSON.parseObject(JSON.toJSONString(mItem), PermissionVo.class);
            if (ObjSelfUtils.isNotEmpty(permissionVo.getParentNode())&&permissionVo.getParentNode()==1) {
                permissionVo.setChildren(getPermissionList(permissionVo.getId()));
            }
            return permissionVo;
        }).collect(Collectors.toList());
        return  permissionVoList;
    }


    public Permission selectById(Long id) {
    return this.permissionMapper.selectById(id);
    }

    public List< Permission > selectBatchIds(List< Long > idList) {
    return this.permissionMapper.selectBatchIds(idList);
    }

    public int insert(Permission permission) {
    return this.permissionMapper.insert(permission);
    }

    public int updateById(Permission permission) {
    return this.permissionMapper.updateById(permission);
    }

    public int deleteById(Long id) {
    return this.permissionMapper.deleteById(id);
    }

    public int deleteBatchIds(List< Long > idList) {
    return this.permissionMapper.deleteBatchIds(idList);
    }
}
