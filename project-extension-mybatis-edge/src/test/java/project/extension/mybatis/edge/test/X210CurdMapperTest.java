package project.extension.mybatis.edge.test;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import project.extension.mybatis.edge.common.AssertExtension;
import project.extension.mybatis.edge.common.OrmExtension;
import project.extension.mybatis.edge.common.OrmObjectResolve;
import project.extension.mybatis.edge.entity.CommonQuickInput;
import project.extension.mybatis.edge.entityFields.CQI_Fields;
import project.extension.mybatis.edge.extention.EntityExtension;
import project.extension.mybatis.edge.mapper.ICommonQuickInputMapper;

import java.util.HashMap;
import java.util.Map;

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
     * 测试新增功能
     *
     * @param name 名称
     */
    @ParameterizedTest
    @MethodSource("multiDataSourceTest")
    @DisplayName("210.测试新增功能")
    @Order(210)
    public void _210(String name)
            throws
            Throwable {
        ICommonQuickInputMapper mapper = OrmObjectResolve.getMapper(name,
                                                                    ICommonQuickInputMapper.class);
        EntityExtension entityExtension = new EntityExtension(null);

        CommonQuickInput dataCreate = entityExtension.initialization(new CommonQuickInput());
        dataCreate.setCategory("测试分类");
        dataCreate.setContent("测试内容");
        dataCreate.setKeyword("测试关键字");
        dataCreate.setPublic_(true);

        int rowsCreate = mapper.insert(dataCreate);

        Assertions.assertEquals(1,
                                rowsCreate,
                                "新增数据失败");

        System.out.printf("\r\n已新增数据，Id：%s\r\n",
                          dataCreate.getId());

        CommonQuickInput dataCheckCreate = mapper.getById(dataCreate.getId());
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
     *
     * @param name 名称
     */
    @ParameterizedTest
    @MethodSource("multiDataSourceTest")
    @DisplayName("211.测试更新功能")
    @Order(211)
    public void _211(String name)
            throws
            Throwable {
        ICommonQuickInputMapper mapper = OrmObjectResolve.getMapper(name,
                                                                    ICommonQuickInputMapper.class);

        CommonQuickInput dataUpdate = new CommonQuickInput();

        dataUpdate.setId(tempData.getId());
        dataUpdate.setCategory("测试分类1");
        dataUpdate.setContent("测试内容1");
        dataUpdate.setKeyword("测试关键字1");
        dataUpdate.setPublic_(false);
        dataUpdate.setCreateBy(tempData.getCreateBy());
        dataUpdate.setCreateTime(tempData.getCreateTime());

        int rowsUpdate = mapper.update(dataUpdate);

        Assertions.assertEquals(1,
                                rowsUpdate,
                                "更新数据失败");

        System.out.printf("\r\n已更新数据，Id：%s\r\n",
                          dataUpdate.getId());

        CommonQuickInput dataCheckUpdate = mapper.getById(dataUpdate.getId());
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
     *
     * @param name 名称
     */
    @ParameterizedTest
    @MethodSource("multiDataSourceTest")
    @DisplayName("212.测试删除功能")
    @Order(212)
    public void _212(String name) {
        ICommonQuickInputMapper mapper = OrmObjectResolve.getMapper(name,
                                                                    ICommonQuickInputMapper.class);

        int rowsDelete = mapper.deleteById(tempData.getId());

        Assertions.assertEquals(1,
                                rowsDelete,
                                "删除数据失败");

        System.out.printf("\r\n已删除数据，Id：%s\r\n",
                          tempData.getId());

        CommonQuickInput dataCheckDelete = mapper.getById(tempData.getId());

        Assertions.assertNull(dataCheckDelete,
                              "数据未删除");

        System.out.printf("\r\n已复查删除的数据，Id：%s\r\n",
                          tempData.getId());

        tempData = null;
    }
}
