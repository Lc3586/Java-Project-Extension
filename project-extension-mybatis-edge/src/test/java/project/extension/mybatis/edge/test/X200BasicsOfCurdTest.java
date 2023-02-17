package project.extension.mybatis.edge.test;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import project.extension.mybatis.edge.INaiveSql;
import project.extension.mybatis.edge.common.AssertExtension;
import project.extension.mybatis.edge.common.TempDataExtension;
import project.extension.mybatis.edge.common.OrmObjectResolve;
import project.extension.mybatis.edge.config.TestDataSourceConfig;
import project.extension.mybatis.edge.entity.CommonQuickInput;
import project.extension.mybatis.edge.entityFields.CQI_Fields;
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
    @MethodSource("project.extension.mybatis.edge.config.TestDataSourceConfig#getMultiTestDataSourceName")
    @DisplayName("200.测试新增功能")
    @Order(200)
    public void _200(String name)
            throws
            Throwable {
        INaiveSql naiveSql = OrmObjectResolve.getOrm(TestDataSourceConfig.getTestDataSource(name));

        EntityExtension entityExtension = new EntityExtension(null);

        CommonQuickInput dataCreate = entityExtension.initialization(new CommonQuickInput());
        dataCreate.setCategory("测试分类");
        dataCreate.setContent("测试内容");
        dataCreate.setKeyword("测试关键字");
        dataCreate.setPublic_(true);

        int rowsCreate = naiveSql.insert(CommonQuickInput.class,
                                         dataCreate)
                                 .executeAffrows();

        Assertions.assertEquals(1,
                                rowsCreate,
                                "新增数据失败");

        System.out.printf("\r\n已新增数据，Id：%s\r\n",
                          dataCreate.getId());

        CommonQuickInput dataCheckCreate = naiveSql.select(CommonQuickInput.class)
                                                   .where(x -> x.and(CQI_Fields.id,
                                                                     FilterCompare.Eq,
                                                                     dataCreate.getId()))
                                                   .first();

        Assertions.assertNotNull(dataCheckCreate,
                                 "查询新增的数据失败");

        TempDataExtension.putData(name,
                                  CommonQuickInput.class,
                                  dataCheckCreate.getId(),
                                  dataCheckCreate);

        AssertExtension.assertEquals(dataCreate,
                                     dataCheckCreate,
                                     CQI_Fields.id,
                                     CQI_Fields.category,
                                     CQI_Fields.content,
                                     CQI_Fields.keyword,
                                     CQI_Fields.public_);

        System.out.printf("\r\n已复查新增的数据，Id：%s\r\n",
                          dataCheckCreate.getId());
    }

    /**
     * 测试更新功能
     *
     * @param name 名称
     */
    @ParameterizedTest
    @MethodSource("project.extension.mybatis.edge.config.TestDataSourceConfig#getMultiTestDataSourceName")
    @DisplayName("201.测试更新功能")
    @Order(201)
    public void _201(String name)
            throws
            Throwable {
        INaiveSql naiveSql = OrmObjectResolve.getOrm(TestDataSourceConfig.getTestDataSource(name));

        CommonQuickInput tempData = TempDataExtension.getFirstData(name,
                                                                   CommonQuickInput.class);

        CommonQuickInput dataUpdate = new CommonQuickInput();

        dataUpdate.setId(tempData.getId());
        dataUpdate.setCategory("测试分类1");
        dataUpdate.setContent("测试内容1");
        dataUpdate.setKeyword("测试关键字1");
        dataUpdate.setPublic_(false);
        dataUpdate.setCreateBy(tempData.getCreateBy());
        dataUpdate.setCreateTime(tempData.getCreateTime());

        int rowsUpdate = naiveSql.update(CommonQuickInput.class,
                                         dataUpdate)
                                 .executeAffrows();

        Assertions.assertEquals(1,
                                rowsUpdate,
                                "更新数据失败");

        System.out.printf("\r\n已更新数据，Id：%s\r\n",
                          dataUpdate.getId());

        CommonQuickInput dataCheckUpdate = naiveSql.select(CommonQuickInput.class)
                                                   .where(x -> x.and(CQI_Fields.id,
                                                                     FilterCompare.Eq,
                                                                     dataUpdate.getId()))
                                                   .first();

        Assertions.assertNotNull(dataCheckUpdate,
                                 "查询更新的数据失败");

        TempDataExtension.putData(name,
                                  CommonQuickInput.class,
                                  tempData.getId(),
                                  dataCheckUpdate);

        AssertExtension.assertEquals(dataUpdate,
                                     dataCheckUpdate,
                                     CQI_Fields.id,
                                     CQI_Fields.category,
                                     CQI_Fields.content,
                                     CQI_Fields.keyword,
                                     CQI_Fields.public_);

        System.out.printf("\r\n已复查更新的数据，Id：%s\r\n",
                          dataCheckUpdate.getId());
    }

    /**
     * 测试删除功能
     *
     * @param name 名称
     */
    @ParameterizedTest
    @MethodSource("project.extension.mybatis.edge.config.TestDataSourceConfig#getMultiTestDataSourceName")
    @DisplayName("202.测试删除功能")
    @Order(202)
    public void _202(String name) {
        INaiveSql naiveSql = OrmObjectResolve.getOrm(TestDataSourceConfig.getTestDataSource(name));

        CommonQuickInput tempData = TempDataExtension.getFirstData(name,
                                                                   CommonQuickInput.class);

        int rowsDelete = naiveSql.delete(CommonQuickInput.class,
                                         tempData)
                                 .executeAffrows();

        Assertions.assertEquals(1,
                                rowsDelete,
                                "删除数据失败");

        System.out.printf("\r\n已删除数据，Id：%s\r\n",
                          tempData.getId());

        CommonQuickInput dataCheckDelete = naiveSql.select(CommonQuickInput.class)
                                                   .where(x -> x.and(CQI_Fields.id,
                                                                     FilterCompare.Eq,
                                                                     tempData.getId()))
                                                   .first();

        Assertions.assertNull(dataCheckDelete,
                              "数据未删除");

        TempDataExtension.removeData(name,
                                     CommonQuickInput.class,
                                     tempData.getId());

        System.out.printf("\r\n已复查删除的数据，Id：%s\r\n",
                          tempData.getId());
    }
}