package project.extension.mybatis.edge.test;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import project.extension.mybatis.edge.INaiveSql;
import project.extension.mybatis.edge.common.AssertExtension;
import project.extension.mybatis.edge.common.TempDataExtension;
import project.extension.mybatis.edge.common.OrmObjectResolve;
import project.extension.mybatis.edge.configure.TestDataSourceConfigure;
import project.extension.mybatis.edge.dbContext.repository.IBaseRepository_Key;
import project.extension.mybatis.edge.entity.CommonQuickInput;
import project.extension.mybatis.edge.entityFields.CQI_Fields;
import project.extension.mybatis.edge.extention.EntityExtension;

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

        CommonQuickInput dataCheckCreate = repository_key.getById(dataCreate.getId());

        Assertions.assertNotNull(dataCheckCreate,
                                 "查询新增的数据失败");

        TempDataExtension.putData(name,
                                  CommonQuickInput.class,
                                  dataCreate.getId(),
                                  dataCreate);

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

        IBaseRepository_Key<CommonQuickInput, String> repository_key
                = naiveSql.getRepository_Key(CommonQuickInput.class,
                                             String.class);

        Assertions.assertNotNull(repository_key,
                                 "获取Repository_Key失败");

        repository_key.update(dataUpdate);

        System.out.printf("\r\n已更新数据，Id：%s\r\n",
                          dataUpdate.getId());

        CommonQuickInput dataCheckUpdate = repository_key.getById(dataUpdate.getId());

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

        CommonQuickInput tempData = TempDataExtension.getFirstData(name,
                                                                   CommonQuickInput.class);

        IBaseRepository_Key<CommonQuickInput, String> repository_key
                = naiveSql.getRepository_Key(CommonQuickInput.class,
                                             String.class);

        Assertions.assertNotNull(repository_key,
                                 "获取Repository_Key失败");

        repository_key.delete(tempData);

        System.out.printf("\r\n已删除数据，Id：%s\r\n",
                          tempData.getId());

        CommonQuickInput dataCheckDelete = repository_key.getById(tempData.getId());

        Assertions.assertNull(dataCheckDelete,
                              "数据未删除");

        TempDataExtension.removeData(name,
                                     CommonQuickInput.class,
                                     tempData.getId());

        System.out.printf("\r\n已复查删除的数据，Id：%s\r\n",
                          tempData.getId());
    }
}