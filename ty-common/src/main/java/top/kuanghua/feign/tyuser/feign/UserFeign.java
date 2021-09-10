package top.kuanghua.feign.tyuser.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import top.kuanghua.feign.config.FeignConfiguration;
import top.kuanghua.khcomomon.entity.ResResult;

@FeignClient(name="ty-user",configuration = FeignConfiguration.class)
@RequestMapping("user")
public interface UserFeign {

    @PostMapping("insertUser")
    ResResult insertUser(@RequestParam("username") String username);
}
