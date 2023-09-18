package top.hugo.demo.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import top.hugo.oss.service.MinioServiceClient;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * mino 测试
 */
@RestController
@RequestMapping("/minio")
@RequiredArgsConstructor
public class MinioController {

    private final MinioServiceClient minioService;

    // 上传
    @PostMapping("/upload")
    public Object upload(@RequestParam("file") MultipartFile multipartFile) {
        return minioService.putObject(multipartFile);
    }

    // 下载文件
    @GetMapping("/download")
    public void download(@RequestParam("fileName") String fileName, HttpServletResponse response) {
        minioService.download(fileName, response);
    }

    // 列出所有存储桶名称
    @PostMapping("/list")
    public List<String> list() {
        return minioService.listBucketNames();
    }

    // 创建存储桶
    @PostMapping("/createBucket")
    public boolean createBucket(String bucketName) {
        return minioService.makeBucket(bucketName);
    }

    // 删除存储桶
    @PostMapping("/deleteBucket")
    public boolean deleteBucket(String bucketName) {
        return minioService.removeBucket(bucketName);
    }

    // 列出存储桶中的所有对象名称
    @PostMapping("/listObjectNames")
    public List<String> listObjectNames(String bucketName) {
        return minioService.listObjectNames(bucketName);
    }

    // 删除一个对象
    @PostMapping("/removeObject")
    public boolean removeObject(String bucketName, String objectName) {
        return minioService.removeObject(bucketName, objectName);
    }

    // 文件访问路径
    @PostMapping("/getObjectUrl")
    public String getObjectUrl(String bucketName, String objectName) {
        return minioService.getObjectUrl(bucketName, objectName);
    }
}