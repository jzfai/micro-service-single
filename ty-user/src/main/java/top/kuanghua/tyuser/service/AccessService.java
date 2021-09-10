package top.kuanghua.tyuser.service;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.stereotype.Service;
import top.kuanghua.tyuser.entity.Access;
import top.kuanghua.tyuser.mapper.AccessMapper;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author kuanghua
 * @since 2020-10-27 20:54:24
 */
@Service
public class AccessService {

    @Resource
    private AccessMapper accessMapper;

    public Page selectPage(Integer pageNum, Integer pageSize,
                           Access access) {
        QueryWrapper<Access> queryWrapper = new QueryWrapper<>(access);
        return this.accessMapper.selectPage(new Page<Access>(pageNum, pageSize), queryWrapper);
    }

    public int insert(Access access) {
        return this.accessMapper.insert(access);
    }

    public int updateById(Access access) {
        return this.accessMapper.updateById(access);
    }
    /*查询权限树*/
    public List<Map> selectAccessTree(){
        /*根据id查询parent_id为0的父元素在查询下面的子元素*/
        List<Access> accessList = this.accessMapper.selectList(new QueryWrapper<Access>().eq("parent_id", 0));
        List<Map> listStreamMap = accessList.stream().map(mItem -> {
            Map coversMap = JSON.parseObject(JSON.toJSONString(mItem), Map.class);
            List<Access> selectList = this.accessMapper.selectList(new QueryWrapper<Access>().eq("parent_id", mItem.getId()));
            coversMap.put("children",selectList);
            return coversMap;
        }).collect(Collectors.toList());
        return listStreamMap;
    }
}