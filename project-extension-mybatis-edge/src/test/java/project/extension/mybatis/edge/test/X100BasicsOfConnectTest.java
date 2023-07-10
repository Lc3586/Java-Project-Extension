package project.extension.mybatis.edge.test;

import net.sf.jsqlparser.expression.LongValue;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.statement.select.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import project.extension.mybatis.edge.common.OrmObjectResolve;
import project.extension.mybatis.edge.config.DataSourceConfig;
import project.extension.mybatis.edge.configure.TestDataSourceConfigure;
import project.extension.mybatis.edge.core.provider.sqlserver.SqlServerSqlProvider;
import project.extension.mybatis.edge.core.provider.standard.IDbFirst;
import project.extension.mybatis.edge.model.DbType;

import java.util.List;

/**
 * 100.数据库连接测试
 *
 * @author LCTR
 * @date 2022-12-15
 */
@Disabled
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class X100BasicsOfConnectTest {
    /**
     * 注入
     */
    @BeforeEach
    public void injection()
            throws
            Throwable {
//        String sql = "SELECT \n"
//                + "\t[a].[name], \n"
//                + "\t[a].[ip], \n"
//                + "\t[a].[port], \n"
//                + "\t[a].[id], \n"
//                + "\t[a].[equipment_type], \n"
//                + "\t[a].[equipment_key], \n"
//                + "\t[a].[time], \n"
//                + "\t[a].[type], \n"
//                + "\t[a].[alarm_status], \n"
//                + "\t[a].[alarm_time], \n"
//                + "\t[a].[equipment_name], \n"
//                + "\t[a].[equipment_ip], \n"
//                + "\t[a].[equipment_port] \n"
//                + "FROM \n"
//                + "\t ( SELECT t0.id, t0.type, t0.equipment_key, t0.equipment_type, t0.time, t0.alarm_status, t0.alarm_time, t0.equipment_name , t0.equipment_port , t0.equipment_ip ,  t0.equipment_name AS [name], t0.equipment_port AS [port], t0.equipment_ip AS [ip] FROM equipment_state_record t0  ) AS [a]  \n"
//                + "WHERE (([a].[ip] != #{ip,javaType=String,jdbcType=NVARCHAR}  AND [a].[ip] IS NOT NULL ) ) \n"
//                + " \n"
//                + "ORDER BY  [a].[time] DESC";
//
//        DataSourceConfig config = new DataSourceConfig();
//        config.setDbType(DbType.JdbcSqlServer);
//        String newSql = new SqlServerSqlProvider(config).originalSql2LimitSql(sql,
//                                                                              10,
//                                                                              10);
//
//        System.out.println(newSql);

//        Select select = (Select) CCJSqlParserUtil.parseStatements(sql)
//                                                 .getStatements()
//                                                 .get(0);
//        PlainSelect plainSelect = (PlainSelect) select.getSelectBody();
//        Offset offset = new Offset();
//        offset.setOffset(new LongValue(10));
//        offset.setOffsetParam("ROWS");
//        Fetch fetch = new Fetch();
//        fetch.setRowCount(10);
//        fetch.setFetchParam("ROWS");
//        plainSelect.setOffset(offset);
//        plainSelect.setFetch(fetch);
//
//        String orderBy = plainSelect.getOrderByElements()
//                                    .toString();
//
//        FromItem fromItem = plainSelect.getFromItem();
//
//        while (fromItem != null) {
//            Class<?> type = fromItem.getClass();
//            System.out.println(type.getName());
//
//            PlainSelect subSelect;
//            if (type.equals(SubSelect.class))
//                subSelect = (PlainSelect) ((SubSelect) fromItem).getSelectBody();
//            else if (type.equals(LateralSubSelect.class))
//                subSelect = (PlainSelect) ((LateralSubSelect) fromItem).getSubSelect()
//                                                                       .getSelectBody();
//            else {
//                fromItem = null;
//                continue;
//            }
//
//            subSelect.setOrderByElements(null);
//
//            System.out.println(subSelect);
//
//            fromItem = subSelect.getFromItem();
//        }
//
//        System.out.println(orderBy);
//
//        System.out.println(plainSelect);

        OrmObjectResolve.injection();
    }

    /**
     * 测试数据库连接是否正常
     *
     * @param name 名称
     */
    @ParameterizedTest
    @MethodSource("project.extension.mybatis.edge.configure.TestDataSourceConfigure#getMultiTestDataSourceName")
    @DisplayName("100.测试数据库连接是否正常")
    @Order(100)
    public void _100(String name) {
        String dataSource = TestDataSourceConfigure.getTestDataSource(name);

        IDbFirst dbFirst = OrmObjectResolve.getDbFirst(dataSource);

        List<String> databases = dbFirst.getDatabases();
        for (String database : databases) {
            System.out.printf("\r\n查询到数据库：%s%n\r\n",
                              database);
        }
    }
}