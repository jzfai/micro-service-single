package top.kuanghua.integrationfront.service;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.exception.ExcelDataConvertException;
import com.alibaba.excel.read.listener.ReadListener;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.stereotype.Service;
import top.kuanghua.integrationfront.entity.Vci;
import top.kuanghua.integrationfront.excel.imp.VciExcelImp;
import top.kuanghua.integrationfront.mapper.VciMapper;
import top.kuanghua.integrationfront.vo.ExcelCheckResult;

import javax.annotation.Resource;
import java.io.InputStream;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * (Vci)
 *
 * @author kuanghua
 * @since 2021-10-20 16:14:17
 */
@Service
public class VciService {

    @Resource
    private VciMapper vciMapper;

    public Page<Vci> selectPage(Integer pageNum, Integer pageSize, QueryWrapper<Vci> queryWrapper) {
        return this.vciMapper.selectPage(new Page<Vci>(pageNum, pageSize), queryWrapper);
    }

    public Vci selectById(Integer id) {
        return this.vciMapper.selectById(id);
    }

    public List<Vci> selectBatchIds(List<Integer> idList) {
        return this.vciMapper.selectBatchIds(idList);
    }

    public int insert(Vci vci) {
        return this.vciMapper.insert(vci);
    }

    public int updateById(Vci vci) {
        return this.vciMapper.updateById(vci);
    }

    public int deleteById(Integer id) {
        return this.vciMapper.deleteById(id);
    }

    public int deleteBatchIds(List<Long> idList) {
        return this.vciMapper.deleteBatchIds(idList);
    }

    /**
     * 导入文件校验
     * @param fileName
     * @return
     */
    public ExcelCheckResult<VciExcelImp> validExcel(InputStream fileName) {
        List<String> errLogList = new ArrayList<>();
        List<VciExcelImp> successList = new ArrayList<>();
        EasyExcel.read(fileName, VciExcelImp.class, new ReadListener<VciExcelImp>() {
            /**
             * 单次缓存的数据量
             */
            static final int BATCH_COUNT = 3000;
            /**
             * 临时存储
             */
            private List<VciExcelImp> cachedData = new ArrayList<>(BATCH_COUNT);

            @Override
            public void onException(Exception exception, AnalysisContext context) throws Exception {
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
            public void invoke(VciExcelImp data, AnalysisContext context) {
                successList.add(data);
                cachedData.add(data);
                if (cachedData.size() >= BATCH_COUNT) {
                    saveData();
                    // 存储完成清理 list
                    cachedData = new ArrayList<>(BATCH_COUNT);
                }
            }

            @Override
            public void doAfterAllAnalysed(AnalysisContext context) {

            }

            /**
             * 加上存储数据库
             */
            private void saveData() {


            }
        }).sheet().headRowNumber(1).doRead();
        return new ExcelCheckResult<>(errLogList, successList);
    }

    /**
     * 导入
     * @param listData
     */
    public void importExcel(List<VciExcelImp>  listData){
        listData.forEach((item) -> {
            //售后维修表
            Vci vci = JSON.parseObject(JSON.toJSONString(item), Vci.class);
            vciMapper.insert(vci);
        });
    }
}
