package top.kuanghua.tyexample.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @Title: CacheAvalancheServiceTest
 * @Description:
 * @Auther: kuanghua
 * @create 2022-01-09 16:35
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class CacheAvalancheServiceTest {

    @Autowired
    private RedisTemplate redisTemplate;

    @Test
    public void test() throws InterruptedException {
        redisTemplate.opsForValue().set("fai", "1");
        String fai = (String) redisTemplate.opsForValue().get("fai");
        System.out.println(fai);
        //Thread.sleep(10000);
    }


}