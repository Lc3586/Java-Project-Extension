package project.extension.mybatis.edge.test;

import org.junit.jupiter.api.*;
import org.springframework.test.annotation.Commit;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import project.extension.mybatis.edge.common.ExceptionExtension;
import project.extension.mybatis.edge.common.OrmExtension;
import project.extension.mybatis.edge.common.OrmInjection;
import project.extension.mybatis.edge.entity.CommonQuickInput;
import project.extension.mybatis.edge.entityFields.CQI_Fields;
import project.extension.mybatis.edge.extention.EntityExtension;
import project.extension.mybatis.edge.model.FilterCompare;
import project.extension.standard.exception.BusinessException;
import project.extension.standard.exception.ModuleException;
import project.extension.tuple.Tuple2;

/**
 * 3x.基础事务测试
 *
 * @author LCTR
 * @date 2022-12-15
 */
@Disabled
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
//@TestExecutionListeners({NaiveTransactionalTestExecutionListener.class})
//@SpringBootTest(classes = SpringBootTestApplication.class)
public class X3TransactionBasicsTest {
    /**
     * 临时数据
     */
    private static CommonQuickInput tempData;

    /**
     * 注入
     */
    @BeforeEach
    public void injection() {
        OrmInjection.injection();
    }

    /**
     * 清理数据
     */
    @AfterAll
    public static void clean()
            throws
            Exception {
        if (tempData == null)
            return;

        OrmExtension.clean(CommonQuickInput.class,
                           tempData);
        tempData = null;
    }

    /**
     * 测试编程式事务提交操作
     */
    @Test
    @DisplayName("300.测试编程式事务提交操作")
    @Order(300)
    public void _300()
            throws
            Throwable {
        EntityExtension entityExtension = new EntityExtension(null);

        CommonQuickInput dataCreate = entityExtension.initialization(new CommonQuickInput());
        dataCreate.setCategory("测试分类");
        dataCreate.setContent("测试内容");
        dataCreate.setKeyword("测试关键字");
        dataCreate.setPublic_(true);

        Tuple2<Boolean, Exception> tranCreate = OrmInjection.masterNaiveSql.transaction(() -> {
            int rowsCreate = OrmInjection.masterNaiveSql.insert(CommonQuickInput.class,
                                                                dataCreate)
                                                        .executeAffrows();

            Assertions.assertEquals(1,
                                    rowsCreate,
                                    "新增数据失败");

            System.out.printf("\r\n已新增数据，Id：%s\r\n",
                              dataCreate.getId());
        });

        Assertions.assertEquals(true,
                                tranCreate.a,
                                "事务未提交");

        System.out.println("\r\n事务已提交");

        CommonQuickInput dataCheckCreate = OrmInjection.masterNaiveSql.select(CommonQuickInput.class)
                                                                      .where(x -> x.and(CQI_Fields.id,
                                                                                        FilterCompare.Eq,
                                                                                        dataCreate.getId()))
                                                                      .first();

        Assertions.assertNotNull(dataCheckCreate,
                                 "事务提交后未能查询到新增的数据");

        System.out.println("\r\n事务提交成功");

        OrmExtension.clean(CommonQuickInput.class,
                           dataCreate);
    }

    /**
     * 测试编程式事务回滚操作
     */
    @Test
    @DisplayName("301.测试编程式事务回滚操作")
    @Order(301)
    public void _301() {
        EntityExtension entityExtension = new EntityExtension(null);

        CommonQuickInput dataCreate = entityExtension.initialization(new CommonQuickInput());
        dataCreate.setCategory("测试分类");
        dataCreate.setContent("测试内容");
        dataCreate.setKeyword("测试关键字");
        dataCreate.setPublic_(true);

        Tuple2<Boolean, Exception> tranCreate = OrmInjection.masterNaiveSql.transaction(() -> {
            int rowsCreate = OrmInjection.masterNaiveSql.insert(CommonQuickInput.class,
                                                                dataCreate)
                                                        .executeAffrows();

            Assertions.assertEquals(1,
                                    rowsCreate,
                                    "新增数据失败");

            System.out.printf("\r\n已新增数据，Id：%s\r\n",
                              dataCreate.getId());

            System.out.println("\r\n回滚事务");
            throw new ModuleException("回滚事务");
        });

        Assertions.assertEquals(false,
                                tranCreate.a,
                                "事务未回滚");

        System.out.println("\r\n事务已回滚");

        CommonQuickInput dataCheckCreate = OrmInjection.masterNaiveSql.select(CommonQuickInput.class)
                                                                      .where(x -> x.and(CQI_Fields.id,
                                                                                        FilterCompare.Eq,
                                                                                        dataCreate.getId()))
                                                                      .first();

        Assertions.assertNull(dataCheckCreate,
                              "事务回滚后依然能查询到新增的数据");

        System.out.println("\r\n事务回滚成功");
    }

    /**
     * 测试编程式事务嵌套提交操作
     *
     * <p>应抛出异常并回滚事务</p>
     */
    @Test
    @DisplayName("310.测试编程式事务嵌套提交操作")
    @Order(310)
    public void _310() {
        EntityExtension entityExtension = new EntityExtension(null);

        CommonQuickInput dataCreate1 = entityExtension.initialization(new CommonQuickInput());
        dataCreate1.setCategory("测试分类1");
        dataCreate1.setContent("测试内容1");
        dataCreate1.setKeyword("测试关键字1");
        dataCreate1.setPublic_(true);

        CommonQuickInput dataCreate2 = entityExtension.initialization(new CommonQuickInput());
        dataCreate2.setCategory("测试分类2");
        dataCreate2.setContent("测试内容2");
        dataCreate2.setKeyword("测试关键字2");
        dataCreate2.setPublic_(true);

        Tuple2<Boolean, Exception> tranCreate1 = OrmInjection.masterNaiveSql.transaction(() -> {
            int rowsCreate1 = OrmInjection.masterNaiveSql.insert(CommonQuickInput.class,
                                                                 dataCreate1)
                                                         .executeAffrows();

            Assertions.assertEquals(1,
                                    rowsCreate1,
                                    "新增1数据失败");

            System.out.printf("\r\n已新增数据1，Id：%s\r\n",
                              dataCreate1.getId());

            Tuple2<Boolean, Exception> tranCreate2 = OrmInjection.masterNaiveSql.transaction(() -> {
                int rowsCreate2 = OrmInjection.masterNaiveSql.insert(CommonQuickInput.class,
                                                                     dataCreate2)
                                                             .executeAffrows();

                Assertions.assertEquals(1,
                                        rowsCreate2,
                                        "新增2数据失败");

                System.out.printf("\r\n已新增数据2，Id：%s\r\n",
                                  dataCreate2.getId());
            });

            Assertions.assertEquals(false,
                                    tranCreate2.a,
                                    "事务2未回滚");

            System.out.println("\r\n事务2已回滚");

            throw new BusinessException("事务2已回滚",
                                        tranCreate2.b);
        });

        Assertions.assertEquals(false,
                                tranCreate1.a,
                                "事务1未回滚");

        ExceptionExtension.output(tranCreate1.b);

        System.out.println("\r\n事务1已回滚");

        CommonQuickInput dataCheckCreate1 = OrmInjection.masterNaiveSql.select(CommonQuickInput.class)
                                                                       .where(x -> x.and(CQI_Fields.id,
                                                                                         FilterCompare.Eq,
                                                                                         dataCreate1.getId()))
                                                                       .first();

        Assertions.assertNull(dataCheckCreate1,
                              "事务回滚后依然能查询到新增的数据1");

        CommonQuickInput dataCheckCreate2 = OrmInjection.masterNaiveSql.select(CommonQuickInput.class)
                                                                       .where(x -> x.and(CQI_Fields.id,
                                                                                         FilterCompare.Eq,
                                                                                         dataCreate2.getId()))
                                                                       .first();

        Assertions.assertNull(dataCheckCreate2,
                              "事务回滚后依然能查询到新增的数据2");

        System.out.println("\r\n事务回滚成功");
    }

    /**
     * 测试声明式事务提交操作（提交）
     */
    @Test
    @Transactional
    @Commit
    @DisplayName("320.测试声明式事务提交操作（提交）")
    @Order(320)
    public void _320() {
        EntityExtension entityExtension = new EntityExtension(null);

        CommonQuickInput dataCreate = entityExtension.initialization(new CommonQuickInput());
        dataCreate.setCategory("测试分类");
        dataCreate.setContent("测试内容");
        dataCreate.setKeyword("测试关键字");
        dataCreate.setPublic_(true);

        int rowsCreate = OrmInjection.masterNaiveSql.insert(CommonQuickInput.class,
                                                            dataCreate)
                                                    .executeAffrows();

        Assertions.assertEquals(1,
                                rowsCreate,
                                "新增数据失败");

        System.out.printf("\r\n已新增数据，Id：%s\r\n",
                          dataCreate.getId());

        tempData = dataCreate;
    }

    /**
     * 测试声明式事务提交操作
     */
    @Test
    @DisplayName("321.测试声明式事务提交操作")
    @Order(321)
    public void _321()
            throws
            Throwable {
        Assertions.assertNotNull(tempData,
                                 "未记录新增的数据");

        CommonQuickInput dataCheckCreate = OrmInjection.masterNaiveSql.select(CommonQuickInput.class)
                                                                      .where(x -> x.and(CQI_Fields.id,
                                                                                        FilterCompare.Eq,
                                                                                        tempData.getId()))
                                                                      .first();

        Assertions.assertNotNull(dataCheckCreate,
                                 "事务提交后未能查询到新增的数据");

        System.out.printf("\r\n已查询到新增数据，Id：%s，事务提交成功\r\n",
                          tempData.getId());

        OrmExtension.clean(CommonQuickInput.class,
                           tempData);

        tempData = null;
    }

    /**
     * 测试声明式事务回滚操作（回滚）
     */
    @Test
    @Transactional
    @Rollback
    @DisplayName("322.测试声明式事务回滚操作（回滚）")
    @Order(322)
    public void _322() {
        EntityExtension entityExtension = new EntityExtension(null);

        CommonQuickInput dataCreate = entityExtension.initialization(new CommonQuickInput());
        dataCreate.setCategory("测试分类");
        dataCreate.setContent("测试内容");
        dataCreate.setKeyword("测试关键字");
        dataCreate.setPublic_(true);

        int rowsCreate = OrmInjection.masterNaiveSql.insert(CommonQuickInput.class,
                                                            dataCreate)
                                                    .executeAffrows();

        Assertions.assertEquals(1,
                                rowsCreate,
                                "新增数据失败");

        System.out.printf("\r\n已新增数据，Id：%s\r\n",
                          dataCreate.getId());

        tempData = dataCreate;
    }

    /**
     * 测试声明式事务回滚操作
     */
    @Test
    @DisplayName("323.测试声明式事务回滚操作")
    @Order(323)
    public void _323() {
        Assertions.assertNotNull(tempData,
                                 "未记录新增的数据");

        CommonQuickInput dataCheckCreate = OrmInjection.masterNaiveSql.select(CommonQuickInput.class)
                                                                      .where(x -> x.and(CQI_Fields.id,
                                                                                        FilterCompare.Eq,
                                                                                        tempData.getId()))
                                                                      .first();

        Assertions.assertNull(dataCheckCreate,
                              "事务回滚后依然能查询到新增的数据");

        System.out.printf("\r\n未查询到新增数据，Id：%s，事务回滚成功\r\n",
                          tempData.getId());

        tempData = null;
    }

    /**
     * 测试声明式事务嵌套编程式事务提交操作
     *
     * <p>应抛出异常并回滚事务</p>
     */
    @Test
    @Transactional
    @Commit
    @DisplayName("330.测试声明式事务嵌套编程式事务提交操作")
    @Order(330)
    public void _330() {
        EntityExtension entityExtension = new EntityExtension(null);

        CommonQuickInput dataCreate = entityExtension.initialization(new CommonQuickInput());
        dataCreate.setCategory("测试分类");
        dataCreate.setContent("测试内容");
        dataCreate.setKeyword("测试关键字");
        dataCreate.setPublic_(true);

        Tuple2<Boolean, Exception> tranCreate = OrmInjection.masterNaiveSql.transaction(() -> {
            int rowsCreate = OrmInjection.masterNaiveSql.insert(CommonQuickInput.class,
                                                                dataCreate)
                                                        .executeAffrows();

            Assertions.assertEquals(1,
                                    rowsCreate,
                                    "新增数据失败");

            System.out.printf("\r\n已新增数据，Id：%s\r\n",
                              dataCreate.getId());

            tempData = dataCreate;
        });

        Assertions.assertEquals(false,
                                tranCreate.a,
                                "编程式事务未回滚");

        ExceptionExtension.output(tranCreate.b);

        System.out.println("\r\n编程式事务已回滚");
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
