package top.hugo.generator.controller;


import top.hugo.common.domain.R;
import top.hugo.generator.service.SqlParseService;
import lombok.RequiredArgsConstructor;
import net.sf.jsqlparser.statement.select.SelectBody;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 解析sql
 *
 * @author kuanghua
 * @since 2023-10-18 17:23:16
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/sqlParse")
public class SqlParseController {
    private final SqlParseService sqlParseService;
    /**
     * 解析parseSql
     * @param sql 传入的sql语句
     */
    @PostMapping ("parseSql")
    public R<SelectBody> parseSql(String sql){
        SelectBody selectBody = sqlParseService.parseSql(sql);
        return R.ok(selectBody);
    }
}
