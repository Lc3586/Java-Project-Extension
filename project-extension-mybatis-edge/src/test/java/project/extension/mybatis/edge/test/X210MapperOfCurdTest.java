package project.extension.mybatis.edge.test;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import project.extension.mybatis.edge.common.AssertExtension;
import project.extension.mybatis.edge.common.TempDataExtension;
import project.extension.mybatis.edge.common.OrmObjectResolve;
import project.extension.mybatis.edge.configure.TestDataSourceConfigure;
import project.extension.mybatis.edge.entity.TestBlobEntity;
import project.extension.mybatis.edge.entity.TestClobEntity;
import project.extension.mybatis.edge.entity.TestGeneralEntity;
import project.extension.mybatis.edge.entity.TestIdentityEntity;
import project.extension.mybatis.edge.entityFields.TBE_Fields;
import project.extension.mybatis.edge.entityFields.TCE_Fields;
import project.extension.mybatis.edge.entityFields.TGE_Fields;
import project.extension.mybatis.edge.entityFields.TIE_Fields;
import project.extension.mybatis.edge.mapper.ITestBlobEntityMapper;
import project.extension.mybatis.edge.mapper.ITestClobEntityMapper;
import project.extension.mybatis.edge.mapper.ITestGeneralEntityMapper;
import project.extension.mybatis.edge.mapper.ITestIdentityEntityMapper;

/**
 * 210.Mapper增删改查测试
 *
 * @author LCTR
 * @date 2023-02-06
 */
@Disabled
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class X210MapperOfCurdTest {
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
    @DisplayName("210.测试新增功能")
    @Order(210)
    public void _210(String name)
            throws
            Throwable {
        ITestGeneralEntityMapper mapper = OrmObjectResolve.getMapper(TestDataSourceConfigure.getTestDataSource(name),
                                                                     ITestGeneralEntityMapper.class);

        TestGeneralEntity dataCreate = TempDataExtension.generateData(TestGeneralEntity.class);

        int rowsCreate = mapper.insert(dataCreate);

        Assertions.assertEquals(1,
                                rowsCreate,
                                "新增数据失败");

        System.out.printf("\r\n已新增数据，Id：%s\r\n",
                          dataCreate.getId());

        TestGeneralEntity dataCheckCreate = mapper.getById(dataCreate.getId());
        Assertions.assertNotNull(dataCheckCreate,
                                 "查询新增的数据失败");

        TempDataExtension.putData(name,
                                  TestGeneralEntity.class,
                                  dataCheckCreate.getId(),
                                  dataCheckCreate);

        AssertExtension.assertEquals(dataCreate,
                                     dataCheckCreate,
                                     TGE_Fields.allFields);

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
    @DisplayName("211.测试更新功能")
    @Order(211)
    public void _211(String name)
            throws
            Throwable {
        ITestGeneralEntityMapper mapper = OrmObjectResolve.getMapper(TestDataSourceConfigure.getTestDataSource(name),
                                                                     ITestGeneralEntityMapper.class);

        TestGeneralEntity tempData = TempDataExtension.getFirstData(name,
                                                                    TestGeneralEntity.class);

        TestGeneralEntity dataUpdate = TempDataExtension.generateData(TestGeneralEntity.class);

        dataUpdate.setId(tempData.getId());

        int rowsUpdate = mapper.update(dataUpdate);

        Assertions.assertEquals(1,
                                rowsUpdate,
                                "更新数据失败");

        System.out.printf("\r\n已更新数据，Id：%s\r\n",
                          dataUpdate.getId());

        TestGeneralEntity dataCheckUpdate = mapper.getById(dataUpdate.getId());
        Assertions.assertNotNull(dataCheckUpdate,
                                 "查询新增的数据失败");

        TempDataExtension.putData(name,
                                  TestGeneralEntity.class,
                                  tempData.getId(),
                                  dataCheckUpdate);

        AssertExtension.assertEquals(dataUpdate,
                                     dataCheckUpdate,
                                     TGE_Fields.allFields);

        System.out.printf("\r\n已复查新增的数据，Id：%s\r\n",
                          dataCheckUpdate.getId());
    }

    /**
     * 测试删除功能
     *
     * @param name 名称
     */
    @ParameterizedTest
    @MethodSource("project.extension.mybatis.edge.configure.TestDataSourceConfigure#getMultiTestDataSourceName")
    @DisplayName("212.测试删除功能")
    @Order(212)
    public void _212(String name) {
        ITestGeneralEntityMapper mapper = OrmObjectResolve.getMapper(TestDataSourceConfigure.getTestDataSource(name),
                                                                     ITestGeneralEntityMapper.class);

        TestGeneralEntity tempData = TempDataExtension.getFirstData(name,
                                                                    TestGeneralEntity.class);

        int rowsDelete = mapper.deleteById(tempData.getId());

        Assertions.assertEquals(1,
                                rowsDelete,
                                "删除数据失败");

        System.out.printf("\r\n已删除数据，Id：%s\r\n",
                          tempData.getId());

        TestGeneralEntity dataCheckDelete = mapper.getById(tempData.getId());

        Assertions.assertNull(dataCheckDelete,
                              "数据未删除");

        TempDataExtension.removeData(name,
                                     TestGeneralEntity.class,
                                     tempData.getId());

        System.out.printf("\r\n已复查删除的数据，Id：%s\r\n",
                          tempData.getId());
    }

    /**
     * 测试新增功能（主键为自增列）
     *
     * @param name 名称
     */
    @ParameterizedTest
    @MethodSource("project.extension.mybatis.edge.configure.TestDataSourceConfigure#getMultiTestDataSourceName")
    @DisplayName("213.测试新增功能（主键为自增列）")
    @Order(213)
    public void _213(String name)
            throws
            Throwable {
        ITestIdentityEntityMapper mapper = OrmObjectResolve.getMapper(TestDataSourceConfigure.getTestDataSource(name),
                                                                      ITestIdentityEntityMapper.class);

        TestIdentityEntity dataCreate = TempDataExtension.generateData(TestIdentityEntity.class);

        int rowsCreate = mapper.insert(dataCreate);

        Assertions.assertEquals(1,
                                rowsCreate,
                                "新增数据失败");

        System.out.printf("\r\n已新增数据，Id：%s\r\n",
                          dataCreate.getId());

        TestIdentityEntity dataCheckCreate = mapper.getById(dataCreate.getId());
        Assertions.assertNotNull(dataCheckCreate,
                                 "查询新增的数据失败");

        TempDataExtension.putData(name,
                                  TestIdentityEntity.class,
                                  dataCheckCreate.getId(),
                                  dataCheckCreate);

        AssertExtension.assertEquals(dataCreate,
                                     dataCheckCreate,
                                     TIE_Fields.allFields);

        System.out.printf("\r\n已复查新增的数据，Id：%s\r\n",
                          dataCheckCreate.getId());
    }

    /**
     * 测试新增功能（包含长文本数据列）
     *
     * @param name 名称
     */
    @ParameterizedTest
    @MethodSource("project.extension.mybatis.edge.configure.TestDataSourceConfigure#getMultiTestDataSourceName")
    @DisplayName("214.测试新增功能（包含长文本数据列）")
    @Order(214)
    public void _214(String name)
            throws
            Throwable {
        ITestClobEntityMapper mapper = OrmObjectResolve.getMapper(TestDataSourceConfigure.getTestDataSource(name),
                                                                  ITestClobEntityMapper.class);

        TestClobEntity dataCreate = TempDataExtension.generateData(TestClobEntity.class);

        int rowsCreate = mapper.insert(dataCreate);

        Assertions.assertEquals(1,
                                rowsCreate,
                                "新增数据失败");

        System.out.printf("\r\n已新增数据，Id：%s\r\n",
                          dataCreate.getId());

        TestClobEntity dataCheckCreate = mapper.getById(dataCreate.getId());
        Assertions.assertNotNull(dataCheckCreate,
                                 "查询新增的数据失败");

        TempDataExtension.putData(name,
                                  TestClobEntity.class,
                                  dataCheckCreate.getId(),
                                  dataCheckCreate);

        AssertExtension.assertEquals(dataCreate,
                                     dataCheckCreate,
                                     TCE_Fields.allFields);

        System.out.printf("\r\n已复查新增的数据，Id：%s\r\n",
                          dataCheckCreate.getId());
    }

    /**
     * 测试新增功能（包含文件数据列）
     *
     * @param name 名称
     */
    @ParameterizedTest
    @MethodSource("project.extension.mybatis.edge.configure.TestDataSourceConfigure#getMultiTestDataSourceName")
    @DisplayName("215.测试新增功能（包含文件数据列）")
    @Order(215)
    public void _215(String name)
            throws
            Throwable {
        ITestBlobEntityMapper mapper = OrmObjectResolve.getMapper(TestDataSourceConfigure.getTestDataSource(name),
                                                                  ITestBlobEntityMapper.class);

        TestBlobEntity dataCreate = TempDataExtension.generateData(TestBlobEntity.class);

        int rowsCreate = mapper.insert(dataCreate);

        Assertions.assertEquals(1,
                                rowsCreate,
                                "新增数据失败");

        System.out.printf("\r\n已新增数据，Id：%s\r\n",
                          dataCreate.getId());

        TestBlobEntity dataCheckCreate = mapper.getById(dataCreate.getId());
        Assertions.assertNotNull(dataCheckCreate,
                                 "查询新增的数据失败");

        TempDataExtension.putData(name,
                                  TestBlobEntity.class,
                                  dataCheckCreate.getId(),
                                  dataCheckCreate);

        AssertExtension.assertEquals(dataCreate,
                                     dataCheckCreate,
                                     TBE_Fields.allFields);

        System.out.printf("\r\n已复查新增的数据，Id：%s\r\n",
                          dataCheckCreate.getId());
    }
}
