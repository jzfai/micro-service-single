package top.hugo.oss.service;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import top.hugo.common.page.TableDataInfo;
import top.hugo.common.utils.StringUtils;
import top.hugo.oss.entity.SysOss;
import top.hugo.oss.mapper.SysOssMapper;
import top.hugo.oss.query.SysOssQuery;
import top.hugo.oss.vo.SysOssVo;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;

/**
 * OSS对象存储表
 *
 * @author kuanghua
 */
@RequiredArgsConstructor
@Service
public class SysOssService {
    private final SysOssMapper sysOssMapper;
    private final MinioClientService minioClientService;

    /*查询条件抽取*/
    private static LambdaQueryWrapper<SysOss> getQueryWrapper(SysOssQuery sysOssQuery) {
        LambdaQueryWrapper<SysOss> lqw = new LambdaQueryWrapper<>();
        lqw.like(ObjectUtil.isNotEmpty(sysOssQuery.getFileName()), SysOss::getFileName, sysOssQuery.getFileName());
        lqw.like(ObjectUtil.isNotEmpty(sysOssQuery.getFileSuffix()), SysOss::getFileSuffix, sysOssQuery.getFileSuffix());
        lqw.like(ObjectUtil.isNotEmpty(sysOssQuery.getOriginalName()), SysOss::getOriginalName, sysOssQuery.getOriginalName());
        lqw.orderByDesc(SysOss::getCreateTime).orderByDesc(SysOss::getUpdateTime);
        return lqw;
    }

    public TableDataInfo<SysOssVo> selectPageSysOssList(SysOssQuery sysOssQuery) {
        LambdaQueryWrapper<SysOss> lqw = getQueryWrapper(sysOssQuery);
        IPage<SysOssVo> page = sysOssMapper.selectVoPage(sysOssQuery.build(), lqw);
        return TableDataInfo.build(page);
    }

    /**
     * 查询OSS对象存储表分页
     *
     * @return
     */

    public List<SysOssVo> selectSysOssList(SysOssQuery sysOssQuery) {
        LambdaQueryWrapper<SysOss> lqw = getQueryWrapper(sysOssQuery);
        return sysOssMapper.selectVoList(lqw);
    }

    /**
     * 查询所有平台
     *
     * @return 平台列表
     */
    public List<SysOssVo> selectSysOssAll() {
        return sysOssMapper.selectVoList();
    }

    /**
     * 通过OSS对象存储表ID查询
     *
     * @param sysOssId OSS对象存储表ID
     * @return 角色对象信息
     */

    public SysOssVo selectSysOssById(Long sysOssId) {
        return sysOssMapper.selectVoById(sysOssId);
    }


    /**
     * 删除OSS对象存储表
     *
     * @param sysOssId 平台ID
     * @return 结果
     */

    public int deleteSysOssById(Long sysOssId) {
        SysOssVo sysOssVo = sysOssMapper.selectVoById(sysOssId);
        minioClientService.removeObject("public", sysOssVo.getOriginalName());

        return sysOssMapper.deleteById(sysOssId);
    }

    /**
     * 批量删除OSS对象存储表
     *
     * @param sysOssIds 需要删除的平台ID
     * @return 结果
     */
    public int deleteSysOssByIds(Collection<Long> sysOssIds) {
        List<SysOssVo> sysOssVos = sysOssMapper.selectVoBatchIds(sysOssIds);
        sysOssVos.forEach(f -> {
            minioClientService.removeObject("public", f.getOriginalName());
        });
        return sysOssMapper.deleteBatchIds(sysOssIds);
    }

    /**
     * 新增保存OSS对象存储表
     *
     * @return 结果
     */

    public HashMap<String, Object> insertSysOss(MultipartFile file) {
        String originalFileName = file.getOriginalFilename();
        String suffix = StringUtils.substring(originalFileName, originalFileName.lastIndexOf("."), originalFileName.length());
        //上传文件
        HashMap<String, Object> uploadResult = minioClientService.putObject(file);
        // 保存文件信息
        SysOss oss = new SysOss();
        oss.setUrl(uploadResult.get("url").toString());
        oss.setFileSuffix(suffix);
        oss.setFileName(uploadResult.get("fileName").toString());
        oss.setFileSuffix(suffix);
        oss.setOriginalName(originalFileName);
        sysOssMapper.insert(oss);

        return uploadResult;
    }


}