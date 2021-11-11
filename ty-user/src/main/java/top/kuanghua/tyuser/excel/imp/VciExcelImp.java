package top.kuanghua.tyuser.excel.imp;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;
import top.kuanghua.tyuser.excel.cv.VciStatusImpCv;
import top.kuanghua.tyuser.excel.cv.VciTimeCv;

import java.util.Date;

/**
 * VCI设备表(Vci)表实体类
 *
 * @author kuanghua
 * @since 2021-10-22 10:34:43
 */
@Data
public class VciExcelImp{
    //供应商
    @ExcelProperty(value = "供应商")
    private String supplier;
    // 商品名称
    // 设备号
    @ExcelProperty(value = "设备号")
    private String sn;
    // 硬件版本
    @ExcelProperty(value = "硬件版本")
    private String hardVersion;
    // 商品规格
    @ExcelProperty(value = "商品规格")
    private String productSpec;
    // 入库单号（批次号）
    @ExcelProperty(value = "入库单号（批次号）")
    private String receiptNo;
    // 入库状态
    @ExcelProperty(value = "入库状态",converter = VciStatusImpCv.class)
    private Integer status;
    // 入库时间
    @ExcelProperty(value = "入库时间",converter = VciTimeCv.class)
    private Date createTime;
}

