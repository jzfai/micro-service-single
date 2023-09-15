package top.hugo.es.esentity;

import cn.easyes.annotation.IndexField;
import cn.easyes.annotation.IndexId;
import cn.easyes.annotation.rely.Analyzer;
import cn.easyes.annotation.rely.FieldType;
import cn.easyes.annotation.rely.IdType;
import lombok.Data;

@Data
public class TestDocument {
    /**
     * es中的唯一id,当您字段命名为id且类型为String时，且不需要采用UUID及自定义ID类型时，可省略此注解
     */
    @IndexId(type = IdType.NONE)
    private String id;
    /**
     * 文档标题,不指定类型默认被创建为keyword类型,可进行精确查询
     */
    private String title;
    /**
     * 文档内容,指定了类型及存储/查询分词器
     */
    @IndexField(fieldType = FieldType.TEXT, analyzer = Analyzer.IK_SMART, searchAnalyzer = Analyzer.IK_MAX_WORD)
    private String content;
}