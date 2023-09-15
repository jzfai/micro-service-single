package top.hugo.demo.controller;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * reids缓存测试
 */
@RestController
@RequestMapping("redisTest")
public class RedisTestController {
    /**
     * getMysqlData
     *
     * @return
     */
    @Cacheable(cacheNames = "test", key = "#id")
    @GetMapping("getMysqlData")
    public List<Map> redisCacheTest(Integer id) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("1", "88");
        hashMap.put("2", "888");
        hashMap.put("id", id);
        ArrayList<Map> maps = new ArrayList<>();
        maps.add(hashMap);
        return maps;
    }
}