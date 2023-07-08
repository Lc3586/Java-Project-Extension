package project.extension.mybatis.edge.mapper;

import org.apache.ibatis.annotations.Mapper;
import project.extension.mybatis.edge.entity.TestGeneralEntity;

import java.util.Collection;
import java.util.List;

/**
 * 测试读写常规数据Mapper接口类
 *
 * @author LCTR
 * @date 2023-02-06
 */
@Mapper
public interface ITestGeneralEntityMapper {
    /**
     * 获取数据集合
     *
     * @param data4Search 搜索参数
     */
    List<TestGeneralEntity> list(TestGeneralEntity data4Search);

    /**
     * 获取数据
     *
     * @param id 主键
     */
    TestGeneralEntity getById(String id);

    /**
     * 新增数据
     */
    int insert(TestGeneralEntity data);

    /**
     * 更新数据
     */
    int update(TestGeneralEntity data);

    /**
     * 删除数据
     *
     * @param id 主键
     */
    int deleteById(String id);

    /**
     * 批量删除数据
     *
     * @param ids 主键集合
     */
    int deleteByIds(Collection<String> ids);

    /**
     * 新增数据至MariaDB
     */
    int insert2MariaDB(TestGeneralEntity data);

    /**
     * 新增数据至Postgresql
     */
    int insert2Postgresql(TestGeneralEntity data);

    /**
     * 从MariaDB获取数据
     *
     * @param id 主键
     */
    TestGeneralEntity getByIdFromMariaDB(String id);

    /**
     * 从Postgresql获取数据
     *
     * @param id 主键
     */
    TestGeneralEntity getByIdFromPostgresql(String id);
}
