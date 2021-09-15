package top.kuanghua.tyupload.controller;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.Map;

/**
 * @Title: UploadControllerTest @Description: @Auther: kuanghua
 * @create 2020/8/20 21:34
 */
@SpringBootTest
@RunWith(SpringRunner.class)
class UploadControllerTest {

    @Autowired
    private UploadControllerTest uploadControllerTest;

    @Test
    void uploadImage() {
        Map<String, String> hashMap = new HashMap<>();
        hashMap.put("", "");
    }
}
