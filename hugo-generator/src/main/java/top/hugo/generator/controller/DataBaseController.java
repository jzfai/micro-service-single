package top.hugo.generator.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.hugo.common.domain.R;
import top.hugo.generator.service.DataBaseService;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Map;

/**
 * 数据源模块
 *
 * @author 熊猫哥
 * @since 2022-07-20 10:10:28
 */
@RestController
@RequestMapping("basis-func/dataBase")
public class DataBaseController {
    @Resource
    private DataBaseService dataBaseService;

    /**
     * 获取数据库元数据
     *
     * @param dbName 库名
     */
    @GetMapping("getAllDatabaseOrTable/{dbName}")
    public R<ArrayList<Map>> getAllTableFromDb(@PathVariable String dbName) {
        ArrayList<Map> allTableFromDb = this.dataBaseService.getAllTableFromDb(dbName);
        return R.ok(allTableFromDb);
    }

    /**
     * 获取数据库表元数据
     *
     * @param dbName 库名
     * @param tbName 表名
     */
    @GetMapping("getAllDatabaseOrTable/{dbName}/{tbName}")
    public R<ArrayList<Map>> getAllColumnFromTb(@PathVariable String dbName, @PathVariable String tbName) {
        ArrayList<Map> allColumnFromTb = this.dataBaseService.getAllColumnFromTb(dbName, tbName);
        return R.ok(allColumnFromTb);
    }
}
