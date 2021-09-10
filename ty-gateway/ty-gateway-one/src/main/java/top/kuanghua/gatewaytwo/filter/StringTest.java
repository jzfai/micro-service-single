package top.kuanghua.gatewaytwo.filter;

import org.apache.commons.lang3.StringUtils;

/**
 * @Title: StringTest
 * @Description:
 * @Auther: kuanghua
 * @create 2021/3/15 10:48
 */
public class StringTest {
    public static void main(String[] args) {
        String a="/api/doc/swagger";
        String b="doc,swagger";


        String[] split = b.split(",");
        for (String s : split) {
//            System.out.println(StringUtils.contains(a, s));
        }


    }
}
