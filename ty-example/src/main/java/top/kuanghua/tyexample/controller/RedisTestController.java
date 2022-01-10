package top.kuanghua.tyexample.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.kuanghua.khcomomon.entity.ResResult;
import top.kuanghua.khcomomon.utils.ObjectUtilsSelf;
import top.kuanghua.khcomomon.utils.RedisClientUtils;
import top.kuanghua.tyexample.entity.ErrorCollection;
import top.kuanghua.tyexample.mapper.ErrorCollectionMapper;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.text.MessageFormat;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @Title: CacheAvalancheController
 * @Description:
 * @Auther: kuanghua
 * @create 2022-01-09 16:48
 */
@Api(tags = "缓存击穿和雪崩解决方案")
@RestController
@RequestMapping("redisCache")
public class RedisTestController {

    @Resource
    private ErrorCollectionMapper errorCollectionMapper;

    @Autowired
    private RedisTemplate redisTemplate;

    @Resource
    private RedisClientUtils redisClient;


    private Integer orderCount = 10;
    private String redisCountKey = "orderCo";


    @PostConstruct
    public void intParam() {
        BoundHashOperations hashKey = redisTemplate.boundHashOps("hashKey");
        hashKey.increment(redisCountKey, orderCount);
    }

    /*  redis缓存击穿问题
    *   1. 当用户根据key 查询数据时，先查询缓存，如果缓存有命中，返回，
        2. 但是如果缓存没有命中直接穿过缓存层，访问数据层 如果有，则存储指缓存，
        3. 但是同样如果没有命中，（也就是数据库中也没有数据）直接返回用户，但是不缓存
        这就是缓存的穿透。如果某一个key 请求量很大，但是存储层也没有数据，大量的请求都会达到存储层就会造成数据库压力巨大，有可能宕机的情况。


       缓存雪崩
       如果缓存集中在一段时间内失效，发生大量的缓存穿透，所有的查询都落在数据库上，造成了缓存雪崩。
       这个没有完美解决办法，但可以分析用户行为，尽量让失效时间点均匀分布。
       + 限流 加锁排队

        在缓存失效后，通过对某一个key加锁或者是队列 来控制key的线程访问的数量。例如：某一个key 只允许一个线程进行 操作。

        + 限流

        在缓存失效后，某一个key 做count统计限流，达到一定的阈值，直接丢弃，不再查询数据库。例如：令牌桶算法。等等。

        + 数据预热

        在缓存失效应当尽量避免某一段时间，可以先进行数据预热，比如某些热门的商品。提前在上线之前，或者开放给用户使用之前，先进行loading 缓存中，这样用户使用的时候，直接从缓存中获取。要注意的是，要更加业务来进行过期时间的设置 ，尽量均匀。

        + 做缓存降级（二级缓存策略）

        当分布式缓存失效的时候，可以采用本地缓存，本地缓存没有再查询数据库。这种方式，可以避免很多数据分布式缓存没有，就直接打到数据库的情况。

    * */

    @ApiOperation(value = "查询错误日志")
    @GetMapping("queryErrorLogList")
    public ResResult queryErrorLogList() {
        String key = "queryErrorLogList";
        /*
         * 解决缓存穿透带来的问题
         * 1.查询值为空的时候也设置到redis的key上
         * 2.给空值的key设定过期时间（为了避免过多的KEY 存储在redis中）
         * */
        if (redisTemplate.hasKey(key)) {
            List redisList = redisTemplate.opsForList().range(key, 0, -1);
            return new ResResult().success(redisList);
        } else {
            List<ErrorCollection> ecList = errorCollectionMapper.selectList(null);
            //无论ecList是否为空都设置到redis的key上,解决缓存穿透带来的雪崩问题
            //如果为空，一般情况下都需要设置一个过期时间，例如：5分钟失效。（为了避免过多的KEY 存储在redis中）
            redisTemplate.opsForList().leftPushAll(key, ecList);
            if (ObjectUtilsSelf.isEmpty(ecList)) {
                //此处设置为20s,进行测试
                redisTemplate.expire(key, 20, TimeUnit.SECONDS);
            } else {
                //此处设置为20s,进行测试,不为空时可以不设置过期时间，根据业务需求进行设置
                redisTemplate.expire(key, 20, TimeUnit.SECONDS);
            }
            return new ResResult().success(ecList);

        }
    }


    @ApiOperation(value = "并发请求改接口测试redis锁")
    @GetMapping("testRedisLock")
    public void testRedisLock() throws InterruptedException {
        //订单超卖问题
//        orderCount--;
//        if (orderCount >= 0) {
//            Thread.sleep(1000);
//            System.out.println(MessageFormat.format("订单还剩{0}", orderCount));
//        }

        /*用redis锁*/
        BoundHashOperations hashKey = redisTemplate.boundHashOps("hashKey");
        Long decrement = hashKey.increment(redisCountKey, -1);
        if (decrement >= 0) {
            System.out.println(MessageFormat.format("redis订单还剩{0}", decrement));
        }

    }

}
