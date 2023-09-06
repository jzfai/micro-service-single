


package top.hugo.oss.vo;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
/**
*  OSS对象存储表返回实体类
*
* @author kuanghua
* @since 2023-09-06 11:14:58
*/
@Data
public class SysOssVo implements Serializable {
private static final long serialVersionUID = 1L;
  /**
   * 上传人
   */
  @ExcelProperty(value = "上传人")
  private String createBy;
  /**
   * 创建时间
   */
  @ExcelProperty(value = "创建时间")
  private Date createTime;
  /**
   * 文件名
   */
  @ExcelProperty(value = "文件名")
  private String fileName;
  /**
   * 文件后缀名
   */
  @ExcelProperty(value = "文件后缀名")
  private String fileSuffix;
  /**
   * 原名
   */
  @ExcelProperty(value = "原名")
  private String originalName;
  /**
   * 对象存储主键
   */
  @ExcelProperty(value = "对象存储主键")
  private Long ossId;
  /**
   * 服务商
   */
  @ExcelProperty(value = "服务商")
  private String service;
  /**
   * 更新人
   */
  @ExcelProperty(value = "更新人")
  private String updateBy;
  /**
   * 更新时间
   */
  @ExcelProperty(value = "更新时间")
  private Date updateTime;
  /**
   * URL地址
   */
  @ExcelProperty(value = "URL地址")
  private String url;
}
