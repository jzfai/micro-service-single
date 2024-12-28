package top.hugo.generator.dto;


import lombok.Data;

/**
 * 请求接收类
 *
 * @author kuanghua
 * @since 2023-10-18 17:23:16
 */
@Data
public class TemplateFileDto {
    /**
     * 文件数组
     */
    private String fileArr;
    /**
     * 文件存储名
     */
    private String name;
    /**
     * id
     */
    private Integer id;

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