package top.kuanghua.tyuser.excel.cv;

import com.alibaba.excel.converters.Converter;
import com.alibaba.excel.metadata.GlobalConfiguration;
import com.alibaba.excel.metadata.data.ReadCellData;
import com.alibaba.excel.metadata.data.WriteCellData;
import com.alibaba.excel.metadata.property.ExcelContentProperty;

public class VciStatusImpCv implements Converter<Integer> {
    @Override
    public Integer convertToJavaData(ReadCellData cellData, ExcelContentProperty contentProperty, GlobalConfiguration globalConfiguration) throws Exception {
        String stringValue = cellData.getStringValue();
        int csId;
        if("未入库".equals(stringValue)){
            csId=0;
        }else if("已入库".equals(stringValue)){
            csId=1;
        }else{
            csId=2;
        }
        return csId;
    }

    //将java的数据类型转为excel数据类型
    @Override
    public WriteCellData<String> convertToExcelData(Integer value, ExcelContentProperty contentProperty, GlobalConfiguration globalConfiguration) throws Exception {
        String cv="";
        if(0==value){
            cv="未入库";
        }else{
            cv="已入库";
        }
        return new WriteCellData<>(cv);
    }
}