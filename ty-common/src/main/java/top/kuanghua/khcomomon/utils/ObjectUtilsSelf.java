package top.kuanghua.khcomomon.utils;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.extension.api.R;
import com.sun.javafx.collections.MappingChange;
import org.apache.commons.lang3.StringUtils;
import top.kuanghua.khcomomon.entity.ResResult;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 从jwt解析得到的数据是Object类型，转换为具体类型可能出现空指针，
 * 这个工具类进行了一些转换
 */
public class ObjectUtilsSelf {
    /*类型转换*/
    public static String toString(Object obj) {
        if (obj == null) {
            return null;
        }
        return obj.toString();
    }

    public static Long toLong(Object obj) {
        if (obj == null) {
            return 0L;
        }
        if (obj instanceof Double || obj instanceof Float) {
            return Long.valueOf(StringUtils.substringBefore(obj.toString(), "."));
        }
        if (obj instanceof Number) {
            return Long.valueOf(obj.toString());
        }
        if (obj instanceof String) {
            return Long.valueOf(obj.toString());
        } else {
            return 0L;
        }
    }

    public static Integer toInt(Object obj) {
        return toLong(obj).intValue();
    }

    /*非空判断*/
    public static boolean isNotEmpty(Object obj) {
        return !isEmpty(obj);
    }

    public static boolean isEmpty(Object obj) {
        if (obj == null) {
            return true;
        } else if (obj.getClass().isArray()) {
            return Array.getLength(obj) == 0;
        } else if (obj instanceof CharSequence) {
            return ((CharSequence)obj).length() == 0;
        } else if (obj instanceof Collection) {
            return ((Collection)obj).isEmpty();
        } else {
            return obj instanceof Map ? ((Map)obj).isEmpty() : false;
        }
    }

    //解析请求数据
    public  static ResResult parseRes(Object resResult){
        ResResult res = JSON.parseObject(JSON.toJSONString(resResult), ResResult.class);
        return res;
    }
    //解析请求数据到data
    public  static Map parseResToData(Object result){
        ResResult resResult = parseRes(result);
        if (resResult.isFlag()) {
            return JSON.parseObject(JSON.toJSONString(resResult.getData()), Map.class);
        }else{
            throw  new RuntimeException(resResult.getMsg());
        }
    }
    //解析请求数据到data
    public  static Map parseResToMap(ResResult resResult){
        if (resResult.isFlag()) {
            return JSON.parseObject(JSON.toJSONString(resResult.getData()), Map.class);
        }else{
            throw  new RuntimeException(resResult.getMsg());
        }
    }
    public  static List parseResToList(ResResult resResult){
        if (resResult.isFlag()) {
            return JSON.parseObject(JSON.toJSONString(resResult.getData()), List.class);
        }else{
            throw  new RuntimeException(resResult.getMsg());
        }
    }
    //将一般的数据转化为map
    public  static Map changeToMap(Object resResult){
        Map<String,Object> res = JSON.parseObject(JSON.toJSONString(resResult), Map.class);
        return res;
    }

}