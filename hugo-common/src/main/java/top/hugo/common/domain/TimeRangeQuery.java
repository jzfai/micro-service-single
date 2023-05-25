package top.hugo.common.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
 * 查询积累
 *
 * @author kuanghua
 */

@Data
public class TimeRangeQuery implements Serializable {

    private static final long serialVersionUID = 1L;
    /**
     * 开始时间
     */
    @Schema(hidden = true)
    private String beginTime;

    /**
     * 结束时间
     */
    @Schema(hidden = true)
    private String endTime;

}
