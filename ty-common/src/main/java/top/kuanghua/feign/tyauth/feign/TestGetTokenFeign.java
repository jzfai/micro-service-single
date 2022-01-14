package top.kuanghua.feign.tyauth.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import top.kuanghua.feign.config.FeignConfiguration;
import top.kuanghua.khcomomon.entity.ResResult;

@FeignClient(name = "ty-auth", configuration = FeignConfiguration.class)
@RequestMapping("feignTest")
public interface TestGetTokenFeign {
    @PostMapping("getTokenInfo")
    public ResResult getTokenInfo();
}