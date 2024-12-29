package top.hugo.generator.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.select.Select;
import net.sf.jsqlparser.statement.select.SelectBody;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
@Slf4j
public class SqlParseService {
    public SelectBody parseSql(String parseSql){
        SelectBody selectBody = null;
        try {
            Statement statement = CCJSqlParserUtil.parse(parseSql);
            Select selectStatement = (Select) statement;
            selectBody = selectStatement.getSelectBody();
        } catch (JSQLParserException e) {
            throw new RuntimeException(e);
        }
        return selectBody;
    }
}
