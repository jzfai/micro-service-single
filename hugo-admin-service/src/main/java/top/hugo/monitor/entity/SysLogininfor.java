package top.hugo.monitor.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * 系统访问记录实体类
 *
 * @author kuanghua
 * @since 2023-11-17 10:02:07
 */
@Data
@TableName(value = "sys_logininfor")
public class SysLogininfor {
    //浏览器类型
    private String browser;

    //访问ID
    @TableId(type = IdType.ASSIGN_ID)
    private Long infoId;
    //登录IP地址
    private String ipaddr;
    //登录地点
    private String loginLocation;
    //访问时间
    private Date loginTime;
    //提示消息
    private String msg;
    //操作系统
    private String os;
    //登录状态（0成功 1失败）
    private String status;
    //用户账号
    private String userName;

}
