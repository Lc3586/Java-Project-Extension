package project.extension.mybatis.edge.core.provider.sqlserver;

import org.springframework.util.StringUtils;
import project.extension.mybatis.edge.config.DataSourceConfig;
import project.extension.mybatis.edge.core.provider.normal.SqlProvider;
import project.extension.mybatis.edge.globalization.Strings;
import project.extension.mybatis.edge.model.DbType;
import project.extension.standard.exception.ModuleException;

import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * SqlServer数据库Sql语句构建器
 *
 * @author LCTR
 * @date 2022-07-15
 */
public class SqlServerSqlProvider
        extends SqlProvider {
    public SqlServerSqlProvider(DataSourceConfig config) {
        super(config,
              new char[]{'[',
                         ']'});
        this.above2012Version = config.getDbType()
                                      .equals(DbType.JdbcSqlServer_2012_plus);
    }

    /**
     * 2012及以上版本
     */
    private final boolean above2012Version;

    /**
     * 原始Sql语句转为获取首条记录的Sql语句
     *
     * @param originalSql 原始Sql语句
     * @param offset      偏移量
     * @param count       数据量
     * @return Sql语句
     */
    @Override
    public String originalSql2LimitSql(String originalSql,
                                       int offset,
                                       int count)
            throws
            ModuleException {
        if (above2012Version)
            return String.format("%s \r\nOFFSET %s ROWS FETCH NEXT %s ROWS ONLY",
                                 originalSql,
                                 offset,
                                 count);
        else {
            //获取sql语句的排序部分
            String orderBySql = null;
            Matcher matcher = Pattern.compile("ORDER BY(.*?)(ASC|DESC)",
                                              Pattern.CASE_INSENSITIVE)
                                     .matcher(originalSql);

            //获取最外层的OrderBY语句
            while (matcher.find()) {
                orderBySql = matcher.group(0);
            }

            //清理查询中所有的OrderBy语句（不清理则会有语法错误：除非同时指定了TOP、OFFSET或FOR XML，否则ORDER BY子句在检视表、内嵌函数、衍生数据表、子查询及通用数据表表达式中均为无效。）
            originalSql = matcher.replaceAll(" ");

            if (!StringUtils.hasText(orderBySql))
                throw new ModuleException(Strings.getSqlServerRequireOrderBy4Paging());

            String alias1 = getValueWithCharacter(String.format("t1_%s",
                                                                UUID.randomUUID()));
            String alias2 = getValueWithCharacter(String.format("t2_%s",
                                                                UUID.randomUUID()));

            //重新设置别名
            orderBySql = orderBySql.replaceAll("\\[(.*?)]\\.",
                                               String.format("%s.",
                                                             alias2));

            return String.format("SELECT %s.* \r\nFROM (\r\n\tSELECT %s.*, \r\n\tROW_NUMBER() OVER(\r\n\t\t%s\r\n\t) AS ROWID \r\n\tFROM (\r\n\t\t%s\r\n\t) AS %s) AS %s \r\nWHERE ROWID BETWEEN %s AND %s",
                                 alias1,
                                 alias2,
                                 orderBySql,
                                 originalSql,
                                 alias2,
                                 alias1,
                                 offset,
                                 offset + count);
        }
    }
}
