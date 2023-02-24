package project.extension.mybatis.edge.test;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import project.extension.mybatis.edge.core.provider.standard.INaiveSql;
import project.extension.mybatis.edge.common.AssertExtension;
import project.extension.mybatis.edge.common.TempDataExtension;
import project.extension.mybatis.edge.common.OrmObjectResolve;
import project.extension.mybatis.edge.configure.TestDataSourceConfigure;
import project.extension.mybatis.edge.dbContext.repository.IBaseRepository_Key;
import project.extension.mybatis.edge.entity.TestGeneralEntity;
import project.extension.mybatis.edge.entityFields.TGE_Fields;

/**
 * 400.基础Repository增删改查测试
 *
 * @author LCTR
 * @date 2023-01-12
 */
@Disabled
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class X400BasicsOfRepositoryCurdTest {
    /**
     * 注入
     */
    @BeforeEach
    public void injection() {
        OrmObjectResolve.injection();
    }

    /**
     * 清理数据
     */
    @AfterAll
    public static void clean() {
        TempDataExtension.clearUp();
    }

    /**
     * 测试新增与查询功能
     *
     * @param name 名称
     */
    @ParameterizedTest
    @MethodSource("project.extension.mybatis.edge.configure.TestDataSourceConfigure#getMultiTestDataSourceName")
    @DisplayName("400.测试新增与查询功能")
    @Order(400)
    public void _400(String name)
            throws
            Throwable {
        INaiveSql naiveSql = OrmObjectResolve.getOrm(TestDataSourceConfigure.getTestDataSource(name));

        TestGeneralEntity dataCreate = TempDataExtension.generateData(TestGeneralEntity.class);

        IBaseRepository_Key<TestGeneralEntity, String> repository_key
                = naiveSql.getRepository_Key(TestGeneralEntity.class,
                                             String.class);

        Assertions.assertNotNull(repository_key,
                                 "获取Repository_Key失败");

        repository_key.insert(dataCreate);

        System.out.printf("\r\n已新增数据，Id：%s\r\n",
                          dataCreate.getId());

        TestGeneralEntity dataCheckCreate = repository_key.getById(dataCreate.getId());

        Assertions.assertNotNull(dataCheckCreate,
                                 "查询新增的数据失败");

        TempDataExtension.putData(name,
                                  TestGeneralEntity.class,
                                  dataCreate.getId(),
                                  dataCreate);

        AssertExtension.assertEquals(dataCreate,
                                     dataCheckCreate,
                                     TGE_Fields.allFields);

        System.out.printf("\r\n已复查新增的数据，Id：%s\r\n",
                          dataCheckCreate.getId());
    }

    /**
     * 测试更新与查询功能
     *
     * @param name 名称
     */
    @ParameterizedTest
    @MethodSource("project.extension.mybatis.edge.configure.TestDataSourceConfigure#getMultiTestDataSourceName")
    @DisplayName("401.测试更新与查询功能")
    @Order(401)
    public void _401(String name)
            throws
            Throwable {
        INaiveSql naiveSql = OrmObjectResolve.getOrm(TestDataSourceConfigure.getTestDataSource(name));

        TestGeneralEntity tempData = TempDataExtension.getFirstData(name,
                                                                    TestGeneralEntity.class);

        TestGeneralEntity dataUpdate = TempDataExtension.generateData(TestGeneralEntity.class);

        dataUpdate.setId(tempData.getId());

        IBaseRepository_Key<TestGeneralEntity, String> repository_key
                = naiveSql.getRepository_Key(TestGeneralEntity.class,
                                             String.class);

        Assertions.assertNotNull(repository_key,
                                 "获取Repository_Key失败");

        repository_key.update(dataUpdate);

        System.out.printf("\r\n已更新数据，Id：%s\r\n",
                          dataUpdate.getId());

        TestGeneralEntity dataCheckUpdate = repository_key.getById(dataUpdate.getId());

        Assertions.assertNotNull(dataCheckUpdate,
                                 "查询更新的数据失败");

        TempDataExtension.putData(name,
                                  TestGeneralEntity.class,
                                  tempData.getId(),
                                  dataCheckUpdate);

        AssertExtension.assertEquals(dataUpdate,
                                     dataCheckUpdate,
                                     TGE_Fields.allFields);

        System.out.printf("\r\n已复查更新的数据，Id：%s\r\n",
                          dataCheckUpdate.getId());
    }

    /**
     * 测试删除与查询功能
     *
     * @param name 名称
     */
    @ParameterizedTest
    @MethodSource("project.extension.mybatis.edge.configure.TestDataSourceConfigure#getMultiTestDataSourceName")
    @DisplayName("402.测试删除与查询功能")
    @Order(402)
    public void _402(String name) {
        INaiveSql naiveSql = OrmObjectResolve.getOrm(TestDataSourceConfigure.getTestDataSource(name));

        TestGeneralEntity tempData = TempDataExtension.getFirstData(name,
                                                                    TestGeneralEntity.class);

        IBaseRepository_Key<TestGeneralEntity, String> repository_key
                = naiveSql.getRepository_Key(TestGeneralEntity.class,
                                             String.class);

        Assertions.assertNotNull(repository_key,
                                 "获取Repository_Key失败");

        repository_key.delete(tempData);

        System.out.printf("\r\n已删除数据，Id：%s\r\n",
                          tempData.getId());

        TestGeneralEntity dataCheckDelete = repository_key.getById(tempData.getId());

        Assertions.assertNull(dataCheckDelete,
                              "数据未删除");

        TempDataExtension.removeData(name,
                                     TestGeneralEntity.class,
                                     tempData.getId());

        System.out.printf("\r\n已复查删除的数据，Id：%s\r\n",
                          tempData.getId());
    }
}