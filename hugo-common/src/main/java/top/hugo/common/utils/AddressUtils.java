package top.hugo.common.utils;

import cn.hutool.core.net.NetUtil;
import cn.hutool.http.HtmlUtil;
import cn.hutool.http.HttpUtil;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

/**
 * 获取地址类
 *
 * @author kuanghua
 */
@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AddressUtils {

    // IP地址查询
    public static final String IP_URL = "http://ip.plyz.net/ip.ashx?";

    // 未知地址
    public static final String UNKNOWN = "XX XX";

    public static String getRealAddressByIP(String ip) {
        //ip = "117.131.99.102";
        String address = UNKNOWN;
        if (StringUtils.isBlank(ip)) {
            return address;
        }
        // 内网不查询
        ip = "0:0:0:0:0:0:0:1".equals(ip) ? "127.0.0.1" : HtmlUtil.cleanHtmlTag(ip);
        if (NetUtil.isInnerIP(ip)) {
            return "内网IP";
        }
        try {
            String rspStr = HttpUtil.createGet(IP_URL).body("ip=" + ip).execute().body();
            if (StringUtils.isEmpty(rspStr)) {
                log.error("获取地理位置异常 {}", ip);
                return UNKNOWN;
            }
            String[] split = rspStr.split("\\|");
            return split[1];
        } catch (Exception e) {
            log.error("获取地理位置异常 {}", ip);
        }
        return address;
    }
}
