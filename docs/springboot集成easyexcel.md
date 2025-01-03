## springboot基础easyexcel

```xml
<!--easyexcel-->
<dependency>
    <groupId>org.apache.poi</groupId>
    <artifactId>poi</artifactId>
    <version>5.2.3</version>
</dependency>
<dependency>
    <groupId>org.apache.poi</groupId>
    <artifactId>poi-ooxml</artifactId>
    <version>5.2.3</version>
</dependency>
<dependency>
    <groupId>com.alibaba</groupId>
    <artifactId>easyexcel</artifactId>
    <version>3.1.5</version>
    <exclusions>
        <exclusion>
            <groupId>org.apache.poi</groupId>
            <artifactId>poi-ooxml-schemas</artifactId>
        </exclusion>
    </exclusions>
</dependency>
```

导出工具类(EasyExcelUtils)

```java
package top.hugo.admin.utils;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.io.resource.ClassPathResource;
import cn.hutool.core.util.IdUtil;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.builder.ExcelWriterSheetBuilder;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.alibaba.excel.write.metadata.fill.FillConfig;
import com.alibaba.excel.write.metadata.fill.FillWrapper;
import com.alibaba.excel.write.style.column.LongestMatchColumnWidthStyleStrategy;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Excel相关处理
 *
 * @author kuanghua
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class EasyExcelUtils {

    /**
     * 同步导入(适用于小数据量)
     *
     * @param is 输入流
     * @return 转换后集合
     */
    public static <T> List<T> importExcel(InputStream is, Class<T> clazz) {
        return EasyExcel.read(is).head(clazz).autoCloseStream(false).sheet().doReadSync();
    }


    //    /**
    //     * 使用校验监听器 异步导入 同步返回
    //     *
    //     * @param is         输入流
    //     * @param clazz      对象类型
    //     * @param isValidate 是否 Validator 检验 默认为是
    //     * @return 转换后集合
    //     */
    //    public static <T> ExcelResult<T> importExcel(InputStream is, Class<T> clazz, boolean isValidate) {
    //        DefaultExcelListener<T> listener = new DefaultExcelListener<>(isValidate);
    //        EasyExcel.read(is, clazz, listener).sheet().doRead();
    //        return listener.getExcelResult();
    //    }

    //    /**
    //     * 使用自定义监听器 异步导入 自定义返回
    //     *
    //     * @param is       输入流
    //     * @param clazz    对象类型
    //     * @param listener 自定义监听器
    //     * @return 转换后集合
    //     */
    //    public static <T> ExcelResult<T> importExcel(InputStream is, Class<T> clazz, ExcelListener<T> listener) {
    //        EasyExcel.read(is, clazz, listener).sheet().doRead();
    //        return listener.getExcelResult();
    //    }

    //    /**
    //     * 导出excel
    //     *
    //     * @param list      导出数据集合
    //     * @param sheetName 工作表的名称
    //     * @param clazz     实体类
    //     * @param response  响应体
    //     */

    public static <T> void exportExcel(List<T> list, String sheetName, Class<T> clazz, HttpServletResponse response) {
        String currentDate = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss").format(new Date());
        try {
            response.setHeader("Access-Control-Expose-Headers", "file-name");
            response.setHeader("file-name", URLEncoder.encode(currentDate + sheetName, "UTF-8") + ".xlsx");
            resetResponse(sheetName, response);
            ServletOutputStream os = response.getOutputStream();
            exportExcel(list, sheetName, clazz, false, os);
        } catch (IOException e) {
            throw new RuntimeException("导出Excel异常"+sheetName);
        }
    }

    /**
     * 导出excel
     *
     * @param list      导出数据集合
     * @param sheetName 工作表的名称
     * @param clazz     实体类
     * @param merge     是否合并单元格
     * @param response  响应体
     */
    //    public static <T> void exportExcel(List<T> list, String sheetName, Class<T> clazz, boolean merge, HttpServletResponse response) {
    //        try {
    //            resetResponse(sheetName, response);
    //            ServletOutputStream os = response.getOutputStream();
    //            exportExcel(list, sheetName, clazz, merge, os);
    //        } catch (IOException e) {
    //            throw new RuntimeException("导出Excel异常");
    //        }
    //    }

    /**
     * 导出excel
     *
     * @param list      导出数据集合
     * @param sheetName 工作表的名称
     * @param clazz     实体类
     * @param os        输出流
     */
//    public static <T> void exportExcel(List<T> list, String sheetName, Class<T> clazz, OutputStream os) {
//        exportExcel(list, sheetName, clazz, false, os);
//    }

//    /**
//     * 导出excel
//     *
//     * @param list      导出数据集合
//     * @param sheetName 工作表的名称
//     * @param clazz     实体类
//     * @param merge     是否合并单元格
//     * @param os        输出流
//     */
    public static <T> void exportExcel(List<T> list, String sheetName, Class<T> clazz, boolean merge, OutputStream os) {
        ExcelWriterSheetBuilder builder = EasyExcel.write(os, clazz)
                .autoCloseStream(false)
                // 自动适配
                .registerWriteHandler(new LongestMatchColumnWidthStyleStrategy())
                // 大数值自动转换 防止失真
               //.registerConverter(new ExcelBigNumberConvert())
                .sheet(sheetName);
        if (merge) {
            // 合并处理器
           //builder.registerWriteHandler(new CellMergeStrategy(list, true));
        }
        builder.doWrite(list);
    }

    /**
     * 单表多数据模板导出 模板格式为 {.属性}
     *
     * @param filename     文件名
     * @param templatePath 模板路径 resource 目录下的路径包括模板文件名
     *                     例如: excel/temp.xlsx
     *                     重点: 模板文件必须放置到启动类对应的 resource 目录下
     * @param data         模板需要的数据
     * @param response     响应体
     */
//    public static void exportTemplate(List<Object> data, String filename, String templatePath, HttpServletResponse response) {
//        try {
//            resetResponse(filename, response);
//            ServletOutputStream os = response.getOutputStream();
//            exportTemplate(data, templatePath, os);
//        } catch (IOException e) {
//            throw new RuntimeException("导出Excel异常");
//        }
//    }

    /**
     * 单表多数据模板导出 模板格式为 {.属性}
     *
     * @param templatePath 模板路径 resource 目录下的路径包括模板文件名
     *                     例如: excel/temp.xlsx
     *                     重点: 模板文件必须放置到启动类对应的 resource 目录下
     * @param data         模板需要的数据
     * @param os           输出流
     */
//    public static void exportTemplate(List<Object> data, String templatePath, OutputStream os) {
//        ClassPathResource templateResource = new ClassPathResource(templatePath);
//        ExcelWriter excelWriter = EasyExcel.write(os)
//                .withTemplate(templateResource.getStream())
//                .autoCloseStream(false)
//                // 大数值自动转换 防止失真
//                .registerConverter(new ExcelBigNumberConvert())
//                .build();
//        WriteSheet writeSheet = EasyExcel.writerSheet().build();
//        if (CollUtil.isEmpty(data)) {
//            throw new IllegalArgumentException("数据为空");
//        }
//        // 单表多数据导出 模板格式为 {.属性}
//        for (Object d : data) {
//            excelWriter.fill(d, writeSheet);
//        }
//        excelWriter.finish();
//    }

    /**
     * 多表多数据模板导出 模板格式为 {key.属性}
     *
     * @param filename     文件名
     * @param templatePath 模板路径 resource 目录下的路径包括模板文件名
     *                     例如: excel/temp.xlsx
     *                     重点: 模板文件必须放置到启动类对应的 resource 目录下
     * @param data         模板需要的数据
     * @param response     响应体
     */
//    public static void exportTemplateMultiList(Map<String, Object> data, String filename, String templatePath, HttpServletResponse response) {
//        try {
//            resetResponse(filename, response);
//            ServletOutputStream os = response.getOutputStream();
//            exportTemplateMultiList(data, templatePath, os);
//        } catch (IOException e) {
//            throw new RuntimeException("导出Excel异常");
//        }
//    }

    /**
     * 多表多数据模板导出 模板格式为 {key.属性}
     *
     * @param templatePath 模板路径 resource 目录下的路径包括模板文件名
     *                     例如: excel/temp.xlsx
     *                     重点: 模板文件必须放置到启动类对应的 resource 目录下
     * @param data         模板需要的数据
     * @param os           输出流
     */
//    public static void exportTemplateMultiList(Map<String, Object> data, String templatePath, OutputStream os) {
//        ClassPathResource templateResource = new ClassPathResource(templatePath);
//        ExcelWriter excelWriter = EasyExcel.write(os)
//                .withTemplate(templateResource.getStream())
//                .autoCloseStream(false)
//                // 大数值自动转换 防止失真
//                .registerConverter(new ExcelBigNumberConvert())
//                .build();
//        WriteSheet writeSheet = EasyExcel.writerSheet().build();
//        if (CollUtil.isEmpty(data)) {
//            throw new IllegalArgumentException("数据为空");
//        }
//        for (Map.Entry<String, Object> map : data.entrySet()) {
//            // 设置列表后续还有数据
//            FillConfig fillConfig = FillConfig.builder().forceNewRow(Boolean.TRUE).build();
//            if (map.getValue() instanceof Collection) {
//                // 多表导出必须使用 FillWrapper
//                excelWriter.fill(new FillWrapper(map.getKey(), (Collection<?>) map.getValue()), fillConfig, writeSheet);
//            } else {
//                excelWriter.fill(map.getValue(), writeSheet);
//            }
//        }
//        excelWriter.finish();
//    }

    /**
     * 重置响应体
     */
    private static void resetResponse(String sheetName, HttpServletResponse response) throws UnsupportedEncodingException {
        String filename = encodingFilename(sheetName);
        setAttachmentResponseHeader(response, filename);
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=UTF-8");
    }



    /**
     * 下载文件名重新编码
     *
     * @param response     响应对象
     * @param realFileName 真实文件名
     */
    public static void setAttachmentResponseHeader(HttpServletResponse response, String realFileName) throws UnsupportedEncodingException {
        String percentEncodedFileName = percentEncode(realFileName);

        StringBuilder contentDispositionValue = new StringBuilder();
        contentDispositionValue.append("attachment; filename=")
                .append(percentEncodedFileName)
                .append(";")
                .append("filename*=")
                .append("utf-8''")
                .append(percentEncodedFileName);

        response.addHeader("Access-Control-Expose-Headers", "Content-Disposition,download-filename");
        response.setHeader("Content-disposition", contentDispositionValue.toString());
        response.setHeader("download-filename", percentEncodedFileName);
    }

    /**
     * 百分号编码工具方法
     *
     * @param s 需要百分号编码的字符串
     * @return 百分号编码后的字符串
     */
    public static String percentEncode(String s) throws UnsupportedEncodingException {
        String encode = URLEncoder.encode(s, StandardCharsets.UTF_8.toString());
        return encode.replaceAll("\\+", "%20");
    }
    /**
     * 解析导出值 0=男,1=女,2=未知
     *
     * @param propertyValue 参数值
     * @param converterExp  翻译注解
     * @param separator     分隔符
     * @return 解析后值
     */
    public static String convertByExp(String propertyValue, String converterExp, String separator) {
        StringBuilder propertyString = new StringBuilder();
        String[] convertSource = converterExp.split(",");
        for (String item : convertSource) {
            String[] itemArray = item.split("=");
            if (StringUtils.containsAny(propertyValue, separator)) {
                for (String value : propertyValue.split(separator)) {
                    if (itemArray[0].equals(value)) {
                        propertyString.append(itemArray[1] + separator);
                        break;
                    }
                }
            } else {
                if (itemArray[0].equals(propertyValue)) {
                    return itemArray[1];
                }
            }
        }
        return StringUtils.stripEnd(propertyString.toString(), separator);
    }

    /**
     * 反向解析值 男=0,女=1,未知=2
     *
     * @param propertyValue 参数值
     * @param converterExp  翻译注解
     * @param separator     分隔符
     * @return 解析后值
     */
    public static String reverseByExp(String propertyValue, String converterExp, String separator) {
        StringBuilder propertyString = new StringBuilder();
        String[] convertSource = converterExp.split(",");
        for (String item : convertSource) {
            String[] itemArray = item.split("=");
            if (StringUtils.containsAny(propertyValue, separator)) {
                for (String value : propertyValue.split(separator)) {
                    if (itemArray[1].equals(value)) {
                        propertyString.append(itemArray[0] + separator);
                        break;
                    }
                }
            } else {
                if (itemArray[1].equals(propertyValue)) {
                    return itemArray[0];
                }
            }
        }
        return StringUtils.stripEnd(propertyString.toString(), separator);
    }
    /**
     * 编码文件名
     */
    public static String encodingFilename(String filename) {
        return IdUtil.fastSimpleUUID() + "_" + filename + ".xlsx";
    }
}
```



excel vo

```java
@Data
public class DemoExportVo implements Serializable {
    private static final long serialVersionUID = 1L;

    @ExcelProperty(value = "用户序号")
    private Integer userId;

    @ExcelProperty(value = "登录名称")
    private String userName;

    @ExcelProperty(value = "最后登录时间")
    private Date loginDate;
}
```



使用例子

```java
package top.hugo.admin.controller;

import cn.hutool.core.date.DateUtil;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import top.hugo.admin.utils.EasyExcelUtils;
import top.hugo.admin.vo.DemoExportVo;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 导出导入测试
 */
@RestController
@RequestMapping("excel")
public class TestExcelController {
    /**
     * 导入接口
     */
    @GetMapping("test")
    public void test(HttpServletResponse response) {
        ArrayList<DemoExportVo> listVo = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            DemoExportVo demoExportVo = new DemoExportVo();
            demoExportVo.setUserId(i+100000000);
            demoExportVo.setUserName(i+"熊猫哥");
            demoExportVo.setLoginDate(DateUtil.date());
            listVo.add(demoExportVo);
        }
        EasyExcelUtils.exportExcel(listVo, "用户数据", DemoExportVo.class, response);
    }
    @PostMapping(value = "import-file",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public void importFile(@RequestPart("file") MultipartFile file) throws IOException {
        List<DemoExportVo> demoExportVos = EasyExcelUtils.importExcel(file.getInputStream(), DemoExportVo.class);
        System.out.println(demoExportVos);
    }
}
```

