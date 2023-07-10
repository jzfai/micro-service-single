package top.hugo.generator.controller;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.hugo.common.domain.R;
import top.hugo.generator.entity.ConfigSave;
import top.hugo.generator.entity.DataBaseInfo;
import top.hugo.generator.service.DataBaseService;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Map;

/**
 * 数据源模块
 *
 * @author kuanghua
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
     * @return
     * @params DataBaseInfo
     */
    @PostMapping("getAllDatabaseTest")
    public ConfigSave getAllDatabaseTest(@RequestBody ConfigSave configSave) {
        return configSave;
    }

    /**
     * 获取数据库元数据
     *
     * @params DataBaseInfo
     */
    @PostMapping("getAllDatabase")
    public R<ArrayList<Map>> getAllTableFromDb(@Validated @RequestBody DataBaseInfo baseInfo) {
        ArrayList<Map> allTableFromDb = this.dataBaseService.getAllTableFromDb(baseInfo);
        return R.ok(allTableFromDb);
    }

    /**
     * 获取数据库表元数据
     */
    @PostMapping("getAllTable")
    public R<ArrayList<Map>> getAllColumnFromTb(@Validated @RequestBody DataBaseInfo baseInfo) {
        ArrayList<Map> allColumnFromTb = this.dataBaseService.getAllColumnFromTb(baseInfo);
        return R.ok(allColumnFromTb);
    }
}
