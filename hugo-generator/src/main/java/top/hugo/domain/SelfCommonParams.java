package top.hugo.domain;

import lombok.Data;

/**
 * @Title: CommonParams
 * @Description:
 * @Auther: kuanghua
 * @create 2020/12/15 12:01
 */
@Data
public class SelfCommonParams extends CommonPageParams {
    private String startTime;
    private String endTime;
}
