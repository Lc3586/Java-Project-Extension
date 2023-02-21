package project.extension.mybatis.edge.core.provider.oracle;

import org.springframework.util.StringUtils;
import project.extension.mybatis.edge.config.DataSourceConfig;
import project.extension.mybatis.edge.core.provider.normal.SqlProvider;

import java.util.Date;
import java.util.UUID;

/**
 * Oracle数据库Sql语句构建器
 *
 * @author LCTR
 * @date 2023-02-21
 */
public class OracleSqlProvider
        extends SqlProvider {
    public OracleSqlProvider(DataSourceConfig config) {
        super(config,
              new char[]{'"',
                         '"'});
    }

    @Override
    public String tableName2Sql(String schema,
                                String tableName,
                                String alias) {
        return String.format(" %s%s%s ",
                             //`dbo`.
                             StringUtils.hasText(schema)
                             ? String.format("%s.",
                                             getValueWithCharacter(schema))
                             : "",
                             //`table1`
                             getValueWithCharacter(tableName),
                             // as `a`
                             StringUtils.hasText(alias)
                             ? String.format(" %s",
                                             getValueWithCharacter(alias))
                             : "");
    }

    @Override
    public String originalSql2AnySql(String originalSql) {
        return String.format("SELECT CASE WHEN COUNT(1) > 0 THEN 1 ELSE 0 END \n\nFROM (%s) %s ",
                             originalSql,
                             getValueWithCharacter(String.format("t_%s",
                                                                 new Date().getTime())));
    }

    @Override
    public String originalSql2LimitSql(String originalSql,
                                       int offset,
                                       int count) {
        String alias = getValueWithCharacter(String.format("T1_%s",
                                                           UUID.randomUUID()
                                                               .getMostSignificantBits()));
        return String.format("SELECT\r\n"
                                     + "\t* \r\n"
                                     + "FROM\r\n"
                                     + "\t(\r\n"
                                     + "\tSELECT\r\n"
                                     + "\t\t%s.*,\r\n"
                                     + "\t\tROWNUM RN \r\n"
                                     + "\tFROM\r\n"
                                     + "\t\t(\r\n"
                                     + "\t\t\t%s\r\n"
                                     + "\t\t) %s \r\n"
                                     + "\tWHERE\r\n"
                                     + "\t\tROWNUM <= %s \r\n"
                                     + "\t) \r\n"
                                     + "WHERE\r\n"
                                     + "\tRN > %s",
                             alias,
                             originalSql,
                             alias,
                             offset + count,
                             offset);
    }

    @Override
    public String originalSql2CountSql(String originalSql) {
        return String.format("SELECT COUNT(1) \r\nFROM (%s) %s ",
                             originalSql,
                             getValueWithCharacter(String.format("t_%s",
                                                                 new Date().getTime())));
    }
}
