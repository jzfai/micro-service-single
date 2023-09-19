package top.hugo.admin.runner;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import top.hugo.admin.service.DataDictService;

@Slf4j
@RequiredArgsConstructor
@Component
public class TypeDataToRedisRunner implements ApplicationRunner {
    private final DataDictService dataDictService;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        dataDictService.loadingDictCache();
        log.info("加载字典缓存数据成功");
    }
}