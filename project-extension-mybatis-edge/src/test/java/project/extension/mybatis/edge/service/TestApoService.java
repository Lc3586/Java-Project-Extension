package project.extension.mybatis.edge.service;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import project.extension.mybatis.edge.annotations.NaiveDataSource;
import project.extension.mybatis.edge.entity.TestGeneralEntity;
import project.extension.mybatis.edge.mapper.ITestGeneralEntityMapper;

import java.util.List;

/**
 * 用于测试动态切换数据源的服务实现类
 *
 * @author LCTR
 * @date 2023-07-08
 */
@Service
@Scope("prototype")
@NaiveDataSource("postgresql")
public class TestApoService
        implements ITestApoService {
    public TestApoService(ITestGeneralEntityMapper testGeneralEntityMapper) {
        this.testGeneralEntityMapper = testGeneralEntityMapper;
    }

    private final ITestGeneralEntityMapper testGeneralEntityMapper;

    @Override
    @NaiveDataSource("mariadb")
    public void insert2MariaDB(TestGeneralEntity data) {
        testGeneralEntityMapper.insert2MariaDB(data);
    }

    @Override
    public void insert2Postgresql(TestGeneralEntity data) {
        testGeneralEntityMapper.insert2Postgresql(data);
    }

    @Override
    @NaiveDataSource("mariadb")
    public TestGeneralEntity getFromMariaDB(String id) {
        return testGeneralEntityMapper.getByIdFromMariaDB(id);
    }

    @Override
    public TestGeneralEntity getFromPostgresql(String id) {
        return testGeneralEntityMapper.getByIdFromPostgresql(id);
    }
}
