


package top.hugo.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * 平台实体类
 *
 * @author 熊猫哥
 * @since 2022-10-07 16:33:13
 */
@Data
@TableName(value = "t_plate_form")
public class PlateForm extends Model<PlateForm> {
    private Integer id;
    @NotBlank(message = "name不能为空")
    private String name;
}
