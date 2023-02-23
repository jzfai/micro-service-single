package top.hugo.vo;

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
    private Integer id;
    private String name;
    private String fileArr;
    private Date createTime;
    private Date updateTime;
    private String creator;
}
