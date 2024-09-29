package top.hugo.admin.vo;

import lombok.Data;

import java.util.Date;

/**
 * 操作日志记录返回实体类
 *
 * @author kuanghua
 * @since 2023-11-21 13:58:50
 */
@Data
public class SysOperLogVo {
    /**
     * 业务类型（0其它 1新增 2修改 3删除）
     */
    private Integer businessType;
    /**
     * 部门名称
     */
    private String deptName;
    /**
     * 错误消息
     */
    private String errorMsg;
    /**
     * 返回参数
     */
    private String jsonResult;
    /**
     * 方法名称
     */
    private String method;
    /**
     * 日志主键
     */
    private Long operId;
    /**
     * 主机地址
     */
    private String operIp;
    /**
     * 操作地点
     */
    private String operLocation;
    /**
     * 操作人员
     */
    private String operName;
    /**
     * 请求参数
     */
    private String operParam;


    /**
     * 关闭时间
     */
    private Long costTime;
    /**
     * 操作时间
     */
    private Date operTime;

    /**
     * 请求URL
     */
    private String operUrl;
    /**
     * 操作类别（0其它 1后台用户 2手机端用户）
     */
    private Integer operatorType;
    /**
     * 请求方式
     */
    private String requestMethod;
    /**
     * 操作状态（0正常 1异常）
     */
    private Integer status;
    /**
     * 模块标题
     */
    private String title;
}
