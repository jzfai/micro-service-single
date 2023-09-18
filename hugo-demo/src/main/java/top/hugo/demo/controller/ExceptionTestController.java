package top.hugo.demo.controller;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import top.hugo.common.exception.ServiceException;

/**
 * 异常测试
 */
@RestController
public class ExceptionTestController {
    @GetMapping("exception")
    public void test() {
        throw new ServiceException("帅的好烦啊");
    }
}