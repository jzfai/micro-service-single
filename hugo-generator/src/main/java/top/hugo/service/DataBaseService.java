package top.hugo.service;

import org.springframework.stereotype.Service;
import top.hugo.service.mapper.DataBaseMapper;

import javax.annotation.Resource;
import java.util.ArrayList;
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
    @Resource
    private DataBaseMapper dataBaseMapper;

    /**
     * @param dbName
     * @return
     */
    public ArrayList<Map> getAllTableFromDb(String dbName) {
        ArrayList<Map> allTableFromDb = this.dataBaseMapper.getAllTableFromDb(dbName);
        return allTableFromDb;
    }

    /**
     * @param dbName
     * @param tbName
     * @return
     */
    public ArrayList<Map> getAllColumnFromTb(String dbName, String tbName) {
        ArrayList<Map> allColumnFromTb = this.dataBaseMapper.getAllColumnFromTb(dbName, tbName);
        return allColumnFromTb;
    }
}
