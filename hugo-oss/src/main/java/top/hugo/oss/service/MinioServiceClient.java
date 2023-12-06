package top.hugo.oss.service;

import io.minio.MinioClient;
import io.minio.ObjectStat;
import io.minio.PutObjectOptions;
import io.minio.Result;
import io.minio.errors.XmlParserException;
import io.minio.messages.Bucket;
import io.minio.messages.Item;
import org.apache.commons.lang3.StringUtils;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.InputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
public class MinioServiceClient implements InitializingBean {
    @Value(value = "${minio.server-url}")
    private String serverUrl;
    @Value(value = "${minio.bucket}")
    private String bucket;

    @Value(value = "${minio.host}")
    private String host;

    @Value(value = "${minio.url}")
    private String url;

    @Value(value = "${minio.access-key}")
    private String accessKey;

    @Value(value = "${minio.secret-key}")
    private String secretKey;

    private MinioClient minioClient;

    @Override
    public void afterPropertiesSet() {
        Assert.hasText(url, "Minio url 为空");
        Assert.hasText(accessKey, "Minio accessKey为空");
        Assert.hasText(secretKey, "Minio secretKey为空");
        try {
            this.minioClient = new MinioClient(this.host, this.accessKey, this.secretKey);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 上传
     */
    public HashMap<String, Object> putObject(MultipartFile multipartFile) {
        // bucket 不存在，创建
        try {
            if (!minioClient.bucketExists(this.bucket)) {
                minioClient.makeBucket(this.bucket);
            }
            try (InputStream inputStream = multipartFile.getInputStream()) {
                // 上传文件的名称
                String originalFileName = multipartFile.getOriginalFilename();
                // PutObjectOptions，上传配置(文件大小，内存中文件分片大小)
                PutObjectOptions putObjectOptions = new PutObjectOptions(multipartFile.getSize(), PutObjectOptions.MIN_MULTIPART_SIZE);
                // 文件的ContentType
                putObjectOptions.setContentType(multipartFile.getContentType());
                minioClient.putObject(this.bucket, originalFileName, inputStream, putObjectOptions);
                String suffix = StringUtils.substring(originalFileName, originalFileName.lastIndexOf("."), originalFileName.length());
                HashMap<String, Object> fileMap = new HashMap<>();
                String fileName = UriUtils.encode(originalFileName, StandardCharsets.UTF_8);
                //匹配数据将数据返回
                fileMap.put("fileName", fileName);
                fileMap.put("fileSuffix", suffix);
                fileMap.put("originalName", originalFileName);
                fileMap.put("url", bucket + File.separator + fileName);
                fileMap.put("fullUrl", serverUrl + File.separator + "minio" + File.separator + bucket + File.separator + fileName);
                // 返回访问路径
                return fileMap;
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 文件下载
     */
    public void download(String fileName, HttpServletResponse response) {
        // 从链接中得到文件名
        InputStream inputStream;
        try {
            ObjectStat stat = minioClient.statObject(bucket, fileName);
            inputStream = minioClient.getObject(bucket, fileName);
            response.setContentType(stat.contentType());
            response.setCharacterEncoding("UTF-8");
            response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8"));
            IOUtils.copy(inputStream, response.getOutputStream());
            inputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("有异常：" + e);
        }
    }

    /**
     * 列出所有存储桶名称
     *
     * @return
     * @throws Exception
     */
    public List<String> listBucketNames() {
        List<Bucket> bucketList = listBuckets();
        List<String> bucketListName = new ArrayList<>();
        for (Bucket bucket : bucketList) {
            bucketListName.add(bucket.name());
        }
        return bucketListName;
    }

    /**
     * 查看所有桶
     *
     * @return
     * @throws Exception
     */
    public List<Bucket> listBuckets() {
        try {
            return minioClient.listBuckets();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 检查存储桶是否存在
     *
     * @param bucketName
     * @return
     * @throws Exception
     */
    public boolean bucketExists(String bucketName) {
        try {
            boolean flag = minioClient.bucketExists(bucketName);
            if (flag) {
                return true;
            }
            return false;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 创建存储桶
     *
     * @param bucketName
     * @return
     * @throws Exception
     */
    public boolean makeBucket(String bucketName) {
        boolean flag = bucketExists(bucketName);
        if (!flag) {
            try {
                minioClient.makeBucket(bucketName);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            return true;
        } else {
            return false;
        }
    }

    /**
     * 删除桶
     *
     * @param bucketName
     * @return
     * @throws Exception
     */
    public boolean removeBucket(String bucketName) {
        boolean flag = bucketExists(bucketName);
        if (flag) {
            Iterable<Result<Item>> myObjects = listObjects(bucketName);
            try {
                for (Result<Item> result : myObjects) {
                    Item item = result.get();
                    // 有对象文件，则删除失败
                    if (item.size() > 0) {
                        return false;
                    }
                }
                // 删除存储桶，注意，只有存储桶为空时才能删除成功。
                minioClient.removeBucket(bucketName);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            flag = bucketExists(bucketName);
            if (!flag) {
                return true;
            }

        }
        return false;
    }

    /**
     * 列出存储桶中的所有对象
     *
     * @param bucketName 存储桶名称
     * @return
     */
    public Iterable<Result<Item>> listObjects(String bucketName) {
        try {
            boolean flag = bucketExists(bucketName);
            if (flag) {
                return minioClient.listObjects(bucketName);
            }
            return null;
        } catch (XmlParserException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 列出存储桶中的所有对象名称
     *
     * @param bucketName 存储桶名称
     * @return
     * @throws Exception
     */
    public List<String> listObjectNames(String bucketName) {
        List<String> listObjectNames = new ArrayList<>();
        boolean flag = bucketExists(bucketName);
        if (flag) {
            Iterable<Result<Item>> myObjects = listObjects(bucketName);
            for (Result<Item> result : myObjects) {
                Item item = null;
                try {
                    item = result.get();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                listObjectNames.add(item.objectName());
            }
        }
        return listObjectNames;
    }

    /**
     * 删除一个对象
     *
     * @param bucketName 存储桶名称
     * @param objectName 存储桶里的对象名称
     */
    public boolean removeObject(String bucketName, String objectName) {
        boolean flag = bucketExists(bucketName);
        if (flag) {
            List<String> objectList = listObjectNames(bucketName);
            for (String s : objectList) {
                if (s.equals(objectName)) {
                    try {
                        minioClient.removeObject(bucketName, objectName);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 文件访问路径
     *
     * @param bucketName 存储桶名称
     * @param objectName 存储桶里的对象名称
     * @return
     * @throws Exception
     */
    public String getObjectUrl(String bucketName, String objectName) {
        boolean flag = bucketExists(bucketName);
        String url = "";
        if (flag) {
            try {
                url = minioClient.getObjectUrl(bucketName, objectName);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        return url;
    }
}