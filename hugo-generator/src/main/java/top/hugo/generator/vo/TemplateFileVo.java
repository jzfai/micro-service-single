package top.hugo.generator.vo;

import lombok.Data;

import java.util.Date;

/**
 * 实体类
 *
 * @author 邝华
 * @since 2022-12-07 13:51:06
 */
@Data
public class TemplateFileVo {
    /*
     * 模板id
     * */
    private Integer id;
    /*
     * 模板名
     * */
    private String name;
    /*
     * 文件名数组
     * */
    private String fileArr;
    /*
     * 创建时间
     * */
    private Date createTime;
    /*
     * 更新时间
     * */
    private Date updateTime;
}
