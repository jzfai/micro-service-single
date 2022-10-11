package top.kuanghua.basisfunc.service;

import com.alibaba.fastjson.JSON;
import org.junit.jupiter.api.Test;
import top.kuanghua.basisfunc.utils.GeneratorTempUtils;

import java.io.IOException;
import java.util.Map;

/**
 * @Title: MybatisPlusGeneratorServiceTest
 * @Description:
 * @Auther: kuanghua
 * @create 2022/10/7 17:03
 */

public class MybatisPlusGeneratorServiceTest {
    @Test
    public void generatorMybatisPlusBasicTmp() throws IOException {
        MybatisPlusGeneratorService generatorService = new MybatisPlusGeneratorService();
        String string = GeneratorTempUtils.readFileToString("D:\\github\\micro-service-plus\\velocity-tmp-dir\\json-data\\mybatis-plus\\" + "basicData.json");
        Map<String, Object> jsonData = JSON.parseObject(string, Map.class);
        generatorService.generatorMybatisPlusBasicTmp(jsonData);
    }

    /*
     * 多表生成
     * */
    @Test
    public void generatorMybatisPlusMulTemp() throws IOException {
        MybatisPlusGeneratorService generatorService = new MybatisPlusGeneratorService();
        String string = GeneratorTempUtils.readFileToString("D:\\github\\micro-service-plus\\velocity-tmp-dir\\json-data\\mybatis-plus\\" + "multiData.json");
        Map<String, Object> jsonData = JSON.parseObject(string, Map.class);
        generatorService.generatorMybatisPlusMulTemp(jsonData);
    }

    /*查询*/
    @Test
    public void generatorMybatisPlusMulQueryTmp() throws IOException {
        MybatisPlusGeneratorService generatorService = new MybatisPlusGeneratorService();
        String string = GeneratorTempUtils.readFileToString("D:\\github\\micro-service-plus\\velocity-tmp-dir\\json-data\\mybatis-plus\\" + "basicData.json");
        Map<String, Object> jsonData = JSON.parseObject(string, Map.class);
        generatorService.generatorMybatisPlusMulQueryTmp(jsonData);
    }
}