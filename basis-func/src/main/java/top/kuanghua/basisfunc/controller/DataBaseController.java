package top.kuanghua.basisfunc.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.kuanghua.basisfunc.service.DataBaseService;
import top.kuanghua.commonpom.entity.ResResult;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Map;

/**
 * @Title: DataBaseController
 * @Description:
 * @Auther: kuanghua
 * @create 2022/6/3 8:53
 */
@Api(tags = "获取数据库表和字段信息")
@RestController
@RequestMapping("dataBase")
public class DataBaseController {
    @Resource
    private DataBaseService dataBaseService;

    @GetMapping("getAllDatabaseOrTable/{dbName}")
    @ApiOperation(value = "mysql获取所有表")
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
    @ApiOperation(value = "mysql获取表及字段信息")
    public ResResult getAllColumnFromTb(@PathVariable String dbName, @PathVariable String tbName) {
        ArrayList<Map> allColumnFromTb = this.dataBaseService.getAllColumnFromTb(dbName, tbName);
        return new ResResult().success(allColumnFromTb);

    }
}
