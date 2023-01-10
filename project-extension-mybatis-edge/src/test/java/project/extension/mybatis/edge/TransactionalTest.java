package project.extension.mybatis.edge;

import org.junit.FixMethodOrder;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.runners.MethodSorters;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import project.extension.mybatis.edge.common.OrmInjection;
import project.extension.mybatis.edge.common.SpringBootTestApplication;
import project.extension.mybatis.edge.entity.CommonQuickInput;
import project.extension.mybatis.edge.entityFields.CQI_Fields;
import project.extension.mybatis.edge.extention.EntityExtension;
import project.extension.mybatis.edge.model.FilterCompare;
import project.extension.standard.exception.ModuleException;
import project.extension.tuple.Tuple2;

/**
 * 事务测试
 *
 * @author LCTR
 * @date 2022-12-15
 */
@SpringBootTest(classes = SpringBootTestApplication.class)
@DisplayName("事务测试")
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TransactionalTest {
    private static String dataId;

    @BeforeEach
    public void injection() {
        OrmInjection.injection();
    }

    /**
     * 测试编程式事务回滚操作
     */
    @Test
    @DisplayName("测试编程式事务回滚操作")
    public void a30()
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
     * 测试编程式事务嵌套回滚操作
     */
    @Test
    @DisplayName("测试编程式事务嵌套回滚操作")
    public void a40()
            throws
            Throwable {
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

                System.out.println("\r\n回滚事务2");
                throw new ModuleException("回滚事务2");
            });

            Assertions.assertEquals(false,
                                    tranCreate2.a,
                                    "事务2未回滚");

            System.out.println("\r\n事务2已回滚");

            System.out.println("\r\n回滚事务1");
            throw new ModuleException("回滚事务1");
        });

        Assertions.assertEquals(false,
                                tranCreate1.a,
                                "事务1未回滚");

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
     * 测试声明式事务回滚操作（回滚）
     */
    @Test
    @Transactional
    @Rollback
    @DisplayName("测试声明式事务回滚操作（回滚）")
    public void a50()
            throws
            Throwable {
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

        dataId = dataCreate.getId();
    }

    /**
     * 测试声明式事务回滚操作
     */
    @Test
    @DisplayName("测试声明式事务回滚操作")
    public void a51()
            throws
            Throwable {
        Assertions.assertNotNull(dataId,
                                 "未记录新增数据的Id");

        CommonQuickInput dataCheckCreate = OrmInjection.masterNaiveSql.select(CommonQuickInput.class)
                                                                      .where(x -> x.and(CQI_Fields.id,
                                                                                        FilterCompare.Eq,
                                                                                        dataId))
                                                                      .first();

        Assertions.assertNull(dataCheckCreate,
                              "事务回滚后依然能查询到新增的数据");

        System.out.printf("\r\n未查询到新增数据，Id：%s，事务回滚成功\r\n",
                          dataId);

        dataId = null;
    }

    /**
     * 测试声明式事务嵌套编程式事务回滚操作（回滚）
     */
    @Test
    @Transactional
    @Rollback
    @DisplayName("测试声明式事务嵌套编程式事务回滚操作（回滚）")
    public void a60()
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

            dataId = dataCreate.getId();

            System.out.println("\r\n回滚编程式事务");
            throw new ModuleException("回滚编程式事务");
        });

        Assertions.assertEquals(false,
                                tranCreate.a,
                                "编程式事务未回滚");

        System.out.println("\r\n编程式事务已回滚");
    }

    /**
     * 测试声明式事务嵌套编程式事务回滚操作
     */
    @Test
    @DisplayName("测试声明式事务嵌套编程式事务回滚操作")
    public void a61()
            throws
            Throwable {
        Assertions.assertNotNull(dataId,
                                 "未记录新增数据的Id");

        CommonQuickInput dataCheckCreate = OrmInjection.masterNaiveSql.select(CommonQuickInput.class)
                                                                      .where(x -> x.and(CQI_Fields.id,
                                                                                        FilterCompare.Eq,
                                                                                        dataId))
                                                                      .first();

        Assertions.assertNull(dataCheckCreate,
                              "声明式事务嵌套编程式事务回滚后依然能查询到新增的数据");

        System.out.printf("\r\n未查询到新增数据，Id：%s，声明式事务嵌套编程式事务回滚成功\r\n",
                          dataId);

        dataId = null;
    }
}
