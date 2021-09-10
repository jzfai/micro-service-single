package top.kuanghua.feign.tyexecl.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import top.kuanghua.feign.config.FeignConfiguration;
import top.kuanghua.khcomomon.entity.ResResult;

/**
 * @Title: ExcelFeign
 * @Description:
 * @Auther: kuanghua
 * @create 2021/7/12 10:09
 */
@FeignClient(name="ty-excel",configuration = FeignConfiguration.class)
@RequestMapping("excel")
public interface ExcelFeign {
    /*查询execl表中的用户*/
    @GetMapping("selectExcelByUser")
    public ResResult selectExcelByUser(@RequestParam("username") String username);
}
