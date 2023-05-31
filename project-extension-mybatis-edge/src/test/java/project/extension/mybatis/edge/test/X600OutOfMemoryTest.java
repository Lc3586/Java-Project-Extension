package project.extension.mybatis.edge.test;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import project.extension.mybatis.edge.common.AssertExtension;
import project.extension.mybatis.edge.common.OrmObjectResolve;
import project.extension.mybatis.edge.common.TempDataExtension;
import project.extension.mybatis.edge.configure.TestDataSourceConfigure;
import project.extension.mybatis.edge.core.provider.standard.INaiveSql;
import project.extension.mybatis.edge.entity.TestGeneralEntity;
import project.extension.mybatis.edge.entityFields.TGE_Fields;
import project.extension.mybatis.edge.model.FilterCompare;

import java.util.concurrent.CompletableFuture;

/**
 * 600.内存溢出测试
 *
 * @author LCTR
 * @date 2023-05-31
 */
@Disabled
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class X600OutOfMemoryTest {
    /**
     * 注入
     */
    @BeforeEach
    public void injection() {
        OrmObjectResolve.injection();
    }

    /**
     * 测试进行大量查询时是否会内存溢出
     *
     * @param name 名称
     */
    @ParameterizedTest
    @MethodSource("project.extension.mybatis.edge.configure.TestDataSourceConfigure#getMultiTestDataSourceName")
    @DisplayName("100.测试进行大量查询时是否会内存溢出")
    @Order(600)
    public void _600(String name) {
        INaiveSql naiveSql = OrmObjectResolve.getOrm(TestDataSourceConfigure.getTestDataSource(name));

        CompletableFuture[] tasks = new CompletableFuture[1];
        for (int i = 0; i < tasks.length; i++) {
            tasks[i] = CompletableFuture.runAsync(() -> {
                for (int j = 0; j < 1000; j++) {
                    try {
                        TestGeneralEntity dataCreate = TempDataExtension.generateData(TestGeneralEntity.class);

                        int rowsCreate = naiveSql.insert(TestGeneralEntity.class,
                                                         dataCreate)
                                                 .executeAffrows();

                        Assertions.assertEquals(1,
                                                rowsCreate,
                                                "新增数据失败");

                        System.out.printf("\r\n已新增数据，Id：%s\r\n",
                                          dataCreate.getId());

                        TestGeneralEntity dataCheckCreate = naiveSql.select(TestGeneralEntity.class)
                                                                    .where(x -> x.and(TGE_Fields.id,
                                                                                      FilterCompare.Eq,
                                                                                      dataCreate.getId()))
                                                                    .first();

                        Assertions.assertNotNull(dataCheckCreate,
                                                 "查询新增的数据失败");

                        AssertExtension.assertEquals(dataCreate,
                                                     dataCheckCreate,
                                                     TGE_Fields.allFields);

                        System.out.printf("\r\n已复查新增的数据，Id：%s\r\n",
                                          dataCheckCreate.getId());

                        int rowsDelete = naiveSql.delete(TestGeneralEntity.class,
                                                         dataCreate)
                                                 .executeAffrows();

                        Assertions.assertEquals(1,
                                                rowsDelete,
                                                "删除数据失败");

                        System.out.printf("\r\n已删除数据，Id：%s\r\n",
                                          dataCreate.getId());

                        TestGeneralEntity dataCheckDelete = naiveSql.select(TestGeneralEntity.class)
                                                                    .where(x -> x.and(TGE_Fields.id,
                                                                                      FilterCompare.Eq,
                                                                                      dataCreate.getId()))
                                                                    .first();

                        Assertions.assertNull(dataCheckDelete,
                                              "数据未删除");

                        TempDataExtension.removeData(name,
                                                     TestGeneralEntity.class,
                                                     dataCreate.getId());

                        System.out.printf("\r\n已复查删除的数据，Id：%s\r\n",
                                          dataCreate.getId());
                    } catch (Throwable ex) {
                        Assertions.fail("执行任务失败",
                                        ex);
                    }
                }
            });
        }

        try {
            CompletableFuture.allOf(tasks)
                             .get();
        } catch (Exception ex) {
            Assertions.fail("等待任务执行结束失败",
                            ex);
        }
    }
}