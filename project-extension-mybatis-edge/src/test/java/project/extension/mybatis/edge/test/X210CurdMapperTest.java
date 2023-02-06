package project.extension.mybatis.edge.test;

import org.junit.jupiter.api.*;
import project.extension.ioc.IOCExtension;
import project.extension.mybatis.edge.common.AssertExtension;
import project.extension.mybatis.edge.common.OrmExtension;
import project.extension.mybatis.edge.common.OrmInjection;
import project.extension.mybatis.edge.entity.CommonQuickInput;
import project.extension.mybatis.edge.entityFields.CQI_Fields;
import project.extension.mybatis.edge.extention.EntityExtension;
import project.extension.mybatis.edge.mapper.ICommonQuickInputMapper;

/**
 * 210.Mapper增删改查测试
 *
 * @author LCTR
 * @date 2023-02-06
 */
@Disabled
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class X210CurdMapperTest {
    /**
     * 临时数据
     */
    private static CommonQuickInput tempData;

    /**
     * 快捷输入Mapper
     */
    private static ICommonQuickInputMapper ICommonQuickInputMapper;

    /**
     * 实体类拓展方法
     */
    private static EntityExtension entityExtension;

    /**
     * 注入
     */
    @BeforeEach
    public void injection() {
        OrmInjection.injection();
        ICommonQuickInputMapper = IOCExtension.applicationContext.getBean(ICommonQuickInputMapper.class);
        entityExtension = new EntityExtension(null);
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
    @DisplayName("210.测试新增功能")
    @Order(210)
    public void _210()
            throws
            Throwable {
        CommonQuickInput dataCreate = entityExtension.initialization(new CommonQuickInput());
        dataCreate.setCategory("测试分类");
        dataCreate.setContent("测试内容");
        dataCreate.setKeyword("测试关键字");
        dataCreate.setPublic_(true);

        int rowsCreate = ICommonQuickInputMapper.insert(dataCreate);

        Assertions.assertEquals(1,
                                rowsCreate,
                                "新增数据失败");

        System.out.printf("\r\n已新增数据，Id：%s\r\n",
                          dataCreate.getId());

        CommonQuickInput dataCheckCreate = ICommonQuickInputMapper.getById(dataCreate.getId());
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
    @DisplayName("211.测试更新功能")
    @Order(211)
    public void _211()
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

        int rowsUpdate = ICommonQuickInputMapper.update(dataUpdate);

        Assertions.assertEquals(1,
                                rowsUpdate,
                                "更新数据失败");

        System.out.printf("\r\n已更新数据，Id：%s\r\n",
                          dataUpdate.getId());

        CommonQuickInput dataCheckUpdate = ICommonQuickInputMapper.getById(dataUpdate.getId());
        Assertions.assertNotNull(dataCheckUpdate,
                                 "查询新增的数据失败");

        AssertExtension.assertEquals(dataUpdate,
                                     dataCheckUpdate,
                                     CQI_Fields.id,
                                     CQI_Fields.category,
                                     CQI_Fields.content,
                                     CQI_Fields.keyword,
                                     CQI_Fields.public_);

        System.out.printf("\r\n已复查新增的数据，Id：%s\r\n",
                          dataCheckUpdate.getId());

        tempData = dataCheckUpdate;
    }

    /**
     * 测试删除功能
     */
    @Test
    @DisplayName("212.测试删除功能")
    @Order(212)
    public void _212() {
        int rowsDelete = ICommonQuickInputMapper.deleteById(tempData.getId());

        Assertions.assertEquals(1,
                                rowsDelete,
                                "删除数据失败");

        System.out.printf("\r\n已删除数据，Id：%s\r\n",
                          tempData.getId());

        CommonQuickInput dataCheckDelete = ICommonQuickInputMapper.getById(tempData.getId());

        Assertions.assertNull(dataCheckDelete,
                              "数据未删除");

        System.out.printf("\r\n已复查删除的数据，Id：%s\r\n",
                          tempData.getId());

        tempData = null;
    }
}
