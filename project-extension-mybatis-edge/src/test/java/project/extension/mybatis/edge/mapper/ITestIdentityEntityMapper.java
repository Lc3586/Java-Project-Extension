package project.extension.mybatis.edge.mapper;

import org.apache.ibatis.annotations.Mapper;
import project.extension.mybatis.edge.entity.TestIdentityEntity;

/**
 * 测试自增主键Mapper接口类
 *
 * @author LCTR
 * @date 2023-03-01
 */
@Mapper
public interface ITestIdentityEntityMapper {
    /**
     * 新增数据
     */
    int insert(TestIdentityEntity data);

    /**
     * 获取数据
     *
     * @param id 主键
     */
    TestIdentityEntity getById(Long id);
}
