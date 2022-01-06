package top.kuanghua.tyupload.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.kuanghua.feign.tyauth.feign.TokenFeign;
import top.kuanghua.khcomomon.entity.ResResult;

import java.util.HashMap;

/**
 * @Title: NacosTestDemo
 * @Description:
 * @Auther: kuanghua
 * @create 2021/5/12 11:16
 */

@Api("nacos测试")
@RequestMapping("NacosTestDemo")
@RestController
public class NacosTestDemo {
    @Autowired
    private TokenFeign tokenFeign;
    @ApiOperation("第一个")
    @GetMapping("test")
    public ResResult test(){
        HashMap hashMap = new HashMap();
        hashMap.put("username","邝华");
        ResResult resResult = tokenFeign.generateToken(hashMap);
        //System.out.println(resResult);
        return resResult;
    }

    @ApiOperation("第二个")
    @GetMapping("test2")
    public ResResult test2(String token){
        ResResult resResult = tokenFeign.parseToken(token);
        return resResult;
    }
}
