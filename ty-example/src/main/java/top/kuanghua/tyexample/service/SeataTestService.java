package top.kuanghua.tyexample.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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

    @Transactional(rollbackFor = Exception.class)
    public void testSeataRollback(){
        userFeign.insertUser("jzfai");
        int i=10/0;
    }
}
