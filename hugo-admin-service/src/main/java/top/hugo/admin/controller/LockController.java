package top.hugo.admin.controller;

import lombok.RequiredArgsConstructor;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * lock 相关
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("lock")
public class LockController {

    private final RedissonClient redissonClient;
    private Integer saleCount = 2;

    /**
     * 超卖程序
     */
    @GetMapping("/sale")
    public String sale() throws InterruptedException {
        if (saleCount > 0) {
            Thread.sleep(2000);
            saleCount = saleCount - 1;
            System.out.println("售出了,还剩" + saleCount);
            return "售出了,还剩" + saleCount;
        } else {
            return "卖完了";
        }
    }


    /**
     * synchronized锁
     *
     * @return
     * @throws InterruptedException
     */
    @GetMapping("/synchronizedSale")
    public String synchronizedSale() throws InterruptedException {
        synchronized (this) {
            if (saleCount > 0) {
                Thread.sleep(2000);
                saleCount = saleCount - 1;
                return "售出了,还剩" + saleCount;
            } else {
                return "卖完了";
            }
        }
    }

    /**
     * 重置数据
     */
    @GetMapping("/resetCount")
    public void resetCount() {
        saleCount = 2;
    }


    /**
     * redisson 分布式锁
     *
     * @return
     */
    @GetMapping("/redission-lock")
    public String redissionLock() {
        RLock lock = redissonClient.getLock("CatelogJson-lock");
        lock.lock();
        try {
            if (saleCount > 0) {
                Thread.sleep(2000);
                saleCount = saleCount - 1;
                return "售出了,还剩" + saleCount;

            } else {
                return "卖完了";
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            lock.unlock();
        }
    }
}