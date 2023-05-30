package project.extension.mybatis.edge.core.provider.mysql;

import com.alibaba.druid.pool.DruidDataSourceFactory;
import org.apache.commons.collections4.map.CaseInsensitiveMap;
import org.springframework.util.StringUtils;
import project.extension.collections.CollectionsExtension;
import project.extension.func.IFunc1;
import project.extension.mybatis.edge.config.DataSourceConfig;
import project.extension.mybatis.edge.core.ado.INaiveAdo;
import project.extension.mybatis.edge.core.provider.normal.DbFirst;
import project.extension.mybatis.edge.globalization.Strings;
import project.extension.mybatis.edge.model.*;
import project.extension.standard.exception.ModuleException;

import java.math.BigDecimal;
import java.sql.JDBCType;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * MySql DbFirst 开发模式相关功能
 *
 * @author LCTR
 * @date 2022-07-18
 */
public class MySqlDbFirst
        extends DbFirst {
    public MySqlDbFirst(DataSourceConfig config,
                        INaiveAdo ado) {
        super(config,
              ado);
        initialization();
    }

    /**
     * 初始化
     */
    private void initialization() {
        //是否已初始化
        if (dbToJavaMap.size() > 0)
            return;

        dbToJavaMap.putIfAbsent("bit",
                                new DbTypeToJavaType("(boolean)",
                                                     "(Boolean)",
                                                     "Boolean.parseBoolean(%s)",
                                                     "Boolean.toString(%s)",
                                                     "boolean",
                                                     "Boolean",
                                                     boolean.class,
                                                     Boolean.class));

        dbToJavaMap.putIfAbsent("tinyint",
                                new DbTypeToJavaType("(byte)",
                                                     "(Byte)",
                                                     "Byte.parseByte(%s)",
                                                     "Byte.toString(%s)",
                                                     "byte",
                                                     "Byte",
                                                     byte.class,
                                                     Byte.class));

        dbToJavaMap.putIfAbsent("smallint",
                                new DbTypeToJavaType("(short)",
                                                     "(Short)",
                                                     "Short.parseShort(%s)",
                                                     "Short.toString(%s)",
                                                     "short",
                                                     "Short",
                                                     short.class,
                                                     Short.class));

        dbToJavaMap.putIfAbsent("integer",
                                new DbTypeToJavaType("(int)",
                                                     "(Integer)",
                                                     "Integer.parseInt(%s)",
                                                     "Integer.toString(%s)",
                                                     "int",
                                                     "Integer",
                                                     int.class,
                                                     Integer.class));

        dbToJavaMap.putIfAbsent("bigint",
                                new DbTypeToJavaType("(long)",
                                                     "(Long)",
                                                     "Long.parseLong(%s)",
                                                     "Long.toString(%s)",
                                                     "long",
                                                     "Long",
                                                     long.class,
                                                     Long.class));

        dbToJavaMap.putIfAbsent("float",
                                new DbTypeToJavaType("(float)",
                                                     "(Float)",
                                                     "Float.parseFloat(%s)",
                                                     "Float.toString(%s)",
                                                     "float",
                                                     "Float",
                                                     float.class,
                                                     Float.class));
        dbToJavaMap.putIfAbsent("double",
                                new DbTypeToJavaType("(double)",
                                                     "(Double)",
                                                     "Double.parseDouble(%s)",
                                                     "Double.toString(%s)",
                                                     "double",
                                                     "Double",
                                                     double.class,
                                                     Double.class));
        dbToJavaMap.putIfAbsent("decimal",
                                new DbTypeToJavaType("(BigDecimal)",
                                                     "(BigDecimal)",
                                                     "new BigDecimal(%s)",
                                                     "%s.toString()",
                                                     "BigDecimal",
                                                     "BigDecimal",
                                                     BigDecimal.class,
                                                     BigDecimal.class));

        dbToJavaMap.putIfAbsent("time",
                                new DbTypeToJavaType("(java.sql.Time)",
                                                     "(java.sql.Time)",
                                                     "new java.sql.Time(Long.parseLong(%s))",
                                                     "Long.toString(%s.getTime())",
                                                     "java.sql.Time",
                                                     "java.sql.Time",
                                                     java.sql.Time.class,
                                                     java.sql.Time.class));
        dbToJavaMap.putIfAbsent("date",
                                new DbTypeToJavaType("(java.sql.Date)",
                                                     "(java.sql.Date)",
                                                     "new java.sql.Date(Long.parseLong(%s))",
                                                     "Long.toString(%s.getTime())",
                                                     "java.sql.Date",
                                                     "java.sql.Date",
                                                     java.sql.Date.class,
                                                     java.sql.Date.class));
        dbToJavaMap.putIfAbsent("datetime",
                                new DbTypeToJavaType("(Date)",
                                                     "(Date)",
                                                     "new Date(Long.parseLong(%s))",
                                                     "Long.toString(%s.getTime())",
                                                     "Date",
                                                     "Date",
                                                     Date.class,
                                                     Date.class));
        dbToJavaMap.putIfAbsent("timestamp",
                                new DbTypeToJavaType("(java.sql.Timestamp)",
                                                     "(java.sql.Timestamp)",
                                                     "new java.sql.Timestamp(Long.parseLong(%s))",
                                                     "Long.toString(%s.getTime())",
                                                     "Date",
                                                     "Date",
                                                     java.sql.Timestamp.class,
                                                     java.sql.Timestamp.class));

        dbToJavaMap.putIfAbsent("longblob",
                                new DbTypeToJavaType("(byte[])",
                                                     "(byte[])",
                                                     "Base64.getDecoder().decode(%s.getBytes(StandardCharsets.UTF_8))",
                                                     "new String(Base64.getEncoder().encode(%s), StandardCharsets.UTF_8)",
                                                     "byte[]",
                                                     "byte[]",
                                                     byte[].class,
                                                     byte[].class));

        dbToJavaMap.putIfAbsent("varchar",
                                new DbTypeToJavaType("(String)",
                                                     "(String)",
                                                     "%s",
                                                     "%s",
                                                     "String",
                                                     "String",
                                                     String.class,
                                                     String.class));
        dbToJavaMap.putIfAbsent("guid",
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
                                      "bit");
                break;
            case "tinyint":
                copyAndPutDbToJavaMap(dbTypeTextFull,
                                      "tinyint");
                break;
            case "smallint":
            case "year":
                copyAndPutDbToJavaMap(dbTypeTextFull,
                                      "smallint");
                break;
            case "mediumint":
            case "integer":
                copyAndPutDbToJavaMap(dbTypeTextFull,
                                      "integer");
                break;
            case "bigint":
                copyAndPutDbToJavaMap(dbTypeTextFull,
                                      "bigint");
                break;
            case "float":
                copyAndPutDbToJavaMap(dbTypeTextFull,
                                      "float");
                break;
            case "real":
            case "double":
                copyAndPutDbToJavaMap(dbTypeTextFull,
                                      "double");
                break;
            case "decimal":
            case "numeric":
                copyAndPutDbToJavaMap(dbTypeTextFull,
                                      "decimal");
                break;
            case "time":
                copyAndPutDbToJavaMap(dbTypeTextFull,
                                      "time");
                break;
            case "date":
                copyAndPutDbToJavaMap(dbTypeTextFull,
                                      "date");
                break;
            case "datetime":
                copyAndPutDbToJavaMap(dbTypeTextFull,
                                      "datetime");
                break;
            case "timestamp":
                copyAndPutDbToJavaMap(dbTypeTextFull,
                                      "timestamp");
                break;
            case "tinyblob":
            case "blob":
            case "mediumblob":
            case "longblob":
            case "binary":
            case "varbinary":
                copyAndPutDbToJavaMap(dbTypeTextFull,
                                      "tinyblob");
                break;
            case "char":
                if (column.getMaxLength() == 36) {
                    copyAndPutDbToJavaMap(dbTypeTextFull,
                                          "guid");
                    break;
                }
            case "tinytext":
            case "text":
            case "mediumtext":
            case "longtext":
            case "varchar":
                copyAndPutDbToJavaMap(dbTypeTextFull,
                                      "varchar");
            default:
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
                                           + "concat(a.table_schema, '.', a.table_name) `F1`,\r\n"
                                           + "a.table_schema as `F2`,\r\n"
                                           + "a.table_name as `F3`,\r\n"
                                           + "a.table_comment as `F4`,\r\n"
                                           + "case when a.table_type = 'BASE TABLE' then 'TABLE' else a.table_type end as `F5` \r\n"
                                           + "from information_schema.tables a \r\n"
                                           + "where %s in (%s) %s ",
                                   ignoreCase
                                   ? "lower(a.table_schema) "
                                   : "a.table_schema ",
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
                                                   : " is null"));

        return super.ado.selectMapList(getSqlSession(),
                                       getMSId(),
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
                                           + "concat(a.table_schema, '.', a.table_name) as `F1`,\r\n"
                                           + "a.column_name as `F2`,\r\n"
                                           + "a.data_type as `F3`,\r\n"
                                           + "ifnull(a.character_maximum_length, 0) as `F4`,\r\n"
                                           + "a.column_type as `F5`,\r\n"
                                           + "case when a.is_nullable = 'YES' then 1 else 0 end as `F6`,\r\n"
                                           + "case when locate('auto_increment', a.extra) > 0 then 1 else 0 end as `F7`,\r\n"
                                           + "a.column_comment as `F8`,\r\n"
                                           + "a.column_default as `F9`\r\n"
                                           + "from information_schema.columns a \r\n"
                                           + "where %s in (%s) \r\n"
                                           + "and %s ",
                                   ignoreCase
                                   ? "lower(a.table_schema) "
                                   : "a.table_schema ",
                                   inDatabase,
                                   tablesMatcher);

        return super.ado.selectMapList(getSqlSession(),
                                       getMSId(),
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
                                           + "concat(a.table_schema, '.', a.table_name) as `F1`,\r\n"
                                           + "a.column_name as `F2`,\r\n"
                                           + "a.index_name as `F3`,\r\n"
                                           + "case when a.non_unique = 0 then 1 else 0 end as `F4`,\r\n"
                                           + "case when a.index_name = 'PRIMARY' then 1 else 0 end as `F5`,\r\n"
                                           + "0 as `F6`,\r\n"
                                           + "0 as `F7`\r\n"
                                           + "from information_schema.statistics a \r\n"
                                           + "where %s in (%s) \r\n"
                                           + "and %s ",
                                   ignoreCase
                                   ? "lower(a.table_schema)"
                                   : "a.table_schema",
                                   inDatabase,
                                   tablesMatcher);

        return super.ado.selectMapList(getSqlSession(),
                                       getMSId(),
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
                                           + "concat(a.constraint_schema, '.', a.table_name) as `F1`,\r\n"
                                           + "a.column_name as `F2`,\r\n"
                                           + "a.constraint_name as `F3`,\r\n"
                                           + "concat(a.referenced_table_schema, '.', a.referenced_table_name) as `F4`,\r\n"
                                           + "1 as `F5`,\r\n"
                                           + "a.referenced_column_name as `F6` \r\n"
                                           + "from information_schema.key_column_usage a \r\n"
                                           + "where %s in (%s) \r\n"
                                           + "and %s \r\n"
                                           + "and not isnull(position_in_unique_constraint)",
                                   ignoreCase
                                   ? "lower(a.constraint_schema)"
                                   : "a.constraint_schema",
                                   inDatabase,
                                   tablesMatcher);

        return super.ado.selectMapList(getSqlSession(),
                                       getMSId(),
                                       sql,
                                       null,
                                       null,
                                       null,
                                       null,
                                       config.getNameConvertType());
    }

    /**
     * 从连接字符串中获取数据库名称
     *
     * @param lower 转小写
     * @return 数据库名称
     */
    private String getDatabase(boolean lower)
            throws
            ModuleException {
        Matcher matcher = Pattern.compile("jdbc:mysql://(.*?)/(.*?)\\?",
                                          Pattern.CASE_INSENSITIVE)
                                 .matcher(String.format("%s;",
                                                        config.getProperties()
                                                              .getProperty(DruidDataSourceFactory.PROP_URL)));
        if (!matcher.find())
            throw new ModuleException(Strings.getCanNotGetDbNameFromUrl("jdbc:mysql://(.*?)/(.*?)\\?"));

        if (lower)
            return matcher.group(2)
                          .toLowerCase(Locale.ROOT);
        else
            return matcher.group(2);
    }

    @Override
    protected String[] splitTableName(String name,
                                      boolean lower) {
        String[] tbname = getSplitTableNames(name,
                                             '`',
                                             '`',
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
     * @param tablename  表名
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
        Map<String, DbTableInfo> tableWithIds_cache = new CaseInsensitiveMap<>();
        //临时存储表名对应的列名和列信息
        Map<String, Map<String, DbColumnInfo>> tableWithColumns_cache = new CaseInsensitiveMap<>();

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
                String db = getDatabase(ignoreCase);
                if (!StringUtils.hasText(db))
                    return tables;
                tbname = new String[]{db,
                                      tbname[0]};
            }
            database = new String[]{tbname[0]};
        } else if (database == null || database.length == 0) {
            String db = getDatabase(ignoreCase);
            if (!StringUtils.hasText(db))
                return tables;
            database = new String[]{db};
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
        //所有的存储过程名
//        List<String[]> sp_names = new ArrayList<>();
//        //表和视图名，存储上限为1000的数据
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
                                       new CaseInsensitiveMap<>());

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
//                case StoreProcedure:
//                    sp_names_lower_1000.add(table.replace("'",
//                                                         "''"));
//                    if (sp_names_lower_1000.size() >= 999) {
//                        sp_names.add(sp_names_lower_1000.toArray(new String[0]));
//                        sp_names_lower_1000.clear();
//                    }
//                    break;
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
            String type = String.valueOf(d.get("F3"));
            //建表时的完整类型名称
            String sqlType = String.valueOf(d.get("F5"));
            //数据的最大长度
            Matcher max_length_matcher = Pattern.compile("\\w+\\((\\d+)")
                                                .matcher(sqlType);
            int max_length = max_length_matcher.find()
                             ? Integer.parseInt(max_length_matcher.group(1))
                             : -1;
            //数据精度
            int precision = 0;
            //数据标度
            int scale = 0;
            Matcher matcher = Pattern.compile("\\w+\\((\\d+),(\\d+)")
                                     .matcher(sqlType);
            if (matcher.find()) {
                precision = Integer.parseInt(matcher.group(1));
                scale = Integer.parseInt(matcher.group(2));
            }

            //是否可为空
            boolean is_nullable = String.valueOf(d.get("F6"))
                                        .equals("1");
            //是否自增
            boolean is_identity = String.valueOf(d.get("F7"))
                                        .equals("1");
            //注释
            String comment = String.valueOf(d.get("F8"));
            //默认值
            String defaultValue = String.valueOf(d.get("F9"));

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
        Map<String, Map<String, DbIndexInfo>> indexColumns = new CaseInsensitiveMap<>();
        //唯一键列
        Map<String, Map<String, DbIndexInfo>> uniqueColumns = new CaseInsensitiveMap<>();

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
            if (!columnInfo.isPrimary() && is_primary_key)
                columnInfo.setPrimary(true);

            //添加索引信息
            Map<String, DbIndexInfo> indexes = new CaseInsensitiveMap<>();
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
            if (is_unique && !is_primary_key) {
                indexes = new CaseInsensitiveMap<>();
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
            Map<String, Map<String, DbForeignInfo>> fkColumns = new CaseInsensitiveMap<>();
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

                if (!tableWithColumns_cache.containsKey(table_id)
                        || !tableWithColumns_cache.get(table_id)
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
                Map<String, DbForeignInfo> foreigns = new CaseInsensitiveMap<>();
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
                if (column.isIdentity())
                    tableWithIds_cache.get(table_id)
                                      .getIdentities()
                                      .add(column);
                if (column.isPrimary())
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
        String sql = " select schema_name from information_schema.schemata where schema_name not in ('information_schema', 'mysql', 'performance_schema')";
        return super.ado.selectList(getSqlSession(),
                                    getMSId(),
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

        //获取当前数据库
        if (tbname.length == 1) {
            String database = getDatabase(ignoreCase);
            tbname = new String[]{database,
                                  tbname[0]};
        }

        String sql = String.format(" select case when count(1) > 0 then 1 else 0 end from information_schema.TABLES where %s%s and %s%s",
                                   ignoreCase
                                   ? "lower(table_schema)"
                                   : "table_schema",
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

        return super.ado.selectOne(getSqlSession(),
                                   getMSId(),
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
            case "bit":
                return JDBCType.BIT;

            case "tinyint":
                return JDBCType.TINYINT;
            case "smallint":
            case "year":
                return JDBCType.SMALLINT;
            case "mediumint":
            case "int":
                return JDBCType.INTEGER;
            case "bigint":
                return JDBCType.BIGINT;

            case "real":
            case "double":
                return JDBCType.DOUBLE;
            case "float":
                return JDBCType.FLOAT;
            case "numeric":
            case "decimal":
                return JDBCType.DECIMAL;

            case "time":
                return JDBCType.TIME;
            case "date":
                return JDBCType.DATE;
            case "datetime":
            case "timestamp":
                return JDBCType.TIMESTAMP;

            case "tinyblob":
            case "blob":
            case "mediumblob":
            case "longblob":
                return JDBCType.BLOB;

            case "binary":
                return JDBCType.BINARY;
            case "varbinary":
                return JDBCType.VARBINARY;

            case "tinytext":
            case "text":
            case "mediumtext":
            case "longtext":
                return JDBCType.CLOB;

            case "char":
                return JDBCType.CHAR;
            case "varchar":
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
