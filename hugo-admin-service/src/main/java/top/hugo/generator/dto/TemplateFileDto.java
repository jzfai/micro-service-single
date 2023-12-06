package top.hugo.generator.dto;


import lombok.Data;

/**
 * 请求接收类
 *
 * @author kuanghua
 * @since 2023-10-18 17:23:16
 */
@Data
public class TemplateFileDto {
    /**
     * 文件数组
     */
    private String fileArr;
    /**
     * 文件存储名
     */
    private String name;
}