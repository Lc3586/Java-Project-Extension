package project.extension.mybatis.edge.test;

import org.junit.jupiter.api.*;
import project.extension.mybatis.edge.common.AssertExtension;
import project.extension.mybatis.edge.common.OrmExtension;
import project.extension.mybatis.edge.common.OrmInjection;
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
public class X200CurdBasicsTest {
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
     * 测试新增功能
     */
    @Test
    @DisplayName("200.测试新增功能")
    @Order(200)
    public void _200()
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

        CommonQuickInput dataCheckCreate = OrmInjection.masterNaiveSql.select(CommonQuickInput.class)
                                                                      .where(x -> x.and(CQI_Fields.id,
                                                                                        FilterCompare.Eq,
                                                                                        dataCreate.getId()))
                                                                      .first();

        Assertions.assertNotNull(dataCheckCreate,
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
     * 测试更新功能
     */
    @Test
    @DisplayName("201.测试更新功能")
    @Order(201)
    public void _201()
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

        int rowsUpdate = OrmInjection.masterNaiveSql.update(CommonQuickInput.class,
                                                            dataUpdate)
                                                    .executeAffrows();

        Assertions.assertEquals(1,
                                rowsUpdate,
                                "更新数据失败");

        System.out.printf("\r\n已更新数据，Id：%s\r\n",
                          dataUpdate.getId());

        CommonQuickInput dataCheckUpdate = OrmInjection.masterNaiveSql.select(CommonQuickInput.class)
                                                                      .where(x -> x.and(CQI_Fields.id,
                                                                                        FilterCompare.Eq,
                                                                                        dataUpdate.getId()))
                                                                      .first();

        Assertions.assertNotNull(dataCheckUpdate,
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
     * 测试删除功能
     */
    @Test
    @DisplayName("202.测试删除功能")
    @Order(202)
    public void _202() {
        int rowsDelete = OrmInjection.masterNaiveSql.delete(CommonQuickInput.class,
                                                            tempData)
                                                    .executeAffrows();

        Assertions.assertEquals(1,
                                rowsDelete,
                                "删除数据失败");

        System.out.printf("\r\n已删除数据，Id：%s\r\n",
                          tempData.getId());

        CommonQuickInput dataCheckDelete = OrmInjection.masterNaiveSql.select(CommonQuickInput.class)
                                                                      .where(x -> x.and(CQI_Fields.id,
                                                                                        FilterCompare.Eq,
                                                                                        tempData.getId()))
                                                                      .first();

        Assertions.assertNull(dataCheckDelete,
                              "数据未删除");

        System.out.printf("\r\n已复查删除的数据，Id：%s\r\n",
                          tempData.getId());

        tempData = null;
    }
}