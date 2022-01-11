package top.kuanghua.tyexample.service;


import com.alibaba.fastjson.JSON;
import com.github.tobato.fastdfs.domain.fdfs.MetaData;
import com.github.tobato.fastdfs.domain.fdfs.StorePath;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.*;

/**
 * @Title: UploadService
 * @Description:
 * @Auther:jzfai
 * @Version: 1.0
 * @create 2020/3/4 21:58
 */
@Service
@Slf4j
public class UploadService {

    @Resource
    private FastFileStorageClient storageClient;

    //private static final List<String> CONTENT_TYPES = Arrays.asList("image/jpeg", "image/gif");

    public String upload(MultipartFile file) throws IOException {
        String originalFilename = file.getOriginalFilename();
        //检验文件类型
//        String contentType = file.getContentType();
//        if (!CONTENT_TYPES.contains(contentType)) {
//            //文件类型不合法
//            log.info("文件类型不合法：{}", originalFilename);
//            throw new RuntimeException("文件内容不合法");
//        }
        //检验文件内容
        BufferedImage bufferedImage = ImageIO.read(file.getInputStream());
        if (bufferedImage == null) {
            log.info("文件内容不合法：{}", originalFilename);
            throw new RuntimeException("文件内容不合法");
        }
        //保存到服务器
        //file.transferTo(new File("D:\\java\\javaproject\\pinyou\\pinyou-upload\\src\\main\\resources\\static\\" + originalFilename));
        //上传到FastDfs
        String ext = StringUtils.substringAfterLast(originalFilename, ".");
        HashSet<MetaData> metaData = new HashSet<>();
        metaData.add(new MetaData("name", file.getOriginalFilename()));
        metaData.add(new MetaData("createTime", JSON.toJSONString(new Date())));
        StorePath storePath = this.storageClient.uploadFile(file.getInputStream(), file.getSize(), ext, metaData);
        return storePath.getFullPath();
    }

    public Map<String, String> uploadGetMetaData(MultipartFile file) throws IOException {
        String originalFilename = file.getOriginalFilename();
        //检验文件类型
        String contentType = file.getContentType();
//        if (!CONTENT_TYPES.contains(contentType)) {
//            //文件类型不合法
//            log.info("文件类型不合法：{}", originalFilename);
//        }
        //检验文件内容
        BufferedImage bufferedImage = ImageIO.read(file.getInputStream());
        if (bufferedImage == null) {
            log.info("文件内容不合法：{}", originalFilename);
        }
        //保存到服务器
        //file.transferTo(new File("D:\\java\\javaproject\\pinyou\\pinyou-upload\\src\\main\\resources\\static\\" + originalFilename));
        //上传到FastDfs
        String ext = StringUtils.substringAfterLast(originalFilename, ".");
        HashSet<MetaData> metaData = new HashSet<>();
        metaData.add(new MetaData("name", file.getOriginalFilename()));
        StorePath storePath = this.storageClient.uploadFile(file.getInputStream(), file.getSize(), ext, metaData);
        //获取数据源信息
        Set<MetaData> metadata = this.storageClient.getMetadata(storePath.getGroup(), storePath.getPath());
        ArrayList<MetaData> metaDataArrayList = new ArrayList<>(metadata);
        String name = metaDataArrayList.get(0).getValue();
        //拼接name和url返回
        Map<String, String> hashMap = new HashMap<>();
        hashMap.put("name", name);
        hashMap.put("url", storePath.getFullPath());
        hashMap.put("group", storePath.getGroup());
        return hashMap;
    }
}
