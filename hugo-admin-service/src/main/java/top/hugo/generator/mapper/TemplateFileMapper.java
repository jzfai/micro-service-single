package top.hugo.generator.mapper;

import top.hugo.db.mapper.BaseMapperPlus;
import top.hugo.generator.entity.TemplateFile;
import top.hugo.generator.vo.TemplateFileVo;

/**
 * Mapper
 *
 * @author kuanghua
 * @since 2023-10-18 17:23:16
 */
//@InterceptorIgnore(dataPermission = "true")
public interface TemplateFileMapper extends BaseMapperPlus<TemplateFileMapper, TemplateFile, TemplateFileVo> {
    
}
