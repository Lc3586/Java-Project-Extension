package project.extension.mybatis.edge;

import org.junit.FixMethodOrder;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.junit4.SpringRunner;
import project.extension.mybatis.edge.config.BaseConfig;
import project.extension.mybatis.edge.core.ado.NaiveDataSource;
import project.extension.mybatis.edge.core.provider.standard.IDbFirst;
import project.extension.mybatis.edge.entity.CommonQuickInput;
import project.extension.mybatis.edge.entityFields.CQI_Fields;
import project.extension.mybatis.edge.extention.EntityExtension;
import project.extension.mybatis.edge.model.FilterCompare;
import project.extension.standard.exception.ModuleException;
import project.extension.tuple.Tuple2;

import javax.sql.DataSource;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

/**
 * SpringBoot构建测试
 *
 * @author LCTR
 * @date 2022-12-15
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = SpringBootTestApplication.class)
@DisplayName("基础功能测试")
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class BaseTest {
    /**
     * 数据必须相等
     *
     * @param data1      数据1
     * @param data2      数据2
     * @param fieldNames 要比较的字段
     */
    private void assertEquals(Object data1,
                              Object data2,
                              String... fieldNames)
            throws
            Throwable {
        Class<?> type = data1.getClass();

        for (String fieldName : fieldNames) {
            Field field = type.getDeclaredField(fieldName);
            field.setAccessible(true);

            Assertions.assertNotEquals(null,
                                       field,
                                       String.format("%s中未找到%s字段",
                                                     type.getName(),
                                                     fieldName));

            Object value1 = field.get(data1);
            Object value2 = field.get(data2);

            Assertions.assertEquals(value1,
                                    value2,
                                    String.format("两个%s类型的数据对象中%s字段的值不相等：%s ≠ %s",
                                                  type.getName(),
                                                  fieldName,
                                                  value1,
                                                  value2));

            System.out.printf("\r\n两个%s类型的数据对象中%s字段的值相等：%s = %s\r\n",
                              type.getName(),
                              fieldName,
                              value1,
                              value2);
        }
    }

    @Autowired
    public ConfigurableApplicationContext application;

    /**
     * 测试程序是否已自动注入
     */
    @Test
    @DisplayName("测试程序是否已自动注入")
    public void a0()
            throws
            Throwable {
        BaseConfig baseConfig = application.getBean(BaseConfig.class);

        Assertions.assertNotEquals(null,
                                   baseConfig,
                                   "未获取到BaseConfig");

        System.out.printf("\r\n%s已注入\r\n",
                          BaseConfig.class.getName());

        NaiveDataSource naiveDataSource = application.getBean(NaiveDataSource.class);

        Assertions.assertNotEquals(null,
                                   naiveDataSource,
                                   "未获取到NaiveDataSource");

        System.out.printf("\r\n%s已注入\r\n",
                          NaiveDataSource.class.getName());

        Map<Object, DataSource> druidDataSourceMap = naiveDataSource.getResolvedDataSources();

        Assertions.assertNotEquals(0,
                                   druidDataSourceMap.size(),
                                   "未获取到任何DruidDataSource");

        for (String dataSource : baseConfig.getAllDataSource()) {
            Assertions.assertNotEquals(false,
                                       druidDataSourceMap.containsKey(dataSource),
                                       String.format("未获取到name为%s的DruidDataSource",
                                                     dataSource));

            System.out.printf("\r\n已创建%s数据源\r\n",
                              dataSource);
        }

        INaiveSql naiveSql = application.getBean(INaiveSql.class);

        Assertions.assertNotEquals(null,
                                   naiveSql,
                                   "未获取到INaiveSql");

        System.out.printf("\r\n%s已注入\r\n",
                          INaiveSql.class.getName());

        IDbFirst dbFirst = naiveSql.getDbFirst();

        Assertions.assertNotEquals(null,
                                   dbFirst,
                                   "未获取到IDbFirst");

        System.out.printf("\r\n成功获取%s\r\n",
                          IDbFirst.class.getName());

        List<String> databases = dbFirst.getDatabases();
        for (String database : databases) {
            System.out.printf("\r\n查询到数据库：%s%n\r\n",
                              database);
        }
    }

    /**
     * 测试ORM的增删改查功能
     */
    @Test
    @DisplayName("测试ORM的增删改查功能")
    public void a1()
            throws
            Throwable {
        INaiveSql naiveSql = application.getBean(INaiveSql.class);

        Assertions.assertNotEquals(null,
                                   naiveSql,
                                   "未获取到INaiveSql");

        System.out.printf("\r\n成功获取%s\r\n",
                          INaiveSql.class.getName());

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

        Assertions.assertNotEquals(null,
                                   dataCheckCreate,
                                   "查询新增的数据失败");

        assertEquals(dataCreate,
                     dataCheckCreate,
                     CQI_Fields.id,
                     CQI_Fields.category,
                     CQI_Fields.content,
                     CQI_Fields.keyword,
                     CQI_Fields.public_);

        System.out.printf("\r\n已复查新增的数据，Id：%s\r\n",
                          dataCheckCreate.getId());

        CommonQuickInput dataUpdate = new CommonQuickInput();

        dataUpdate.setId(dataCheckCreate.getId());
        dataUpdate.setCategory("测试分类1");
        dataUpdate.setContent("测试内容1");
        dataUpdate.setKeyword("测试关键字1");
        dataUpdate.setPublic_(false);
        dataUpdate.setCreateBy(dataCheckCreate.getCreateBy());
        dataUpdate.setCreateTime(dataCheckCreate.getCreateTime());

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

        Assertions.assertNotEquals(null,
                                   dataCheckUpdate,
                                   "查询更新的数据失败");

        System.out.printf("\r\n已复查更新的数据，Id：%s\r\n",
                          dataCheckUpdate.getId());

        assertEquals(dataUpdate,
                     dataCheckUpdate,
                     CQI_Fields.id,
                     CQI_Fields.category,
                     CQI_Fields.content,
                     CQI_Fields.keyword,
                     CQI_Fields.public_);

        int rowsDelete = naiveSql.delete(CommonQuickInput.class,
                                         dataUpdate)
                                 .executeAffrows();

        Assertions.assertEquals(1,
                                rowsDelete,
                                "删除数据失败");

        System.out.printf("\r\n已删除数据，Id：%s\r\n",
                          dataUpdate.getId());

        CommonQuickInput dataCheckDelete = naiveSql.select(CommonQuickInput.class)
                                                   .where(x -> x.and(CQI_Fields.id,
                                                                     FilterCompare.Eq,
                                                                     dataUpdate.getId()))
                                                   .first();

        Assertions.assertNull(dataCheckDelete,
                              "数据未删除");

        System.out.printf("\r\n已复查删除的数据，Id：%s\r\n",
                          dataCheckUpdate.getId());
    }

    /**
     * 测试事务
     */
    @Test
    @DisplayName("测试事务")
    public void a2()
            throws
            Throwable {
        INaiveSql naiveSql = application.getBean(INaiveSql.class);

        Assertions.assertNotEquals(null,
                                   naiveSql,
                                   "未获取到INaiveSql");

        System.out.printf("\r\n成功获取%s\r\n",
                          INaiveSql.class.getName());

        EntityExtension entityExtension = new EntityExtension(null);

        CommonQuickInput dataCreate = entityExtension.initialization(new CommonQuickInput());
        dataCreate.setCategory("测试分类");
        dataCreate.setContent("测试内容");
        dataCreate.setKeyword("测试关键字");
        dataCreate.setPublic_(true);

        Tuple2<Boolean, Exception> tranCreate = naiveSql.transaction(() -> {
            int rowsCreate = naiveSql.insert(CommonQuickInput.class,
                                             dataCreate)
                                     .executeAffrows();

            Assertions.assertEquals(1,
                                    rowsCreate,
                                    "新增数据失败");

            System.out.printf("\r\n已新增数据，Id：%s\r\n",
                              dataCreate.getId());

            System.out.println("回滚事务");
            throw new ModuleException("回滚事务");
        });

        Assertions.assertEquals(false,
                                tranCreate.a,
                                "事务未回滚");

        System.out.println("事务已回滚");

        CommonQuickInput dataCheckCreate = naiveSql.select(CommonQuickInput.class)
                                                   .where(x -> x.and(CQI_Fields.id,
                                                                     FilterCompare.Eq,
                                                                     dataCreate.getId()))
                                                   .first();

        Assertions.assertNull(dataCheckCreate,
                              "事务回滚后依然能查询到新增的数据");

        System.out.println("事务回滚成功");
    }
}