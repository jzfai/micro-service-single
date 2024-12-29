package top.hugo.generator.controller;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.hugo.common.domain.R;
import top.hugo.generator.entity.DataBaseInfo;
import top.hugo.generator.service.DataBaseService;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Map;
import java.util.Set;

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
     * 获取所有库信息
     *
     * @return
     * @params DataBaseInfo
     */
    @PostMapping("getAllSchema")
    public R<Set<String>> getAllSchema(@Validated @RequestBody DataBaseInfo baseInfo) {
        Set<String> allSchema = this.dataBaseService.getAllSchema(baseInfo,baseInfo.getUrl());
        return R.ok(allSchema);
    }


    /*
     * 刷新缓存
     * */
    @PostMapping("flushAllSchema")
    public R<Object> flushAllSchema(@RequestBody DataBaseInfo dataBaseInfo){
        dataBaseService.flushAllSchema(dataBaseInfo);
        return R.ok();
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
        // 这里区分数据库类型
        // 通过 dbConfigId 去数据库中查询配置
        // DatabaseConfig databaseConfig = dataBaseService.getStarrocksTables(baseInfo.getDbConfigId());
        // if ("3".equals(databaseConfig.getType())){
        //     ArrayList<Map> allColumnFromTb = dataBaseService.getStarrocksDb(baseInfo);
        //     return R.ok(allColumnFromTb);
        // }
        ArrayList<Map> allColumnFromTb = this.dataBaseService.getAllColumnFromTb(baseInfo);
        return R.ok(allColumnFromTb);
    }
}
