### ‘docker-compose 安装 minio

新建docker/compose目录

```shell
mkdir -p /docker/compose/
```



```yml
cat>/docker/compose/minio.yml<<EOF
version: '3'
services:  
  minio:
    image: minio/minio:RELEASE.2022-05-26T05-48-41Z
    container_name: minio
    ports:
      # api 端口
      - "9000:9000"
      # 控制台端口
      - "9001:9001"
    environment:
      # 时区上海
      TZ: Asia/Shanghai
      # 管理后台用户名
      MINIO_ROOT_USER: admin
      # 管理后台密码，最小8个字符
      MINIO_ROOT_PASSWORD: admin123456
      # https需要指定域名
      #MINIO_SERVER_URL: "https://xxx.com:9000"
      #MINIO_BROWSER_REDIRECT_URL: "https://xxx.com:9001"
      # 开启压缩 on 开启 off 关闭
      MINIO_COMPRESS: "off"
      # 扩展名 .pdf,.doc 为空 所有类型均压缩
      MINIO_COMPRESS_EXTENSIONS: ""
      # mime 类型 application/pdf 为空 所有类型均压缩
      MINIO_COMPRESS_MIME_TYPES: ""
    volumes:
      # 映射当前目录下的data目录至容器内/data目录
      - /docker/minio/data:/data
      # 映射配置目录
      - /docker/minio/config:/root/.minio/
    command: server --address ':9000' --console-address ':9001' /data  # 指定容器中的目录 /data
    privileged: true
    network_mode: "host"
EOF
```



部署

```shell
docker-compose -f  /docker/compose/minio.yml  up -d  minio
```


开发端口

```
9000,9001
```



控制台地址：ip:9001

admin:admin123456



## springboot集成minio

相关文章：
https://blog.csdn.net/yueyue763184/article/details/131147025



### maven

```xml
 <!--minio整合包-->
<dependency>
    <groupId>io.minio</groupId>
    <artifactId>minio</artifactId>
    <version>7.0.2</version>
</dependency>
```



yml配置文件

```yml
minio:
  server-url: xxx
  host: http://${minio.server-url}:9000
  bucket: public
  url: ${minio.host}/${minio.bucket}/
  access-key: admin
  secret-key: admin123456
```

>注：xxx: 服务部署ip



MinioClientService 类

```java
import io.minio.MinioClient;
import io.minio.ObjectStat;
import io.minio.PutObjectOptions;
import io.minio.Result;
import io.minio.errors.XmlParserException;
import io.minio.messages.Bucket;
import io.minio.messages.Item;
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
     * @throws Exception
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
```



MinioController类

```java
package top.hugo.demo.controller;
import top.hugo.oss.service.MinioServiceClient;
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
```



### 配置nginx访问映射到minio存储目录

```shell
cat> /docker/nginx/conf/conf.d/minio.conf<<EOF
 server {
        listen 80;
        server_name xxx;
        location ^~/minio/{
             alias  /docker/minio/data/;
        }
 }
EOF
```

>xxx: 换成你服务器ip



配置nginx 映射

```yml
cat>/docker/compose/nginx.yml<<EOF
version: '3'
services:
  nginx:
    image: nginx:1.22.1
    container_name: nginx
    environment:
      # 时区上海
      TZ: Asia/Shanghai
    ports:
      - "80:80"
      - "443:443"
    volumes:
      # 配置文件映射
      - /docker/nginx/conf:/etc/nginx
      # 页面目录
      - /docker/nginx/html:/usr/share/nginx/html
      # 日志目录
      - /docker/nginx/logs:/var/log/nginx
      # minio目录
      - /docker/minio/data:/docker/minio/data
    privileged: true
    network_mode: "host"
EOF
```

重新部署

```sehll
docker-compose -f  /docker/compose/nginx.yml  up -d nginx
```

