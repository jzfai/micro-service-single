package top.hugo.web.controller.system;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import top.hugo.common.domain.R;
import top.hugo.web.controller.system.bo.BrandBo;

/**
 * 参数配置 信息操作处理
 *
 * @author Lion Li
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("demo")
public class SysConfigController {

    /**
     * 测试接口
     *
     * @param brandBo
     * @return
     * @author 邝华
     * @date 2023-01-31 9:33
     */
    @GetMapping("test1")
    public R<BrandBo> sendSimpleMessage(BrandBo brandBo) {
        return R.ok(brandBo);
    }


    /**
     * 测试接口
     *
     * @param name 用户名
     * @param age  年龄
     */
    @GetMapping("test2")
    public R<String> test2(String name, @RequestParam(name="age",required = false) String age) {
        return R.ok(name+age);
    }
}
