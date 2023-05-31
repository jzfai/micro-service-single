package top.hugo.generator.mapper;

import org.apache.ibatis.annotations.Param;

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
    ArrayList<Map> getAllTableFromDb(@Param("dbName") String dbName);


    /**
     * mysql获取表所有字段信息
     *
     * @param tbName
     * @return
     */
    ArrayList<Map> getAllColumnFromTb(@Param("dbName") String dbName, @Param("tbName") String tbName);
}
