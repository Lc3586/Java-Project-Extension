package project.extension.mybatis.edge.test;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import project.extension.console.extension.ConsoleUtils;
import project.extension.mybatis.edge.common.AssertExtension;
import project.extension.mybatis.edge.common.OrmObjectResolve;
import project.extension.mybatis.edge.common.TempDataExtension;
import project.extension.mybatis.edge.configure.TestDataSourceConfigure;
import project.extension.mybatis.edge.core.mapper.MappedStatementIdManager;
import project.extension.mybatis.edge.core.provider.standard.INaiveSql;
import project.extension.mybatis.edge.entity.TestGeneralEntity;
import project.extension.mybatis.edge.entityFields.TGE_Fields;
import project.extension.mybatis.edge.model.FilterCompare;

import java.util.concurrent.*;
import java.util.regex.Pattern;

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
     * 清理数据
     */
    @AfterAll
    public static void clean() {
        TempDataExtension.clearUp();
    }

    static final ConcurrentMap<String, Long> idMap = new ConcurrentHashMap<>();

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

        CompletableFuture[] tasks = new CompletableFuture[20];
        ExecutorService pool = Executors.newFixedThreadPool(tasks.length);

        for (int i = 0; i < 20; i++) {
            tasks[i] = CompletableFuture.runAsync(() -> {
                                                      for (int j = 0; j < 100; j++) {
                                                          try {
                                                              TestGeneralEntity dataCreate = TempDataExtension.generateData(TestGeneralEntity.class);

                                                              if (idMap.containsKey(dataCreate.getId()))
                                                                  Assertions.fail(String.format("%d线程和%d线程生成了重复的id: %s",
                                                                                                Thread.currentThread()
                                                                                                      .getId(),
                                                                                                idMap.get(dataCreate.getId()),
                                                                                                dataCreate.getId()));

                                                              idMap.put(dataCreate.getId(),
                                                                        Thread.currentThread()
                                                                              .getId());

                                                              int rowsCreate = naiveSql.insert(TestGeneralEntity.class,
                                                                                               dataCreate)
                                                                                       .executeAffrows();

                                                              Assertions.assertEquals(1,
                                                                                      rowsCreate,
                                                                                      "新增数据失败");

//                                                              System.out.printf("\r\n已新增数据，Id：%s\r\n",
//                                                                                dataCreate.getId());

                                                              TestGeneralEntity dataCheckCreate = naiveSql.select(TestGeneralEntity.class)
                                                                                                          .where(x -> x.and(TGE_Fields.id,
                                                                                                                            FilterCompare.Eq,
                                                                                                                            dataCreate.getId()))
                                                                                                          .first();

                                                              Assertions.assertNotNull(dataCheckCreate,
                                                                                       "查询新增的数据失败");

                                                              AssertExtension.assertEquals(dataCreate,
                                                                                           dataCheckCreate,
                                                                                           false,
                                                                                           TGE_Fields.allFields);

//                                                              System.out.printf("\r\n已复查新增的数据，Id：%s\r\n",
//                                                                                dataCheckCreate.getId());

                                                              TempDataExtension.putData(name,
                                                                                        TestGeneralEntity.class,
                                                                                        dataCheckCreate.getId(),
                                                                                        dataCheckCreate);

                                                              int rowsDelete = naiveSql.delete(TestGeneralEntity.class,
                                                                                               dataCreate)
                                                                                       .executeAffrows();

                                                              Assertions.assertEquals(1,
                                                                                      rowsDelete,
                                                                                      "删除数据失败");

//                                                              System.out.printf("\r\n已删除数据，Id：%s\r\n",
//                                                                                dataCreate.getId());

                                                              TestGeneralEntity dataCheckDelete = naiveSql.select(TestGeneralEntity.class)
                                                                                                          .where(x -> x.and(TGE_Fields.id,
                                                                                                                            FilterCompare.Eq,
                                                                                                                            dataCreate.getId()))
                                                                                                          .first();

                                                              Assertions.assertNull(dataCheckDelete,
                                                                                    "数据未删除");

//                                                              System.out.printf("\r\n已复查删除的数据，Id：%s\r\n",
//                                                                                dataCreate.getId());

                                                              TempDataExtension.removeData(name,
                                                                                           TestGeneralEntity.class,
                                                                                           dataCreate.getId());
                                                          } catch (IllegalArgumentException ex) {
                                                              String id = Pattern.compile("^Mapped Statements collection does not contain value for (.*?)$")
                                                                                 .matcher(ex.getMessage())
                                                                                 .group(1);

                                                              System.err.printf("\r\n%s\r\n",
                                                                                id);

                                                              throw ex;
                                                          } catch (Exception ex) {
                                                              Assertions.fail("执行任务失败",
                                                                              ex);
                                                          }
                                                      }
                                                  },
                                                  pool);
        }

        CompletableFuture<Void> printTask = CompletableFuture.runAsync(() -> {
                                                                           while (true) {
                                                                               try {
                                                                                   ConsoleUtils.consoleWrite("并发量",
                                                                                                             MappedStatementIdManager.used);

                                                                                   if (CompletableFuture.allOf(tasks)
                                                                                                        .isDone() && MappedStatementIdManager.used == 0) {
                                                                                       ConsoleUtils.consoleWrite("并发量",
                                                                                                                 "已结束");
                                                                                       break;
                                                                                   }

                                                                                   Thread.sleep(1000);
                                                                               } catch (Exception ignore) {

                                                                               }
                                                                           }
                                                                       },
                                                                       Executors.newSingleThreadExecutor());

        try {
            CompletableFuture.allOf(printTask)
                             .get();
        } catch (Exception ex) {
            Assertions.fail("等待任务执行结束失败",
                            ex);
        }
    }
}