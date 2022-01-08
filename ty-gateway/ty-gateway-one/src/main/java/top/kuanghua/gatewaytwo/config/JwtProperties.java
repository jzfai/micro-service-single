package top.kuanghua.gatewaytwo.config;


import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import top.kuanghua.gatewaytwo.utils.RsaUtils;

import javax.annotation.PostConstruct;
import java.io.File;
import java.security.PrivateKey;
import java.security.PublicKey;


@Data
@Slf4j
@ConfigurationProperties(prefix = "jwt-properties")
public class JwtProperties {
    private String secret;
    private String pubKeyPath;
    private String priKeyPath;
    private PublicKey publicKey;
    private PrivateKey privateKey;
    private Integer expire;
    private String cookieName;
    private Integer cookieMaxAge;

    @PostConstruct
    public void init() throws Exception {
//        String classPath = ResourceUtils.getURL("classpath:").getPath();
//        String pubKeyPath=classPath+"\\rsafile\\rsa.pub";
//        String priKeyPath=classPath+"\\rsafile\\rsa.pri";
        File pubKeyFile = new File(pubKeyPath);
        File priKeyFile = new File(priKeyPath);
        if (!pubKeyFile.exists() || !priKeyFile.exists()) {
            File fileParent = pubKeyFile.getParentFile();
            if (!fileParent.exists()) {
                fileParent.mkdirs();
            }
            throw new RuntimeException("秘钥文件不存在");
            // 获取公钥和私钥
            /*秘钥最好不要跟换*/
//            try {
//                RsaUtils.generateKey(pubKeyPath, priKeyPath, secret);
//            } catch (Exception e) {
//                log.error("生成秘钥失败");
//                //e.printStackTrace();
//            }r
        }
        //读取公钥和私钥文件
        this.publicKey = RsaUtils.getPublicKey(pubKeyPath);
        this.privateKey = RsaUtils.getPrivateKey(priKeyPath);
    }
}