package top.hugo.generator.domain;

import lombok.Data;

/**
 * @Title: CommonParams
 * @Description:
 * @Auther: kuanghua
 * @create 2020/12/15 12:01
 */
@Data
public class CommonPageParams {


    /**
     * 页数（默认10）
     */
    private Integer pageSize;

    /**
     * 页码（默认1）
     */
    private Integer pageNum;

    public Integer getPageSize() {
        if (pageSize == null) {
            return 10;
        } else {
            return pageSize;
        }
    }

    public Integer getPageNum() {
        if (pageSize == null) {
            return 1;
        } else {
            return pageNum;
        }
    }
}
