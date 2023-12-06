package top.hugo.monitor.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.Date;

/**
 * 系统访问记录请求接收类
 *
 * @author kuanghua
 * @since 2023-11-17 10:02:07
 */
@Data
public class SysLogininforDto {
    /**
     * 浏览器类型
     */
    private String browser;
    /**
     * 访问ID
     */
    private Long infoId;
    /**
     * 登录IP地址
     */
    @NotBlank(message = "ipaddr不能为空")
    private String ipaddr;
    /**
     * 登录地点
     */
    private String loginLocation;
    /**
     * 访问时间
     */
    private Date loginTime;
    /**
     * 提示消息
     */
    private String msg;
    /**
     * 操作系统
     */
    private String os;
    /**
     * 登录状态（0成功 1失败）
     */
    private String status;
    /**
     * 用户账号
     */
    @NotBlank(message = "userName不能为空")
    private String userName;
}