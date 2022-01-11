package top.kuanghua.tyexcel.service;

import com.alibaba.excel.context.AnalysisContext;

import com.alibaba.excel.enums.CellExtraTypeEnum;
import com.alibaba.excel.exception.ExcelDataConvertException;
import com.alibaba.excel.metadata.CellExtra;
import com.alibaba.excel.metadata.data.ReadCellData;
import com.alibaba.excel.read.listener.ReadListener;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import top.kuanghua.tyexcel.bo.RuiDeKqBo;
import com.alibaba.excel.EasyExcel;
import org.springframework.stereotype.Service;
import top.kuanghua.tyexcel.vo.ExcelCheckResult;


import java.io.InputStream;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.*;

;

/**
 * @Title: ExeclService
 * @Description:
 * @Auther: kuanghua
 * @create 2021/7/9 11:35
 */
@Service
@Slf4j
public class ExcelService {

    @Autowired
    private DaoService daoService;

    public ExcelCheckResult<RuiDeKqBo> importExcel(InputStream fileName) {

        List<String> errLogList = new ArrayList<>();
        List<RuiDeKqBo> successList = new ArrayList<>();

        EasyExcel.read(fileName, RuiDeKqBo.class, new ReadListener<RuiDeKqBo>() {
            /**
             * 单次缓存的数据量
             */
            public static final int BATCH_COUNT = 3000;
            /**
             *临时存储
             */
            private List<RuiDeKqBo> cachedData = new ArrayList<>(BATCH_COUNT);

            @Override
            public void onException(Exception exception, AnalysisContext context) throws Exception {
                log.error("解析失败，但是继续解析下一行:{}", exception.getMessage());
                // 如果是某一个单元格的转换异常 能获取到具体行号
                // 如果要获取头的信息 配合invokeHeadMap使用
                if (exception instanceof ExcelDataConvertException) {
                    ExcelDataConvertException excelDataConvertException = (ExcelDataConvertException) exception;

                    String formatString = MessageFormat.format("第{0}行，第{1}列解析异常",
                            excelDataConvertException.getRowIndex(), excelDataConvertException.getColumnIndex());

                    errLogList.add(formatString);

                }
            }

            @Override
            public void invokeHead(Map<Integer, ReadCellData<?>> headMap, AnalysisContext context) {
//                System.out.println("头数据");
//                System.out.println(JSON.toJSONString(headMap));
            }

            @Override
            public void invoke(RuiDeKqBo data, AnalysisContext context) {
                successList.add(data);
                cachedData.add(data);
                if (cachedData.size() >= BATCH_COUNT) {
                    saveData();
                    // 存储完成清理 list
                    cachedData = new ArrayList<>(BATCH_COUNT);
                }
            }
            @Override
            public void extra(CellExtra extra, AnalysisContext context) {
                log.info("读取到了一条额外信息:{}", JSON.toJSONString(extra));
                switch (extra.getType()) {
                    case COMMENT:
                        log.info("额外信息是批注,在rowIndex:{},columnIndex;{},内容是:{}", extra.getRowIndex(), extra.getColumnIndex(),
                                extra.getText());
                        break;
                    case HYPERLINK:
                        if ("Sheet1!A1".equals(extra.getText())) {
                            log.info("额外信息是超链接,在rowIndex:{},columnIndex;{},内容是:{}", extra.getRowIndex(),
                                    extra.getColumnIndex(), extra.getText());
                        } else if ("Sheet2!A1".equals(extra.getText())) {
                            log.info("额外信息是超链接,而且覆盖了一个区间,在firstRowIndex:{},firstColumnIndex;{},lastRowIndex:{},lastColumnIndex:{},"
                                            + "内容是:{}",
                                    extra.getFirstRowIndex(), extra.getFirstColumnIndex(), extra.getLastRowIndex(),
                                    extra.getLastColumnIndex(), extra.getText());
                        } else {
                            log.info("Unknown hyperlink!");
                        }
                        break;
                    case MERGE:
                        log.info(
                                "额外信息是超链接,而且覆盖了一个区间,在firstRowIndex:{},firstColumnIndex;{},lastRowIndex:{},lastColumnIndex:{}",
                                extra.getFirstRowIndex(), extra.getFirstColumnIndex(), extra.getLastRowIndex(),
                                extra.getLastColumnIndex());
                        break;
                    default:
                }

            }

            @Override
            public void doAfterAllAnalysed(AnalysisContext context) {
//                daoService.test();
//                saveData();
//                log.info(excelCheckResult.toString());
            }

            /**
             * 加上存储数据库
             */
            private void saveData() {
                log.info("{}条数据，开始存储数据库！", cachedData.size());
                log.info("存储数据库成功！");


            }
            //注：headRowNumber(1)如果不写会自动匹配
        })// 需要读取批注 默认不读取
                .extraRead(CellExtraTypeEnum.COMMENT)
                // 需要读取超链接 默认不读取
                .extraRead(CellExtraTypeEnum.HYPERLINK)
                // 需要读取合并单元格信息 默认不读取
                .extraRead(CellExtraTypeEnum.MERGE).sheet().headRowNumber(1).doRead();

        return new ExcelCheckResult<RuiDeKqBo>(errLogList, successList);
    }

    /*
     * 导出Excel
     * */
//    public void exportExcel() {
//        String  fileName = "D:\\construct\\micro-service-plus\\ty-excel\\src\\main\\java\\top\\kuanghua\\tyexcel\\" + System.currentTimeMillis() + ".xlsx";
//
//       Set<String> excludeColumnFiledNames = new HashSet<String>();
//        excludeColumnFiledNames.add("date");
//        // 这里 需要指定写用哪个class去写，然后写到第一个sheet，名字为模板 然后文件流会自动关闭
//        EasyExcel.write(fileName, ComplexHeadData.class).sheet("模板")
//                .doWrite(data());
//    }
}

