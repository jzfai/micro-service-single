## springboot集成阿里云短信



### maven

```xml
<!--短信-->
<dependency>
    <groupId>com.aliyun</groupId>
    <artifactId>aliyun-java-sdk-core</artifactId>
    <version>4.5.16</version>
</dependency>
<dependency>
    <groupId>com.aliyun</groupId>
    <artifactId>aliyun-java-sdk-dysmsapi</artifactId>
    <version>2.1.0</version>
</dependency>
```

生成阿里云  秘钥，签名，模板 
https://blog.csdn.net/qq_48472773/article/details/132201423



yml配置文件

```yml
--- # sms 短信
aliyun:
  sms:
    accessKeyId: LTAIbsP99BfwW3B0
    secret: hjgQ05dpOdtsWrFOBYxwCN7ytXxHnr
    signName: 熊猫哥后台管理系统
    templateCode: SMS_463637197
```



AliyunSMSTemplate

```java
package top.hugo.ms.core;

import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class AliyunSMSTemplate {
    @Value("${aliyun.sms.signName}")
    private String signName;
    @Value("${aliyun.sms.templateCode}")
    private String templateCode;
    @Value("${aliyun.sms.accessKeyId}")
    private String accessKeyId;
    @Value("${aliyun.sms.secret}")
    private String secret;

    /**
     * 发送短信
     *
     * @param phoneNumbers 手机号
     * @param code         验证码
     */
    public void sendMessage(String phoneNumbers, String code) {
        //default 地域节点，默认就好；  后面是 阿里云的 id和秘钥
        DefaultProfile profile = DefaultProfile.getProfile("default", accessKeyId, secret);
        IAcsClient client = new DefaultAcsClient(profile);
        //这里不能修改
        CommonRequest request = new CommonRequest();
        request.setSysMethod(MethodType.POST);
        request.setSysDomain("dysmsapi.aliyuncs.com");    //短信服务的服务接入地址
        request.setSysVersion("2017-05-25");        //API的版本号
        request.setSysAction("SendSms");    //API的名称
        request.putQueryParameter("PhoneNumbers", phoneNumbers);    //接收短信的手机号码
        request.putQueryParameter("SignName", signName);//    短信签名名称
        request.putQueryParameter("TemplateCode", templateCode);//    短信模板Code，注意这个模板要和签名对应
        request.putQueryParameter("TemplateParam", "{\"code\":\"" + code + "\"}"); //短信模板变量对应的实际值
        try {
            CommonResponse response = client.getCommonResponse(request);
            System.out.println(response.getData());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 随机生成验证码
     *
     * @param length 长度为4位或者6位
     * @return
     */
    public Integer generateValidateCode(int length) {
        Integer code = null;
        if (length == 4) {
            code = new Random().nextInt(9999);//生成随机数，最大为9999
            if (code < 1000) {
                code = code + 1000;//保证随机数为4位数字
            }
        } else if (length == 6) {
            code = new Random().nextInt(999999);//生成随机数，最大为999999
            if (code < 100000) {
                code = code + 100000;//保证随机数为6位数字
            }
        } else {
            throw new RuntimeException("只能生成4位或6位数字验证码");
        }
        return code;
    }

}
```



SmsController测试类

```java
package top.hugo.admin.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.hugo.common.domain.R;
import top.hugo.ms.core.AliyunSMSTemplate;

/**
 * 短信演示案例
 * 请先阅读文档 否则无法使用
 *
 * @author Lion Li
 * @version 4.2.0
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/demo/sms")
public class SmsController {
    private final AliyunSMSTemplate aliyunSMSTemplate;
    /**
     * 发送短信Aliyun
     *
     * @param phones 电话号
     * @param templateId 模板ID
     */
    @GetMapping("/sendAliyun")
    public R<Object> sendAliyun(String phones) {
        Integer code = aliyunSMSTemplate.generateValidateCode(6);
        aliyunSMSTemplate.sendMessage(phones, code.toString());
        return R.ok();
    }
}
```