package top.hugo.generator.dto;

import lombok.Data;
import top.hugo.generator.entity.TemplateFile;

@Data
public class TemplateChangeDto extends TemplateFile {
    private String fileName;
    private String code;
}
