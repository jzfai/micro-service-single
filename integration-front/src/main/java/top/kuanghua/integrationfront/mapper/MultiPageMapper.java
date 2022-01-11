package top.kuanghua.integrationfront.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Select;

import java.util.Map;
/**
 * @Title: MultiPageMapper
 * @Description:
 * @Auther: kuanghua
 * @create 2020/12/15 19:34
 */
public interface MultiPageMapper {
    @Select("select phone,username,create_time from user")
    Page<Map<String,Object>> queryAllUsers(Page<Map<String, Object>> pagination);
}
