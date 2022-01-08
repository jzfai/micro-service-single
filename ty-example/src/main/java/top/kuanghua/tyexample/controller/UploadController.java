package top.kuanghua.tyexample.controller;

import com.netflix.hystrix.contrib.javanica.annotation.DefaultProperties;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import top.kuanghua.khcomomon.entity.ResResult;
import top.kuanghua.tyexample.service.UploadService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * @Title: UploadController @Description: @Auther:jzfai @Version: 1.0
 * @create 2020/3/4 21:58
 */
@RestController
@RequestMapping("upload")
@Api(tags = "图片上传")
@DefaultProperties(defaultFallback = "fallBackMethod") // 指定一个类的全局熔断方法
@RefreshScope
public class UploadController {


    @Autowired
    private UploadService uploadService;

    @ApiOperation(value = "上传图片或文件", notes = "上传图片接口")
    @PostMapping("image")
    public ResResult uploadImage(@RequestParam("file") MultipartFile file)
            throws IOException, InterruptedException {
        String url = this.uploadService.upload(file);
        Thread.sleep(20000);
        HashMap<String, String> map = new HashMap<>();
        map.put("shortPath", url);
        return new ResResult().success(map);
    }

    //
//    @ApiOperation(value = "上传图片(多张)")
//    @PostMapping("uploadGetMetaData")
//    public ResResult uploadGetMetaData(@RequestParam("files") MultipartFile[] files)
//            throws IOException, InterruptedException {
//        ArrayList<Map> strings = new ArrayList<>();
//        for (MultipartFile file : files) {
//            Map<String, String> stringStringMap = this.uploadService.uploadGetMetaData(file);
//            strings.add(stringStringMap);
//        }
//        //Thread.sleep(5000);
//        return new ResResult().success(strings);
//    }
    @ApiOperation(value = "批量上传图片或文件")
    @PostMapping("files")
    public ResResult uploadFileList(@RequestParam("files") MultipartFile[] files) throws IOException {
        ArrayList<String> strings = new ArrayList<>();
        for (MultipartFile file : files) {
            String url = this.uploadService.upload(file);
            strings.add(url);
        }
        return new ResResult().success(strings);
    }

    public String fallBackMethod() {
        return "请求繁忙，请稍后再试！";
    }

//    @Value(value = "${demo.a}")
//    private String data12;
//    @Value(value = "${demo.b}")
//    private String datas;
//    @ApiOperation(value = "test")
//    @GetMapping("test")
//    public String test() {
//        System.out.println("data ：" + data12 + ",datas="+datas);
//        return "data ：" + data12 + ",datas="+datas;
//    }
}
