package top.hugo.generator.controller;

import cn.hutool.core.io.resource.InputStreamResource;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import top.hugo.common.domain.R;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * 文件上传模块
 *
 * @Auther: kuanghua
 * @create 2022-01-12 13:35
 */

@RestController
@RequestMapping("basis-func/upload")
@Slf4j
public class GoFastDFSController {

    /**
     * 上传文件
     *
     * @param file 上传的文件
     */
    @PostMapping("file")
    public R<Map<String, Object>> goFastfdfs(@RequestParam("file") MultipartFile file) {
        try {
            InputStreamResource isr = new InputStreamResource(file.getInputStream(), file.getOriginalFilename());
            Map<String, Object> params = new HashMap<>();
            params.put("file", isr);
            params.put("name", file.getOriginalFilename());
            params.put("size", file.getSize());
            params.put("output", "json");
            String resp = HttpUtil.post("http://111.230.198.245/group1/upload", params);
            Map<String, Object> coversMap = JSON.parseObject(resp, Map.class);
            log.info(coversMap.toString());
            return R.ok(coversMap);
        } catch (IOException e) {
            throw new RuntimeException("上传文件异常");
        }
    }
}