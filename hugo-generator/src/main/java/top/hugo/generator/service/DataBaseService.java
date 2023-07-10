package top.hugo.generator.service;

import org.springframework.stereotype.Service;
import top.hugo.common.exception.ServiceException;
import top.hugo.common.utils.JdbcUtils;
import top.hugo.common.utils.StringUtils;
import top.hugo.generator.entity.DataBaseInfo;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * @Title: DataBaseService
 * @Description:
 * @Auther: kuanghua
 * @create 2022/6/3 8:50
 * https://blog.csdn.net/qq_39323945/article/details/124661309
 */
@Service
public class DataBaseService {
//    @Resource
//    private DataBaseMapper dataBaseMapper;

//    /**
//     * @param dbName
//     */
//    public ArrayList<Map> getAllTableFromDb(String dbName) {
//        ArrayList<Map> allTableFromDb = this.dataBaseMapper.getAllTableFromDb(dbName);
//        return allTableFromDb;
//    }
//
//    /**
//     * @param dbName
//     * @param tbName
//     */
//    public ArrayList<Map> getAllColumnFromTb(String dbName, String tbName) {
//        ArrayList<Map> allColumnFromTb = this.dataBaseMapper.getAllColumnFromTb(dbName, tbName);
//        return allColumnFromTb;
//    }

    /*
     * 转换 "" to ''
     * */
    public String changeStringToQue(String name) {
        String[] split = (name.replaceAll("，", ",")).split(",");
        return "'" + StringUtils.join(split, "','") + "'";
    }

    /**
     * 获取库数据
     */
    public ArrayList<Map> getAllTableFromDb(DataBaseInfo dataBaseInfo) {
        Connection conn = null;
        Statement sta = null;
        ResultSet rs = null;
        ArrayList<Map> maps = new ArrayList<Map>();
        String url = "jdbc:mysql://" + dataBaseInfo.getUrl() + "?useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&useSSL=true&serverTimezone=GMT%2B8&autoReconnect=true&rewriteBatchedStatements=true";
        try {
            conn = JdbcUtils.getConnection(url, dataBaseInfo.getName(), dataBaseInfo.getPassword());
            sta = conn.createStatement();
            String sql = "select table_schema, table_name, table_comment, create_time, update_time " +
                    "from information_schema.tables where TABLE_SCHEMA = " + changeStringToQue(dataBaseInfo.getDbName());
            rs = sta.executeQuery(sql);

            while (rs.next()) {
                HashMap<String, Object> hashMap = new HashMap<>();
                hashMap.put("tableSchema", rs.getObject("table_schema"));
                hashMap.put("tableName", rs.getObject("table_name"));
                hashMap.put("tableComment", rs.getObject("table_comment"));
                hashMap.put("createTime", rs.getObject("create_time"));
                hashMap.put("updateTime", rs.getObject("update_time"));
                maps.add(hashMap);
            }
        } catch (SQLException e) {
            throw new ServiceException("数据库连接错误" + "" + e.getMessage());
        } finally {
            JdbcUtils.releaseConnection(conn, sta, rs);
        }
        return maps;
    }

    /**
     * 获取表数据
     */
    public ArrayList<Map> getAllColumnFromTb(DataBaseInfo dataBaseInfo) {

        Connection conn = null;
        Statement sta = null;
        ResultSet rs = null;
        ArrayList<Map> maps = new ArrayList<Map>();
        String url = "jdbc:mysql://" + dataBaseInfo.getUrl() + "?useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&useSSL=true&serverTimezone=GMT%2B8&autoReconnect=true&rewriteBatchedStatements=true";
        try {
            conn = JdbcUtils.getConnection(url, dataBaseInfo.getName(), dataBaseInfo.getPassword());
            sta = conn.createStatement();
            String sql = "select table_schema, table_name, column_name, is_nullable, data_type, column_type, column_key, extra, column_comment " +
                    "from INFORMATION_SCHEMA.COLUMNS COL where TABLE_SCHEMA =" + changeStringToQue(dataBaseInfo.getDbName()) + " and COL.TABLE_NAME = " + changeStringToQue(dataBaseInfo.getTbName());
            rs = sta.executeQuery(sql);

            while (rs.next()) {
                HashMap<String, Object> hashMap = new HashMap<>();
                hashMap.put("tableSchema", rs.getObject("table_schema"));
                hashMap.put("tableName", rs.getObject("table_name"));
                hashMap.put("columnName", rs.getObject("column_name"));
                hashMap.put("isNullable", rs.getObject("is_nullable"));
                hashMap.put("dataType", rs.getObject("data_type"));
                hashMap.put("columnType", rs.getObject("column_type"));
                hashMap.put("columnKey", rs.getObject("column_key"));
                hashMap.put("extra", rs.getObject("extra"));
                hashMap.put("columnComment", rs.getObject("column_comment"));
                maps.add(hashMap);
            }
        } catch (SQLException e) {
            throw new ServiceException("数据库连接错误" + "" + e.getMessage());
        } finally {
            JdbcUtils.releaseConnection(conn, sta, rs);
        }
        return maps;
    }
}
