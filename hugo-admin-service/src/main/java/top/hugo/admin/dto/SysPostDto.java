package top.hugo.admin.dto;

import lombok.Data;

import java.util.Date;

/**
 * 岗位信息表请求接收类
 *
 * @author kuanghua
 * @since 2023-11-20 09:39:53
 */
@Data
public class SysPostDto {
    /**
     * 创建者
     */
    private String createBy;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 岗位编码
     */
    private String postCode;
    /**
     * 岗位ID
     */
    private Long postId;
    /**
     * 岗位名称
     */
    private String postName;
    /**
     * 显示顺序
     */
    private Integer postSort;
    /**
     * 备注
     */
    private String remark;
    /**
     * 状态（0正常 1停用）
     */
    private String status;
    /**
     * 更新者
     */
    private String updateBy;
    /**
     * 更新时间
     */
    private Date updateTime;
}