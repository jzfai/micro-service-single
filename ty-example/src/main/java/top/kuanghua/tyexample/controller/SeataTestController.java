package top.kuanghua.tyexample.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.kuanghua.tyexample.service.SeataTestService;

/**
 * @Title: SeataTestController
 * @Description:
 * @Auther: kuanghua
 * @create 2022-01-10 15:05
 */

@Api(tags = "seata测试")
@RestController
@RequestMapping("seataTest")
public class SeataTestController {
    @Autowired
    private SeataTestService seataTestService;

    @ApiOperation(value = "测试seata回滚")
    @GetMapping("test-seata-rollback")
    public void testSeataRollback() {
        try {
            seataTestService.testSeataRollback();
        } catch (InterruptedException e) {
            throw new RuntimeException("错误了");
        }
    }
}
