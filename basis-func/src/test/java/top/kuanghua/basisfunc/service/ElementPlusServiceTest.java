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
 * @Title: ElementPlusServiceTest
 * @Description:
 * @Auther: kuanghua
 * @create 2022/10/1 15:59
 */
public  class ElementPlusServiceTest {
    @Test
    public void generatorTableQuery() throws IOException {
        String string = GeneratorTempUtils.readFileToString("D:\\github\\micro-service-plus\\velocity-tmp-dir\\json-data\\element-plus\\" + "table-query.json");
        Map<String, Object> jsonData = JSON.parseObject(string, Map.class);
        ElementPlusService elementPlusService = new ElementPlusService();
        elementPlusService.generatorTableQuery(jsonData);
    }
    @Test
    public void generatorAddEdit() throws IOException {
        String string = GeneratorTempUtils.readFileToString("D:\\github\\micro-service-plus\\velocity-tmp-dir\\json-data\\element-plus\\" + "add-edit.json");
        Map<String, Object> jsonData = JSON.parseObject(string, Map.class);
        ElementPlusService elementPlusService = new ElementPlusService();
        elementPlusService.generatorAddEdit(jsonData);
    }
    @Test
    public void generatorDetail() throws IOException {
        String string = GeneratorTempUtils.readFileToString("D:\\github\\micro-service-plus\\velocity-tmp-dir\\json-data\\element-plus\\" + "detail.json");
        Map<String, Object> jsonData = JSON.parseObject(string, Map.class);
        ElementPlusService elementPlusService = new ElementPlusService();
        elementPlusService.generatorDetail(jsonData);
    }


}