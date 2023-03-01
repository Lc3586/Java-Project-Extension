package project.extension.mybatis.edge.mapper;

import org.apache.ibatis.annotations.Mapper;
import project.extension.mybatis.edge.entity.TestBlobEntity;

/**
 * 测试读写文件数据Mapper接口类
 *
 * @author LCTR
 * @date 2023-03-01
 */
@Mapper
public interface ITestBlobEntityMapper {
    /**
     * 新增数据
     */
    int insert(TestBlobEntity data);

    /**
     * 获取数据
     *
     * @param id 主键
     */
    TestBlobEntity getById(String id);
}
