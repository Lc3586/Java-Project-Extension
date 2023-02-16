package project.extension.mybatis.edge.test;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.test.annotation.Commit;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import project.extension.mybatis.edge.INaiveSql;
import project.extension.mybatis.edge.common.ExceptionExtension;
import project.extension.mybatis.edge.common.OrmExtension;
import project.extension.mybatis.edge.common.OrmObjectResolve;
import project.extension.mybatis.edge.dbContext.repository.IBaseRepository_Key;
import project.extension.mybatis.edge.entity.CommonQuickInput;
import project.extension.mybatis.edge.extention.EntityExtension;
import project.extension.standard.exception.BusinessException;
import project.extension.standard.exception.ModuleException;
import project.extension.tuple.Tuple2;

import java.util.HashMap;
import java.util.Map;

/**
 * 500.基础Repository事务测试
 *
 * @author LCTR
 * @date 2023-01-12
 */
@Disabled
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
//@TestExecutionListeners({NaiveTransactionalTestExecutionListener.class})
//@SpringBootTest(classes = SpringBootTestApplication.class)
public class X500TransactionRepositoryBasicsTest {
    /**
     * 全部数据源
     */
    private static final Map<String, String> dataSourceMap = new HashMap<>();

    /**
     * 多数据源测试
     *
     * @return 数据源
     */
    private static String[] multiDataSourceTest() {
        dataSourceMap.put("MySQL 8.0",
                          "mysql");
        dataSourceMap.put("MariaDB 10.10",
                          "mariadb");
        dataSourceMap.put("SqlServer 2012 降级处理",
                          "sqlserver");
        dataSourceMap.put("SqlServer 2012",
                          "sqlserver2012");
        dataSourceMap.put("达梦 8",
                          "dameng");
        dataSourceMap.put("Oracle 19c",
                          "oracle");
        dataSourceMap.put("PostgreSQL 15",
                          "postgresql");
        return dataSourceMap.keySet()
                            .toArray(new String[0]);
    }

    /**
     * 临时数据
     */
    private static CommonQuickInput tempData;

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
     *
     * @param name 名称
     */
    @ParameterizedTest
    @MethodSource("multiDataSourceTest")
    @DisplayName("500.测试编程式事务提交操作")
    @Order(500)
    public void _500(String name)
            throws
            Throwable {
        INaiveSql naiveSql = OrmObjectResolve.getOrm(dataSourceMap.get(name));

        EntityExtension entityExtension = new EntityExtension(null);

        CommonQuickInput dataCreate = entityExtension.initialization(new CommonQuickInput());
        dataCreate.setCategory("测试分类");
        dataCreate.setContent("测试内容");
        dataCreate.setKeyword("测试关键字");
        dataCreate.setPublic_(true);

        IBaseRepository_Key<CommonQuickInput, String> repository_key
                = naiveSql.getRepository_Key(CommonQuickInput.class,
                                             String.class);

        Assertions.assertNotNull(repository_key,
                                 "获取Repository_Key失败");

        Tuple2<Boolean, Exception> tranCreate = repository_key.transaction(() -> {
            repository_key.insert(dataCreate);

            System.out.printf("\r\n已新增数据，Id：%s\r\n",
                              dataCreate.getId());
        });

        Assertions.assertEquals(true,
                                tranCreate.a,
                                "事务未提交");

        System.out.println("\r\n事务已提交");

        CommonQuickInput dataCheckCreate = repository_key.getById(dataCreate.getId());

        Assertions.assertNotNull(dataCheckCreate,
                                 "事务提交后未能查询到新增的数据");

        System.out.println("\r\n事务提交成功");

        OrmExtension.clean(CommonQuickInput.class,
                           dataCreate);
    }

    /**
     * 测试编程式事务回滚操作
     *
     * @param name 名称
     */
    @ParameterizedTest
    @MethodSource("multiDataSourceTest")
    @DisplayName("501.测试编程式事务回滚操作")
    @Order(501)
    public void _501(String name) {
        INaiveSql naiveSql = OrmObjectResolve.getOrm(dataSourceMap.get(name));

        EntityExtension entityExtension = new EntityExtension(null);

        CommonQuickInput dataCreate = entityExtension.initialization(new CommonQuickInput());
        dataCreate.setCategory("测试分类");
        dataCreate.setContent("测试内容");
        dataCreate.setKeyword("测试关键字");
        dataCreate.setPublic_(true);

        IBaseRepository_Key<CommonQuickInput, String> repository_key
                = naiveSql.getRepository_Key(CommonQuickInput.class,
                                             String.class);

        Assertions.assertNotNull(repository_key,
                                 "获取Repository_Key失败");

        Tuple2<Boolean, Exception> tranCreate = repository_key.transaction(() -> {
            repository_key.insert(dataCreate);

            System.out.printf("\r\n已新增数据，Id：%s\r\n",
                              dataCreate.getId());

            System.out.println("\r\n回滚事务");
            throw new ModuleException("回滚事务");
        });

        Assertions.assertEquals(false,
                                tranCreate.a,
                                "事务未回滚");

        System.out.println("\r\n事务已回滚");

        CommonQuickInput dataCheckCreate = repository_key.getById(dataCreate.getId());

        Assertions.assertNull(dataCheckCreate,
                              "事务回滚后依然能查询到新增的数据");

        System.out.println("\r\n事务回滚成功");
    }

    /**
     * 测试编程式事务嵌套提交操作
     * <p>应抛出异常并回滚事务</p>
     *
     * @param name 名称
     */
    @ParameterizedTest
    @MethodSource("multiDataSourceTest")
    @DisplayName("510.测试编程式事务嵌套提交操作")
    @Order(510)
    public void _510(String name) {
        INaiveSql naiveSql = OrmObjectResolve.getOrm(dataSourceMap.get(name));

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

        IBaseRepository_Key<CommonQuickInput, String> repository_key
                = naiveSql.getRepository_Key(CommonQuickInput.class,
                                             String.class);

        Assertions.assertNotNull(repository_key,
                                 "获取Repository_Key失败");

        Tuple2<Boolean, Exception> tranCreate1 = repository_key.transaction(() -> {
            repository_key.insert(dataCreate1);

            System.out.printf("\r\n已新增数据1，Id：%s\r\n",
                              dataCreate1.getId());

            Tuple2<Boolean, Exception> tranCreate2 = repository_key.transaction(() -> {
                repository_key.insert(dataCreate2);

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

        CommonQuickInput dataCheckCreate1 = repository_key.getById(dataCreate1.getId());

        Assertions.assertNull(dataCheckCreate1,
                              "事务回滚后依然能查询到新增的数据1");

        CommonQuickInput dataCheckCreate2 = repository_key.getById(dataCreate2.getId());

        Assertions.assertNull(dataCheckCreate2,
                              "事务回滚后依然能查询到新增的数据2");

        System.out.println("\r\n事务回滚成功");
    }

    /**
     * 测试声明式事务提交操作（提交）
     *
     * @param name 名称
     */
    @ParameterizedTest
    @MethodSource("multiDataSourceTest")
    @Transactional
    @Commit
    @DisplayName("520.测试声明式事务提交操作（提交）")
    @Order(520)
    public void _520(String name) {
        INaiveSql naiveSql = OrmObjectResolve.getOrm(dataSourceMap.get(name));

        EntityExtension entityExtension = new EntityExtension(null);

        CommonQuickInput dataCreate = entityExtension.initialization(new CommonQuickInput());
        dataCreate.setCategory("测试分类");
        dataCreate.setContent("测试内容");
        dataCreate.setKeyword("测试关键字");
        dataCreate.setPublic_(true);

        IBaseRepository_Key<CommonQuickInput, String> repository_key
                = naiveSql.getRepository_Key(CommonQuickInput.class,
                                             String.class);

        Assertions.assertNotNull(repository_key,
                                 "获取Repository_Key失败");

        repository_key.insert(dataCreate);

        System.out.printf("\r\n已新增数据，Id：%s\r\n",
                          dataCreate.getId());

        tempData = dataCreate;
    }

    /**
     * 测试声明式事务提交操作
     *
     * @param name 名称
     */
    @ParameterizedTest
    @MethodSource("multiDataSourceTest")
    @DisplayName("521.测试声明式事务提交操作")
    @Order(521)
    public void _521(String name)
            throws
            Throwable {
        INaiveSql naiveSql = OrmObjectResolve.getOrm(dataSourceMap.get(name));

        Assertions.assertNotNull(tempData,
                                 "未记录新增的数据");

        IBaseRepository_Key<CommonQuickInput, String> repository_key
                = naiveSql.getRepository_Key(CommonQuickInput.class,
                                             String.class);

        Assertions.assertNotNull(repository_key,
                                 "获取Repository_Key失败");

        CommonQuickInput dataCheckCreate = repository_key.getById(tempData.getId());

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
     *
     * @param name 名称
     */
    @ParameterizedTest
    @MethodSource("multiDataSourceTest")
    @Transactional
    @Rollback
    @DisplayName("522.测试声明式事务回滚操作（回滚）")
    @Order(522)
    public void _522(String name) {
        INaiveSql naiveSql = OrmObjectResolve.getOrm(dataSourceMap.get(name));

        EntityExtension entityExtension = new EntityExtension(null);

        CommonQuickInput dataCreate = entityExtension.initialization(new CommonQuickInput());
        dataCreate.setCategory("测试分类");
        dataCreate.setContent("测试内容");
        dataCreate.setKeyword("测试关键字");
        dataCreate.setPublic_(true);

        IBaseRepository_Key<CommonQuickInput, String> repository_key
                = naiveSql.getRepository_Key(CommonQuickInput.class,
                                             String.class);

        Assertions.assertNotNull(repository_key,
                                 "获取Repository_Key失败");

        repository_key.insert(dataCreate);

        System.out.printf("\r\n已新增数据，Id：%s\r\n",
                          dataCreate.getId());

        tempData = dataCreate;
    }

    /**
     * 测试声明式事务回滚操作
     *
     * @param name 名称
     */
    @ParameterizedTest
    @MethodSource("multiDataSourceTest")
    @DisplayName("523.测试声明式事务回滚操作")
    @Order(523)
    public void _523(String name) {
        INaiveSql naiveSql = OrmObjectResolve.getOrm(dataSourceMap.get(name));

        Assertions.assertNotNull(tempData,
                                 "未记录新增的数据");

        IBaseRepository_Key<CommonQuickInput, String> repository_key
                = naiveSql.getRepository_Key(CommonQuickInput.class,
                                             String.class);

        Assertions.assertNotNull(repository_key,
                                 "获取Repository_Key失败");

        CommonQuickInput dataCheckCreate = repository_key.getById(tempData.getId());

        Assertions.assertNull(dataCheckCreate,
                              "事务回滚后依然能查询到新增的数据");

        System.out.printf("\r\n未查询到新增数据，Id：%s，事务回滚成功\r\n",
                          tempData.getId());

        tempData = null;
    }

    /**
     * 测试声明式事务嵌套编程式事务提交操作
     * <p>应抛出异常并回滚事务</p>
     *
     * @param name 名称
     */
    @ParameterizedTest
    @MethodSource("multiDataSourceTest")
    @Transactional
    @Commit
    @DisplayName("530.测试声明式事务嵌套编程式事务提交操作")
    @Order(530)
    public void _530(String name) {
        INaiveSql naiveSql = OrmObjectResolve.getOrm(dataSourceMap.get(name));

        EntityExtension entityExtension = new EntityExtension(null);

        CommonQuickInput dataCreate = entityExtension.initialization(new CommonQuickInput());
        dataCreate.setCategory("测试分类");
        dataCreate.setContent("测试内容");
        dataCreate.setKeyword("测试关键字");
        dataCreate.setPublic_(true);

        IBaseRepository_Key<CommonQuickInput, String> repository_key
                = naiveSql.getRepository_Key(CommonQuickInput.class,
                                             String.class);

        Assertions.assertNotNull(repository_key,
                                 "获取Repository_Key失败");

        Tuple2<Boolean, Exception> tranCreate = repository_key.transaction(() -> {
            repository_key.insert(dataCreate);

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
}
