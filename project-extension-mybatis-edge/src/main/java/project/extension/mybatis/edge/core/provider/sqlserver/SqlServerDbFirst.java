package project.extension.mybatis.edge.core.provider.sqlserver;

import com.alibaba.druid.pool.DruidDataSourceFactory;
import org.springframework.util.StringUtils;
import project.extension.collections.CollectionsExtension;
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

/**
 * SqlServer DbFirst 开发模式相关功能
 *
 * @author LCTR
 * @date 2022-07-15
 */
public class SqlServerDbFirst
        extends DbFirst {
    public SqlServerDbFirst(DataSourceConfig config,
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
                                new DbTypeToJavaType("(short)",
                                                     "(Short)",
                                                     "Short.parseShort(%s)",
                                                     "Short.toString(%s)",
                                                     "short",
                                                     "Short",
                                                     short.class,
                                                     Short.class));

        dbToJavaMap.putIfAbsent("int",
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

        dbToJavaMap.putIfAbsent("real",
                                new DbTypeToJavaType("(float)",
                                                     "(Float)",
                                                     "Float.parseFloat(%s)",
                                                     "Float.toString(%s)",
                                                     "float",
                                                     "Float",
                                                     float.class,
                                                     Float.class));
        dbToJavaMap.putIfAbsent("float",
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

        dbToJavaMap.putIfAbsent("date",
                                new DbTypeToJavaType("(java.sql.Date)",
                                                     "(java.sql.Date)",
                                                     "new java.sql.Date(Long.parseLong(%s))",
                                                     "Long.toString(%s.getTime())",
                                                     "java.sql.Date",
                                                     "java.sql.Date",
                                                     java.sql.Date.class,
                                                     java.sql.Date.class));
        dbToJavaMap.putIfAbsent("time",
                                new DbTypeToJavaType("(java.sql.Time)",
                                                     "(java.sql.Time)",
                                                     "new java.sql.Time(Long.parseLong(%s))",
                                                     "Long.toString(%s.getTime())",
                                                     "java.sql.Time",
                                                     "java.sql.Time",
                                                     java.sql.Time.class,
                                                     java.sql.Time.class));
        dbToJavaMap.putIfAbsent("datetime",
                                new DbTypeToJavaType("(Date)",
                                                     "(Date)",
                                                     "new Date(Long.parseLong(%s))",
                                                     "Long.toString(%s.getTime())",
                                                     "Date",
                                                     "Date",
                                                     Date.class,
                                                     Date.class));

        dbToJavaMap.putIfAbsent("datetime2",
                                new DbTypeToJavaType("(java.sql.Timestamp)",
                                                     "(java.sql.Timestamp)",
                                                     "new java.sql.Timestamp(Long.parseLong(%s))",
                                                     "Long.toString(%s.getTime())",
                                                     "Date",
                                                     "Date",
                                                     java.sql.Timestamp.class,
                                                     java.sql.Timestamp.class));

        dbToJavaMap.putIfAbsent("datetimeoffset",
                                new DbTypeToJavaType("(microsoft.sql.DateTimeOffset)",
                                                     "(microsoft.sql.DateTimeOffset)",
                                                     "microsoft.sql.DateTimeOffset.valueOf(new java.sql.Timestamp(Long.parseLong(%s)), 0)",
                                                     "Long.toString(%s.getTimestamp().getTime())",
                                                     "Date",
                                                     "Date",
                                                     microsoft.sql.DateTimeOffset.class,
                                                     microsoft.sql.DateTimeOffset.class));

        dbToJavaMap.putIfAbsent("binary",
                                new DbTypeToJavaType("(byte[])",
                                                     "(Byte[])",
                                                     "Base64.getDecoder().decode(%s.getBytes(StandardCharsets.UTF_8))",
                                                     "new String(Base64.getEncoder().encode(%s), StandardCharsets.UTF_8)",
                                                     "byte[]",
                                                     "Byte[]",
                                                     byte[].class,
                                                     Byte[].class));

        dbToJavaMap.putIfAbsent("char",
                                new DbTypeToJavaType("(char)",
                                                     "(Character)",
                                                     "%s.charAt(0)",
                                                     "Character.toString(%s)",
                                                     "char",
                                                     "Character",
                                                     char.class,
                                                     Character.class));
        dbToJavaMap.putIfAbsent("nvarchar",
                                new DbTypeToJavaType("(String)",
                                                     "(String)",
                                                     "%s",
                                                     "%s",
                                                     "String",
                                                     "String",
                                                     String.class,
                                                     String.class));

        dbToJavaMap.putIfAbsent("uniqueidentifier",
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
            case "smallint":
                copyAndPutDbToJavaMap(dbTypeTextFull,
                                      "tinyint");
                break;
            case "int":
                copyAndPutDbToJavaMap(dbTypeTextFull,
                                      "int");
                break;
            case "bigint":
                copyAndPutDbToJavaMap(dbTypeTextFull,
                                      "bigint");
                break;
            case "real":
                copyAndPutDbToJavaMap(dbTypeTextFull,
                                      "real");
                break;
            case "float":
                copyAndPutDbToJavaMap(dbTypeTextFull,
                                      "float");
                break;
            case "numeric":
            case "decimal":
            case "money":
            case "smallmoney":
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
            case "datetime2":
            case "smalldatetime":
                copyAndPutDbToJavaMap(dbTypeTextFull,
                                      "datetime2");
                break;
            case "datetimeoffset":
                copyAndPutDbToJavaMap(dbTypeTextFull,
                                      "datetimeoffset");
                break;
            case "binary":
            case "varbinary":
            case "image":
            case "timestamp":
            case "udt":
                copyAndPutDbToJavaMap(dbTypeTextFull,
                                      "binary");
                break;
            case "char":
            case "nchar":
                copyAndPutDbToJavaMap(dbTypeTextFull,
                                      "char");
                break;
            case "varchar":
            case "text":
            case "nvarchar":
            case "ntext":
                copyAndPutDbToJavaMap(dbTypeTextFull,
                                      "nvarchar");
                break;
            case "uniqueidentifier":
                copyAndPutDbToJavaMap(dbTypeTextFull,
                                      "uniqueidentifier");
                break;
            default:
                copyAndPutDbToJavaMap(dbTypeTextFull,
                                      "unknown");
                break;
        }
    }

    /**
     * 查询表信息
     *
     * @param currentDatabase 当前数据库
     * @param targetDatabase  指定数据库
     * @param tbname          表名
     * @param ignoreCase      忽略大小写
     * @return 表信息
     */
    private List<Map<String, Object>> selectTableInfo(String currentDatabase,
                                                      String targetDatabase,
                                                      String[] tbname,
                                                      boolean ignoreCase)
            throws
            ModuleException {
        String sql = String.format("use [%s];\r\n"
                                           + "select * from ( \r\n"
                                           + "select \r\n"
                                           + "a.object_id as [F1],\r\n"
                                           + "b.name as [F2],\r\n"
                                           + "a.name as [F3],\r\n"
                                           + "(select value from sys.extended_properties where major_id = a.object_id AND minor_id = 0 AND name = 'MS_Description') as [F4],\r\n"
                                           + "'TABLE' as [F5] \r\n"
                                           + "from sys.tables a \r\n"
                                           + "left join sys.schemas b on b.schema_id = a.schema_id \r\n"
                                           + "where not(b.name = 'dbo' and a.name = 'sysdiagrams') \r\n"
                                           + "\r\n"
                                           + "UNION ALL \r\n"
                                           + "\r\n"
                                           + "select \r\n"
                                           + "a.object_id,\r\n"
                                           + "b.name,\r\n"
                                           + "a.name,\r\n"
                                           + "(select value from sys.extended_properties where major_id = a.object_id AND minor_id = 0 AND name = 'MS_Description'),\r\n"
                                           + "'VIEW' \r\n"
                                           + "from sys.views a \r\n"
                                           + "inner join sys.schemas b on b.schema_id = a.schema_id \r\n"
                                           + "\r\n"
                                           + "union all \r\n"
                                           + "\r\n"
                                           + "select \r\n"
                                           + "a.object_id, \r\n"
                                           + "b.name, \r\n"
                                           + "a.name, \r\n"
                                           + "(select value from sys.extended_properties where major_id = a.object_id AND minor_id = 0 AND name = 'MS_Description'), \r\n"
                                           + "'StoreProcedure' \r\n"
                                           + "from sys.procedures a \r\n"
                                           + "inner join sys.schemas b on b.schema_id = a.schema_id \r\n"
                                           + "where a.type = 'P' and charindex('diagram', a.name) = 0 \r\n"
                                           + ") ft_dbf %s \r\n"
                                           + "order by type desc, [owner], [name]; \r\n"
                                           + "use [%s];",
                                   targetDatabase,
                                   tbname == null
                                   ? ""
                                   : String.format(" where %s%s and %s%s",
                                                   ignoreCase
                                                   ? "lower([owner])"
                                                   : "[owner]",
                                                   StringUtils.hasText(tbname[1])
                                                   ? String.format("='%s'",
                                                                   tbname[1])
                                                   : " is null",
                                                   ignoreCase
                                                   ? "lower([name])"
                                                   : "[name]",
                                                   StringUtils.hasText(tbname[2])
                                                   ? String.format("='%s'",
                                                                   tbname[2])
                                                   : " is null"),
                                   currentDatabase);

        return this.ado.selectMapList(getSqlSession(),
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
     * @param currentDatabase 当前数据库
     * @param targetDatabase  指定数据库
     * @param t_v_MatcherSql  用来匹配表、视图的sql语句
     * @param sp_MatcherSql   用来匹配存储过程的sql语句
     * @return 列信息
     */
    private List<Map<String, Object>> selectColumnInfo(String currentDatabase,
                                                       String targetDatabase,
                                                       String t_v_MatcherSql,
                                                       String sp_MatcherSql)
            throws
            ModuleException {
        String sql_format = "select \r\n"
                + "isnull(e.name,'') + '.' + isnull(d.name,'') as [F1],\r\n"
                + "a.object_id as [F2],\r\n"
                + "a.name as [F3],\r\n"
                + "b.name as [F4],\r\n"
                + "case\r\n"
                + " when b.name in ('text', 'ntext', 'image') then -1\r\n"
                + " when b.name in ('nchar', 'nvarchar') then a.max_length / 2\r\n"
                + " else a.max_length end as [F5],\r\n"
                + "b.name + case \r\n"
                + " when b.name in ('char', 'varchar', 'nchar', 'nvarchar', 'binary', 'varbinary') then '(' + \r\n"
                + "  case when a.max_length = -1 then 'MAX' \r\n"
                + "  when b.name in ('nchar', 'nvarchar') then cast(a.max_length / 2 as varchar)\r\n"
                + "  else cast(a.max_length as varchar) end + ')'\r\n"
                + " when b.name in ('numeric', 'decimal') then '(' + cast(a.precision as varchar) + ',' + cast(a.scale as varchar) + ')'\r\n"
                + " else '' end as [F6],\r\n"
                + "( select value from sys.extended_properties where major_id = a.object_id AND minor_id = a.column_id AND name = 'MS_Description' and class=1) as [F7],\r\n"
                + "%s a \r\n"
                + "inner join sys.types b on b.user_type_id = a.user_type_id \r\n"
                + "left join sys.tables d on d.object_id = a.object_id \r\n"
                + "left join sys.schemas e on e.schema_id = d.schema_id \r\n%s"
                + "where %s \r\n";

        String sql = String.format(sql_format,
                                   "a.is_nullable as [F8], \r\n"
                                           + "a.is_identity as [F9], \r\n"
                                           + "f.text as [F10] \r\n"
                                           + "from sys.columns ",
                                   "left join syscomments f on f.id = a.default_object_id \r\n",
                                   t_v_MatcherSql);

        if (sp_MatcherSql.length() > 0) {
            sql += String.format("\r\n"
                                         + "union all \r\n"
                                         + "\r\n"
                                         + "%s \r\n",
                                 String.format(sql_format.replaceAll("select value from sys.extended_properties where major_id = a.object_id AND minor_id = a.column_id \r\n",
                                                                     "select value from sys.extended_properties where major_id = a.object_id AND minor_id = a.parameter_id \r\n"),
                                               "cast(0 as bit) as [F8], \r\n"
                                                       + "a.is_output as [F9], \r\n"
                                                       + "'' as [F10] \r\n"
                                                       + "from sys.parameters ",
                                               "",
                                               sp_MatcherSql));
        }

        sql = String.format("use [%s];\r\n"
                                    + "%s;\r\n"
                                    + "use [%s];",
                            targetDatabase,
                            sql,
                            currentDatabase);

        return this.ado.selectMapList(getSqlSession(),
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
     * @param currentDatabase 当前数据库
     * @param targetDatabase  指定数据库
     * @param t_v_MatcherSql  用来匹配表、视图的sql语句
     * @return 索引/唯一键信息
     */
    private List<Map<String, Object>> selectIndexInfo(String currentDatabase,
                                                      String targetDatabase,
                                                      String t_v_MatcherSql)
            throws
            ModuleException {
        String sql = String.format("use [%s];\r\n"
                                           + "select \r\n"
                                           + "a.object_id as [F1],\r\n"
                                           + "c.name as [F2],\r\n"
                                           + "b.name as [F3],\r\n"
                                           + "b.is_unique as [F4],\r\n"
                                           + "b.is_primary_key as [F5],\r\n"
                                           + "cast(case when b.type_desc = 'CLUSTERED' then 1 else 0 end as bit) as [F6],\r\n"
                                           + "case when a.is_descending_key = 1 then 1 else 0 end as [F7],\r\n"
                                           + "from sys.index_columns a \r\n"
                                           + "inner join sys.indexes b on b.object_id = a.object_id and b.index_id = a.index_id \r\n"
                                           + "left join sys.columns c on c.object_id = a.object_id and c.column_id = a.column_id \r\n"
                                           + "where %s;\r\n"
                                           + "use [%s];",
                                   targetDatabase,
                                   t_v_MatcherSql,
                                   currentDatabase);

        return this.ado.selectMapList(getSqlSession(),
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
     * @param currentDatabase 当前数据库
     * @param targetDatabase  指定数据库
     * @param t_v_MatcherSql  用来匹配表、视图的sql语句
     * @return 外键信息
     */
    private List<Map<String, Object>> selectForeignInfo(String currentDatabase,
                                                        String targetDatabase,
                                                        String t_v_MatcherSql)
            throws
            ModuleException {
        String sql = String.format("use [%s];\r\n"
                                           + "select \r\n"
                                           + "b.object_id as [F1],\r\n"
                                           + "c.name as [F2],\r\n"
                                           + "e.name as [F3],\r\n"
                                           + "a.referenced_object_id as [F4],\r\n"
                                           + "cast(1 as bit) as [F5],\r\n"
                                           + "d.name as [F6],\r\n"
                                           + "null as [F7],\r\n"
                                           + "null as [F8] \r\n"
                                           + "from sys.foreign_key_columns a \r\n"
                                           + "inner join sys.tables b on b.object_id = a.parent_object_id \r\n"
                                           + "inner join sys.columns c on c.object_id = a.parent_object_id and c.column_id = a.parent_column_id \r\n"
                                           + "inner join sys.columns d on d.object_id = a.referenced_object_id and d.column_id = a.referenced_column_id \r\n"
                                           + "left join sys.foreign_keys e on e.object_id = a.constraint_object_id \r\n"
                                           + "where %s;\r\n"
                                           + "use [%s];",
                                   targetDatabase,
                                   t_v_MatcherSql.replaceAll("a\\.object_id",
                                                             "b.object_id"),
                                   currentDatabase);

        return this.ado.selectMapList(getSqlSession(),
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
        Matcher matcher = Pattern.compile("jdbc:sqlserver://(.*?);DatabaseName=(.*?);.*?$",
                                          Pattern.CASE_INSENSITIVE)
                                 .matcher(String.format("%s;",
                                                        config.getProperties()
                                                              .getProperty(DruidDataSourceFactory.PROP_URL)));
        if (!matcher.find())
            throw new ModuleException(Strings.getCanNotGetDbNameFromUrl("jdbc:sqlserver://(.*?);DatabaseName=(.*?);.*?$"));

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
                                             '[',
                                             ']',
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
        //获取当前数据库名称
        String currentDatabase = getDatabase(ignoreCase);

        //获取数据库名+表名
        String[] tbname = null;
        String[] dbs = database == null || database.length == 0
                       ? new String[]{currentDatabase}
                       : database;

        if (StringUtils.hasText(tablename)) {
            tbname = splitTableName(tablename,
                                    ignoreCase);
            if (tbname == null)
                throw new ModuleException(Strings.getUnknownValue("tablename",
                                                                  tablename));
            if (tbname.length == 1)
                tbname = new String[]{currentDatabase,
                                      "dbo",
                                      tbname[0]};
            if (tbname.length == 2)
                tbname = new String[]{currentDatabase,
                                      tbname[0],
                                      tbname[1]};
            dbs = new String[]{tbname[0]};
        }

        //所有数据库的表结构信息
        List<DbTableInfo> allTables = new ArrayList<>();

        //遍历数据库
        for (String db : dbs) {
            if (!StringUtils.hasText(db))
                continue;

            //当前遍历数据库的表结构信息
            List<DbTableInfo> tables = new ArrayList<>();

            //临时存储表名对应的表结构信息
            Map<Integer, DbTableInfo> tableWithIds_cache = new HashMap<>();
            //临时存储表名对应的列名和列信息
            Map<Integer, Map<String, DbColumnInfo>> tableWithColumns_cache = new HashMap<>();

            //查询表结构信息
            List<Map<String, Object>> dataMap = selectTableInfo(currentDatabase,
                                                                db,
                                                                tbname,
                                                                ignoreCase);

            if (dataMap == null || dataMap.size() == 0)
                return tables;

            //所有的表和视图的标识
            List<Integer[]> t_v_ids = new ArrayList<>();
            //所有的存储过程的标识
            List<Integer[]> sp_ids = new ArrayList<>();
            //表和视图的标识，存储上限为1000的数据
            List<Integer> t_v_ids_1000 = new ArrayList<>();
            //存储过程的标识，存储上限为1000的数据
            List<Integer> sp_ids_1000 = new ArrayList<>();

            for (Map<String, Object> d : dataMap) {
                //对象标识
                int object_id = Integer.parseInt(String.valueOf(d.get("F1")));
                //所有者
                String owner = String.valueOf(d.get("F2"));
                //表名
                String table = String.valueOf(d.get("F3"));
                //注释
                String comment = String.valueOf(d.get("F4"));
                //数据库表类型
                DbTableType type = DbTableType.valueOf(String.valueOf(d.get("F5")));

                //添加表结构信息
                tableWithIds_cache.put(object_id,
                                       new DbTableInfo(Integer.toString(object_id),
                                                       owner,
                                                       table,
                                                       comment,
                                                       type));
                tableWithColumns_cache.put(object_id,
                                           new HashMap<>());

                //确保集合中的数组的长度不超过1000
                switch (type) {
                    case TABLE:
                    case VIEW:
                        t_v_ids_1000.add(object_id);
                        if (t_v_ids_1000.size() >= 999) {
                            t_v_ids.add(t_v_ids_1000.toArray(new Integer[0]));
                            t_v_ids_1000.clear();
                        }
                        break;
                    case StoreProcedure:
                        sp_ids_1000.add(object_id);
                        if (sp_ids_1000.size() >= 999) {
                            sp_ids.add(sp_ids_1000.toArray(new Integer[0]));
                            sp_ids_1000.clear();
                        }
                        break;
                }
            }

            //处理最后的一点数据
            if (t_v_ids_1000.size() > 0) {
                t_v_ids.add(t_v_ids_1000.toArray(new Integer[0]));
                t_v_ids_1000.clear();
            }

            if (sp_ids_1000.size() > 0) {
                sp_ids.add(sp_ids_1000.toArray(new Integer[0]));
                sp_ids_1000.clear();
            }

            //没有表和视图时直接返回数据
            if (t_v_ids.size() == 0)
                return tables;

            //用来在sql语句中筛选出当前操作涉及的表、视图、存储过程
            IFunc1<List<Integer[]>, String> getMatcherSql = idList -> {
                if (idList.size() == 0)
                    return "";
                StringBuilder tablesMatcherSB = new StringBuilder().append("(");
                for (int i = 0; i < idList.size(); i++) {
                    if (i > 0) tablesMatcherSB.append(" or ");
                    tablesMatcherSB.append("a.object_id in (");

                    for (int j = 0; j < idList.get(i).length; j++) {
                        if (j > 0) tablesMatcherSB.append(",");
                        tablesMatcherSB.append(String.format("'%s'",
                                                             idList.get(i)[j]));
                    }
                    tablesMatcherSB.append(")");
                }
                tablesMatcherSB.append(")");
                return tablesMatcherSB.toString();
            };

            String t_v_Matcher = getMatcherSql.invoke(t_v_ids);
            String sp_Matcher = getMatcherSql.invoke(sp_ids);

            //查询列信息
            dataMap = selectColumnInfo(currentDatabase,
                                       db,
                                       t_v_Matcher,
                                       sp_Matcher);

            if (dataMap == null)
                return tables;

            //列的位置
            int position = 0;
            for (Map<String, Object> d : dataMap) {
//                //表名
//                String table_id = String.valueOf(d.get("F1"));
                //对象标识
                int object_id = Integer.parseInt(String.valueOf(d.get("F2")));
                //列名
                String column = String.valueOf(d.get("F3"));
                //数据类型
                String type = String.valueOf(d.get("F4"));
                //数据长度
                Tuple2<Boolean, Integer> length_result = NumericExtension.tryParseInt(String.valueOf(d.get("F5")));
                int max_length = length_result.a
                                 ? length_result.b
                                 : 0;
                //如果数据的最大长度为0，则判定为可变长度的数据类型（TEXT, CLOB......）
                if (max_length == 0)
                    max_length = -1;
                //建表时的完整类型名称
                String sqlType = String.valueOf(d.get("F6"));
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

                //注释
                String comment = String.valueOf(d.get("F7"));
                //是否可为空
                boolean is_nullable = String.valueOf(d.get("F8"))
                                            .equals("1");
                //是否自增
                boolean is_identity = String.valueOf(d.get("F9"))
                                            .equals("1");
                //默认值
                String defaultValue = String.valueOf(d.get("F10"));

                //添加列信息
                tableWithColumns_cache.get(object_id)
                                      .put(column,
                                           new DbColumnInfo(tableWithIds_cache.get(object_id),
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

                tableWithColumns_cache.get(object_id)
                                      .get(column)
                                      .setJdbcType(getJDBCType(tableWithColumns_cache.get(object_id)
                                                                                     .get(column)));
                tableWithColumns_cache.get(object_id)
                                      .get(column)
                                      .setJavaType(getJavaTypeInfo(tableWithColumns_cache.get(object_id)
                                                                                         .get(column)));
            }

            //查询索引/唯一键信息
            dataMap = selectIndexInfo(currentDatabase,
                                      db,
                                      t_v_Matcher);

            if (dataMap == null)
                return tables;

            //索引列
            Map<Integer, Map<String, DbIndexInfo>> indexColumns = new HashMap<>();
            //唯一键列
            Map<Integer, Map<String, DbIndexInfo>> uniqueColumns = new HashMap<>();

            for (Map<String, Object> d : dataMap) {
                //对象标识
                int object_id = Integer.parseInt(String.valueOf(d.get("F1")));
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
//                //是否聚簇索引
//                boolean is_clustered = String.valueOf(d.get("F6"))
//                                             .equals("1");
                //是否降序
                boolean is_desc = String.valueOf(d.get("F7"))
                                        .equals("1");

                if (!tableWithColumns_cache.containsKey(object_id)
                        || !tableWithColumns_cache.get(object_id)
                                                  .containsKey(column))
                    continue;

                //获取之前查询到的列信息
                DbColumnInfo columnInfo = tableWithColumns_cache.get(object_id)
                                                                .get(column);
                //将列标识为主键
                if (!columnInfo.isPrimary() && is_primary_key)
                    columnInfo.setPrimary(true);

                //添加索引信息
                Map<String, DbIndexInfo> indexes = new HashMap<>();
                DbIndexInfo index = new DbIndexInfo(index_id,
                                                    is_unique);

                if (!indexColumns.containsKey(object_id))
                    indexColumns.put(object_id,
                                     indexes);
                else
                    indexes = indexColumns.get(object_id);

                if (!indexes.containsKey(index_id))
                    indexes.put(index_id,
                                index);

                //添加索引相关的列信息
                index.getColumns()
                     .add(new DbIndexColumnInfo(columnInfo,
                                                is_desc));

                //添加唯一列信息
                if (is_unique && !is_primary_key) {
                    indexes = new HashMap<>();
                    index = new DbIndexInfo(index_id,
                                            true);

                    if (!uniqueColumns.containsKey(object_id))
                        uniqueColumns.put(object_id,
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

            for (Integer object_id : indexColumns.keySet()) {
                for (String name : indexColumns.get(object_id)
                                               .keySet()) {
                    tableWithIds_cache.get(object_id)
                                      .getIndexesDict()
                                      .put(name,
                                           indexColumns.get(object_id)
                                                       .get(name));
                }
            }

            for (Integer object_id : uniqueColumns.keySet()) {
                for (String name : uniqueColumns.get(object_id)
                                                .keySet()) {
                    //重新排序
                    uniqueColumns.get(object_id)
                                 .get(name)
                                 .getColumns()
                                 .sort(Comparator.comparing(c -> c.getColumn()
                                                                  .getName()));

                    tableWithIds_cache.get(object_id)
                                      .getUniquesDict()
                                      .put(name,
                                           uniqueColumns.get(object_id)
                                                        .get(name));
                }
            }

            //如果未指定数据库，则查询外键信息
            if (tbname == null) {
                dataMap = selectForeignInfo(currentDatabase,
                                            db,
                                            t_v_Matcher);

                if (dataMap == null)
                    return tables;

                //外键列
                Map<Integer, Map<String, DbForeignInfo>> fkColumns = new HashMap<>();
                for (Map<String, Object> d : dataMap) {
                    //对象标识
                    int object_id;
                    Tuple2<Boolean, Integer> object_id_result = NumericExtension.tryParseInt(String.valueOf(d.get("F1")));
                    object_id = object_id_result.a
                                ? object_id_result.b
                                : 0;
                    //列名
                    String column = String.valueOf(d.get("F2"))
                                          .trim();
                    //外键名
                    String fk_id = String.valueOf(d.get("F3"));
                    //关联对象标识
                    int referenced_object_id;
                    Tuple2<Boolean, Integer> referenced_object_id_result = NumericExtension.tryParseInt(String.valueOf(d.get("F4")));
                    referenced_object_id = referenced_object_id_result.a
                                           ? referenced_object_id_result.b
                                           : 0;
//                    //是否为外键
//                    boolean is_foreign_key = String.valueOf(d.get("F5"))
//                                                   .equals("1");
                    //关联列名
                    String referenced_column = String.valueOf(d.get("F6"));
//                    //关联数据库名
//                    String referenced_db = String.valueOf(d.get("F7"));
//                    //关联表名
//                    String referenced_table = String.valueOf(d.get("F8"));

                    if (!tableWithColumns_cache.containsKey(object_id)
                            || !tableWithColumns_cache.get(object_id)
                                                      .containsKey(column))
                        continue;

                    //获取之前查询到的列信息
                    DbColumnInfo columnInfo = tableWithColumns_cache.get(object_id)
                                                                    .get(column);
                    //关联表信息
                    DbTableInfo ref_table = null;
                    //关联列信息
                    DbColumnInfo ref_columnInfo = null;
                    if (tableWithIds_cache.containsKey(referenced_object_id)) {
                        ref_table = tableWithIds_cache.get(referenced_object_id);
                        ref_columnInfo = tableWithColumns_cache.get(referenced_object_id)
                                                               .get(referenced_column);
                    }

                    //添加外键信息
                    Map<String, DbForeignInfo> foreigns = new HashMap<>();
                    DbForeignInfo foreign = new DbForeignInfo(tableWithIds_cache.get(object_id),
                                                              ref_table);
                    if (!fkColumns.containsKey(object_id))
                        fkColumns.put(object_id,
                                      foreigns);
                    else
                        foreigns = fkColumns.get(object_id);

                    if (!foreigns.containsKey(fk_id))
                        foreigns.put(fk_id,
                                     foreign);

                    //添加外键相关的列信息
                    foreign.getColumns()
                           .add(columnInfo);
                    foreign.getReferencedColumns()
                           .add(ref_columnInfo);
                }

                for (Integer object_id : fkColumns.keySet()) {
                    for (String name : fkColumns.get(object_id)
                                                .keySet()) {
                        tableWithIds_cache.get(object_id)
                                          .getIndexesDict()
                                          .put(name,
                                               indexColumns.get(object_id)
                                                           .get(name));
                    }
                }
            }

            for (Integer object_id : tableWithColumns_cache.keySet()) {
                for (DbColumnInfo column : tableWithColumns_cache.get(object_id)
                                                                 .values()) {
                    tableWithIds_cache.get(object_id)
                                      .getColumns()
                                      .add(column);
                    if (column.isIdentity())
                        tableWithIds_cache.get(object_id)
                                          .getIdentities()
                                          .add(column);
                    if (column.isPrimary())
                        tableWithIds_cache.get(object_id)
                                          .getPrimaries()
                                          .add(column);
                }
            }

            //列排序
            for (DbTableInfo table : tableWithIds_cache.values()) {
                tables.add(sort(table));
            }

            //清理临时存储的数据
            tableWithIds_cache.clear();
            tableWithColumns_cache.clear();
            allTables.addAll(tables);
        }

        //所有表信息按名称（升序）排序
        allTables.sort(Comparator.comparing(DbTableInfo::getSchema)
                                 .thenComparing(DbTableInfo::getName));

        return allTables;
    }

    @Override
    public List<String> getDatabases()
            throws
            ModuleException {
        String sql = " select name from sys.databases where name not in ('master','tempdb','model','msdb')";
        return this.ado.selectList(getSqlSession(),
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

        //获取当前数据库名称
        String currentDatabase = getDatabase(ignoreCase);

        if (tbname.length == 1)
            tbname = new String[]{currentDatabase,
                                  "dbo",
                                  tbname[0]};
        if (tbname.length == 2)
            tbname = new String[]{currentDatabase,
                                  tbname[0],
                                  tbname[1]};

        String sql = String.format(" use [%s];"
                                           + "select case when count(1) > 0 then 1 else 0 end from sys.tables a "
                                           + "inner join sys.schemas b on b.schema_id = a.schema_id "
                                           + "where %s%s and %s%s;"
                                           + "use [%s];",
                                   tbname[0],
                                   ignoreCase
                                   ? "lower(b.name)"
                                   : "b.name",
                                   StringUtils.hasText(tbname[1])
                                   ? String.format("='%s'",
                                                   tbname[1])
                                   : " is null",
                                   ignoreCase
                                   ? "lower(a.name)"
                                   : "a.name",
                                   StringUtils.hasText(tbname[2])
                                   ? String.format("='%s'",
                                                   tbname[2])
                                   : " is null",
                                   currentDatabase);

        return this.ado.selectOne(getSqlSession(),
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
        String dbTypeText = column.getDbTypeText() == null
                            ? ""
                            : column.getDbTypeText()
                                    .toLowerCase(Locale.ROOT);
        switch (dbTypeText) {
            case "bit":
                return JDBCType.BIT;
            case "tinyint":
                return JDBCType.TINYINT;
            case "smallint":
                return JDBCType.SMALLINT;
            case "int":
                return JDBCType.INTEGER;
            case "bigint":
                return JDBCType.BIGINT;
            case "numeric":
            case "decimal":
            case "money":
            case "smallmoney":
                return JDBCType.DECIMAL;
            case "real":
                return JDBCType.FLOAT;
            case "float":
                return JDBCType.DOUBLE;
            case "date":
                return JDBCType.DATE;
            case "time":
                return JDBCType.TIME;
            case "datetime":
            case "datetime2":
            case "datetimeoffset":
            case "smalldatetime":
                return JDBCType.TIMESTAMP;
            case "binary":
            case "varbinary":
            case "image":
            case "timestamp":
            case "udt":
                return JDBCType.VARBINARY;
            case "text":
                return JDBCType.CLOB;
            case "ntext":
                return JDBCType.NCLOB;
            case "char":
                return JDBCType.CHAR;
            case "nchar":
                return JDBCType.NCHAR;
            case "nvarchar":
                return JDBCType.NVARCHAR;
            case "varchar":
            case "uniqueidentifier":
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
