package top.hugo.oss.bo;


import lombok.Data;

/**
 * OSS对象存储分页查询对象 sys_oss
 *
 * @author Lion Li
 */
@Data
public class SysOssBo {

    /**
     * 文件名
     */
    private String fileName;
    /**
     * 原名
     */
    private String originalName;
    /**
     * 文件后缀名
     */
    private String fileSuffix;
    /**
     * URL地址
     */
    private String url;
    /**
     * 服务商
     */
    private String service;

}
