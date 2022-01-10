package top.kuanghua.tyexample.service;

import org.springframework.stereotype.Service;
import top.kuanghua.feign.tyuser.feign.UserFeign;

import javax.annotation.Resource;

/**
 * @Title: SeataTestService
 * @Description:
 * @Auther: kuanghua
 * @create 2022-01-10 15:09
 */
@Service
public class SeataTestService {

    @Resource
    private UserFeign userFeign;


    public void testSeataRollback() throws InterruptedException {
        userFeign.insertUser("jzfai");
        Thread.sleep(2000);
        int fai = 1 / 0;
    }
}
