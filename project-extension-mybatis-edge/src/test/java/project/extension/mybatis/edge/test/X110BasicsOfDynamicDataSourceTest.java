package project.extension.mybatis.edge.test;

import org.junit.jupiter.api.*;
import project.extension.ioc.IOCExtension;
import project.extension.mybatis.edge.aop.INaiveAop;
import project.extension.mybatis.edge.common.AssertExtension;
import project.extension.mybatis.edge.common.OrmObjectResolve;
import project.extension.mybatis.edge.common.TempDataExtension;
import project.extension.mybatis.edge.entity.TestGeneralEntity;
import project.extension.mybatis.edge.entityFields.TGE_Fields;
import project.extension.mybatis.edge.model.FilterCompare;
import project.extension.mybatis.edge.service.ITestApoService;

/**
 * 110.基础动态数据源测试
 *
 * @author LCTR
 * @date 2023-07-07
 */
@Disabled
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class X110BasicsOfDynamicDataSourceTest {
    private ITestApoService testApoService;

    /**
     * 注入
     */
    @BeforeEach
    public void injection() {
        testApoService = IOCExtension.applicationContext.getBean(ITestApoService.class);

        IOCExtension.applicationContext.getBean(INaiveAop.class)
                                       .addListenerDataSourceChanged(x -> System.out.printf("\r\n[%d]线程切换数据源：%s → %s\r\n",
                                                                                            x.getThreadId(),
                                                                                            x.getOriginalDataSource(),
                                                                                            x.getCurrentDataSource()));
    }

    /**
     * 清理数据
     */
    @AfterAll
    public static void clean() {
        TempDataExtension.clearUp();
    }

    /**
     * 测试通过方法注解切换数据源
     */
    @Test
    @DisplayName("110.测试通过方法注解切换数据源")
    @Order(110)
    public void _110()
            throws
            Throwable {
        TestGeneralEntity dataCreate = TempDataExtension.generateData(TestGeneralEntity.class);

        testApoService.insert2MariaDB(dataCreate);

        System.out.printf("\r\n已新增数据至MariaDB，Id：%s\r\n",
                          dataCreate.getId());

        TestGeneralEntity dataCheckCreate = testApoService.getFromMariaDB(dataCreate.getId());
        Assertions.assertNotNull(dataCheckCreate,
                                 "从MariaDB查询新增的数据失败");

        TempDataExtension.putData("mariadb",
                                  TestGeneralEntity.class,
                                  dataCheckCreate.getId(),
                                  dataCheckCreate);

        AssertExtension.assertEquals(dataCreate,
                                     dataCheckCreate,
                                     true,
                                     TGE_Fields.allFields);

        System.out.printf("\r\n已从MariaDB复查新增的数据，Id：%s\r\n",
                          dataCheckCreate.getId());
    }

    /**
     * 测试通过方法注解切换数据源（验证）
     */
    @Test
    @DisplayName("111.测试通过方法注解切换数据源（验证）")
    @Order(111)
    public void _111()
            throws
            Throwable {
        TestGeneralEntity tempData = TempDataExtension.getFirstData("mariadb",
                                                                    TestGeneralEntity.class);

        TestGeneralEntity dataCheckTwiceCreate = OrmObjectResolve.getOrm("mariadb")
                                                                 .select(TestGeneralEntity.class)
                                                                 .where(x -> x.and(TGE_Fields.id,
                                                                                   FilterCompare.Eq,
                                                                                   tempData.getId()))
                                                                 .first();
        Assertions.assertNotNull(dataCheckTwiceCreate,
                                 "验证查询MariaDB新增的数据失败");

        AssertExtension.assertEquals(tempData,
                                     dataCheckTwiceCreate,
                                     true,
                                     TGE_Fields.allFields);

        System.out.printf("\r\n已验证MariaDB新增的数据，Id：%s\r\n",
                          dataCheckTwiceCreate.getId());

        OrmObjectResolve.getOrm("mariadb")
                        .delete(TestGeneralEntity.class,
                                tempData)
                        .executeAffrows();

        System.out.printf("\r\n已清理MariaDB新增的数据，Id：%s\r\n",
                          dataCheckTwiceCreate.getId());

        TempDataExtension.removeData("mariadb",
                                     TestGeneralEntity.class,
                                     tempData.getId());
    }

    /**
     * 测试通过类注解切换数据源
     */
    @Test
    @DisplayName("112.测试通过类注解切换数据源")
    @Order(112)
    public void _112()
            throws
            Throwable {
        TestGeneralEntity dataCreate = TempDataExtension.generateData(TestGeneralEntity.class);

        testApoService.insert2Postgresql(dataCreate);

        System.out.printf("\r\n已新增数据至Postgresql，Id：%s\r\n",
                          dataCreate.getId());

        TestGeneralEntity dataCheckCreate = testApoService.getFromPostgresql(dataCreate.getId());
        Assertions.assertNotNull(dataCheckCreate,
                                 "从Postgresql查询新增的数据失败");

        TempDataExtension.putData("postgresql",
                                  TestGeneralEntity.class,
                                  dataCheckCreate.getId(),
                                  dataCheckCreate);

        AssertExtension.assertEquals(dataCreate,
                                     dataCheckCreate,
                                     true,
                                     TGE_Fields.allFields);

        System.out.printf("\r\n已从Postgresql复查新增的数据，Id：%s\r\n",
                          dataCheckCreate.getId());
    }

    /**
     * 测试通过类注解切换数据源（验证）
     */
    @Test
    @DisplayName("113.测试通过类注解切换数据源（验证）")
    @Order(113)
    public void _113()
            throws
            Throwable {
        TestGeneralEntity tempData = TempDataExtension.getFirstData("postgresql",
                                                                    TestGeneralEntity.class);

        TestGeneralEntity dataCheckTwiceCreate = OrmObjectResolve.getOrm("postgresql")
                                                                 .select(TestGeneralEntity.class)
                                                                 .where(x -> x.and(TGE_Fields.id,
                                                                                   FilterCompare.Eq,
                                                                                   tempData.getId()))
                                                                 .first();
        Assertions.assertNotNull(dataCheckTwiceCreate,
                                 "验证查询Postgresql新增的数据失败");

        AssertExtension.assertEquals(tempData,
                                     dataCheckTwiceCreate,
                                     true,
                                     TGE_Fields.allFields);

        System.out.printf("\r\n已验证Postgresql新增的数据，Id：%s\r\n",
                          dataCheckTwiceCreate.getId());

        OrmObjectResolve.getOrm("postgresql")
                        .delete(TestGeneralEntity.class,
                                tempData)
                        .executeAffrows();

        System.out.printf("\r\n已清理Postgresql新增的数据，Id：%s\r\n",
                          dataCheckTwiceCreate.getId());

        TempDataExtension.removeData("postgresql",
                                     TestGeneralEntity.class,
                                     tempData.getId());
    }

    /**
     * 测试混合方式切换数据源
     */
    @Test
    @DisplayName("114.测试混合方式切换数据源")
    @Order(114)
    public void _114()
            throws
            Throwable {
        TestGeneralEntity dataCreate1 = TempDataExtension.generateData(TestGeneralEntity.class);

        testApoService.insert2Postgresql(dataCreate1);

        System.out.printf("\r\n已新增数据至Postgresql，Id：%s\r\n",
                          dataCreate1.getId());

        TestGeneralEntity dataCreate2 = TempDataExtension.generateData(TestGeneralEntity.class);

        testApoService.insert2MariaDB(dataCreate2);

        System.out.printf("\r\n已新增数据至MariaDB(，Id：%s\r\n",
                          dataCreate2.getId());

        TestGeneralEntity dataCheckCreate1 = testApoService.getFromPostgresql(dataCreate1.getId());
        Assertions.assertNotNull(dataCheckCreate1,
                                 "从Postgresql查询新增的数据失败");

        TempDataExtension.putData("postgresql",
                                  TestGeneralEntity.class,
                                  dataCheckCreate1.getId(),
                                  dataCheckCreate1);

        AssertExtension.assertEquals(dataCreate1,
                                     dataCheckCreate1,
                                     true,
                                     TGE_Fields.allFields);

        System.out.printf("\r\n已从Postgresql复查新增的数据，Id：%s\r\n",
                          dataCheckCreate1.getId());

        TestGeneralEntity dataCheckCreate2 = testApoService.getFromMariaDB(dataCreate2.getId());
        Assertions.assertNotNull(dataCheckCreate2,
                                 "从MariaDB查询新增的数据失败");

        TempDataExtension.putData("mariadb",
                                  TestGeneralEntity.class,
                                  dataCheckCreate2.getId(),
                                  dataCheckCreate2);

        AssertExtension.assertEquals(dataCreate2,
                                     dataCheckCreate2,
                                     true,
                                     TGE_Fields.allFields);

        System.out.printf("\r\n已从MariaDB复查新增的数据，Id：%s\r\n",
                          dataCheckCreate2.getId());
    }

    /**
     * 测试混合方式切换数据源（验证）
     */
    @Test
    @DisplayName("115.测试混合方式切换数据源（验证）")
    @Order(115)
    public void _115()
            throws
            Throwable {
        TestGeneralEntity tempData1 = TempDataExtension.getFirstData("postgresql",
                                                                     TestGeneralEntity.class);

        TestGeneralEntity dataCheckTwiceCreate1 = OrmObjectResolve.getOrm("postgresql")
                                                                  .select(TestGeneralEntity.class)
                                                                  .where(x -> x.and(TGE_Fields.id,
                                                                                    FilterCompare.Eq,
                                                                                    tempData1.getId()))
                                                                  .first();
        Assertions.assertNotNull(dataCheckTwiceCreate1,
                                 "验证Postgresql查询新增的数据失败");

        AssertExtension.assertEquals(tempData1,
                                     dataCheckTwiceCreate1,
                                     true,
                                     TGE_Fields.allFields);

        System.out.printf("\r\n已验证Postgresql新增的数据，Id：%s\r\n",
                          dataCheckTwiceCreate1.getId());

        TestGeneralEntity tempData2 = TempDataExtension.getFirstData("mariadb",
                                                                     TestGeneralEntity.class);

        TestGeneralEntity dataCheckTwiceCreate2 = OrmObjectResolve.getOrm("mariadb")
                                                                  .select(TestGeneralEntity.class)
                                                                  .where(x -> x.and(TGE_Fields.id,
                                                                                    FilterCompare.Eq,
                                                                                    tempData2.getId()))
                                                                  .first();
        Assertions.assertNotNull(dataCheckTwiceCreate2,
                                 "验证查询MariaDB新增的数据失败");

        AssertExtension.assertEquals(tempData2,
                                     dataCheckTwiceCreate2,
                                     true,
                                     TGE_Fields.allFields);

        System.out.printf("\r\n已验证MariaDB新增的数据，Id：%s\r\n",
                          dataCheckTwiceCreate2.getId());
    }
}