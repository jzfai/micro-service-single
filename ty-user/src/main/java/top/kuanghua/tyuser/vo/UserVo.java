package top.kuanghua.tyuser.vo;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * @Title: UserVo
 * @Description:
 * @Auther: kuanghua
 * @create 2020/12/15 19:32
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="多表分页测试")
public class UserVo {
    private Integer id;

    private String username;

    private String password;

    private String phone;

    private Date createTime;

    private Date updateTime;

    private String deleted;

    private String salt;
}
