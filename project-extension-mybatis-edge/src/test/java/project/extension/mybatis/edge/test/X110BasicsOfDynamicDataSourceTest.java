package project.extension.mybatis.edge.test;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import project.extension.ioc.IOCExtension;
import project.extension.mybatis.edge.annotations.NaiveDataSource;
import project.extension.mybatis.edge.common.OrmObjectResolve;
import project.extension.mybatis.edge.configure.TestDataSourceConfigure;
import project.extension.mybatis.edge.core.provider.standard.IDbFirst;
import project.extension.mybatis.edge.entity.TestGeneralEntity;
import project.extension.mybatis.edge.mapper.ITestGeneralEntityMapper;

import java.util.List;

/**
 * 110.基础动态数据源测试
 *
 * @author LCTR
 * @date 2023-07-07
 */
@Disabled
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@NaiveDataSource("sqlserver")
public class X110BasicsOfDynamicDataSourceTest {
    private ITestGeneralEntityMapper testGeneralEntityMapper;

    /**
     * 注入
     */
    @BeforeEach
    public void injection() {
        testGeneralEntityMapper = IOCExtension.applicationContext.getBean(ITestGeneralEntityMapper.class);
    }

    /**
     * 测试通过方法注解切换至达梦数据库
     */
    @Test
    @DisplayName("111.测试通过方法注解切换至达梦数据库")
    @Order(111)
    @NaiveDataSource("dameng")
    public void _111() {
        List<TestGeneralEntity> list = testGeneralEntityMapper.list(new TestGeneralEntity());
    }

    /**
     * 测试通过类注解切换至SQL Server数据库
     */
    @Test
    @DisplayName("112.测试通过类注解切换至SQL Server数据库")
    @Order(112)
    public void _112() {
        List<TestGeneralEntity> list = testGeneralEntityMapper.list(new TestGeneralEntity());
    }
}