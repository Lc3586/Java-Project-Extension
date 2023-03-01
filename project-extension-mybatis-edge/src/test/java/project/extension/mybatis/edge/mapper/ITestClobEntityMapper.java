package project.extension.mybatis.edge.mapper;

import org.apache.ibatis.annotations.Mapper;
import project.extension.mybatis.edge.entity.TestClobEntity;

/**
 * 测试读写长文本数据Mapper接口类
 *
 * @author LCTR
 * @date 2023-03-01
 */
@Mapper
public interface ITestClobEntityMapper {
    /**
     * 新增数据
     */
    int insert(TestClobEntity data);

    /**
     * 获取数据
     *
     * @param id 主键
     */
    TestClobEntity getById(String id);
}
