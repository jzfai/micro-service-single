package top.hugo.domain;
import lombok.Data;

/**
 * @Title: CommonParams
 * @Description:
 * @Auther: kuanghua
 * @create 2020/12/15 12:01
 */
@Data
public class CommonPageParams {

    private Integer pageSize;

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
