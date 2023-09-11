package top.hugo.demo.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import top.hugo.common.utils.JacksonUtils;

import java.util.HashMap;
import java.util.Map;


/**
 * JacksonTest
 */
@DisplayName("JacksonTest")
public class JacksonTest {
    /**
     * JacksonTest
     */
    @Test
    public void JacksonTestFunc() {
        HashMap<String, Object> objectHashMap = new HashMap<>();
        objectHashMap.put("1", "帅的好烦啊");
        String toStr = JacksonUtils.toJsonString(objectHashMap);
        Map map = JacksonUtils.parseObject(toStr, Map.class);
        System.out.println(map);
    }
}