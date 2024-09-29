package top.hugo.generator.dto;

import lombok.Data;

@Data
public class TemplateChangeDto {
    private String fileName;
    private String code;
    /**
     * id
     */
    private Integer modelId;

    /**
     * id
     */
    private Integer id;
    /**
     * id
     */
    private Integer fileId;
}
