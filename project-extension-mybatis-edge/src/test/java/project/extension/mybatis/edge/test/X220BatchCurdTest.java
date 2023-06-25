package project.extension.mybatis.edge.test;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import project.extension.mybatis.edge.common.AssertExtension;
import project.extension.mybatis.edge.common.ExceptionExtension;
import project.extension.mybatis.edge.common.OrmObjectResolve;
import project.extension.mybatis.edge.common.TempDataExtension;
import project.extension.mybatis.edge.configure.TestDataSourceConfigure;
import project.extension.mybatis.edge.core.provider.standard.INaiveSql;
import project.extension.mybatis.edge.entity.TestBlobEntity;
import project.extension.mybatis.edge.entity.TestClobEntity;
import project.extension.mybatis.edge.entity.TestGeneralEntity;
import project.extension.mybatis.edge.entity.TestIdentityEntity;
import project.extension.mybatis.edge.entityFields.TBE_Fields;
import project.extension.mybatis.edge.entityFields.TCE_Fields;
import project.extension.mybatis.edge.entityFields.TGE_Fields;
import project.extension.mybatis.edge.entityFields.TIE_Fields;
import project.extension.mybatis.edge.model.FilterCompare;

import java.util.ArrayList;
import java.util.List;

/**
 * 220.批量增删改查测试
 *
 * @author LCTR
 * @date 2023-06-25
 */
@Disabled
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class X220BatchCurdTest {
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
     * 测试批量新增功能
     *
     * @param name 名称
     */
    @ParameterizedTest
    @MethodSource("project.extension.mybatis.edge.configure.TestDataSourceConfigure#getMultiTestDataSourceName")
    @DisplayName("220.测试批量新增功能")
    @Order(220)
    public void _220(String name)
            throws
            Throwable {
        INaiveSql naiveSql = OrmObjectResolve.getOrm(TestDataSourceConfigure.getTestDataSource(name));

        List<TestGeneralEntity> dataCreateList = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            dataCreateList.add(TempDataExtension.generateData(TestGeneralEntity.class));
        }

        int rowsCreate = naiveSql.batchInsert(TestGeneralEntity.class,
                                              dataCreateList)
                                 .executeAffrows();

        Assertions.assertEquals(dataCreateList.size(),
                                rowsCreate,
                                "批量新增数据失败");

        System.out.printf("\r\n已批量新增%d条数据\r\n",
                          dataCreateList.size());

        for (TestGeneralEntity dataCreate : dataCreateList) {
            TestGeneralEntity dataCheckCreate = naiveSql.select(TestGeneralEntity.class)
                                                        .where(x -> x.and(TGE_Fields.id,
                                                                          FilterCompare.Eq,
                                                                          dataCreate.getId()))
                                                        .first();

            Assertions.assertNotNull(dataCheckCreate,
                                     "查询批量新增的数据失败");

            TempDataExtension.putData(name,
                                      TestGeneralEntity.class,
                                      dataCheckCreate.getId(),
                                      dataCheckCreate);

            AssertExtension.assertEquals(dataCreate,
                                         dataCheckCreate,
                                         true,
                                         TGE_Fields.allFields);

            System.out.printf("\r\n已复查批量新增的数据，Id：%s\r\n",
                              dataCheckCreate.getId());
        }
    }

    /**
     * 测试批量更新功能
     *
     * @param name 名称
     */
    @ParameterizedTest
    @MethodSource("project.extension.mybatis.edge.configure.TestDataSourceConfigure#getMultiTestDataSourceName")
    @DisplayName("221.测试批量更新功能")
    @Order(221)
    public void _221(String name)
            throws
            Throwable {
        INaiveSql naiveSql = OrmObjectResolve.getOrm(TestDataSourceConfigure.getTestDataSource(name));

        List<TestGeneralEntity> tempDataList = TempDataExtension.getAllData(name,
                                                                            TestGeneralEntity.class);

        List<TestGeneralEntity> dataUpdateList = new ArrayList<>();
        for (TestGeneralEntity testGeneralEntity : tempDataList) {
            TestGeneralEntity dataUpdate = TempDataExtension.generateData(TestGeneralEntity.class);
            dataUpdate.setId(testGeneralEntity
                                     .getId());
            dataUpdateList.add(dataUpdate);
        }

        int rowsUpdate = naiveSql.batchUpdate(TestGeneralEntity.class,
                                              dataUpdateList)
                                 .executeAffrows();

        Assertions.assertEquals(dataUpdateList.size(),
                                rowsUpdate,
                                "批量更新数据失败");

        System.out.printf("\r\n已批量更新%d条数据\r\n",
                          dataUpdateList.size());

        for (TestGeneralEntity dataUpdate : dataUpdateList) {
            TestGeneralEntity dataCheckUpdate = naiveSql.select(TestGeneralEntity.class)
                                                        .where(x -> x.and(TGE_Fields.id,
                                                                          FilterCompare.Eq,
                                                                          dataUpdate.getId()))
                                                        .first();

            Assertions.assertNotNull(dataCheckUpdate,
                                     "查询批量更新的数据失败");

            TempDataExtension.putData(name,
                                      TestGeneralEntity.class,
                                      dataCheckUpdate.getId(),
                                      dataCheckUpdate);

            AssertExtension.assertEquals(dataUpdate,
                                         dataCheckUpdate,
                                         true,
                                         TGE_Fields.allFields);

            System.out.printf("\r\n已复查批量更新的数据，Id：%s\r\n",
                              dataCheckUpdate.getId());
        }
    }

    /**
     * 测试批量删除功能
     *
     * @param name 名称
     */
    @ParameterizedTest
    @MethodSource("project.extension.mybatis.edge.configure.TestDataSourceConfigure#getMultiTestDataSourceName")
    @DisplayName("222.测试批量删除功能")
    @Order(222)
    public void _222(String name) {
        INaiveSql naiveSql = OrmObjectResolve.getOrm(TestDataSourceConfigure.getTestDataSource(name));

        List<TestGeneralEntity> tempDataList = TempDataExtension.getAllData(name,
                                                                            TestGeneralEntity.class);

        int rowsDelete = naiveSql.batchDelete(TestGeneralEntity.class,
                                              tempDataList)
                                 .executeAffrows();

        Assertions.assertEquals(tempDataList.size(),
                                rowsDelete,
                                "批量删除数据失败");

        System.out.printf("\r\n已批量删除%d条数据\r\n",
                          tempDataList.size());

        for (TestGeneralEntity tempData : tempDataList) {
            TestGeneralEntity dataCheckDelete = naiveSql.select(TestGeneralEntity.class)
                                                        .where(x -> x.and(TGE_Fields.id,
                                                                          FilterCompare.Eq,
                                                                          tempData.getId()))
                                                        .first();

            Assertions.assertNull(dataCheckDelete,
                                  "数据未删除");

            TempDataExtension.removeData(name,
                                         TestGeneralEntity.class,
                                         tempData.getId());

            System.out.printf("\r\n已复查删除的数据，Id：%s\r\n",
                              tempData.getId());
        }
    }

    /**
     * 测试批量新增功能（主键为自增列）
     *
     * @param name 名称
     */
    @ParameterizedTest
    @MethodSource("project.extension.mybatis.edge.configure.TestDataSourceConfigure#getMultiTestDataSourceName")
    @DisplayName("203.测试批量新增功能（主键为自增列）")
    @Order(223)
    public void _223(String name)
            throws
            Throwable {
        INaiveSql naiveSql = OrmObjectResolve.getOrm(TestDataSourceConfigure.getTestDataSource(name));

        List<TestIdentityEntity> dataCreateList = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            dataCreateList.add(TempDataExtension.generateData(TestIdentityEntity.class));
        }

        int rowsCreate = naiveSql.batchInsert(TestIdentityEntity.class,
                                              dataCreateList)
                                 .executeAffrows();

        Assertions.assertEquals(dataCreateList.size(),
                                rowsCreate,
                                "批量新增数据失败");

        System.out.printf("\r\n已批量新增%d条数据\r\n",
                          dataCreateList.size());

        for (TestIdentityEntity dataCreate : dataCreateList) {
            Assertions.assertNotNull(dataCreate.getId(),
                                     "新增数据读取自增Id失败");

            System.out.printf("\r\n已新增数据，Id：%s\r\n",
                              dataCreate.getId());

            TestIdentityEntity dataCheckCreate = naiveSql.select(TestIdentityEntity.class)
                                                         .where(x -> x.and(TIE_Fields.id,
                                                                           FilterCompare.Eq,
                                                                           dataCreate.getId()))
                                                         .first();

            Assertions.assertNotNull(dataCheckCreate,
                                     "查询新增的数据失败");

            TempDataExtension.putData(name,
                                      TestIdentityEntity.class,
                                      dataCheckCreate.getId(),
                                      dataCheckCreate);

            AssertExtension.assertEquals(dataCreate,
                                         dataCheckCreate,
                                         true,
                                         TIE_Fields.allFields);

            System.out.printf("\r\n已复查新增的数据，Id：%s\r\n",
                              dataCheckCreate.getId());
        }

        TempDataExtension.clearUp();
    }

    /**
     * 测试批量新增功能（包含长文本数据列）
     *
     * @param name 名称
     */
    @ParameterizedTest
    @MethodSource("project.extension.mybatis.edge.configure.TestDataSourceConfigure#getMultiTestDataSourceName")
    @DisplayName("224.测试批量新增功能（包含长文本数据列）")
    @Order(224)
    public void _224(String name)
            throws
            Throwable {
        INaiveSql naiveSql = OrmObjectResolve.getOrm(TestDataSourceConfigure.getTestDataSource(name));

        List<TestClobEntity> dataCreateList = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            dataCreateList.add(TempDataExtension.generateData(TestClobEntity.class));
        }

        int rowsCreate = naiveSql.batchInsert(TestClobEntity.class,
                                              dataCreateList)
                                 .executeAffrows();

        Assertions.assertEquals(dataCreateList.size(),
                                rowsCreate,
                                "批量新增数据失败");

        System.out.printf("\r\n已批量新增%d条数据\r\n",
                          dataCreateList.size());

        for (TestClobEntity dataCreate : dataCreateList) {
            Assertions.assertNotNull(dataCreate.getId(),
                                     "新增数据读取自增Id失败");

            System.out.printf("\r\n已新增数据，Id：%s\r\n",
                              dataCreate.getId());

            TestClobEntity dataCheckCreate = naiveSql.select(TestClobEntity.class)
                                                     .where(x -> x.and(TCE_Fields.id,
                                                                       FilterCompare.Eq,
                                                                       dataCreate.getId()))
                                                     .first();

            Assertions.assertNotNull(dataCheckCreate,
                                     "查询新增的数据失败");

            TempDataExtension.putData(name,
                                      TestClobEntity.class,
                                      dataCheckCreate.getId(),
                                      dataCheckCreate);

            AssertExtension.assertEquals(dataCreate,
                                         dataCheckCreate,
                                         true,
                                         TCE_Fields.allFields);

            System.out.printf("\r\n已复查新增的数据，Id：%s\r\n",
                              dataCheckCreate.getId());
        }

        TempDataExtension.clearUp();
    }

    /**
     * 测试批量新增功能（包含文件数据列）
     *
     * @param name 名称
     */
    @ParameterizedTest
    @MethodSource("project.extension.mybatis.edge.configure.TestDataSourceConfigure#getMultiTestDataSourceName")
    @DisplayName("225.测试批量新增功能（包含文件数据列）")
    @Order(225)
    public void _225(String name)
            throws
            Throwable {
        INaiveSql naiveSql = OrmObjectResolve.getOrm(TestDataSourceConfigure.getTestDataSource(name));

        List<TestBlobEntity> dataCreateList = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            dataCreateList.add(TempDataExtension.generateData(TestBlobEntity.class));
        }

        int rowsCreate = naiveSql.batchInsert(TestBlobEntity.class,
                                              dataCreateList)
                                 .executeAffrows();

        Assertions.assertEquals(dataCreateList.size(),
                                rowsCreate,
                                "批量新增数据失败");

        System.out.printf("\r\n已批量新增%d条数据\r\n",
                          dataCreateList.size());

        for (TestBlobEntity dataCreate : dataCreateList) {
            Assertions.assertNotNull(dataCreate.getId(),
                                     "新增数据读取自增Id失败");

            System.out.printf("\r\n已新增数据，Id：%s\r\n",
                              dataCreate.getId());

            TestBlobEntity dataCheckCreate = naiveSql.select(TestBlobEntity.class)
                                                     .where(x -> x.and(TBE_Fields.id,
                                                                       FilterCompare.Eq,
                                                                       dataCreate.getId()))
                                                     .first();

            Assertions.assertNotNull(dataCheckCreate,
                                     "查询新增的数据失败");

            TempDataExtension.putData(name,
                                      TestBlobEntity.class,
                                      dataCheckCreate.getId(),
                                      dataCheckCreate);

            AssertExtension.assertEquals(dataCreate,
                                         dataCheckCreate,
                                         true,
                                         TBE_Fields.allFields);

            System.out.printf("\r\n已复查新增的数据，Id：%s\r\n",
                              dataCheckCreate.getId());
        }

        TempDataExtension.clearUp();
    }

    /**
     * 测试批量新增事务回滚功能
     *
     * @param name 名称
     */
    @ParameterizedTest
    @MethodSource("project.extension.mybatis.edge.configure.TestDataSourceConfigure#getMultiTestDataSourceName")
    @DisplayName("226.测试批量新增事务回滚功能")
    @Order(226)
    public void _226(String name)
            throws
            Throwable {
        INaiveSql naiveSql = OrmObjectResolve.getOrm(TestDataSourceConfigure.getTestDataSource(name));

        List<TestGeneralEntity> dataCreateList = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            if (i == 50)
                //插入一条错误数据
                dataCreateList.add(new TestGeneralEntity());
            dataCreateList.add(TempDataExtension.generateData(TestGeneralEntity.class));
        }

        Exception throwEx = null;

        try {
            naiveSql.batchInsert(TestGeneralEntity.class,
                                 dataCreateList)
                    .executeAffrows();
        } catch (Exception ex) {
            throwEx = ex;
        }

        Assertions.assertNotNull(throwEx,
                                 "批量新增数据未抛出异常");

        ExceptionExtension.output(throwEx);

        for (TestGeneralEntity dataCreate : dataCreateList) {
            TestGeneralEntity dataCheckCreate = naiveSql.select(TestGeneralEntity.class)
                                                        .where(x -> x.and(TGE_Fields.id,
                                                                          FilterCompare.Eq,
                                                                          dataCreate.getId()))
                                                        .first();

            Assertions.assertNull(dataCheckCreate,
                                  "批量新增失败时数据应该为空");
        }
    }
}