package top.hugo.demo.controller;

import com.baomidou.dynamic.datasource.annotation.DS;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.hugo.demo.entity.SysPlatform;
import top.hugo.demo.mapper.SysPlatformMapper;

import java.util.List;

/**
 * mysql使用测试
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("mysqlTest")
public class MysqlTestController {
    private final SysPlatformMapper sysPlatformMapper;

    /**
     * mysql测试
     *
     * @return
     */
    @GetMapping("getMysqlData")
    public List<SysPlatform> test() {
        List<SysPlatform> sysPlatforms = sysPlatformMapper.selectList();
        return sysPlatforms;
    }


    /**
     * mysql  xml测试
     *
     * @return
     */
    @GetMapping("testXml")
    public List<SysPlatform> testXml() {
        List<SysPlatform> sysPlatforms = sysPlatformMapper.selectListXml();
        return sysPlatforms;
    }

    /**
     * mysql  testSlave
     *
     * @return
     */
    @GetMapping("testSlave")
    @DS("slave")
    public List<SysPlatform> testSlave() {
        List<SysPlatform> sysPlatforms = sysPlatformMapper.selectListXml();
        return sysPlatforms;
    }
}