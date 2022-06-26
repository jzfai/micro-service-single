package top.kuanghua.integrationfront.excel.cv;

import com.alibaba.excel.converters.Converter;
import com.alibaba.excel.metadata.GlobalConfiguration;
import com.alibaba.excel.metadata.data.ReadCellData;
import com.alibaba.excel.metadata.data.WriteCellData;
import com.alibaba.excel.metadata.property.ExcelContentProperty;
import top.kuanghua.commonpom.utils.SelfObjUtils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class VciTimeCv implements Converter<Date> {
    @Override
    public Date convertToJavaData(ReadCellData cellData, ExcelContentProperty contentProperty, GlobalConfiguration globalConfiguration) throws Exception {
        String stringValue = cellData.getNumberValue().toString();
        String DATE_FORMAT = "yyyyMMdd";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DATE_FORMAT);
        Date parse = simpleDateFormat.parse(stringValue);
        return parse;
    }

    @Override
    public WriteCellData<String> convertToExcelData(Date value, ExcelContentProperty contentProperty, GlobalConfiguration globalConfiguration) throws Exception {
        return new WriteCellData<>(SelfObjUtils.formatDateTime(value));
    }

}