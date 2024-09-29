package top.hugo.admin.mapper;

import top.hugo.admin.domain.TestPrams;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface TestUserMapper {
    List<Map> generatorTest(@Param("testPrams") TestPrams testPrams);
}
