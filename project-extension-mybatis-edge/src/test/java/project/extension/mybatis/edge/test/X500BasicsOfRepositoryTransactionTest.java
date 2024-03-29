package project.extension.mybatis.edge.test;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.test.annotation.Commit;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.transaction.annotation.Transactional;
import project.extension.mybatis.edge.core.provider.standard.INaiveSql;
import project.extension.mybatis.edge.common.ExceptionExtension;
import project.extension.mybatis.edge.common.NaiveTransactionalTestExecutionListener;
import project.extension.mybatis.edge.common.TempDataExtension;
import project.extension.mybatis.edge.common.OrmObjectResolve;
import project.extension.mybatis.edge.configure.TestDataSourceConfigure;
import project.extension.mybatis.edge.dbContext.repository.IBaseRepository_Key;
import project.extension.mybatis.edge.entity.TestGeneralEntity;
import project.extension.mybatis.edge.entityFields.TGE_Fields;
import project.extension.mybatis.edge.model.FilterCompare;
import project.extension.standard.exception.BusinessException;
import project.extension.standard.exception.ModuleException;
import project.extension.tuple.Tuple2;

/**
 * 500.基础Repository事务测试
 *
 * @author LCTR
 * @date 2023-01-12
 */
@Disabled
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestExecutionListeners({NaiveTransactionalTestExecutionListener.class})
//@SpringBootTest(classes = SpringBootTestApplication.class)
public class X500BasicsOfRepositoryTransactionTest {
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
     * 测试编程式事务提交操作
     *
     * @param name 名称
     */
    @ParameterizedTest
    @MethodSource("project.extension.mybatis.edge.configure.TestDataSourceConfigure#getMultiTestDataSourceName")
    @DisplayName("500.测试编程式事务提交操作")
    @Order(500)
    public void _500(String name) {
        INaiveSql naiveSql = OrmObjectResolve.getOrm(TestDataSourceConfigure.getTestDataSource(name));

        TestGeneralEntity dataCreate = TempDataExtension.generateData(TestGeneralEntity.class);

        IBaseRepository_Key<TestGeneralEntity, String> repository_key
                = naiveSql.getRepository_Key(TestGeneralEntity.class,
                                             String.class);

        Assertions.assertNotNull(repository_key,
                                 "获取Repository_Key失败");

        Tuple2<Boolean, Exception> tranCreate = repository_key.transaction(() -> {
            repository_key.insert(dataCreate);

            TempDataExtension.putData(name,
                                      TestGeneralEntity.class,
                                      dataCreate.getId(),
                                      dataCreate);

            System.out.printf("\r\n已新增数据，Id：%s\r\n",
                              dataCreate.getId());
        });

        if (!tranCreate.a)
            Assertions.fail("事务未提交",
                            tranCreate.b);

        System.out.println("\r\n事务已提交");

        TestGeneralEntity dataCheckCreate = repository_key.getById(dataCreate.getId());

        Assertions.assertNotNull(dataCheckCreate,
                                 "事务提交后未能查询到新增的数据");

        TempDataExtension.cleanData(name,
                                    TestGeneralEntity.class,
                                    dataCreate.getId());

        System.out.println("\r\n事务提交成功");
    }

    /**
     * 测试编程式事务回滚操作
     *
     * @param name 名称
     */
    @ParameterizedTest
    @MethodSource("project.extension.mybatis.edge.configure.TestDataSourceConfigure#getMultiTestDataSourceName")
    @DisplayName("501.测试编程式事务回滚操作")
    @Order(501)
    public void _501(String name) {
        INaiveSql naiveSql = OrmObjectResolve.getOrm(TestDataSourceConfigure.getTestDataSource(name));

        TestGeneralEntity dataCreate = TempDataExtension.generateData(TestGeneralEntity.class);

        IBaseRepository_Key<TestGeneralEntity, String> repository_key
                = naiveSql.getRepository_Key(TestGeneralEntity.class,
                                             String.class);

        Assertions.assertNotNull(repository_key,
                                 "获取Repository_Key失败");

        Tuple2<Boolean, Exception> tranCreate = repository_key.transaction(() -> {
            repository_key.insert(dataCreate);

            TempDataExtension.putData(name,
                                      TestGeneralEntity.class,
                                      dataCreate.getId(),
                                      dataCreate);

            System.out.printf("\r\n已新增数据，Id：%s\r\n",
                              dataCreate.getId());

            System.out.println("\r\n回滚事务");
            throw new ModuleException("回滚事务");
        });

        Assertions.assertEquals(false,
                                tranCreate.a,
                                "事务未回滚");

        System.out.println("\r\n事务已回滚");

        TestGeneralEntity dataCheckCreate = repository_key.getById(dataCreate.getId());

        Assertions.assertNull(dataCheckCreate,
                              "事务回滚后依然能查询到新增的数据");

        TempDataExtension.removeData(name,
                                     TestGeneralEntity.class,
                                     dataCreate.getId());

        System.out.println("\r\n事务回滚成功");
    }

    /**
     * 测试编程式事务嵌套提交操作
     *
     * @param name 名称
     */
    @ParameterizedTest
    @MethodSource("project.extension.mybatis.edge.configure.TestDataSourceConfigure#getMultiTestDataSourceName")
    @DisplayName("510.测试编程式事务嵌套提交操作")
    @Order(510)
    public void _510(String name) {
        INaiveSql naiveSql = OrmObjectResolve.getOrm(TestDataSourceConfigure.getTestDataSource(name));

        TestGeneralEntity dataCreate1 = TempDataExtension.generateData(TestGeneralEntity.class);

        TestGeneralEntity dataCreate2 = TempDataExtension.generateData(TestGeneralEntity.class);

        IBaseRepository_Key<TestGeneralEntity, String> repository_key
                = naiveSql.getRepository_Key(TestGeneralEntity.class,
                                             String.class);

        Assertions.assertNotNull(repository_key,
                                 "获取Repository_Key失败");

        Tuple2<Boolean, Exception> tranCreate1 = repository_key.transaction(() -> {
            repository_key.insert(dataCreate1);

            TempDataExtension.putData(name,
                                      TestGeneralEntity.class,
                                      dataCreate1.getId(),
                                      dataCreate1);

            System.out.printf("\r\n已新增数据1，Id：%s\r\n",
                              dataCreate1.getId());

            Tuple2<Boolean, Exception> tranCreate2 = repository_key.transaction(() -> {
                repository_key.insert(dataCreate2);

                TempDataExtension.putData(name,
                                          TestGeneralEntity.class,
                                          dataCreate2.getId(),
                                          dataCreate2);

                System.out.printf("\r\n已新增数据2，Id：%s\r\n",
                                  dataCreate2.getId());
            });

            Assertions.assertEquals(true,
                                    tranCreate2.a,
                                    "事务2未提交");

            System.out.println("\r\n事务2已提交");
        });

        Assertions.assertEquals(true,
                                tranCreate1.a,
                                "事务1未提交");

        System.out.println("\r\n事务1已提交");

        TestGeneralEntity dataCheckCreate1 = repository_key.getById(dataCreate1.getId());

        Assertions.assertNotNull(dataCheckCreate1,
                                 "事务提交后未能查询到新增的数据1");

        TempDataExtension.cleanData(name,
                                    TestGeneralEntity.class,
                                    dataCreate1.getId());

        TestGeneralEntity dataCheckCreate2 = repository_key.getById(dataCreate2.getId());

        Assertions.assertNotNull(dataCheckCreate2,
                                 "事务提交后未能查询到新增的数据2");

        TempDataExtension.cleanData(name,
                                    TestGeneralEntity.class,
                                    dataCreate2.getId());

        System.out.println("\r\n事务提交成功");
    }

    /**
     * 测试声明式事务提交操作（提交）
     *
     * @param name 名称
     */
    @ParameterizedTest
    @MethodSource("project.extension.mybatis.edge.configure.TestDataSourceConfigure#getMultiTestDataSourceName")
    @Transactional
    @Commit
    @DisplayName("520.测试声明式事务提交操作（提交）")
    @Order(520)
    public void _520(String name) {
        TempDataExtension.putThreadTransaction(Thread.currentThread()
                                                     .getId(),
                                               name);

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

        TempDataExtension.putData(name,
                                  TestGeneralEntity.class,
                                  dataCreate.getId(),
                                  dataCreate);
    }

    /**
     * 测试声明式事务提交操作
     *
     * @param name 名称
     */
    @ParameterizedTest
    @MethodSource("project.extension.mybatis.edge.configure.TestDataSourceConfigure#getMultiTestDataSourceName")
    @DisplayName("521.测试声明式事务提交操作")
    @Order(521)
    public void _521(String name) {
        INaiveSql naiveSql = OrmObjectResolve.getOrm(TestDataSourceConfigure.getTestDataSource(name));

        TestGeneralEntity tempData = TempDataExtension.getFirstData(name,
                                                                    TestGeneralEntity.class);

        IBaseRepository_Key<TestGeneralEntity, String> repository_key
                = naiveSql.getRepository_Key(TestGeneralEntity.class,
                                             String.class);

        Assertions.assertNotNull(repository_key,
                                 "获取Repository_Key失败");

        TestGeneralEntity dataCheckCreate = repository_key.getById(tempData.getId());

        Assertions.assertNotNull(dataCheckCreate,
                                 "事务提交后未能查询到新增的数据");

        System.out.printf("\r\n已查询到新增数据，Id：%s，事务提交成功\r\n",
                          tempData.getId());

        TempDataExtension.cleanData(name,
                                    TestGeneralEntity.class,
                                    tempData.getId());
    }

    /**
     * 测试声明式事务回滚操作（回滚）
     *
     * @param name 名称
     */
    @ParameterizedTest
    @MethodSource("project.extension.mybatis.edge.configure.TestDataSourceConfigure#getMultiTestDataSourceName")
    @Transactional
    @Rollback
    @DisplayName("522.测试声明式事务回滚操作（回滚）")
    @Order(522)
    public void _522(String name) {
        TempDataExtension.putThreadTransaction(Thread.currentThread()
                                                     .getId(),
                                               name);

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

        TempDataExtension.putData(name,
                                  TestGeneralEntity.class,
                                  dataCreate.getId(),
                                  dataCreate);
    }

    /**
     * 测试声明式事务回滚操作
     *
     * @param name 名称
     */
    @ParameterizedTest
    @MethodSource("project.extension.mybatis.edge.configure.TestDataSourceConfigure#getMultiTestDataSourceName")
    @DisplayName("523.测试声明式事务回滚操作")
    @Order(523)
    public void _523(String name) {
        INaiveSql naiveSql = OrmObjectResolve.getOrm(TestDataSourceConfigure.getTestDataSource(name));

        TestGeneralEntity tempData = TempDataExtension.getFirstData(name,
                                                                    TestGeneralEntity.class);

        IBaseRepository_Key<TestGeneralEntity, String> repository_key
                = naiveSql.getRepository_Key(TestGeneralEntity.class,
                                             String.class);

        Assertions.assertNotNull(repository_key,
                                 "获取Repository_Key失败");

        TestGeneralEntity dataCheckCreate = repository_key.getById(tempData.getId());

        Assertions.assertNull(dataCheckCreate,
                              "事务回滚后依然能查询到新增的数据");

        TempDataExtension.removeData(name,
                                     TestGeneralEntity.class,
                                     tempData.getId());

        System.out.printf("\r\n未查询到新增数据，Id：%s，事务回滚成功\r\n",
                          tempData.getId());
    }

    /**
     * 测试声明式事务嵌套编程式事务提交操作（提交）
     *
     * @param name 名称
     */
    @ParameterizedTest
    @MethodSource("project.extension.mybatis.edge.configure.TestDataSourceConfigure#getMultiTestDataSourceName")
//    @BeforeTestMethod
//    @BeforeTestExecution
//    @BeforeTransaction
    @Transactional
    @Commit
    @DisplayName("530.测试声明式事务嵌套编程式事务提交操作（提交）")
    @Order(530)
    public void _530(String name) {
        TempDataExtension.putThreadTransaction(Thread.currentThread()
                                                     .getId(),
                                               name);

        INaiveSql naiveSql = OrmObjectResolve.getOrm(TestDataSourceConfigure.getTestDataSource(name));

        TestGeneralEntity dataCreate = TempDataExtension.generateData(TestGeneralEntity.class);

        IBaseRepository_Key<TestGeneralEntity, String> repository_key
                = naiveSql.getRepository_Key(TestGeneralEntity.class,
                                             String.class);

        Assertions.assertNotNull(repository_key,
                                 "获取Repository_Key失败");

        Tuple2<Boolean, Exception> tranCreate = repository_key.transaction(() -> {
            repository_key.insert(dataCreate);

            System.out.printf("\r\n已新增数据，Id：%s\r\n",
                              dataCreate.getId());

            TempDataExtension.putData(name,
                                      TestGeneralEntity.class,
                                      dataCreate.getId(),
                                      dataCreate);
        });

        Assertions.assertEquals(true,
                                tranCreate.a,
                                "编程式事务未提交");

        System.out.println("\r\n编程式事务已提交");
    }

    /**
     * 测试声明式事务嵌套编程式事务提交操作
     *
     * @param name 名称
     */
    @ParameterizedTest
    @MethodSource("project.extension.mybatis.edge.configure.TestDataSourceConfigure#getMultiTestDataSourceName")
    @DisplayName("531.测试声明式事务嵌套编程式事务提交操作")
    @Order(531)
    public void _531(String name) {
        INaiveSql naiveSql = OrmObjectResolve.getOrm(TestDataSourceConfigure.getTestDataSource(name));

        TestGeneralEntity tempData = TempDataExtension.getFirstData(name,
                                                                    TestGeneralEntity.class);

        TestGeneralEntity dataCheckCreate = naiveSql.select(TestGeneralEntity.class)
                                                    .where(x -> x.and(TGE_Fields.id,
                                                                      FilterCompare.Eq,
                                                                      tempData.getId()))
                                                    .first();

        Assertions.assertNotNull(dataCheckCreate,
                                 "事务提交后未能查询到新增的数据");

        TempDataExtension.cleanData(name,
                                    TestGeneralEntity.class,
                                    tempData.getId());

        System.out.printf("\r\n已查询到新增数据，Id：%s，事务提交成功\r\n",
                          tempData.getId());
    }
}
