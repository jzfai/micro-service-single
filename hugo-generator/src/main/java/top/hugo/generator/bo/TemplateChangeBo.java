package top.hugo.generator.bo;

import lombok.Data;
import top.hugo.generator.entity.TemplateFile;

@Data
public class TemplateChangeBo extends TemplateFile {
    private String fileName;
    private String code;
}
