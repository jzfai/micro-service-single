package top.hugo.admin.controller;


import top.hugo.admin.dto.SysPostDto;
import top.hugo.admin.entity.SysPost;
import top.hugo.admin.query.SysPostQuery;
import top.hugo.admin.service.SysPostService;
import top.hugo.admin.vo.SysPostVo;
import top.hugo.common.domain.R;
import top.hugo.common.utils.BeanCopyUtils;
import top.hugo.domain.TableDataInfo;
import top.hugo.easyexecl.utils.EasyExcelUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 岗位信息表信息操作处理
 *
 * @author kuanghua|
 * @since 2023-11-20 09:39:53
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/system/post")
public class SysPostController {
    private final SysPostService sysPostService;

    /**
     * 获取sysPost列表
     *
     * @return
     */
    //@SaCheckPermission("system:sysPost:list")
    @PostMapping("/list")
    public TableDataInfo<SysPostVo> list(@RequestBody @Validated SysPostQuery sysPost) {
        return sysPostService.selectPageSysPostList(sysPost);
    }

    /**
     * 导出sysPost列表
     */
//@Log(title = "sysPost管理", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(SysPostQuery sysPost, HttpServletResponse response) {
        List<SysPostVo> list = sysPostService.selectSysPostList(sysPost);
        EasyExcelUtils.exportExcel(list, "sysPost数据", SysPostVo.class, response);
    }

    /**
     * 根据sysPost编号获取详细信息
     *
     * @param sysPostId sysPostID
     */

    @GetMapping(value = "/{sysPostId}")
    public R<SysPost> getInfo(@PathVariable Long sysPostId) {
        return R.ok(sysPostService.selectSysPostById(sysPostId));
    }

    /**
     * 新增sysPost
     */
    @PostMapping
    public R<Void> add(@Validated @RequestBody SysPostDto sysPostDto) {
        SysPost sysPost = BeanCopyUtils.copy(sysPostDto, SysPost.class);
        return R.result(sysPostService.insertSysPost(sysPost));
    }

    /**
     * 修改sysPost
     */
    @PutMapping
    public R<Void> edit(@Validated @RequestBody SysPostDto sysPostDto) {
        SysPost sysPost = BeanCopyUtils.copy(sysPostDto, SysPost.class);
        return R.result(sysPostService.updateSysPost(sysPost));
    }

    /**
     * 删除sysPost
     *
     * @param sysPostIds sysPostID串
     */
//@Log(title = "sysPost管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{sysPostIds}")
    public R<Void> remove(@PathVariable Long[] sysPostIds) {
        return R.result(sysPostService.deleteSysPostByIds(sysPostIds));
    }

    /**
     * 获取sysPost选择框列表
     */
    @GetMapping("/selectSysPostAll")
    public R<List<SysPostVo>> selectSysPostAll() {
        List<SysPostVo> sysPosts = sysPostService.selectSysPostAll();
        return R.ok(sysPosts);
    }
}
