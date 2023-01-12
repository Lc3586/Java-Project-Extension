package project.extension.mybatis.edge.test;

import org.junit.jupiter.api.*;
import project.extension.mybatis.edge.common.AssertExtension;
import project.extension.mybatis.edge.common.OrmExtension;
import project.extension.mybatis.edge.common.OrmInjection;
import project.extension.mybatis.edge.dbContext.repository.IBaseRepository_Key;
import project.extension.mybatis.edge.entity.CommonQuickInput;
import project.extension.mybatis.edge.entityFields.CQI_Fields;
import project.extension.mybatis.edge.extention.EntityExtension;

/**
 * 4x.基础Repository增删改查测试
 *
 * @author LCTR
 * @date 2023-01-12
 */
@Disabled
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class X4CurdRepositoryBasicsTest {
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
     * 测试新增与查询功能
     */
    @Test
    @DisplayName("400.测试新增与查询功能")
    @Order(400)
    public void _400()
            throws
            Throwable {
        EntityExtension entityExtension = new EntityExtension(null);

        CommonQuickInput dataCreate = entityExtension.initialization(new CommonQuickInput());
        dataCreate.setCategory("测试分类");
        dataCreate.setContent("测试内容");
        dataCreate.setKeyword("测试关键字");
        dataCreate.setPublic_(true);

        IBaseRepository_Key<CommonQuickInput, String> repository_key
                = OrmInjection.masterNaiveSql.getRepository_Key(CommonQuickInput.class,
                                                                String.class);

        Assertions.assertNotNull(repository_key,
                                 "获取Repository_Key失败");

        repository_key.insert(dataCreate);

        System.out.printf("\r\n已新增数据，Id：%s\r\n",
                          dataCreate.getId());

        CommonQuickInput dataCheckCreate = repository_key.getById(dataCreate.getId());

        Assertions.assertNotEquals(null,
                                   dataCheckCreate,
                                   "查询新增的数据失败");

        AssertExtension.assertEquals(dataCreate,
                                     dataCheckCreate,
                                     CQI_Fields.id,
                                     CQI_Fields.category,
                                     CQI_Fields.content,
                                     CQI_Fields.keyword,
                                     CQI_Fields.public_);

        System.out.printf("\r\n已复查新增的数据，Id：%s\r\n",
                          dataCheckCreate.getId());

        tempData = dataCheckCreate;
    }

    /**
     * 测试更新与查询功能
     */
    @Test
    @DisplayName("401.测试更新与查询功能")
    @Order(401)
    public void _401()
            throws
            Throwable {
        CommonQuickInput dataUpdate = new CommonQuickInput();

        dataUpdate.setId(tempData.getId());
        dataUpdate.setCategory("测试分类1");
        dataUpdate.setContent("测试内容1");
        dataUpdate.setKeyword("测试关键字1");
        dataUpdate.setPublic_(false);
        dataUpdate.setCreateBy(tempData.getCreateBy());
        dataUpdate.setCreateTime(tempData.getCreateTime());

        IBaseRepository_Key<CommonQuickInput, String> repository_key
                = OrmInjection.masterNaiveSql.getRepository_Key(CommonQuickInput.class,
                                                                String.class);

        Assertions.assertNotNull(repository_key,
                                 "获取Repository_Key失败");

        repository_key.update(dataUpdate);

        System.out.printf("\r\n已更新数据，Id：%s\r\n",
                          dataUpdate.getId());

        CommonQuickInput dataCheckUpdate = repository_key.getById(dataUpdate.getId());

        Assertions.assertNotEquals(null,
                                   dataCheckUpdate,
                                   "查询更新的数据失败");

        AssertExtension.assertEquals(dataUpdate,
                                     dataCheckUpdate,
                                     CQI_Fields.id,
                                     CQI_Fields.category,
                                     CQI_Fields.content,
                                     CQI_Fields.keyword,
                                     CQI_Fields.public_);

        System.out.printf("\r\n已复查更新的数据，Id：%s\r\n",
                          dataCheckUpdate.getId());

        tempData = dataCheckUpdate;
    }

    /**
     * 测试删除与查询功能
     */
    @Test
    @DisplayName("402.测试删除与查询功能")
    @Order(402)
    public void _402() {
        IBaseRepository_Key<CommonQuickInput, String> repository_key
                = OrmInjection.masterNaiveSql.getRepository_Key(CommonQuickInput.class,
                                                                String.class);

        Assertions.assertNotNull(repository_key,
                                 "获取Repository_Key失败");

        repository_key.delete(tempData);

        System.out.printf("\r\n已删除数据，Id：%s\r\n",
                          tempData.getId());

        CommonQuickInput dataCheckDelete = repository_key.getById(tempData.getId());

        Assertions.assertNull(dataCheckDelete,
                              "数据未删除");

        System.out.printf("\r\n已复查删除的数据，Id：%s\r\n",
                          tempData.getId());

        tempData = null;
    }
}