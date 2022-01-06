package top.kuanghua.tyexample.utils;

import com.alibaba.fastjson.JSON;
import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import top.kuanghua.tyexample.config.SmsProperties;

import java.util.Map;


@Component
@Slf4j
public class SendSmsUtils {

    @Autowired
    private SmsProperties smsProperties;

    public void sendMsg(String phone, String code, String signName, String codeTemplate){
        DefaultProfile profile = DefaultProfile.getProfile("cn-hangzhou", smsProperties.getAccessKeyId(), smsProperties.getAccessKeySecret());
        IAcsClient client = new DefaultAcsClient(profile);
        CommonRequest request = new CommonRequest();
        request.setSysMethod(MethodType.POST);
        request.setSysDomain("dysmsapi.aliyuncs.com");
        request.setSysVersion("2017-05-25");
        request.setSysAction("SendSms");
        request.putQueryParameter("RegionId", "cn-hangzhou");
        request.putQueryParameter("PhoneNumbers", phone);
        request.putQueryParameter("SignName", signName);
        request.putQueryParameter("TemplateCode", codeTemplate);
        request.putQueryParameter("TemplateParam", "{\"code\":\""+code+"\"}");
        try {
            CommonResponse response = client.getCommonResponse(request);
            String data = response.getData();
            Map map = JSON.parseObject(data, Map.class);
            if (map.get("Code").equals("OK")){
                log.warn("发送短信成功 "+data);
            }else{
                // 记录失败日志
                log.error("发送短信失败 "+data);
            }
        } catch (Exception e) {
            // 记录失败日志
            log.error("发送短信失败 "+e.getMessage());
            throw new RuntimeException("发送短信失败 "+e.getMessage());
        }
    }
}
