package top.kuanghua.integrationfront.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import top.kuanghua.integrationfront.entity.Admin;

import java.util.Map;

/**
 * 用户列表(Admin)表数据库访问层
 *
 * @author kuanghua
 * @since 2021-03-19 10:45:42
 */
public interface AdminMapper extends BaseMapper<Admin> {

//    List<Admin> selectListAdmin(Admin admin);
    Page<Map<String,Object>> selectListAdmin(Page<Map<String, Object>> pagination, @Param("admin") Admin admin);

}
