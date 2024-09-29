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


    /**
     * 创建人
     */
    private String createBy;

    /**
     * 更新人
     */
    private String updateBy;


    /**
     * 基础包名
     */
    private String originPackage;

    /**
     * 模块数组
     */
    private String modelList;
    /**
     * 接口定义规则
     */
    private String apiConfig;

    /**
     * 项目类型:1=前端,2基础层,3应用层
     */
    private Integer projectType;


    /**
     * 基础层信息
     */
    private String baseLayerInfo;



    /**
     * 基础层id
     */
    private Integer baseLayerId;
}
