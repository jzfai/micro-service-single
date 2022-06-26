package top.kuanghua.integrationfront.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.Map;

/**
 * 多表中实体类的注释Mapper
 *
 * @author 熊猫哥
 * @since 2022-06-25 10:32:12
 */
public interface PairmentMapper {
    Page<Map> selectPairment(Page<Map> pagination, @Param("params") Map params);

    Map selectByKey(@Param("sn") String sn);
}
