package top.hugo.admin.mapper;

import org.apache.ibatis.annotations.Param;
import top.hugo.admin.domain.TestPrams;

import java.util.List;
import java.util.Map;

public interface TestUserMapper {
    List<Map> generatorTest(@Param("testPrams") TestPrams testPrams);
}
