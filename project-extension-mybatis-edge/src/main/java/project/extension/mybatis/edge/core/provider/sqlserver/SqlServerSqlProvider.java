package project.extension.mybatis.edge.core.provider.sqlserver;

import org.springframework.util.StringUtils;
import project.extension.mybatis.edge.config.DataSourceConfig;
import project.extension.mybatis.edge.core.provider.normal.SqlProvider;
import project.extension.mybatis.edge.globalization.Strings;
import project.extension.mybatis.edge.model.DbType;
import project.extension.standard.exception.ModuleException;

import java.util.Arrays;
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
     * <p>1、如果是2012及以上版本，则ORDER BY子句必须包含一个唯一列或保证列是唯一的，详见微软文档，链接如下。</p>
     * <p>   https://learn.microsoft.com/en-us/sql/t-sql/queries/select-order-by-clause-transact-sql?view=sql-server-ver15</p>
     * <p>2、如果是2012以下版本，则所有的子查询中都不能使用order by子句，否则会有如下的语法错误。</p>
     * <p>   "除非同时指定了TOP、OFFSET或FOR XML，否则ORDER BY子句在检视表、内嵌函数、衍生数据表、子查询及通用数据表表达式中均为无效。"</p>
     *
     * @param originalSql 原始Sql语句
     * @param offset      偏移量
     * @param count       数据量
     */
    @Override
    public String originalSql2LimitSql(String originalSql,
                                       int offset,
                                       int count)
            throws
            ModuleException {
        if (above2012Version) {
            //TODO 在Order By子句中自动追加主键列或唯一列（不存在则不追加处理）
            return String.format("%s \r\nOFFSET %s ROWS FETCH NEXT %s ROWS ONLY",
                                 originalSql,
                                 offset,
                                 count);
        } else {
            char[] chars = originalSql.toCharArray();
            int begin = -1;
            //获取sql语句的排序部分
            String orderBySql = null;
            StringBuilder orderBySqlSB = new StringBuilder();
            main:
            for (int i = 0; i < chars.length; i++) {
                if (begin != -1) {
                    if (chars[i] == 'A'
                            && chars[i + 1] == 'S'
                            && chars[i + 2] == 'C') {
                        if (orderBySql == null)
                            orderBySqlSB.append("ASC");
                        i += 2;
                    } else if (chars[i] == 'D'
                            && chars[i + 1] == 'E'
                            && chars[i + 2] == 'S'
                            && chars[i + 3] == 'C') {
                        if (orderBySql == null)
                            orderBySqlSB.append("DESC");
                        i += 3;
                    } else {
                        //获取最外层的Order BY子句
                        if (orderBySql == null)
                            orderBySqlSB.append(chars[i]);
                        continue;
                    }

                    for (int j = i + 1; j < chars.length; j++) {
                        if (chars[j] == ',')
                            continue main;
                        else if (chars[j] != ' '
                                && chars[j] != '\r'
                                && chars[j] != '\n'
                                & chars[j] != '\t')
                            break;
                    }

                    //清理查询中所有的Order By子句（不清理则会有语法错误：除非同时指定了TOP、OFFSET或FOR XML，否则ORDER BY子句在检视表、内嵌函数、衍生数据表、子查询及通用数据表表达式中均为无效。）
                    Arrays.fill(chars,
                                begin,
                                i + 1,
                                ' ');
                    begin = -1;

                    if (orderBySql == null)
                        orderBySql = orderBySqlSB.toString();
                } else if (i + 8 < chars.length) {
                    if (chars[i] == 'O'
                            && chars[i + 1] == 'R'
                            && chars[i + 2] == 'D'
                            && chars[i + 3] == 'E'
                            && chars[i + 4] == 'R'
                            && chars[i + 5] == ' '
                            && chars[i + 6] == 'B'
                            && chars[i + 7] == 'Y') {
                        if (orderBySql == null)
                            orderBySqlSB.append("ORDER BY");
                        begin = i;
                        i += 7;
                    }
                }
            }

            originalSql = new String(chars);

            if (!StringUtils.hasText(orderBySql))
                throw new ModuleException(Strings.getSqlServerRequireOrderBy4Paging());

            String alias1 = getValueWithCharacter(String.format("T1_%s",
                                                                UUID.randomUUID()
                                                                    .getMostSignificantBits()));
            String alias2 = getValueWithCharacter(String.format("T2_%s",
                                                                UUID.randomUUID()
                                                                    .getMostSignificantBits()));

            //获取OrderBy子句中的别名
            Matcher orderByForAliasMatcher = Pattern.compile("\\[(.*?)]\\.",
                                                             Pattern.CASE_INSENSITIVE)
                                                    .matcher(orderBySql);
            if (orderByForAliasMatcher.find())
                //重新设置别名
                orderBySql = orderBySql.replace(String.format("[%s].",
                                                              orderByForAliasMatcher.group(1)),
                                                String.format("%s.",
                                                              alias2));

            //TODO 如果order by子句的列未包含在select子句中，则此处自动添加，目前先通过查询全部列解决此问题
            //获取最外层的select子句的列部分
            begin = -1;
            String selectForSql = null;
            StringBuilder selectForSqlSB = new StringBuilder();
            for (int i = 0; i < chars.length; i++) {
                if (begin != -1) {
                    if (chars[i] == 'F'
                            && chars[i + 1] == 'R'
                            && chars[i + 2] == 'O'
                            && chars[i + 3] == 'M') {
                        selectForSqlSB.append(",");
                        selectForSql = selectForSqlSB.toString();

                        //替换为查询全部列
                        String newSelectForSql;
                        Matcher selectForAliasMatcher = Pattern.compile("\\[(.*?)]\\.",
                                                                        Pattern.CASE_INSENSITIVE)
                                                               .matcher(selectForSql);

                        //获取Select子句中的别名
                        if (selectForAliasMatcher.find())
                            newSelectForSql = String.format("[%s].*",
                                                            selectForAliasMatcher.group(1));
                        else
                            newSelectForSql = "*";

                        //重新设置别名
                        selectForSql = selectForSql.replace(selectForAliasMatcher.group(0),
                                                            String.format("%s.",
                                                                          alias2));

                        originalSql = String.format("%s %s %s",
                                                    originalSql.substring(0,
                                                                          begin + 6),
                                                    newSelectForSql,
                                                    originalSql.substring(i));
                        break;
                    } else
                        selectForSqlSB.append(chars[i]);
                } else {
                    if (i + 8 < chars.length) {
                        if (chars[i] == 'S'
                                && chars[i + 1] == 'E'
                                && chars[i + 2] == 'L'
                                && chars[i + 3] == 'E'
                                && chars[i + 4] == 'C'
                                && chars[i + 5] == 'T') {
                            begin = i;
                            i += 5;
                        }
                    }
                }
            }

            if (!StringUtils.hasText(selectForSql))
                throw new ModuleException(Strings.getSqlServerRequireSelect());

            return String.format("SELECT \r\n\t%s.* \r\nFROM (\r\n\tSELECT \r\n\t\t%s \r\n\t\tROW_NUMBER() OVER (\r\n\t\t\t%s\r\n\t\t) AS ROWID \r\n\tFROM (\r\n\t\t%s\r\n\t) AS %s) AS %s \r\nWHERE ROWID BETWEEN %s AND %s",
                                 alias1,
                                 selectForSql,
                                 orderBySql,
                                 originalSql,
                                 alias2,
                                 alias1,
                                 offset + 1,
                                 offset + count);
        }
    }
}
