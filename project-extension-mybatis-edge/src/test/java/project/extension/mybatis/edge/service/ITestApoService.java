package project.extension.mybatis.edge.service;

import project.extension.mybatis.edge.entity.TestGeneralEntity;

/**
 * 用于测试动态切换数据源的服务接口类
 *
 * @author LCTR
 * @date 2023-07-08
 */
public interface ITestApoService {
    /**
     * 数据插入至MariaDB
     */
    void insert2MariaDB(TestGeneralEntity data);

    /**
     * 数据插入至Postgresql
     */
    void insert2Postgresql(TestGeneralEntity data);

    /**
     * 获取来自MariaDB的数据
     */
    TestGeneralEntity getFromMariaDB(String id);

    /**
     * 获取来自PostgresqlDB的数据
     */
    TestGeneralEntity getFromPostgresql(String id);
}
