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
     * @return
     */
    public String sendMessage(String phoneNumbers, String code) {
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
        CommonResponse response = null;
        try {
            response = client.getCommonResponse(request);

        } catch (Exception e) {
            throw new RuntimeException("短信发送异常");
        }
        return response.getData();
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