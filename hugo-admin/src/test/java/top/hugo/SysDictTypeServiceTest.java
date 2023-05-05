package top.hugo;

import org.junit.jupiter.api.DisplayName;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest // 此注解只能在 springboot 主包下使用 需包含 main 方法与 yml 配置文件
@DisplayName("单元测试案例")
public class SysDictTypeServiceTest {
//    @Test
//    public void testAssertEquals() {
//        DictService bean = SpringUtils.getBean(DictService.class);
//        System.out.println(bean);
//    }
//
//    @Test
//    public void getCacheData() {
//        Object sys_normal_disable = CacheUtils.get(CacheNames.SYS_DICT, "sys_normal_disable");
//        List<SysDictData> sysDictData = JsonUtils.parseArray(JsonUtils.toJsonString(sys_normal_disable), SysDictData.class);
//        System.out.println(sysDictData);
//    }
}