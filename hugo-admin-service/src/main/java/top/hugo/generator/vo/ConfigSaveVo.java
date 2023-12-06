package top.hugo.generator.vo;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 代码生成配置保存返回实体类
 *
 * @author kuanghua
 * @since 2023-10-18 11:42:26
 */
@Data
public class ConfigSaveVo implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 配置数据
     */
    @ExcelProperty(value = "配置数据")
    private String generatorConfig;
    /**
     * 配置名称
     */
    @ExcelProperty(value = "配置名称")
    private String name;
}
