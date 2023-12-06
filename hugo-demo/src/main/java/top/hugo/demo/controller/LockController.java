package top.hugo.demo.controller;

import lombok.RequiredArgsConstructor;
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

    private Integer saleCount = 100;

    /**
     * 插入
     */
    @GetMapping("/sale")
    public void sale() {
        saleCount = saleCount - 1;
        if (saleCount > 0) {
            System.out.println("售出了" + saleCount);
        }
    }
    
}