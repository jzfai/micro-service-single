package top.hugo.generator.vo;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 返回实体类
 *
 * @author kuanghua
 * @since 2023-10-18 17:23:16
 */
@Data
public class TemplateFileVo implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 创建时间
     */
    @ExcelProperty(value = "创建时间")
    private Date createTime;
    /**
     * 逻辑删除
     */
    @ExcelProperty(value = "逻辑删除")
    private Integer deleted;
    /**
     * 文件数组
     */
    @ExcelProperty(value = "文件数组")
    private String fileArr;
    /**
     * 主键id
     */
    @ExcelProperty(value = "主键id")
    private Integer id;
    /**
     * 文件存储名
     */
    @ExcelProperty(value = "文件存储名")
    private String name;
}
