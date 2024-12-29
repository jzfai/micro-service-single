package top.hugo.generator.service;

import org.apache.commons.lang3.StringUtils;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import top.hugo.common.exception.ServiceException;
import top.hugo.generator.entity.DataBaseInfo;
import top.hugo.generator.entity.DatabaseConfig;
import top.hugo.generator.mapper.DatabaseConfigMapper;
import top.hugo.generator.utils.JdbcUtils;
import top.hugo.redis.utils.RedisUtils;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

/**
 * @Title: DataBaseService
 * @Description:
 * @Auther: kuanghua
 * @create 2022/6/3 8:50
 * https://blog.csdn.net/qq_39323945/article/details/124661309
 */
@Service
public class DataBaseService {

    private DatabaseConfigMapper databaseConfigMapper;

    /*
     * 转换 "" to ''
     * */
    public String changeStringToQue(String name) {
        String[] split = (name.replaceAll("，", ",")).split(",");
        return "'" + StringUtils.join(split, "','") + "'";
    }

    /*
     * 刷新缓存
     * */
    public void flushAllSchema(DataBaseInfo dataBaseInfo){
        RedisUtils.getClient().getMapCache("getAllSchema").delete();
        getAllSchema(dataBaseInfo,dataBaseInfo.getUrl());
    }

    /**
     * 获取所有的库
     */
    @Cacheable(cacheNames = "getAllSchema", key = "#ip")
    public Set<String> getAllSchema(DataBaseInfo dataBaseInfo, String ip) {
        Connection conn = null;
        Statement sta = null;
        ResultSet rs = null;
        Set<String> sets = new HashSet<>();
        String url = "jdbc:mysql://" + dataBaseInfo.getUrl() + "?useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&useSSL=true&serverTimezone=GMT%2B8&autoReconnect=true&rewriteBatchedStatements=true";
        try {
            conn = JdbcUtils.getConnection("jdbc:mysql://"+dataBaseInfo.getUrl(), dataBaseInfo.getUserName(), dataBaseInfo.getPassword());
            sta = conn.createStatement();
            String sql = "select table_schema, table_name, table_comment, create_time, update_time " +
                    "from information_schema.tables";
            rs = sta.executeQuery(sql);
            while (rs.next()) {
                HashMap<String, Object> hashMap = new HashMap<>();
                sets.add(rs.getObject("table_schema").toString());
            }
        } catch (SQLException e) {
            throw new ServiceException("数据库连接错误" + "" + e.getMessage());
        } finally {
            JdbcUtils.releaseConnection(conn, sta, rs);
        }
        return sets;
    }

    /**
     * 获取库数据
     */
    public ArrayList<Map> getAllTableFromDb(DataBaseInfo dataBaseInfo) {
        Connection conn = null;
        Statement sta = null;
        ResultSet rs = null;
        ArrayList<Map> maps = new ArrayList<>();
        String url = "jdbc:mysql://" + dataBaseInfo.getUrl() + "?useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&useSSL=true&serverTimezone=GMT%2B8&autoReconnect=true&rewriteBatchedStatements=true";
        try {
            conn = JdbcUtils.getConnection(url, dataBaseInfo.getUserName(), dataBaseInfo.getPassword());
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
            conn = JdbcUtils.getConnection(url, dataBaseInfo.getUserName(), dataBaseInfo.getPassword());
            sta = conn.createStatement();
            String sql = "select table_schema, table_name, column_name, is_nullable, data_type, column_type, column_key, extra, column_comment " +
                    "from INFORMATION_SCHEMA.COLUMNS COL where TABLE_SCHEMA =" + changeStringToQue(dataBaseInfo.getDbName()) + " and COL.TABLE_NAME = " + changeStringToQue(dataBaseInfo.getTbName()) +" order  by ORDINAL_POSITION asc ";
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

    // 获取starrocks 数据库列表
    public ArrayList<Map> getStarrocksDb(DataBaseInfo dataBaseInfo){
        Connection conn = null;
        Statement sta = null;
        ResultSet rs = null;
        ArrayList<Map> maps = new ArrayList<Map>();
        String url = "jdbc:mysql://" + dataBaseInfo.getUrl() + "?useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&useSSL=true&serverTimezone=GMT%2B8&autoReconnect=true&rewriteBatchedStatements=true";
        try {
            conn = JdbcUtils.getConnection(url, dataBaseInfo.getUserName(), dataBaseInfo.getPassword());
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

    // 通过数据库获取对应的所有表名
    public DatabaseConfig getStarrocksTables(Integer dbConfigId){
        // 根据库名在_statistics_ 此表中加载出所有表名
        DatabaseConfig databaseConfig = databaseConfigMapper.selectById(dbConfigId);
        return databaseConfig;
    }



}
