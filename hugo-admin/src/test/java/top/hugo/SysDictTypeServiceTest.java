package top.hugo;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;

@SpringBootTest // 此注解只能在 springboot 主包下使用 需包含 main 方法与 yml 配置文件
@DisplayName("单元测试案例")
public class SysDictTypeServiceTest {
    HashMap<String, String> hashMap = new HashMap<>();

    @Test
    public void testAssertEquals() {
//        System.out.println(toName(1, 1));
        System.out.println();
    }


    // 前端传入数值转换 orderSourceType，bizLine
    //orderSourceBiz 前端传入 1-1
    public ObCv toProp(String orderSourceBizName) {
        String[] split = orderSourceBizName.split("-");
        ObCv obCv = new ObCv();
        obCv.setOrderSourceType(Integer.parseInt(split[0]));
        obCv.setBizLine(Integer.parseInt(split[1]));
        return obCv;
    }

    /*
     * 传入orderSourceType bizLine 转换为对应的名称
     * key: 前端传递的key值
     * */
    public String toOrderSourceBizName(Integer orderSourceType, Integer bizLine) {
        hashMap.put("1-1", "换电订单");
        hashMap.put("2-1", "车电分离-换电订单");
        hashMap.put("2-2", "车电分离-电池订单");
        String key = orderSourceType + "-" + bizLine;
        return hashMap.get(key);
    }
//    @Test
//    public void getCacheData() {
//        Object sys_normal_disable = CacheUtils.get(CacheNames.SYS_DICT, "sys_normal_disable");
//        List<SysDictData> sysDictData = JsonUtils.parseArray(JsonUtils.toJsonString(sys_normal_disable), SysDictData.class);
//        System.out.println(sysDictData);
//    }
}