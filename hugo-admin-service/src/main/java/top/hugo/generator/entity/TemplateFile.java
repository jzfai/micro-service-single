package top.hugo.generator.entity;

import top.hugo.admin.domain.BaseEntity;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 实体类
 *
 * @author kuanghua
 * @since 2023-10-18 17:23:16
 */
@Data
@TableName(value = "t_template_file")
public class TemplateFile extends BaseEntity {
    @TableLogic
    //逻辑删除:0=未删除,2=已删除
    private Integer deleted;
    //文件数组
    private String fileArr;
    //主键id
    @TableId(type = IdType.AUTO)
    private Integer id;

    //文件存储名
    private String name;

    //基础包名
    private String originPackage;


    //模块数组
    private String modelList;

    //接口定义规则
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
