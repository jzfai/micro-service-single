package top.kuanghua.khcomomon.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Title: CommonParams
 * @Description:
 * @Auther: kuanghua
 * @create 2020/12/15 12:01
 */
@Data
@ApiModel
public class KHCommonPageParams {

    @ApiModelProperty("页数")
    private  Integer pageSize;

    @ApiModelProperty("页码")
    private  Integer pageNum;
    public Integer getPageSize() {
        if(pageSize ==null ){
            return 10;
        }else{
            return pageSize;
        }
    }
    public Integer getPageNum() {
        if(pageSize ==null){
            return 1;
        }else{
            return pageNum;
        }
    }
}
