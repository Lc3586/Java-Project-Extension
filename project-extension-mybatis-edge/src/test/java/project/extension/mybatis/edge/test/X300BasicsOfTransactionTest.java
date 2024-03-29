package project.extension.mybatis.edge.test;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.test.annotation.Commit;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.transaction.annotation.Transactional;
import project.extension.mybatis.edge.core.provider.standard.INaiveSql;
import project.extension.mybatis.edge.common.*;
import project.extension.mybatis.edge.configure.TestDataSourceConfigure;
import project.extension.mybatis.edge.entity.TestGeneralEntity;
import project.extension.mybatis.edge.entityFields.TGE_Fields;
import project.extension.mybatis.edge.model.FilterCompare;
import project.extension.standard.exception.BusinessException;
import project.extension.standard.exception.ModuleException;
import project.extension.tuple.Tuple2;

/**
 * 300.基础事务测试
 *
 * @author LCTR
 * @date 2022-12-15
 */
@Disabled
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestExecutionListeners({NaiveTransactionalTestExecutionListener.class})
//@SpringBootTest(classes = SpringBootTestApplication.class)
public class X300BasicsOfTransactionTest {
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
    @DisplayName("300.测试编程式事务提交操作")
    @Order(300)
    public void _300(String name) {
        INaiveSql naiveSql = OrmObjectResolve.getOrm(TestDataSourceConfigure.getTestDataSource(name));

        TestGeneralEntity dataCreate = TempDataExtension.generateData(TestGeneralEntity.class);

        Tuple2<Boolean, Exception> tranCreate = naiveSql.transaction(() -> {
            int rowsCreate = naiveSql.insert(TestGeneralEntity.class,
                                             dataCreate)
                                     .executeAffrows();

            Assertions.assertEquals(1,
                                    rowsCreate,
                                    "新增数据失败");

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

        TestGeneralEntity dataCheckCreate = naiveSql.select(TestGeneralEntity.class)
                                                    .where(x -> x.and(TGE_Fields.id,
                                                                      FilterCompare.Eq,
                                                                      dataCreate.getId()))
                                                    .first();

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
    @DisplayName("301.测试编程式事务回滚操作")
    @Order(301)
    public void _301(String name) {
        INaiveSql naiveSql = OrmObjectResolve.getOrm(TestDataSourceConfigure.getTestDataSource(name));

        TestGeneralEntity dataCreate = TempDataExtension.generateData(TestGeneralEntity.class);

        Tuple2<Boolean, Exception> tranCreate = naiveSql.transaction(() -> {
            int rowsCreate = naiveSql.insert(TestGeneralEntity.class,
                                             dataCreate)
                                     .executeAffrows();

            Assertions.assertEquals(1,
                                    rowsCreate,
                                    "新增数据失败");

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

        TestGeneralEntity dataCheckCreate = naiveSql.select(TestGeneralEntity.class)
                                                    .where(x -> x.and(TGE_Fields.id,
                                                                      FilterCompare.Eq,
                                                                      dataCreate.getId()))
                                                    .first();

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
    @DisplayName("310.测试编程式事务嵌套提交操作")
    @Order(310)
    public void _310(String name) {
        INaiveSql naiveSql = OrmObjectResolve.getOrm(TestDataSourceConfigure.getTestDataSource(name));

        TestGeneralEntity dataCreate1 = TempDataExtension.generateData(TestGeneralEntity.class);

        TestGeneralEntity dataCreate2 = TempDataExtension.generateData(TestGeneralEntity.class);

        Tuple2<Boolean, Exception> tranCreate1 = naiveSql.transaction(() -> {
            int rowsCreate1 = naiveSql.insert(TestGeneralEntity.class,
                                              dataCreate1)
                                      .executeAffrows();

            Assertions.assertEquals(1,
                                    rowsCreate1,
                                    "新增1数据失败");

            TempDataExtension.putData(name,
                                      TestGeneralEntity.class,
                                      dataCreate1.getId(),
                                      dataCreate1);

            System.out.printf("\r\n已新增数据1，Id：%s\r\n",
                              dataCreate1.getId());

            Tuple2<Boolean, Exception> tranCreate2 = naiveSql.transaction(() -> {
                int rowsCreate2 = naiveSql.insert(TestGeneralEntity.class,
                                                  dataCreate2)
                                          .executeAffrows();

                Assertions.assertEquals(1,
                                        rowsCreate2,
                                        "新增2数据失败");

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

        TestGeneralEntity dataCheckCreate1 = naiveSql.select(TestGeneralEntity.class)
                                                     .where(x -> x.and(TGE_Fields.id,
                                                                       FilterCompare.Eq,
                                                                       dataCreate1.getId()))
                                                     .first();

        Assertions.assertNotNull(dataCheckCreate1,
                                 "事务提交后未能查询到新增的数据1");

        TempDataExtension.cleanData(name,
                                    TestGeneralEntity.class,
                                    dataCreate1.getId());

        TestGeneralEntity dataCheckCreate2 = naiveSql.select(TestGeneralEntity.class)
                                                     .where(x -> x.and(TGE_Fields.id,
                                                                       FilterCompare.Eq,
                                                                       dataCreate2.getId()))
                                                     .first();

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
    @DisplayName("320.测试声明式事务提交操作（提交）")
    @Order(320)
    public void _320(String name) {
        INaiveSql naiveSql = OrmObjectResolve.getOrm(TestDataSourceConfigure.getTestDataSource(name));

        TestGeneralEntity dataCreate = TempDataExtension.generateData(TestGeneralEntity.class);

        int rowsCreate = naiveSql.insert(TestGeneralEntity.class,
                                         dataCreate)
                                 .executeAffrows();

        Assertions.assertEquals(1,
                                rowsCreate,
                                "新增数据失败");

        TempDataExtension.putData(name,
                                  TestGeneralEntity.class,
                                  dataCreate.getId(),
                                  dataCreate);

        System.out.printf("\r\n已新增数据，Id：%s\r\n",
                          dataCreate.getId());
    }

    /**
     * 测试声明式事务提交操作
     *
     * @param name 名称
     */
    @ParameterizedTest
    @MethodSource("project.extension.mybatis.edge.configure.TestDataSourceConfigure#getMultiTestDataSourceName")
    @DisplayName("321.测试声明式事务提交操作")
    @Order(321)
    public void _321(String name) {
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

    /**
     * 测试声明式事务回滚操作（回滚）
     *
     * @param name 名称
     */
    @ParameterizedTest
    @MethodSource("project.extension.mybatis.edge.configure.TestDataSourceConfigure#getMultiTestDataSourceName")
    @Transactional
    @Rollback
    @DisplayName("322.测试声明式事务回滚操作（回滚）")
    @Order(322)
    public void _322(String name) {
        INaiveSql naiveSql = OrmObjectResolve.getOrm(TestDataSourceConfigure.getTestDataSource(name));

        TestGeneralEntity dataCreate = TempDataExtension.generateData(TestGeneralEntity.class);

        int rowsCreate = naiveSql.insert(TestGeneralEntity.class,
                                         dataCreate)
                                 .executeAffrows();

        Assertions.assertEquals(1,
                                rowsCreate,
                                "新增数据失败");

        TempDataExtension.putData(name,
                                  TestGeneralEntity.class,
                                  dataCreate.getId(),
                                  dataCreate);

        System.out.printf("\r\n已新增数据，Id：%s\r\n",
                          dataCreate.getId());
    }

    /**
     * 测试声明式事务回滚操作
     *
     * @param name 名称
     */
    @ParameterizedTest
    @MethodSource("project.extension.mybatis.edge.configure.TestDataSourceConfigure#getMultiTestDataSourceName")
    @DisplayName("323.测试声明式事务回滚操作")
    @Order(323)
    public void _323(String name) {
        INaiveSql naiveSql = OrmObjectResolve.getOrm(TestDataSourceConfigure.getTestDataSource(name));

        TestGeneralEntity tempData = TempDataExtension.getFirstData(name,
                                                                    TestGeneralEntity.class);

        TestGeneralEntity dataCheckCreate = naiveSql.select(TestGeneralEntity.class)
                                                    .where(x -> x.and(TGE_Fields.id,
                                                                      FilterCompare.Eq,
                                                                      tempData.getId()))
                                                    .first();

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
    @Transactional
    @Commit
    @DisplayName("330.测试声明式事务嵌套编程式事务提交操作（提交）")
    @Order(330)
    public void _330(String name) {
        INaiveSql naiveSql = OrmObjectResolve.getOrm(TestDataSourceConfigure.getTestDataSource(name));

        TestGeneralEntity dataCreate = TempDataExtension.generateData(TestGeneralEntity.class);

        Tuple2<Boolean, Exception> tranCreate = naiveSql.transaction(() -> {
            int rowsCreate = naiveSql.insert(TestGeneralEntity.class,
                                             dataCreate)
                                     .executeAffrows();

            Assertions.assertEquals(1,
                                    rowsCreate,
                                    "新增数据失败");

            TempDataExtension.putData(name,
                                      TestGeneralEntity.class,
                                      dataCreate.getId(),
                                      dataCreate);

            System.out.printf("\r\n已新增数据，Id：%s\r\n",
                              dataCreate.getId());
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
    @DisplayName("331.测试声明式事务嵌套编程式事务提交操作")
    @Order(331)
    public void _331(String name) {
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

//
//    /**
//     * 测试声明式事务嵌套编程式事务提交操作
//     */
//    @Test
//    @DisplayName("测试声明式事务嵌套编程式事务提交操作")
//    public void a61()
//            throws
//            Throwable {
//        Assertions.assertNotNull(tempData,
//                                 "未记录新增的数据");
//
//        CommonQuickInput dataCheckCreate = OrmInjection.masterNaiveSql.select(CommonQuickInput.class)
//                                                                      .where(x -> x.and(CQI_Fields.id,
//                                                                                        FilterCompare.Eq,
//                                                                                        tempData.getId()))
//                                                                      .first();
//
//        Assertions.assertNull(dataCheckCreate,
//                              "声明式事务嵌套编程式事务提交后未能查询到新增的数据");
//
//        System.out.printf("\r\n已查询到新增数据，Id：%s，声明式事务嵌套编程式事务提交成功\r\n",
//                          tempData.getId());
//
//        OrmExtension.clean(CommonQuickInput.class,
//                           tempData);
//
//        tempData = null;
//    }
//
//    /**
//     * 测试声明式事务嵌套编程式事务回滚操作（回滚）
//     */
//    @Test
//    @Transactional
//    @Rollback
//    @DisplayName("测试声明式事务嵌套编程式事务回滚操作（回滚）")
//    public void a62()
//            throws
//            Throwable {
//        EntityExtension entityExtension = new EntityExtension(null);
//
//        CommonQuickInput dataCreate = entityExtension.initialization(new CommonQuickInput());
//        dataCreate.setCategory("测试分类");
//        dataCreate.setContent("测试内容");
//        dataCreate.setKeyword("测试关键字");
//        dataCreate.setPublic_(true);
//
//        Tuple2<Boolean, Exception> tranCreate = OrmInjection.masterNaiveSql.transaction(() -> {
//            int rowsCreate = OrmInjection.masterNaiveSql.insert(CommonQuickInput.class,
//                                                                dataCreate)
//                                                        .executeAffrows();
//
//            Assertions.assertEquals(1,
//                                    rowsCreate,
//                                    "新增数据失败");
//
//            System.out.printf("\r\n已新增数据，Id：%s\r\n",
//                              dataCreate.getId());
//
//            tempData = dataCreate;
//
//            System.out.println("\r\n回滚编程式事务");
//            throw new ModuleException("回滚编程式事务");
//        });
//
//        Assertions.assertEquals(false,
//                                tranCreate.a,
//                                "编程式事务未回滚");
//
//        System.out.println("\r\n编程式事务已回滚");
//    }
//
//    /**
//     * 测试声明式事务嵌套编程式事务回滚操作
//     */
//    @Test
//    @DisplayName("测试声明式事务嵌套编程式事务回滚操作")
//    public void a63()
//            throws
//            Throwable {
//        Assertions.assertNotNull(tempData,
//                                 "未记录新增的数据");
//
//        CommonQuickInput dataCheckCreate = OrmInjection.masterNaiveSql.select(CommonQuickInput.class)
//                                                                      .where(x -> x.and(CQI_Fields.id,
//                                                                                        FilterCompare.Eq,
//                                                                                        tempData.getId()))
//                                                                      .first();
//
//        Assertions.assertNull(dataCheckCreate,
//                              "声明式事务嵌套编程式事务回滚后依然能查询到新增的数据");
//
//        System.out.printf("\r\n未查询到新增数据，Id：%s，声明式事务嵌套编程式事务回滚成功\r\n",
//                          tempData.getId());
//
//        tempData = null;
//    }
}
