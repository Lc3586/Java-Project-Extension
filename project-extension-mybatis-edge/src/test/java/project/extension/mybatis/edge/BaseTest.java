package project.extension.mybatis.edge;

import org.junit.FixMethodOrder;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.runners.MethodSorters;
import org.springframework.boot.test.context.SpringBootTest;
import project.extension.ioc.IOCExtension;
import project.extension.mybatis.edge.common.OrmInjection;
import project.extension.mybatis.edge.common.SpringBootTestApplication;
import project.extension.mybatis.edge.core.ado.NaiveDataSource;
import project.extension.mybatis.edge.core.provider.standard.IDbFirst;
import project.extension.mybatis.edge.entity.CommonQuickInput;
import project.extension.mybatis.edge.entityFields.CQI_Fields;
import project.extension.mybatis.edge.extention.EntityExtension;
import project.extension.mybatis.edge.model.FilterCompare;

import javax.sql.DataSource;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

/**
 * 基础测试
 *
 * @author LCTR
 * @date 2022-12-15
 */
@SpringBootTest(classes = SpringBootTestApplication.class)
@DisplayName("基础测试")
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

    @BeforeEach
    public void injection() {
        OrmInjection.injection();
    }

    /**
     * 测试数据库连接是否正常
     */
    @Test
    @DisplayName("测试数据库连接是否正常")
    public void a10()
            throws
            Throwable {
        NaiveDataSource naiveDataSource = IOCExtension.applicationContext.getBean(NaiveDataSource.class);

        Assertions.assertNotEquals(null,
                                   naiveDataSource,
                                   "未获取到NaiveDataSource");

        System.out.printf("\r\n%s已注入\r\n",
                          NaiveDataSource.class.getName());

        Map<Object, DataSource> druidDataSourceMap = naiveDataSource.getResolvedDataSources();

        Assertions.assertNotEquals(0,
                                   druidDataSourceMap.size(),
                                   "未获取到任何DruidDataSource");

        for (String dataSource : OrmInjection.baseConfig.getAllDataSource()) {
            Assertions.assertNotEquals(false,
                                       druidDataSourceMap.containsKey(dataSource),
                                       String.format("未获取到name为%s的DruidDataSource",
                                                     dataSource));

            System.out.printf("\r\n已创建%s数据源\r\n",
                              dataSource);

            INaiveSql orm;
            String ormName;
            if (dataSource.equals(OrmInjection.baseConfig.getDataSource())) {
                orm = OrmInjection.masterNaiveSql;
                ormName = String.format("主库-%s",
                                        dataSource);
            } else {
                orm = OrmInjection.multiNaiveSql.getSlaveOrm(dataSource);
                ormName = String.format("从库-%s",
                                        dataSource);
            }

            Assertions.assertNotNull(orm,
                                     String.format("未获取到%s的orm",
                                                   ormName));

            IDbFirst dbFirst = orm.getDbFirst();

            Assertions.assertNotEquals(null,
                                       dbFirst,
                                       String.format("未获取到%s的IDbFirst",
                                                     ormName));

            System.out.printf("\r\n成功获取%s的%s\r\n",
                              ormName,
                              IDbFirst.class.getName());

            List<String> databases = dbFirst.getDatabases();
            for (String database : databases) {
                System.out.printf("\r\n%s查询到数据库：%s%n\r\n",
                                  ormName,
                                  database);
            }
        }
    }

    /**
     * 测试ORM的增删改查功能
     */
    @Test
    @DisplayName("测试ORM的增删改查功能")
    public void a20()
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

        int rowsDelete = OrmInjection.masterNaiveSql.delete(CommonQuickInput.class,
                                                            dataUpdate)
                                                    .executeAffrows();

        Assertions.assertEquals(1,
                                rowsDelete,
                                "删除数据失败");

        System.out.printf("\r\n已删除数据，Id：%s\r\n",
                          dataUpdate.getId());

        CommonQuickInput dataCheckDelete = OrmInjection.masterNaiveSql.select(CommonQuickInput.class)
                                                                      .where(x -> x.and(CQI_Fields.id,
                                                                                        FilterCompare.Eq,
                                                                                        dataUpdate.getId()))
                                                                      .first();

        Assertions.assertNull(dataCheckDelete,
                              "数据未删除");

        System.out.printf("\r\n已复查删除的数据，Id：%s\r\n",
                          dataCheckUpdate.getId());
    }
}