package top.kuanghua.khcomomon.config;

import java.util.Map;

/**
 * @Title: TokenParseInfo
 * @Description:
 * @Auther: kuanghua
 * @create 2021/3/15 14:37
 */
public class TokenParseInfo {
    // 定义一个线程域，存放登录用户
    private static final ThreadLocal<Map> t1 = new ThreadLocal<>();

    public TokenParseInfo(Map map) {
        t1.set(map);
    }

    public static Map getT1() {
        return t1.get();
    }

    public static void removeT1() {
        t1.remove();
    }
}
