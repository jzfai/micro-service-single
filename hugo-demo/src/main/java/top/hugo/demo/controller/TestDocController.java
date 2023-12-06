package top.hugo.demo.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 文档测试
 */
@RestController
public class TestDocController {
    /**
     * test接口
     *
     * @param name 名字
     * @param age  年龄
     */
    @GetMapping("test")
    public String test(String name, String age) {
        return name + age;
    }
}