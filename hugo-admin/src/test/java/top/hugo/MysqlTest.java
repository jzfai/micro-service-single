package top.hugo;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import top.hugo.common.utils.JdbcUtils;
import top.hugo.common.utils.StringUtils;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@DisplayName("sql测试")
public class MysqlTest {
    HashMap<String, String> hashMap = new HashMap<>();

    @Test
    public void TestQuery() {

        Connection conn = null;
        Statement sta = null;
        ResultSet rs = null;
        try {
            String url = "jdbc:mysql://159.75.144.202:3310?useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&useSSL=true&serverTimezone=GMT%2B8&autoReconnect=true&rewriteBatchedStatements=true";
            String user = "root";
            String password = "@Root123";
            conn = JdbcUtils.getConnection(url, user, password);
            sta = conn.createStatement();
            String sql = "select table_schema, table_name, table_comment, create_time, update_time from information_schema.tables where TABLE_SCHEMA = 'micro-service-single'";
            rs = sta.executeQuery(sql);
            ArrayList<Map> maps = new ArrayList<Map>();
            while (rs.next()) {
                HashMap<String, Object> hashMap = new HashMap<>();
                hashMap.put("tableSchema", rs.getObject("table_schema"));
                hashMap.put("tableName", rs.getObject("table_name"));
                hashMap.put("tableComment", rs.getObject("table_comment"));
                hashMap.put("createTime", rs.getObject("create_time"));
                hashMap.put("updateTime", rs.getObject("update_time"));
                maps.add(hashMap);
            }
            System.out.println(maps);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JdbcUtils.releaseConnection(conn, sta, rs);
        }
    }


    @Test
    public void TestQu() {
        System.out.println(test("micro-service-single"));
    }

    public String test(String name) {
        String[] split = (name.replaceAll("，", ",")).split(",");
        return "'" + StringUtils.join(split, "','") + "'";
    }
}