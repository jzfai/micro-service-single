package top.hugo.system.service;


import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import top.hugo.common.constant.UserConstants;
import top.hugo.common.domain.PageQuery;
import top.hugo.common.exception.ServiceException;
import top.hugo.common.page.TableDataInfo;
import top.hugo.system.entity.SysPost;
import top.hugo.system.entity.SysUserPost;
import top.hugo.system.mapper.SysPostMapper;
import top.hugo.system.mapper.SysUserPostMapper;

import java.util.Arrays;
import java.util.List;

/**
 * 岗位信息 服务层处理
 *
 * @author kuanghua
 */
@RequiredArgsConstructor
@Service
public class SysPostService {

    private final SysPostMapper baseMapper;
    private final SysUserPostMapper userPostMapper;

    public TableDataInfo<SysPost> selectPagePostList(SysPost post, PageQuery pageQuery) {
        LambdaQueryWrapper<SysPost> lqw = new LambdaQueryWrapper<SysPost>()
                .like(ObjectUtil.isNotEmpty(post.getPostCode()), SysPost::getPostCode, post.getPostCode())
                .eq(ObjectUtil.isNotEmpty(post.getStatus()), SysPost::getStatus, post.getStatus())
                .like(ObjectUtil.isNotEmpty(post.getPostName()), SysPost::getPostName, post.getPostName());
        Page<SysPost> page = baseMapper.selectPage(pageQuery.build(), lqw);
        return TableDataInfo.build(page);
    }

    /**
     * 查询岗位信息集合
     *
     * @param post 岗位信息
     * @return 岗位信息集合
     */

    public List<SysPost> selectPostList(SysPost post) {
        return baseMapper.selectList(new LambdaQueryWrapper<SysPost>()
                .like(ObjectUtil.isNotEmpty(post.getPostCode()), SysPost::getPostCode, post.getPostCode())
                .eq(ObjectUtil.isNotEmpty(post.getStatus()), SysPost::getStatus, post.getStatus())
                .like(ObjectUtil.isNotEmpty(post.getPostName()), SysPost::getPostName, post.getPostName()));
    }

    /**
     * 查询所有岗位
     *
     * @return 岗位列表
     */

    public List<SysPost> selectPostAll() {
        return baseMapper.selectList();
    }

    /**
     * 通过岗位ID查询岗位信息
     *
     * @param postId 岗位ID
     * @return 角色对象信息
     */

    public SysPost selectPostById(Long postId) {
        return baseMapper.selectById(postId);
    }

    /**
     * 根据用户ID获取岗位选择框列表
     *
     * @param userId 用户ID
     * @return 选中岗位ID列表
     */

    public List<Long> selectPostListByUserId(Long userId) {
        return baseMapper.selectPostListByUserId(userId);
    }

    /**
     * 校验岗位名称是否唯一
     *
     * @param post 岗位信息
     * @return 结果
     */

    public String checkPostNameUnique(SysPost post) {
        boolean exist = baseMapper.exists(new LambdaQueryWrapper<SysPost>()
                .eq(SysPost::getPostName, post.getPostName())
                .ne(ObjectUtil.isNotNull(post.getPostId()), SysPost::getPostId, post.getPostId()));
        if (exist) {
            return UserConstants.NOT_UNIQUE;
        }
        return UserConstants.UNIQUE;
    }

    /**
     * 校验岗位编码是否唯一
     *
     * @param post 岗位信息
     * @return 结果
     */

    public String checkPostCodeUnique(SysPost post) {
        boolean exist = baseMapper.exists(new LambdaQueryWrapper<SysPost>()
                .eq(SysPost::getPostCode, post.getPostCode())
                .ne(ObjectUtil.isNotNull(post.getPostId()), SysPost::getPostId, post.getPostId()));
        if (exist) {
            return UserConstants.NOT_UNIQUE;
        }
        return UserConstants.UNIQUE;
    }

    /**
     * 通过岗位ID查询岗位使用数量
     *
     * @param postId 岗位ID
     * @return 结果
     */

    public long countUserPostById(Long postId) {
        return userPostMapper.selectCount(new LambdaQueryWrapper<SysUserPost>().eq(SysUserPost::getPostId, postId));
    }

    /**
     * 删除岗位信息
     *
     * @param postId 岗位ID
     * @return 结果
     */

    public int deletePostById(Long postId) {
        return baseMapper.deleteById(postId);
    }

    /**
     * 批量删除岗位信息
     *
     * @param postIds 需要删除的岗位ID
     * @return 结果
     */

    public int deletePostByIds(Long[] postIds) {
        for (Long postId : postIds) {
            SysPost post = selectPostById(postId);
            if (countUserPostById(postId) > 0) {
                throw new ServiceException(String.format("%1$s已分配,不能删除", post.getPostName()));
            }
        }
        return baseMapper.deleteBatchIds(Arrays.asList(postIds));
    }

    /**
     * 新增保存岗位信息
     *
     * @param post 岗位信息
     * @return 结果
     */

    public int insertPost(SysPost post) {
        return baseMapper.insert(post);
    }

    /**
     * 修改保存岗位信息
     *
     * @param post 岗位信息
     * @return 结果
     */

    public int updatePost(SysPost post) {
        return baseMapper.updateById(post);
    }
}
