package top.hugo.web.controller.bo;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * 品牌表实体类2
 */
@Data
public class BrandBo {
    private Integer id;
    // 用户名
    @NotBlank(message = "name不能为空")
    private String name;
    /**
     * image 图片
     */
    private String image;

    /**
    * letter不能为空
    * */
    @NotBlank(message = "letter不能为空")
    private String letter;
    @NotBlank(message = "seq不能为空")
    private String seq;
}
