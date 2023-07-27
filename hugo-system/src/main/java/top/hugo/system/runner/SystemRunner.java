package top.hugo.oss.runner;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import top.hugo.system.service.SysDictTypeService;

/**
 * 初始化 system 模块对应业务数据
 *
 * @author Lion Li
 */
@Slf4j
@RequiredArgsConstructor
@Component
public class SystemRunner implements ApplicationRunner {
    private final SysDictTypeService dictTypeService;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        dictTypeService.loadingDictCache();
        log.info("加载字典缓存数据成功");
    }

}
