package project.extension.mybatis.edge.core.provider.dameng;

import com.alibaba.druid.pool.DruidDataSourceFactory;
import org.springframework.util.StringUtils;
import project.extension.collections.CollectionsExtension;
import project.extension.cryptography.MD5Utils;
import project.extension.func.IFunc1;
import project.extension.mybatis.edge.config.DataSourceConfig;
import project.extension.mybatis.edge.core.ado.INaiveAdo;
import project.extension.mybatis.edge.core.provider.normal.DbFirst;
import project.extension.mybatis.edge.globalization.Strings;
import project.extension.mybatis.edge.model.*;
import project.extension.number.NumericExtension;
import project.extension.standard.exception.ModuleException;
import project.extension.tuple.Tuple2;

import java.math.BigDecimal;
import java.sql.JDBCType;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Dameng DbFirst 开发模式相关功能
 *
 * @author LCTR
 * @date 2022-06-10
 */
public class DamengDbFirst
        extends DbFirst {
    public DamengDbFirst(DataSourceConfig config,
                         INaiveAdo ado) {
        super(config,
              ado,
              "DamengDbFirst");
        initialization();
    }

    /**
     * 初始化
     */
    private void initialization() {
        //是否已初始化
        if (dbToJavaMap.size() > 0)
            return;

        dbToJavaMap.putIfAbsent("number(1)",
                                new DbTypeToJavaType("(boolean)",
                                                     "(Boolean)",
                                                     "Boolean.parseBoolean(%s)",
                                                     "Boolean.toString(%s)",
                                                     "boolean",
                                                     "Boolean",
                                                     boolean.class,
                                                     Boolean.class));

        dbToJavaMap.putIfAbsent("number(3)",
                                new DbTypeToJavaType("(byte)",
                                                     "(Byte)",
                                                     "Byte.parseByte(%s)",
                                                     "Byte.toString(%s)",
                                                     "byte",
                                                     "Byte",
                                                     byte.class,
                                                     Byte.class));

        dbToJavaMap.putIfAbsent("number(5)",
                                new DbTypeToJavaType("(short)",
                                                     "(Short)",
                                                     "Short.parseShort(%s)",
                                                     "Short.toString(%s)",
                                                     "short",
                                                     "Short",
                                                     short.class,
                                                     Short.class));

        dbToJavaMap.putIfAbsent("number(10)",
                                new DbTypeToJavaType("(int)",
                                                     "(Integer)",
                                                     "Integer.parseInt(%s)",
                                                     "Integer.toString(%s)",
                                                     "int",
                                                     "Integer",
                                                     int.class,
                                                     Integer.class));

        dbToJavaMap.putIfAbsent("number(19)",
                                new DbTypeToJavaType("(long)",
                                                     "(Long)",
                                                     "Long.parseLong(%s)",
                                                     "Long.toString(%s)",
                                                     "long",
                                                     "Long",
                                                     long.class,
                                                     Long.class));

        dbToJavaMap.putIfAbsent("float(63)",
                                new DbTypeToJavaType("(float)",
                                                     "(Float)",
                                                     "Float.parseFloat(%s)",
                                                     "Float.toString(%s)",
                                                     "float",
                                                     "Float",
                                                     float.class,
                                                     Float.class));
        dbToJavaMap.putIfAbsent("float(126)",
                                new DbTypeToJavaType("(double)",
                                                     "(Double)",
                                                     "Double.parseDouble(%s)",
                                                     "Double.toString(%s)",
                                                     "double",
                                                     "Double",
                                                     double.class,
                                                     Double.class));
        dbToJavaMap.putIfAbsent("number(10,2)",
                                new DbTypeToJavaType("(BigDecimal)",
                                                     "(BigDecimal)",
                                                     "new BigDecimal(%s)",
                                                     "%s.toString()",
                                                     "BigDecimal",
                                                     "BigDecimal",
                                                     BigDecimal.class,
                                                     BigDecimal.class));

        dbToJavaMap.putIfAbsent("date",
                                new DbTypeToJavaType("(java.sql.Date)",
                                                     "(java.sql.Date)",
                                                     "new java.sql.Date(Long.parseLong(%s))",
                                                     "Long.toString(%s.getTime())",
                                                     "java.sql.Date",
                                                     "java.sql.Date",
                                                     java.sql.Date.class,
                                                     java.sql.Date.class));
        dbToJavaMap.putIfAbsent("interval day(2) to second(6)",
                                new DbTypeToJavaType("(java.sql.Time)",
                                                     "(java.sql.Time)",
                                                     "new java.sql.Time(Long.parseLong(%s))",
                                                     "Long.toString(%s.getTime())",
                                                     "java.sql.Time",
                                                     "java.sql.Time",
                                                     java.sql.Time.class,
                                                     java.sql.Time.class));
        dbToJavaMap.putIfAbsent("timestamp(6)",
                                new DbTypeToJavaType("(Date)",
                                                     "(Date)",
                                                     "new Date(Long.parseLong(%s))",
                                                     "Long.toString(%s.getTime())",
                                                     "Date",
                                                     "Date",
                                                     Date.class,
                                                     Date.class));

        dbToJavaMap.putIfAbsent("blob",
                                new DbTypeToJavaType("(byte[])",
                                                     "(Byte[])",
                                                     "Base64.getDecoder().decode(%s.getBytes(StandardCharsets.UTF_8))",
                                                     "new String(Base64.getEncoder().encode(%s), StandardCharsets.UTF_8)",
                                                     "byte[]",
                                                     "Byte[]",
                                                     byte[].class,
                                                     Byte[].class));

        dbToJavaMap.putIfAbsent("nvarchar2(255)",
                                new DbTypeToJavaType("(String)",
                                                     "(String)",
                                                     "%s",
                                                     "%s",
                                                     "String",
                                                     "String",
                                                     String.class,
                                                     String.class));
        dbToJavaMap.putIfAbsent("char(36 char)",
                                new DbTypeToJavaType("(UUID)",
                                                     "(UUID)",
                                                     "UUID.fromString(%s)",
                                                     "%s.toString()",
                                                     "UUID",
                                                     "UUID",
                                                     UUID.class,
                                                     UUID.class));

        dbToJavaMap.putIfAbsent("unknown",
                                new DbTypeToJavaType("(Object)",
                                                     "(Object)",
                                                     "(Object)%s",
                                                     "%s.toString()",
                                                     "Object",
                                                     "Object",
                                                     Object.class,
                                                     Object.class));
    }

    /**
     * 检查并设置映射表
     *
     * @param column 列信息
     */
    private void checkAndSetupJavaMap(DbColumnInfo column) {
        String dbTypeTextFull = column.getDbTypeTextFull();

        if (dbToJavaMap.containsKey(dbTypeTextFull))
            return;

        String dbTypeText = column.getDbTypeText() == null
                            ? ""
                            : column.getDbTypeText()
                                    .toLowerCase(Locale.ROOT);
        switch (dbTypeText) {
            case "bit":
                copyAndPutDbToJavaMap(dbTypeTextFull,
                                      "number(1)");
                break;
            case "byte":
            case "tinyint":
                copyAndPutDbToJavaMap(dbTypeTextFull,
                                      "number(3)");
                break;
            case "smallint":
                copyAndPutDbToJavaMap(dbTypeTextFull,
                                      "number(5)");
                break;
            case "int":
            case "integer":
                copyAndPutDbToJavaMap(dbTypeTextFull,
                                      "number(10)");
                break;
            case "bigint":
                copyAndPutDbToJavaMap(dbTypeTextFull,
                                      "number(19)");
                break;
            case "real":
            case "binary_float":
                copyAndPutDbToJavaMap(dbTypeTextFull,
                                      "float(63)");
                break;
            case "double":
            case "float":
            case "double precision":
            case "binary_double":
                copyAndPutDbToJavaMap(dbTypeTextFull,
                                      "float(126)");
                break;
            case "numeric":
            case "number":
                if (column.getScale() == 0) {
                    if (column.getPrecision() >= 19)
                        copyAndPutDbToJavaMap(dbTypeTextFull,
                                              "number(19)");
                    else if (column.getPrecision() >= 10)
                        copyAndPutDbToJavaMap(dbTypeTextFull,
                                              "number(10)");
                    else if (column.getPrecision() >= 5)
                        copyAndPutDbToJavaMap(dbTypeTextFull,
                                              "number(5)");
                    else if (column.getPrecision() >= 3)
                        copyAndPutDbToJavaMap(dbTypeTextFull,
                                              "number(3)");
                    else
                        copyAndPutDbToJavaMap(dbTypeTextFull,
                                              "number(1)");
                    break;
                }
            case "dec":
            case "decimal":
                copyAndPutDbToJavaMap(dbTypeTextFull,
                                      "number(10,2)");
                break;
            case "time":
            case "interval day to second":
            case "interval year to month":
            case "interval year":
            case "interval month":
            case "interval day":
            case "interval day to hour":
            case "interval day to minute":
            case "interval hour":
            case "interval hour to minute":
            case "interval hour to second":
            case "interval minute":
            case "interval minute to second":
            case "interval second":
            case "time with time zone":
                copyAndPutDbToJavaMap(dbTypeTextFull,
                                      "interval day(2) to second(6)");
                break;
            case "date":
                copyAndPutDbToJavaMap(dbTypeTextFull,
                                      "date");
                break;
            case "datetime":
            case "timestamp":
            case "timestamp with local time zone":
            case "timestamp with time zone":
                copyAndPutDbToJavaMap(dbTypeTextFull,
                                      "timestamp(6)");
                break;
            case "binary":
            case "varbinary":
            case "blob":
            case "image":
            case "longvarbinary":
            case "bfile":
            case "raw":
            case "long raw":
                copyAndPutDbToJavaMap(dbTypeTextFull,
                                      "blob");
                break;
            case "character":
            case "char":
            case "nchar":
            case "nvarchar":
            case "nvarchar2":
            case "nclob":
            case "clob":
            case "varchar":
            case "varchar2":
            case "text":
            case "longvarchar":
            case "rowid":
                copyAndPutDbToJavaMap(dbTypeTextFull,
                                      "nvarchar2(255)");
            default:
                copyAndPutDbToJavaMap(dbTypeTextFull,
                                      "unknown");
                break;
        }
    }

    /**
     * 查询表信息
     *
     * @param inDatabase 用来筛选数据库的sql语句
     * @param tbname     表名
     * @param ignoreCase 忽略大小写
     * @return 表信息
     */
    private List<Map<String, Object>> selectTableInfo(String inDatabase,
                                                      String[] tbname,
                                                      boolean ignoreCase)
            throws
            ModuleException {
        String sql = String.format("select \r\n"
                                           + "a.owner || '.' || a.table_name as \"F1\",\r\n"
                                           + "a.owner as \"F2\",\r\n"
                                           + "a.table_name as \"F3\",\r\n"
                                           + "b.comments as \"F4\",\r\n"
                                           + "'TABLE' as \"F5\" \r\n"
                                           + "from all_tables a \r\n"
                                           + "left join all_tab_comments b on b.owner = a.owner and b.table_name = a.table_name and b.table_type = 'TABLE' \r\n"
                                           + "where %s in (%s) %s \r\n"
                                           + "\r\n"
                                           + "UNION ALL \r\n"
                                           + "\r\n"
                                           + "select \r\n"
                                           + "a.owner || '.' || a.view_name,\r\n"
                                           + "a.owner,\r\n"
                                           + "a.view_name,\r\n"
                                           + "b.comments,\r\n"
                                           + "'VIEW' \r\n"
                                           + "from all_views a \r\n"
                                           + "left join all_tab_comments b on b.owner = a.owner and b.table_name = a.view_name and b.table_type = 'VIEW' \r\n"
                                           + "where %s in (%s) %s ",
                                   ignoreCase
                                   ? "lower(a.owner) "
                                   : "a.owner ",
                                   inDatabase,
                                   tbname == null
                                   ? ""
                                   : String.format(" and %s%s",
                                                   ignoreCase
                                                   ? "lower(a.table_name)"
                                                   : "a.table_name",
                                                   StringUtils.hasText(tbname[1])
                                                   ? String.format("='%s'",
                                                                   tbname[1])
                                                   : " is null"),
                                   ignoreCase
                                   ? "lower(a.owner) "
                                   : "a.owner ",
                                   inDatabase,
                                   tbname == null
                                   ? ""
                                   : String.format(" and %s%s",
                                                   ignoreCase
                                                   ? "lower(a.view_name)"
                                                   : "a.view_name",
                                                   StringUtils.hasText(tbname[1])
                                                   ? String.format("='%s'",
                                                                   tbname[1])
                                                   : " is null"));

        return this.ado.selectMapList(getSqlSession(),
                                      getMSId(MD5Utils.hash(sql)),
                                      sql,
                                      null,
                                      null,
                                      null,
                                      null,
                                      config.getNameConvertType());
    }

    /**
     * 查询列信息
     *
     * @param inDatabase    用来筛选数据库的sql语句
     * @param tablesMatcher 用来匹配表名的sql语句
     * @param ignoreCase    忽略大小写
     * @return 列信息
     */
    private List<Map<String, Object>> selectColumnInfo(String inDatabase,
                                                       String tablesMatcher,
                                                       boolean ignoreCase)
            throws
            ModuleException {
        String sql = String.format("select \r\n"
                                           + "a.owner || '.' || a.table_name as \"F1\",\r\n"
                                           + "a.column_name as \"F2\",\r\n"
                                           + "a.data_type as \"F3\",\r\n"
                                           + "a.data_length as \"F4\",\r\n"
                                           + "a.data_precision as \"F5\",\r\n"
                                           + "a.data_scale as \"F6\",\r\n"
                                           + "a.char_used as \"F7\",\r\n"
                                           + "case when a.nullable = 'N' then 0 else 1 end as \"F8\",\r\n"
                                           + "nvl((select 1 from user_sequences where upper(sequence_name)=upper(a.table_name||'_seq_'||a.column_name) and rownum < 2), 0) as \"F9\",\r\n"
                                           + "b.comments as \"F10\",\r\n"
                                           + "a.data_default as \"F11\" \r\n"
                                           + "from all_tab_cols a \r\n"
                                           + "left join all_col_comments b on b.owner = a.owner and b.table_name = a.table_name and b.column_name = a.column_name \r\n"
                                           + "where %s in (%s) \r\n"
                                           + "and %s ",
                                   ignoreCase
                                   ? "lower(a.owner) "
                                   : "a.owner ",
                                   inDatabase,
                                   tablesMatcher);

        return this.ado.selectMapList(getSqlSession(),
                                      getMSId(MD5Utils.hash(sql)),
                                      sql,
                                      null,
                                      null,
                                      null,
                                      null,
                                      config.getNameConvertType());
    }

    /**
     * 查询索引/唯一键信息
     *
     * @param inDatabase    用来筛选数据库的sql语句
     * @param tablesMatcher 用来匹配表名的sql语句
     * @param ignoreCase    忽略大小写
     * @return 索引/唯一键信息
     */
    private List<Map<String, Object>> selectIndexInfo(String inDatabase,
                                                      String tablesMatcher,
                                                      boolean ignoreCase)
            throws
            ModuleException {
        String sql = String.format("select \r\n"
                                           + "a.table_owner || '.' || a.table_name as \"F1\",\r\n"
                                           + "c.column_name as \"F2\",\r\n"
                                           + "c.index_name as \"F3\",\r\n"
                                           + "case when a.uniqueness = 'UNIQUE' then 1 else 0 end as \"F4\",\r\n"
                                           + "case when exists(select 1 from all_constraints where index_name = a.index_name and constraint_type = 'P') then 1 else 0 end as \"F5\",\r\n"
                                           + "0 as \"F6\",\r\n"
                                           + "case when c.descend = 'DESC' then 1 else 0 end as \"F7\",\r\n"
                                           + "c.column_position as \"F8\" \r\n"
                                           + "from all_indexes a,\r\n"
                                           + "all_ind_columns c \r\n"
                                           + "where a.index_name = c.index_name \r\n"
                                           + "and a.table_owner = c.table_owner \r\n"
                                           + "and a.table_name = c.table_name \r\n"
                                           + "and %s in (%s) \r\n"
                                           + "and %s ",
                                   ignoreCase
                                   ? "lower(a.table_owner)"
                                   : "a.table_owner",
                                   inDatabase,
                                   tablesMatcher);

        return this.ado.selectMapList(getSqlSession(),
                                      getMSId(MD5Utils.hash(sql)),
                                      sql,
                                      null,
                                      null,
                                      null,
                                      null,
                                      config.getNameConvertType());
    }

    /**
     * 查询外键信息
     *
     * @param inDatabase    用来筛选数据库的sql语句
     * @param tablesMatcher 用来匹配表名的sql语句
     * @param ignoreCase    忽略大小写
     * @return 外键信息
     */
    private List<Map<String, Object>> selectForeignInfo(String inDatabase,
                                                        String tablesMatcher,
                                                        boolean ignoreCase)
            throws
            ModuleException {
        String sql = String.format("select \r\n"
                                           + "a.owner || '.' || a.table_name as \"F1\",\r\n"
                                           + "c.column_name as \"F2\",\r\n"
                                           + "c.constraint_name as \"F3\",\r\n"
                                           + "b.owner || '.' || b.table_name as \"F4\",\r\n"
                                           + "1 as \"F5\",\r\n"
                                           + "d.column_name as \"F6\" \r\n"
                                           + "\r\n"
                                           + "-- a.owner 外键拥有者,\r\n"
                                           + "-- a.table_name 外键表,\r\n"
                                           + "-- c.column_name 外键列,\r\n"
                                           + "-- b.owner 主键拥有者,\r\n"
                                           + "-- b.table_name 主键表,\r\n"
                                           + "-- d.column_name 主键列,\r\n"
                                           + "-- c.constraint_name 外键名,\r\n"
                                           + "-- d.constraint_name 主键名\r\n"
                                           + "\r\n"
                                           + "from \r\n"
                                           + "all_constraints a,\r\n"
                                           + "all_constraints b,\r\n"
                                           + "all_cons_columns c, --外键表\r\n"
                                           + "all_cons_columns d  --主键表\r\n"
                                           + "where \r\n"
                                           + "a.r_constraint_name = b.constraint_name \r\n"
                                           + "and a.constraint_type = 'R' \r\n"
                                           + "and b.constraint_type = 'P' \r\n"
                                           + "and a.r_owner = b.owner \r\n"
                                           + "and a.constraint_name = c.constraint_name \r\n"
                                           + "and b.constraint_name = d.constraint_name \r\n"
                                           + "and a.owner = c.owner \r\n"
                                           + "and a.table_name = c.table_name \r\n"
                                           + "and b.owner = d.owner \r\n"
                                           + "and b.table_name = d.table_name \r\n"
                                           + "and %s in (%s) and %s ",
                                   ignoreCase
                                   ? "lower(a.owner)"
                                   : "a.owner",
                                   inDatabase,
                                   tablesMatcher);

        return this.ado.selectMapList(getSqlSession(),
                                      getMSId(MD5Utils.hash(sql)),
                                      sql,
                                      null,
                                      null,
                                      null,
                                      null,
                                      config.getNameConvertType());
    }

    /**
     * 从数据库查询用户标识
     *
     * @return 用户标识
     */
    private String getUserIdFromDatabase()
            throws
            ModuleException {
        //从数据库中查询
        String sql = " select username from user_users";
        return this.ado.selectOne(getSqlSession(),
                                  getMSId(MD5Utils.hash(sql)),
                                  sql,
                                  null,
                                  null,
                                  String.class,
                                  null,
                                  null,
                                  config.getNameConvertType());
    }

    /**
     * 从连接字符串中获取用户标识
     *
     * @param lower 转小写
     * @return 用户标识
     */
    private String getUserIdFromConnectionString(boolean lower)
            throws
            ModuleException {
        Matcher matcher = Pattern.compile("jdbc:dm://(.*?)/(.*?)\\?",
                                          Pattern.CASE_INSENSITIVE)
                                 .matcher(config.getProperties()
                                                .getProperty(DruidDataSourceFactory.PROP_URL));
        if (!matcher.find())
            throw new ModuleException(Strings.getCanNotGetDbNameFromUrl("jdbc:dm://(.*?)/(.*?)\\?"));

        if (lower)
            return matcher.group(2)
                          .toLowerCase(Locale.ROOT);
        else
            return matcher.group(2);
    }

    /**
     * 获取数据库数据类型的建表语句完整类型名称
     *
     * @param type      类型
     * @param length    长度
     * @param precision 精度
     * @param scale     标度
     * @return 完整类型名称
     */
    private String getSqlTypeFullName(String type,
                                      int length,
                                      int precision,
                                      int scale) {
        String sqlType = type.toUpperCase(Locale.ROOT);

        if (sqlType.startsWith("INTERVAL DAY TO SECOND"))
            sqlType = String.format("INTERVAL DAY(%s) TO SECOND(%s)",
                                    (scale - 1536) / 16,
                                    (scale - 1536) % 16);
        else if (Pattern.compile("INTERVAL YEAR\\(\\d+\\) TO MONTH",
                                 Pattern.CASE_INSENSITIVE)
                        .matcher(sqlType)
                        .find()) {
            return sqlType;
        } else if (sqlType.startsWith("TIMESTAMP"))
            sqlType += scale <= 0
                       ? ""
                       : String.format("(%s)",
                                       scale);
        else if (sqlType.startsWith("BLOB")) {
            return sqlType;
        } else if (sqlType.startsWith("CLOB")) {
            return sqlType;
        } else if (sqlType.startsWith("NCLOB")) {
            return sqlType;
        } else if (sqlType.startsWith("TEXT")) {
            return sqlType;
        } else if (sqlType.equals("REAL") || sqlType.equals("DOUBLE") || sqlType.equals("FLOAT")) {
            return sqlType;
        } else if (precision > 0 && scale > 0)
            sqlType += String.format("(%s,%s)",
                                     precision,
                                     scale);
        else if (precision > 0)
            sqlType += String.format("(%s)",
                                     precision);
        else
            sqlType += String.format("(%s)",
                                     length);

        return sqlType;
    }

    @Override
    protected String[] splitTableName(String name,
                                      boolean lower) {
        String[] tbname = getSplitTableNames(name,
                                             '\"',
                                             '\"',
                                             2);

        if (tbname == null)
            return null;

        if (lower)
            for (int i = 0; i < tbname.length; i++)
                tbname[i] = tbname[i].toLowerCase(Locale.ROOT);

        return tbname;
    }

    /**
     * 获取数据库表结构信息
     *
     * @param database   数据库名
     * @param tablename  表明
     * @param ignoreCase 忽略大小写
     * @return 数据库表结构信息集合
     */
    private List<DbTableInfo> getTables(String[] database,
                                        String tablename,
                                        boolean ignoreCase)
            throws
            ModuleException {
        //所有的表结构信息
        List<DbTableInfo> tables = new ArrayList<>();

        //临时存储表名对应的表结构信息
        Map<String, DbTableInfo> tableWithIds_cache = new HashMap<>();
        //临时存储表名对应的列名和列信息
        Map<String, Map<String, DbColumnInfo>> tableWithColumns_cache = new HashMap<>();

        //尝试获取模式名+表名
        //模式名即为数据库名
        String[] tbname = null;
        if (StringUtils.hasText(tablename)) {
            tbname = splitTableName(tablename,
                                    ignoreCase);
            if (tbname == null)
                throw new ModuleException(Strings.getUnknownValue("tablename",
                                                                  tablename));
            if (tbname.length == 1) {
                String userId = getUserIdFromDatabase();
                if (!StringUtils.hasText(userId))
                    return tables;
                tbname = new String[]{userId,
                                      tbname[0]};
            }
            if (ignoreCase)
                tbname = new String[]{tbname[0].toLowerCase(Locale.ROOT),
                                      tbname[1].toLowerCase(Locale.ROOT)};
            database = new String[]{tbname[0]};
        } else if (database == null || database.length == 0) {
            String userId = getUserIdFromDatabase();
            if (!StringUtils.hasText(userId))
                return tables;
            database = new String[]{userId};
        }

        //用来在sql语句中筛选出当前操作涉及的数据库
        String inDatabase = Arrays.stream(database)
                                  .filter(StringUtils::hasText)
                                  .map(x -> String.format("'%s'",
                                                          x))
                                  .collect(Collectors.joining(","));

        //查询表结构信息
        List<Map<String, Object>> dataMap = selectTableInfo(inDatabase,
                                                            tbname,
                                                            ignoreCase);

        if (dataMap == null || dataMap.size() == 0)
            return tables;

        //所有的表和视图名
        List<String[]> t_v_names = new ArrayList<>();
//        //所有的存储过程名
//        List<String[]> sp_names = new ArrayList<>();
        //表和视图名，存储上限为1000的数据
        List<String> t_v_names_lower_1000 = new ArrayList<>();
//        //存储过程名，存储上限为1000的数据
//        List<String> sp_names_lower_1000 = new ArrayList<>();

        for (Map<String, Object> d : dataMap) {
            //表名
            String table_id = String.valueOf(d.get("F1"));
            //模式名
            String schema = String.valueOf(d.get("F2"));
            //表名
            String table = String.valueOf(d.get("F3"));
            //注释
            String comment = String.valueOf(d.get("F4"));
            //数据库表类型
            DbTableType type = DbTableType.valueOf(String.valueOf(d.get("F5")));

            //如果当前只有默认数据库
            if (database.length == 1) {
                table_id = table_id.substring(table_id.indexOf('.') + 1);
                schema = "";
            }

            //添加表结构信息
            tableWithIds_cache.put(table_id,
                                   new DbTableInfo(table_id,
                                                   schema,
                                                   table,
                                                   comment,
                                                   type));
            tableWithColumns_cache.put(table_id,
                                       new HashMap<>());

            //确保集合中的数组的长度不超过1000
            switch (type) {
                case TABLE:
                case VIEW:
                    t_v_names_lower_1000.add(table.replace("'",
                                                           "''"));
                    if (t_v_names_lower_1000.size() >= 999) {
                        t_v_names.add(t_v_names_lower_1000.toArray(new String[0]));
                        t_v_names_lower_1000.clear();
                    }
                    break;
                case StoreProcedure:
//                    sp_names_lower_1000.add(table.replace("'",
//                                                         "''"));
//                    if (sp_names_lower_1000.size() >= 999) {
//                        sp_names.add(sp_names_lower_1000.toArray(new String[0]));
//                        sp_names_lower_1000.clear();
//                    }
                    break;
            }
        }

        //处理最后的一点数组
        if (t_v_names_lower_1000.size() > 0) {
            t_v_names.add(t_v_names_lower_1000.toArray(new String[0]));
            t_v_names_lower_1000.clear();
        }
//        if (sp_names_lower_1000.size() > 0) {
//            sp_names.add(sp_names_lower_1000.toArray(new String[0]));
//            sp_names_lower_1000.clear();
//        }

        //没有表和视图时直接返回数据
        if (t_v_names.size() == 0)
            return tables;

        //用来在sql语句中筛选出当前操作涉及的表、视图、存储过程
        IFunc1<List<String[]>, String> getMatcherSql = nameList -> {
            StringBuilder tablesMatcherSB = new StringBuilder().append("(");
            for (int i = 0; i < t_v_names.size(); i++) {
                if (i > 0) tablesMatcherSB.append(" or ");
                tablesMatcherSB.append(ignoreCase
                                       ? "lower(a.table_name) in ("
                                       : "a.table_name in (");

                for (int j = 0; j < t_v_names.get(i).length; j++) {
                    if (j > 0) tablesMatcherSB.append(",");
                    tablesMatcherSB.append(String.format("'%s'",
                                                         t_v_names.get(i)[j].toLowerCase(Locale.ROOT)));
                }
                tablesMatcherSB.append(")");
            }
            tablesMatcherSB.append(")");
            return tablesMatcherSB.toString();
        };

        String tablesMatcher = getMatcherSql.invoke(t_v_names);

        //查询列信息
        dataMap = selectColumnInfo(inDatabase,
                                   tablesMatcher,
                                   ignoreCase);

        if (dataMap == null)
            return tables;

        //列的位置
        int position = 0;
        for (Map<String, Object> d : dataMap) {
            //表名
            String table_id = String.valueOf(d.get("F1"));
            //列名
            String column = String.valueOf(d.get("F2"));
            //数据类型
            String type = String.valueOf(d.get("F3"))
                                .replaceAll("\\(\\d+\\)",
                                            "");
            //数据长度
            Tuple2<Boolean, Integer> length_result = NumericExtension.tryParseInt(String.valueOf(d.get("F4")));
            int length = length_result.a
                         ? length_result.b
                         : 0;
            //数据精度
            Tuple2<Boolean, Integer> precision_result = NumericExtension.tryParseInt(String.valueOf(d.get("F5")));
            int precision = precision_result.a
                            ? precision_result.b
                            : length;
            //数据标度
            Tuple2<Boolean, Integer> scale_result = NumericExtension.tryParseInt(String.valueOf(d.get("F6")));
            int scale = scale_result.a
                        ? scale_result.b
                        : 0;
            //建表时的完整类型名称
            String sqlType = getSqlTypeFullName(String.valueOf(d.get("F3")),
                                                length,
                                                precision,
                                                scale);
            //数据的最大长度
            Matcher max_length_matcher = Pattern.compile("\\w+\\((\\d+)")
                                                .matcher(sqlType);
            int max_length = max_length_matcher.find()
                             ? Integer.parseInt(max_length_matcher.group(1))
                             : -1;
            //是否可为空
            boolean is_nullable = String.valueOf(d.get("F8"))
                                        .equals("1");
            //是否自增
            boolean is_identity = String.valueOf(d.get("F9"))
                                        .equals("1");
            //注释
            String comment = String.valueOf(d.get("F10"));
            //默认值
            String defaultValue = String.valueOf(d.get("F11"));

            //如果数据的最大长度为0，则判定为可变长度的数据类型（TEXT, CLOB......）
            if (max_length == 0)
                max_length = -1;

            //如果当前只有默认数据库
            if (database.length == 1)
                table_id = table_id.substring(table_id.indexOf('.') + 1);

            //添加列信息
            tableWithColumns_cache.get(table_id)
                                  .put(column,
                                       new DbColumnInfo(tableWithIds_cache.get(table_id),
                                                        column,
                                                        false,
                                                        is_identity,
                                                        is_nullable,
                                                        ++position,
                                                        type,
                                                        sqlType,
                                                        max_length,
                                                        precision,
                                                        scale,
                                                        defaultValue,
                                                        comment));

            tableWithColumns_cache.get(table_id)
                                  .get(column)
                                  .setJdbcType(getJDBCType(tableWithColumns_cache.get(table_id)
                                                                                 .get(column)));
            tableWithColumns_cache.get(table_id)
                                  .get(column)
                                  .setJavaType(getJavaTypeInfo(tableWithColumns_cache.get(table_id)
                                                                                     .get(column)));
        }

        //查询索引/唯一键信息
        dataMap = selectIndexInfo(inDatabase,
                                  tablesMatcher,
                                  ignoreCase);

        if (dataMap == null)
            return tables;

        //索引列
        Map<String, Map<String, DbIndexInfo>> indexColumns = new HashMap<>();
        //唯一键列
        Map<String, Map<String, DbIndexInfo>> uniqueColumns = new HashMap<>();

        for (Map<String, Object> d : dataMap) {
            //表名
            String table_id = String.valueOf(d.get("F1"));
            //列名
            String column = String.valueOf(d.get("F2"))
                                  .trim();
            //索引名
            String index_id = String.valueOf(d.get("F3"));
            //是否唯一
            boolean is_unique = String.valueOf(d.get("F4"))
                                      .equals("1");
            //是否为主键
            boolean is_primary_key = String.valueOf(d.get("F5"))
                                           .equals("1");
//            //是否聚簇索引
//            boolean is_clustered = String.valueOf(d.get("F6"))
//                                         .equals("1");
            //是否降序
            boolean is_desc = String.valueOf(d.get("F7"))
                                    .equals("1");

            //如果当前只有默认数据库
            if (database.length == 1)
                table_id = table_id.substring(table_id.indexOf('.') + 1);

            if (!tableWithColumns_cache.containsKey(table_id)
                    || !tableWithColumns_cache.get(table_id)
                                              .containsKey(column))
                continue;

            //获取之前查询到的列信息
            DbColumnInfo columnInfo = tableWithColumns_cache.get(table_id)
                                                            .get(column);
            //将列标识为主键
            if (!columnInfo.getIsPrimary() && is_primary_key)
                columnInfo.setIsPrimary(true);
            //主键列不处理
            if (is_primary_key)
                continue;

            //添加索引信息
            Map<String, DbIndexInfo> indexes = new HashMap<>();
            DbIndexInfo index = new DbIndexInfo(index_id,
                                                is_unique);

            if (!indexColumns.containsKey(table_id))
                indexColumns.put(table_id,
                                 indexes);
            else
                indexes = indexColumns.get(table_id);

            if (!indexes.containsKey(index_id))
                indexes.put(index_id,
                            index);

            //添加索引相关的列信息
            index.getColumns()
                 .add(new DbIndexColumnInfo(columnInfo,
                                            is_desc));

            //添加唯一列信息
            if (is_unique) {
                indexes = new HashMap<>();
                index = new DbIndexInfo(index_id,
                                        true);

                if (!uniqueColumns.containsKey(table_id))
                    uniqueColumns.put(table_id,
                                      indexes);

                if (!indexes.containsKey(index_id))
                    indexes.put(index_id,
                                index);

                //添加唯一列相关的列信息
                index.getColumns()
                     .add(new DbIndexColumnInfo(columnInfo,
                                                is_desc));
            }
        }

        for (String table_id : indexColumns.keySet()) {
            for (String name : indexColumns.get(table_id)
                                           .keySet()) {
                tableWithIds_cache.get(table_id)
                                  .getIndexesDict()
                                  .put(name,
                                       indexColumns.get(table_id)
                                                   .get(name));
            }
        }

        for (String table_id : uniqueColumns.keySet()) {
            for (String name : uniqueColumns.get(table_id)
                                            .keySet()) {
                //重新排序
                uniqueColumns.get(table_id)
                             .get(name)
                             .getColumns()
                             .sort(Comparator.comparing(c -> c.getColumn()
                                                              .getName()));

                tableWithIds_cache.get(table_id)
                                  .getUniquesDict()
                                  .put(name,
                                       uniqueColumns.get(table_id)
                                                    .get(name));
            }
        }

        //如果未指定数据库，则查询外键信息
        if (tbname == null) {
            dataMap = selectForeignInfo(inDatabase,
                                        tablesMatcher,
                                        ignoreCase);

            if (dataMap == null)
                return tables;

            //外键列
            Map<String, Map<String, DbForeignInfo>> fkColumns = new HashMap<>();
            for (Map<String, Object> d : dataMap) {
                //表名
                String table_id = String.valueOf(d.get("F1"));
                //列名
                String column = String.valueOf(d.get("F2"))
                                      .trim();
                //外键名
                String fk_id = String.valueOf(d.get("F3"));
                //关联表名
                String ref_table_id = String.valueOf(d.get("F4"));
//                //是否为外键
//                boolean is_foreign_key = String.valueOf(d.get("F5"))
//                                               .equals("1");
                //关联列名
                String ref_column = String.valueOf(d.get("F6"));

                //如果当前只有默认数据库
                if (database.length == 1) {
                    table_id = table_id.substring(table_id.indexOf('.') + 1);
                    ref_table_id = ref_table_id.substring(ref_table_id.indexOf('.') + 1);
                }

                if (!tableWithColumns_cache.containsKey(table_id) || !tableWithColumns_cache.get(table_id)
                                                                                            .containsKey(column))
                    continue;

                //获取之前查询到的列信息
                DbColumnInfo columnInfo = tableWithColumns_cache.get(table_id)
                                                                .get(column);
                if (!tableWithIds_cache.containsKey(ref_table_id))
                    continue;
                //关联表信息
                DbTableInfo ref_table = tableWithIds_cache.get(ref_table_id);
                //关联列信息
                DbColumnInfo ref_columnInfo = tableWithColumns_cache.get(ref_table_id)
                                                                    .get(ref_column);

                //添加外键信息
                Map<String, DbForeignInfo> foreigns = new HashMap<>();
                DbForeignInfo foreign = new DbForeignInfo(tableWithIds_cache.get(table_id),
                                                          ref_table);
                if (!fkColumns.containsKey(table_id))
                    fkColumns.put(table_id,
                                  foreigns);
                else
                    foreigns = fkColumns.get(table_id);

                if (!foreigns.containsKey(fk_id))
                    foreigns.put(fk_id,
                                 foreign);

                //添加外键相关的列信息
                foreign.getColumns()
                       .add(columnInfo);
                foreign.getReferencedColumns()
                       .add(ref_columnInfo);
            }

            for (String table_id : fkColumns.keySet()) {
                for (String name : fkColumns.get(table_id)
                                            .keySet()) {
                    tableWithIds_cache.get(table_id)
                                      .getIndexesDict()
                                      .put(name,
                                           indexColumns.get(table_id)
                                                       .get(name));
                }
            }
        }

        for (String table_id : tableWithColumns_cache.keySet()) {
            for (DbColumnInfo column : tableWithColumns_cache.get(table_id)
                                                             .values()) {
                tableWithIds_cache.get(table_id)
                                  .getColumns()
                                  .add(column);
                if (column.getIsIdentity())
                    tableWithIds_cache.get(table_id)
                                      .getIdentities()
                                      .add(column);
                if (column.getIsPrimary())
                    tableWithIds_cache.get(table_id)
                                      .getPrimaries()
                                      .add(column);
            }
        }

        //排序
        for (DbTableInfo table : tableWithIds_cache.values()) {
            tables.add(sort(table));
        }

        //所有表信息按名称（升序）排序
        tables.sort(Comparator.comparing(DbTableInfo::getSchema)
                              .thenComparing(DbTableInfo::getName));

        //清理临时存储的数据
        tableWithIds_cache.clear();
        tableWithColumns_cache.clear();

        return tables;
    }

    @Override
    public List<String> getDatabases()
            throws
            ModuleException {
        String sql = " select username from all_users";
        return this.ado.selectList(getSqlSession(),
                                   getMSId(MD5Utils.hash(sql)),
                                   sql,
                                   null,
                                   null,
                                   String.class,
                                   null,
                                   null,
                                   config.getNameConvertType());
    }

    @Override
    public List<DbTableInfo> getTablesByDatabase(String... database)
            throws
            ModuleException {
        return getTables(database,
                         null,
                         false);
    }

    @Override
    public DbTableInfo getTableByName(String name,
                                      boolean ignoreCase)
            throws
            ModuleException {
        return CollectionsExtension.firstOrDefault(getTables(null,
                                                             name,
                                                             ignoreCase));
    }

    @Override
    public boolean existsTable(String name,
                               boolean ignoreCase)
            throws
            ModuleException {
        String[] tbname = splitTableName(name,
                                         ignoreCase);
        if (tbname == null)
            throw new ModuleException(Strings.getUnknownValue("name",
                                                              name));

        //获取当前模式名，也就是用户标识
        if (tbname.length == 1) {
            String userId = getUserIdFromConnectionString(ignoreCase);
            tbname = new String[]{userId,
                                  tbname[0]};
        }

        String sql = String.format(" select case when count(1) > 0 then 1 else 0 end from all_tab_comments where %s%s and %s%s",
                                   ignoreCase
                                   ? "lower(owner)"
                                   : "owner",
                                   StringUtils.hasText(tbname[0])
                                   ? String.format("='%s'",
                                                   tbname[0])
                                   : " is null",
                                   ignoreCase
                                   ? "lower(table_name)"
                                   : "table_name",
                                   StringUtils.hasText(tbname[1])
                                   ? String.format("='%s'",
                                                   tbname[1])
                                   : " is null");

        return this.ado.selectOne(getSqlSession(),
                                  getMSId(MD5Utils.hash(sql)),
                                  sql,
                                  null,
                                  null,
                                  Boolean.class,
                                  null,
                                  null,
                                  config.getNameConvertType());
    }

    @Override
    public JDBCType getJDBCType(DbColumnInfo column) {
        String dbTypeTextFull = column.getDbTypeTextFull() == null
                                ? ""
                                : column.getDbTypeTextFull()
                                        .toLowerCase(Locale.ROOT);
        switch (dbTypeTextFull) {
            case "number(1)":
            case "numeric(1)":
                return JDBCType.BIT;
            case "number(3)":
            case "numeric(3)":
                return JDBCType.TINYINT;
            case "number(5)":
            case "numeric(5)":
                return JDBCType.SMALLINT;
            case "number(10)":
            case "numeric(10)":
                return JDBCType.INTEGER;
            case "number(19)":
            case "numeric(19)":
                return JDBCType.BIGINT;
            case "float(63)":
                return JDBCType.FLOAT;
            case "float(126)":
                return JDBCType.DOUBLE;
            case "number(10,2)":
                return JDBCType.DECIMAL;

            case "interval day(2) to second(6)":
                return JDBCType.TIME;
            case "datetime":
            case "timestamp(6)":
            case "timestamp(6) with local time zone":
                return JDBCType.TIMESTAMP;

            case "blob":
                return JDBCType.BLOB;
            case "clob":
                return JDBCType.CLOB;
            case "nvarchar2(255)":
                return JDBCType.NVARCHAR;

            case "char(36)":
                return JDBCType.CHAR;
        }

        String dbTypeText = column.getDbTypeText() == null
                            ? ""
                            : column.getDbTypeText()
                                    .toLowerCase(Locale.ROOT);
        switch (dbTypeText) {
            case "bit":
                return JDBCType.BIT;
            case "byte":
            case "tinyint":
                return JDBCType.TINYINT;
            case "smallint":
                return JDBCType.SMALLINT;
            case "int":
            case "integer":
                return JDBCType.INTEGER;
            case "bigint":
                return JDBCType.BIGINT;
            case "real":
            case "binary_float":
                return JDBCType.FLOAT;
            case "double":
            case "float":
            case "double precision":
            case "binary_double":
                return JDBCType.DOUBLE;
            case "numeric":
            case "number":
                if (column.getScale() == 0) {
                    if (column.getPrecision() >= 19)
                        return JDBCType.BIGINT;
                    else if (column.getPrecision() >= 10)
                        return JDBCType.INTEGER;
                    else if (column.getPrecision() >= 5)
                        return JDBCType.SMALLINT;
                    else if (column.getPrecision() >= 3)
                        return JDBCType.TINYINT;
                    else
                        return JDBCType.BIT;
                }
            case "dec":
            case "decimal":
                return JDBCType.DECIMAL;
            case "time":
            case "interval day to second":
            case "interval year to month":
            case "interval year":
            case "interval month":
            case "interval day":
            case "interval day to hour":
            case "interval day to minute":
            case "interval hour":
            case "interval hour to minute":
            case "interval hour to second":
            case "interval minute":
            case "interval minute to second":
            case "interval second":
            case "time with time zone":
                return JDBCType.TIME;
            case "date":
                return JDBCType.DATE;
            case "timestamp":
            case "timestamp with local time zone":
            case "timestamp with time zone":
                return JDBCType.TIMESTAMP;
            case "binary":
            case "varbinary":
            case "blob":
            case "image":
            case "longvarbinary":
            case "bfile":
            case "raw":
            case "long raw":
                return JDBCType.VARBINARY;
            case "character":
            case "char":
                return JDBCType.CHAR;
            case "nchar":
                return JDBCType.NCHAR;
            case "nvarchar":
            case "nvarchar2":
            case "nclob":
                return JDBCType.NVARCHAR;
            case "clob":
            case "varchar":
            case "varchar2":
            case "text":
            case "longvarchar":
            case "rowid":
            default:
                return JDBCType.VARCHAR;
        }
    }

    @Override
    public String getJavaTypeConvert(DbColumnInfo column) {
        checkAndSetupJavaMap(column);
        return super.getJavaTypeConvert(column);
    }

    @Override
    public String getJavaType(DbColumnInfo column) {
        checkAndSetupJavaMap(column);
        return super.getJavaType(column);
    }

    @Override
    public String getJavaPackageType(DbColumnInfo column) {
        checkAndSetupJavaMap(column);
        return super.getJavaPackageType(column);
    }

    @Override
    public Class<?> getJavaTypeInfo(DbColumnInfo column) {
        checkAndSetupJavaMap(column);
        return super.getJavaTypeInfo(column);
    }

    @Override
    public Class<?> getJavaPackageTypeInfo(DbColumnInfo column) {
        checkAndSetupJavaMap(column);
        return super.getJavaPackageTypeInfo(column);
    }

    @Override
    public String getJavaStringify(DbColumnInfo column) {
        checkAndSetupJavaMap(column);
        return super.getJavaStringify(column);
    }

    @Override
    public String getJavaParse(DbColumnInfo column) {
        checkAndSetupJavaMap(column);
        return super.getJavaParse(column);
    }
}
