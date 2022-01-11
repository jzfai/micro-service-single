package top.kuanghua.controller;

import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.kuanghua.khcomomon.entity.ResResult;

/**
 * @Title: hstrixController
 * @Description:
 * @Auther: kuanghua
 * @create 2022-01-08 18:15
 */
@RestController
public class HstrixController {
    @ApiOperation(value = "")
    @RequestMapping("/fallback")
    public ResResult fallback() {
        ResResult resResult = new ResResult();
        //设置20010为熔断状态吗
        resResult.setCode(20010);
        resResult.setMsg("接口发生了熔断");
        resResult.setFlag(false);
        return resResult;
    }
}