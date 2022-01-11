package top.kuanghua.integrationfront.vo;

import lombok.Data;

import java.util.List;

/**
 * @Title: ExcelCheckResult
 * @Description:
 * @Auther: kuanghua
 * @create 2021-11-02 11:33
 */

@Data
public class ExcelCheckResult<T> {
    private int errNum;
    private int successNum;
    private boolean checkResult;
    private List<String> errList;
    private List<T> successList;

    public ExcelCheckResult(List<String> errList, List<T> successList) {
        this.errList = errList;
        this.successList = successList;
        this.errNum=errList.size();
        this.successNum=successList.size();
        this.checkResult=errList.size()==0;
    }
}
