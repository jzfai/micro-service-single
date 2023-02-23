package top.hugo.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.hugo.domain.ResResult;
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
@RequestMapping("dataBase")
public class DataBaseController {
    @Resource
    private DataBaseService dataBaseService;

    @GetMapping("getAllDatabaseOrTable/{dbName}")
    public ResResult getAllTableFromDb(@PathVariable String dbName) {
        ArrayList<Map> allTableFromDb = this.dataBaseService.getAllTableFromDb(dbName);
        return new ResResult().success(allTableFromDb);
    }
    /**
     * @param dbName
     * @param tbName
     * @return
     */
    @GetMapping("getAllDatabaseOrTable/{dbName}/{tbName}")
    public ResResult getAllColumnFromTb(@PathVariable String dbName, @PathVariable String tbName) {
        ArrayList<Map> allColumnFromTb = this.dataBaseService.getAllColumnFromTb(dbName, tbName);
        return new ResResult().success(allColumnFromTb);
    }
}
