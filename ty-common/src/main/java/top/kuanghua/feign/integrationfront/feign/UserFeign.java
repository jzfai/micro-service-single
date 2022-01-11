package top.kuanghua.feign.integrationfront.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import top.kuanghua.feign.config.FeignConfiguration;
import top.kuanghua.khcomomon.entity.ResResult;

@FeignClient(name="integration-front",configuration = FeignConfiguration.class)
@RequestMapping("user")
public interface UserFeign {
    @PostMapping("insertUser")
    ResResult insertUser(@RequestParam("username") String username);
}
