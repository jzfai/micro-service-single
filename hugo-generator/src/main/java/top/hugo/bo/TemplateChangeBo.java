package top.hugo.bo;

import lombok.Data;
import top.hugo.entity.TemplateFile;

@Data
public class TemplateChangeBo extends TemplateFile {
    private String fileName;
    private String code;
}
