package top.hugo.system.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
public class DemoController {
    private static final Logger logger = LoggerFactory.getLogger(DemoController.class);

    @GetMapping("/demo")
    public String demo(HttpServletRequest request) {
        String realIp = getRealIp(request);
        logger.info("Real IP address of client is: {}", realIp);
        return "Demo page";
    }

    /**
     * 获取真实IP地址
     *
     * @param request HTTP请求
     * @return 真实IP地址
     */
    private String getRealIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip != null && ip.length() != 0 && !"unknown".equalsIgnoreCase(ip)) {
            // 使用逗号分隔的多个IP地址中的第一个
            ip = ip.split(",")[0];
        } else {
            ip = request.getRemoteAddr();
        }
        return ip;
    }
}
