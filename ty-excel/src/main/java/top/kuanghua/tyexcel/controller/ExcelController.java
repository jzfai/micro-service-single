package top.kuanghua.tyexcel.controller;

import com.alibaba.excel.EasyExcel;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import springfox.documentation.annotations.ApiIgnore;
import top.kuanghua.khcomomon.entity.ResResult;
import top.kuanghua.khcomomon.utils.ObjectUtilsSelf;
import top.kuanghua.tyexcel.bo.ComplexHeadData;
import top.kuanghua.tyexcel.bo.DemoData;
import top.kuanghua.tyexcel.bo.RuiDeKqBo;
import top.kuanghua.tyexcel.service.ExcelService;
import top.kuanghua.tyexcel.vo.ExcelCheckResult;


import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Title: ExcelController
 * @Description:
 * @Auther: kuanghua
 * @create 2021/7/9 11:37
 */
@RestController
@RequestMapping("excel")
@Api(tags="文档导入相关")
@Slf4j
public class ExcelController {

    @Autowired
    private ExcelService excelService;

    @ApiOperation("导入excel")
    @PostMapping("importExcel")
    public ResResult importExcel(@RequestParam("file") MultipartFile file) throws IOException {
        ExcelCheckResult<RuiDeKqBo> excelCheckResult = excelService.importExcel(file.getInputStream());
        return new ResResult().success(excelCheckResult);
    }

    private List<DemoData> data() {
        List<DemoData> list = new ArrayList<DemoData>();
        for (int i = 0; i < 10; i++) {
            DemoData data = new DemoData();
            data.setString("字符串" + i);
            data.setDate(new Date());
            data.setDoubleData(0.56);
            list.add(data);
        }
        return list;
    }

    @ApiOperation("导出excel")
    @PostMapping("exportExcel")
    public void exportExcel(HttpServletResponse response) throws IOException {

        // 这里注意 有同学反应使用swagger 会导致各种问题，请直接用浏览器或者用postman
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setCharacterEncoding("utf-8");
        // 这里URLEncoder.encode可以防止中文乱码 当然和easyexcel没有关系
        String fileName = URLEncoder.encode("测试", "UTF-8").replaceAll("\\+", "%20");
        response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");
        EasyExcel.write(response.getOutputStream(), ComplexHeadData.class).sheet("模板").doWrite(data());

//        excelService.exportExcel();
//        return new ResResult().success();
    }
}
