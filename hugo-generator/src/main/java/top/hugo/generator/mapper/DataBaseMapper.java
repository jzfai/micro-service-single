package top.hugo.generator.mapper;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.ArrayList;
import java.util.Map;

/**
 * @Title: DataBaseMapper
 * @Description:
 * @Auther: kuanghua
 * @create 2022/6/3 8:38
 */
public interface DataBaseMapper {

    /**
     * mysql获取所有表
     *
     * @param dbName
     * @return
     */
    @Select("SELECT  table_schema,table_name,table_comment,create_time,update_time  FROM  information_schema.tables WHERE TABLE_SCHEMA = #{dbName}")
    ArrayList<Map> getAllTableFromDb(@Param("dbName") String dbName);


    /**
     * mysql获取表所有字段信息
     *
     * @param tbName
     * @return
     */
    @Select("SELECT table_schema,table_name,column_name,is_nullable,data_type,column_type,column_key,extra,column_comment FROM INFORMATION_SCHEMA.COLUMNS COL Where TABLE_SCHEMA=#{dbName} AND  COL.TABLE_NAME=#{tbName}")
    ArrayList<Map> getAllColumnFromTb(@Param("dbName") String dbName, @Param("tbName") String tbName);
}
