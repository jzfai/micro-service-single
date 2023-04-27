package top.hugo.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.hugo.common.domain.R;
import top.hugo.service.DataBaseService;


import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Map;

/**
 * @Title: DataBaseController
 * @Description:
 * @Auther: kuanghua
 * @create 2022/6/3 8:53
 */
@RestController
@RequestMapping("basis-func/dataBase")
public class DataBaseController {
    @Resource
    private DataBaseService dataBaseService;

    @GetMapping("getAllDatabaseOrTable/{dbName}")
    public R<ArrayList<Map>> getAllTableFromDb(@PathVariable String dbName) {
        ArrayList<Map> allTableFromDb = this.dataBaseService.getAllTableFromDb(dbName);
        return R.ok(allTableFromDb);
    }
    /**
     * @param dbName
     * @param tbName
     * @return
     */
    @GetMapping("getAllDatabaseOrTable/{dbName}/{tbName}")
    public R<ArrayList<Map>> getAllColumnFromTb(@PathVariable String dbName, @PathVariable String tbName) {
        ArrayList<Map> allColumnFromTb = this.dataBaseService.getAllColumnFromTb(dbName, tbName);
        return R.ok(allColumnFromTb);
    }
}
