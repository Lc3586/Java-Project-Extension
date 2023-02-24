package project.extension.mybatis.edge.test;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import project.extension.mybatis.edge.core.provider.standard.INaiveSql;
import project.extension.mybatis.edge.common.AssertExtension;
import project.extension.mybatis.edge.common.TempDataExtension;
import project.extension.mybatis.edge.common.OrmObjectResolve;
import project.extension.mybatis.edge.configure.TestDataSourceConfigure;
import project.extension.mybatis.edge.entity.TestGeneralEntity;
import project.extension.mybatis.edge.entityFields.TGE_Fields;
import project.extension.mybatis.edge.extention.EntityExtension;
import project.extension.mybatis.edge.model.FilterCompare;

/**
 * 200.基础增删改查测试
 *
 * @author LCTR
 * @date 2022-12-15
 */
@Disabled
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class X200BasicsOfCurdTest {
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
     * 测试新增功能
     *
     * @param name 名称
     */
    @ParameterizedTest
    @MethodSource("project.extension.mybatis.edge.configure.TestDataSourceConfigure#getMultiTestDataSourceName")
    @DisplayName("200.测试新增功能")
    @Order(200)
    public void _200(String name)
            throws
            Throwable {
        INaiveSql naiveSql = OrmObjectResolve.getOrm(TestDataSourceConfigure.getTestDataSource(name));


        EntityExtension entityExtension = new EntityExtension(null);

        TestGeneralEntity dataCreate = TempDataExtension.generateData(TestGeneralEntity.class);

        int rowsCreate = naiveSql.insert(TestGeneralEntity.class,
                                         dataCreate)
                                 .executeAffrows();

        Assertions.assertEquals(1,
                                rowsCreate,
                                "新增数据失败");

        System.out.printf("\r\n已新增数据，Id：%s\r\n",
                          dataCreate.getId());

        TestGeneralEntity dataCheckCreate = naiveSql.select(TestGeneralEntity.class)
                                                    .where(x -> x.and(TGE_Fields.id,
                                                                      FilterCompare.Eq,
                                                                      dataCreate.getId()))
                                                    .first();

        Assertions.assertNotNull(dataCheckCreate,
                                 "查询新增的数据失败");

        TempDataExtension.putData(name,
                                  TestGeneralEntity.class,
                                  dataCheckCreate.getId(),
                                  dataCheckCreate);

        AssertExtension.assertEquals(dataCreate,
                                     dataCheckCreate,
                                     TGE_Fields.id,
                                     TGE_Fields.string,
                                     TGE_Fields.text,
                                     TGE_Fields.short_,
                                     TGE_Fields.integer);

        System.out.printf("\r\n已复查新增的数据，Id：%s\r\n",
                          dataCheckCreate.getId());
    }

    /**
     * 测试更新功能
     *
     * @param name 名称
     */
    @ParameterizedTest
    @MethodSource("project.extension.mybatis.edge.configure.TestDataSourceConfigure#getMultiTestDataSourceName")
    @DisplayName("201.测试更新功能")
    @Order(201)
    public void _201(String name)
            throws
            Throwable {
        INaiveSql naiveSql = OrmObjectResolve.getOrm(TestDataSourceConfigure.getTestDataSource(name));

        TestGeneralEntity tempData = TempDataExtension.getFirstData(name,
                                                                    TestGeneralEntity.class);

        TestGeneralEntity dataUpdate = TempDataExtension.generateData(TestGeneralEntity.class);

        dataUpdate.setId(tempData.getId());

        int rowsUpdate = naiveSql.update(TestGeneralEntity.class,
                                         dataUpdate)
                                 .executeAffrows();

        Assertions.assertEquals(1,
                                rowsUpdate,
                                "更新数据失败");

        System.out.printf("\r\n已更新数据，Id：%s\r\n",
                          dataUpdate.getId());

        TestGeneralEntity dataCheckUpdate = naiveSql.select(TestGeneralEntity.class)
                                                    .where(x -> x.and(TGE_Fields.id,
                                                                      FilterCompare.Eq,
                                                                      dataUpdate.getId()))
                                                    .first();

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
     * 测试删除功能
     *
     * @param name 名称
     */
    @ParameterizedTest
    @MethodSource("project.extension.mybatis.edge.configure.TestDataSourceConfigure#getMultiTestDataSourceName")
    @DisplayName("202.测试删除功能")
    @Order(202)
    public void _202(String name) {
        INaiveSql naiveSql = OrmObjectResolve.getOrm(TestDataSourceConfigure.getTestDataSource(name));

        TestGeneralEntity tempData = TempDataExtension.getFirstData(name,
                                                                    TestGeneralEntity.class);

        int rowsDelete = naiveSql.delete(TestGeneralEntity.class,
                                         tempData)
                                 .executeAffrows();

        Assertions.assertEquals(1,
                                rowsDelete,
                                "删除数据失败");

        System.out.printf("\r\n已删除数据，Id：%s\r\n",
                          tempData.getId());

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