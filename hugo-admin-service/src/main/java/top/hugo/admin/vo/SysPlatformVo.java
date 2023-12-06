package top.hugo.admin.vo;

import lombok.Data;

import java.util.Date;

/**
 * 平台返回实体类
 *
 * @author kuanghua
 * @since 2023-11-10 11:54:52
 */
@Data
public class SysPlatformVo {
    /**
     * 主键
     */
    private Integer id;
    /**
     * 平台的名字
     */
    private String name;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 更新时间
     */
    private Date updateTime;
    /**
     * 创建人
     */
    private String createBy;
    /**
     * 更新人
     */
    private String updateBy;
}
