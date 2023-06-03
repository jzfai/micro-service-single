






package top.hugo.system.query;
import lombok.Data;
import top.hugo.common.domain.PageAndTimeRangeQuery;

import java.util.Date;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
/**
 *  平台管理请求接收类
 *
 * @author 邝华
 * @since 2022-12-16 14:42:22
 */
@Data
public class SysPlatformQuery extends PageAndTimeRangeQuery {
    /**
     * 平台的名字
     */
    @Pattern(regexp = "^0?1[0-9]{10}$",message = "name输入有误-手机号")
    private String name;
}
