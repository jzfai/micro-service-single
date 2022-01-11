package top.kuanghua.tyexample.service;

import io.seata.spring.annotation.GlobalTransactional;
import org.springframework.stereotype.Service;
import top.kuanghua.feign.integrationfront.feign.UserFeign;

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

    @GlobalTransactional(rollbackFor = Exception.class)
    public void testSeataRollback(){
        userFeign.insertUser("jzfai");
        int i=10/0;
    }
}
