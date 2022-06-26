package top.kuanghua.commonpom.utils;

import com.alibaba.fastjson.JSON;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import top.kuanghua.commonpom.entity.ResResult;

import java.io.File;
import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 从jwt解析得到的数据是Object类型，转换为具体类型可能出现空指针，
 * 这个工具类进行了一些转换
 */
public class SelfObjUtils {
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
            return ((CharSequence) obj).length() == 0;
        } else if (obj instanceof Collection) {
            return ((Collection) obj).isEmpty();
        } else {
            return obj instanceof Map ? ((Map) obj).isEmpty() : false;
        }
    }

    //解析请求数据
    public static ResResult parseRes(Object resResult) {
        ResResult res = JSON.parseObject(JSON.toJSONString(resResult), ResResult.class);
        return res;
    }

    //解析请求数据到data
    public static Map parseResToData(Object result) {
        ResResult resResult = parseRes(result);
        if (resResult.isFlag()) {
            return JSON.parseObject(JSON.toJSONString(resResult.getData()), Map.class);
        } else {
            throw new RuntimeException(resResult.getMsg());
        }
    }

    //解析请求数据到data
    public static Map parseResToMap(ResResult resResult) {
        if (resResult.isFlag()) {
            return JSON.parseObject(JSON.toJSONString(resResult.getData()), Map.class);
        } else {
            throw new RuntimeException(resResult.getMsg());
        }
    }

    public static List parseResToList(ResResult resResult) {
        if (resResult.isFlag()) {
            return JSON.parseObject(JSON.toJSONString(resResult.getData()), List.class);
        } else {
            throw new RuntimeException(resResult.getMsg());
        }
    }

    //将一般的数据转化为map
    public static Map<String, Object> changeToMap(Object resResult) {
        return JSON.parseObject(JSON.toJSONString(resResult), Map.class);
    }

    /**
     * 将Object数据转化为 ArrayList
     *
     * @param resResult
     * @return
     */
    public static ArrayList<Map<String, Object>> changeToArrayMap(Object resResult) {
        return JSON.parseObject(JSON.toJSONString(resResult), ArrayList.class);
    }

    public static ArrayList<String> changeToArrayString(Object resResult) {
        return JSON.parseObject(JSON.toJSONString(resResult), ArrayList.class);
    }

    public static ArrayList<Integer> changeToArrayInteger(Object resResult) {
        return JSON.parseObject(JSON.toJSONString(resResult), ArrayList.class);
    }

    /**
     * @return 时间年月日
     */
    public static String getCurrentDateTimeTrim() {
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");//设置日期格式
        return df.format(new Date());
    }
    /**
     * @return 时间年月日
     */
    public static String getCurrentDateTime() {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
        return df.format(new Date());
    }

    /**
     * @return 当前年月
     */
    public static String getCurrentDate() {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");//设置日期格式
        return df.format(new Date());
    }

    /**
     * @param date
     * @return
     */
    public static String formatDateTime(Date date) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
        return df.format(date);
    }


    /**
     * MultipartFile change to file
     *
     * @param multiFile
     * @return
     */
    public static File MultipartFileToFile(MultipartFile multiFile) {
        // 获取文件名
        String fileName = multiFile.getOriginalFilename();
        // 获取文件后缀
        String prefix = fileName.substring(fileName.lastIndexOf("."));
        // 若须要防止生成的临时文件重复,能够在文件名后添加随机码

        try {
            File file = File.createTempFile(fileName, prefix);
            multiFile.transferTo(file);
            return file;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}