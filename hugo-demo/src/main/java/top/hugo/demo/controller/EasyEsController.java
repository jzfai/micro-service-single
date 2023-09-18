package top.hugo.demo.controller;

import cn.easyes.core.conditions.LambdaEsQueryWrapper;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import top.hugo.es.esentity.TestDocument;
import top.hugo.es.esmapper.DocumentMapper;

import java.util.List;


/**
 * EasyEs 相关
 */
@RestController
@RequiredArgsConstructor
public class EasyEsController {

    private final DocumentMapper documentMapper;

    /**
     * 插入
     */
    @GetMapping("/insert")
    public Integer insert() {
        // 初始化-> 新增数据
        TestDocument document = new TestDocument();
        document.setTitle("老汉");
        document.setContent("推*技术过硬");
        return documentMapper.insert(document);
    }

    /**
     * 查询
     */
    @GetMapping("/search")
    public List<TestDocument> search() {
        // 查询出所有标题为老汉的文档列表
        LambdaEsQueryWrapper<TestDocument> wrapper = new LambdaEsQueryWrapper<>();
        wrapper.eq(TestDocument::getTitle, "老汉");
        return documentMapper.selectList(wrapper);
    }
}