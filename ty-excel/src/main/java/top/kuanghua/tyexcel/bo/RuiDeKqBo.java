package top.kuanghua.tyexcel.bo;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.format.DateTimeFormat;
import lombok.Data;

import java.util.Date;

/**
 * @Title: RuiDeKqBo
 * @Description:
 * @Auther: kuanghua
 * @create 2021/7/9 10:27
 */
@Data
public class RuiDeKqBo {
    @ExcelProperty(value = "部门",converter = CustomStringStringConverter.class)
    private String department;
    @ExcelProperty("姓名")
    private String username;
    @ExcelProperty("日期")
    @DateTimeFormat("yyyy年")
    private Date dateTime;
    @ExcelProperty("签到时间")
    private String checkInTime;
    private String row1;
    private String row2;
}
