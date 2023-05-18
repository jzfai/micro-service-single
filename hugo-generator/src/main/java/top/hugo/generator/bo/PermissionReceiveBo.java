package top.hugo.generator.bo;

import lombok.Data;

import java.util.List;

/**
 * @Title: PermissionReceiveBo
 * @Description:
 * @Auther: kuanghua
 * @create 2022-10-21 16:52
 */
@Data
public class PermissionReceiveBo {
    private Integer plateFormId;
    private List<String> PermissionList;
}
