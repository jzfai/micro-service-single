package top.kuanghua.basisfunc.service;

import com.alibaba.fastjson.JSON;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import top.kuanghua.basisfunc.utils.GeneratorTempUtils;

import java.io.IOException;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @Title: MybatisPlusGeneratorServiceTest
 * @Description:
 * @Auther: kuanghua
 * @create 2022/10/7 17:03
 */

public class MybatisPlusGeneratorServiceTest {
    @Test
    public  void generatorMybatisPlusBasicTmp() throws IOException {
        MybatisPlusGeneratorService generatorService = new MybatisPlusGeneratorService();
        String string = GeneratorTempUtils.readFileToString("D:\\github\\micro-service-plus\\velocity-tmp-dir\\json-data\\mybatis-plus\\" + "basicData.json");
        Map<String, Object> jsonData = JSON.parseObject(string, Map.class);
        generatorService.generatorMybatisPlusBasicTmp(jsonData);
    }
}