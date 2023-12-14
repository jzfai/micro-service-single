package top.hugo.oss.query;


import lombok.Data;
import top.hugo.domain.PageAndTimeRangeQuery;

/**
 * OSS对象存储表请求接收类
 *
 * @author kuanghua
 * @since 2023-09-06 11:14:58
 */
@Data
public class SysOssQuery extends PageAndTimeRangeQuery {
    /**
     * 文件名
     */
    private String fileName;
    /**
     * 文件后缀名
     */
    private String fileSuffix;
    /**
     * 原名
     */
    private String originalName;


}
