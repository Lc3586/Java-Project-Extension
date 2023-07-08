package project.extension.mybatis.edge.core.provider.postgresql;

import com.alibaba.druid.pool.DruidDataSourceFactory;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
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
import sun.security.util.BitArray;

import java.math.BigDecimal;
import java.sql.JDBCType;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * PostgreSQL DbFirst 开发模式相关功能
 *
 * @author LCTR
 * @date 2023-02-21
 */
public class PostgreSQLDbFirst
        extends DbFirst {
    public PostgreSQLDbFirst(DataSourceConfig config,
                             INaiveAdo ado) {
        super(config,
              ado);
        initialization();

        //TODO 待实现
    }

    /**
     * 初始化
     */
    private void initialization() {
        //是否已初始化
        if (dbToJavaMap.size() > 0)
            return;

        dbToJavaMap.putIfAbsent("boolean",
                                new DbTypeToJavaType("(boolean)",
                                                     "(Boolean)",
                                                     "Boolean.parseBoolean(%s)",
                                                     "Boolean.toString(%s)",
                                                     "boolean",
                                                     "Boolean",
                                                     boolean.class,
                                                     Boolean.class));

        dbToJavaMap.putIfAbsent("boolean[]",
                                new DbTypeToJavaType("(boolean[])",
                                                     "(Boolean[])",
                                                     "JSONObject.parseObject(%s, boolean[].class)",
                                                     "JSON.toJSONString(%s)",
                                                     "boolean[]",
                                                     "Boolean[]",
                                                     boolean[].class,
                                                     Boolean[].class));

        dbToJavaMap.putIfAbsent("smallint",
                                new DbTypeToJavaType("(short)",
                                                     "(Short)",
                                                     "Short.parseShort(%s)",
                                                     "Short.toString(%s)",
                                                     "short",
                                                     "Short",
                                                     short.class,
                                                     Short.class));

        dbToJavaMap.putIfAbsent("smallint[]",
                                new DbTypeToJavaType("(short[])",
                                                     "(Short[])",
                                                     "JSONObject.parseObject(%s, short[].class)",
                                                     "JSON.toJSONString(%s)",
                                                     "short[]",
                                                     "Short[]",
                                                     short[].class,
                                                     Short[].class));

        dbToJavaMap.putIfAbsent("integer",
                                new DbTypeToJavaType("(int)",
                                                     "(Integer)",
                                                     "Integer.parseInt(%s)",
                                                     "Integer.toString(%s)",
                                                     "int",
                                                     "Integer",
                                                     int.class,
                                                     Integer.class));

        dbToJavaMap.putIfAbsent("integer[]",
                                new DbTypeToJavaType("(int[])",
                                                     "(Integer[])",
                                                     "JSONObject.parseObject(%s, int[].class)",
                                                     "JSON.toJSONString(%s)",
                                                     "int[]",
                                                     "Integer[]",
                                                     int[].class,
                                                     Integer[].class));

        dbToJavaMap.putIfAbsent("int4range",
                                new DbTypeToJavaType("(int[])",
                                                     "(Integer[])",
                                                     "JSONObject.parseObject(%s, int[].class)",
                                                     "JSON.toJSONString(%s)",
                                                     "int[]",
                                                     "Integer[]",
                                                     int[].class,
                                                     Integer[].class));

        dbToJavaMap.putIfAbsent("int4range[]",
                                new DbTypeToJavaType("(int[][])",
                                                     "(Integer[][])",
                                                     "JSONObject.parseObject(%s, int[].class)",
                                                     "JSON.toJSONString(%s)",
                                                     "int[][]",
                                                     "Integer[][]",
                                                     int[][].class,
                                                     Integer[][].class));

        dbToJavaMap.putIfAbsent("bigint",
                                new DbTypeToJavaType("(long)",
                                                     "(Long)",
                                                     "Long.parseLong(%s)",
                                                     "Long.toString(%s)",
                                                     "long",
                                                     "Long",
                                                     long.class,
                                                     Long.class));

        dbToJavaMap.putIfAbsent("bigint[]",
                                new DbTypeToJavaType("(long[])",
                                                     "(Long[])",
                                                     "JSONObject.parseObject(%s, long[].class)",
                                                     "JSON.toJSONString(%s)",
                                                     "long[]",
                                                     "Long[]",
                                                     long[].class,
                                                     Long[].class));

        dbToJavaMap.putIfAbsent("int8range",
                                new DbTypeToJavaType("(long[])",
                                                     "(Long[])",
                                                     "JSONObject.parseObject(%s, long[].class)",
                                                     "JSON.toJSONString(%s)",
                                                     "long[]",
                                                     "Long[]",
                                                     long[].class,
                                                     Long[].class));

        dbToJavaMap.putIfAbsent("int8range[]",
                                new DbTypeToJavaType("(long[][])",
                                                     "(Long[][])",
                                                     "JSONObject.parseObject(%s, long[].class)",
                                                     "JSON.toJSONString(%s)",
                                                     "long[][]",
                                                     "Long[][]",
                                                     long[][].class,
                                                     Long[][].class));

        dbToJavaMap.putIfAbsent("real",
                                new DbTypeToJavaType("(float)",
                                                     "(Float)",
                                                     "Float.parseFloat(%s)",
                                                     "Float.toString(%s)",
                                                     "float",
                                                     "Float",
                                                     float.class,
                                                     Float.class));

        dbToJavaMap.putIfAbsent("real[]",
                                new DbTypeToJavaType("(float[])",
                                                     "(Float[])",
                                                     "JSONObject.parseObject(%s, float[].class)",
                                                     "JSON.toJSONString(%s)",
                                                     "float",
                                                     "Float",
                                                     float[].class,
                                                     Float[].class));
        dbToJavaMap.putIfAbsent("double precision",
                                new DbTypeToJavaType("(double)",
                                                     "(Double)",
                                                     "Double.parseDouble(%s)",
                                                     "Double.toString(%s)",
                                                     "double",
                                                     "Double",
                                                     double.class,
                                                     Double.class));
        dbToJavaMap.putIfAbsent("double precision[]",
                                new DbTypeToJavaType("(double[])",
                                                     "(Double[])",
                                                     "JSONObject.parseObject(%s, double[].class)",
                                                     "JSON.toJSONString(%s)",
                                                     "double[]",
                                                     "Double[]",
                                                     double[].class,
                                                     Double[].class));
        dbToJavaMap.putIfAbsent("numeric",
                                new DbTypeToJavaType("(BigDecimal)",
                                                     "(BigDecimal)",
                                                     "new BigDecimal(%s)",
                                                     "%s.toString()",
                                                     "BigDecimal",
                                                     "BigDecimal",
                                                     BigDecimal.class,
                                                     BigDecimal.class));
        dbToJavaMap.putIfAbsent("numeric[]",
                                new DbTypeToJavaType("(BigDecimal[])",
                                                     "(BigDecimal[])",
                                                     "JSONObject.parseObject(%s, BigDecimal[].class)",
                                                     "JSON.toJSONString(%s)",
                                                     "BigDecimal[]",
                                                     "BigDecimal[]",
                                                     BigDecimal[].class,
                                                     BigDecimal[].class));
        dbToJavaMap.putIfAbsent("numrange",
                                new DbTypeToJavaType("(BigDecimal[])",
                                                     "(BigDecimal[])",
                                                     "JSONObject.parseObject(%s, BigDecimal[].class)",
                                                     "JSON.toJSONString(%s)",
                                                     "BigDecimal[]",
                                                     "BigDecimal[]",
                                                     BigDecimal[].class,
                                                     BigDecimal[].class));
        dbToJavaMap.putIfAbsent("numrange[]",
                                new DbTypeToJavaType("(BigDecimal[][])",
                                                     "(BigDecimal[][])",
                                                     "JSONObject.parseObject(%s, BigDecimal[].class)",
                                                     "JSON.toJSONString(%s)",
                                                     "BigDecimal[][]",
                                                     "BigDecimal[][]",
                                                     BigDecimal[][].class,
                                                     BigDecimal[][].class));

        dbToJavaMap.putIfAbsent("date",
                                new DbTypeToJavaType("(java.sql.Date)",
                                                     "(java.sql.Date)",
                                                     "new java.sql.Date(Long.parseLong(%s))",
                                                     "Long.toString(%s.getTime())",
                                                     "java.sql.Date",
                                                     "java.sql.Date",
                                                     java.sql.Date.class,
                                                     java.sql.Date.class));
        dbToJavaMap.putIfAbsent("date[]",
                                new DbTypeToJavaType("(java.sql.Date[])",
                                                     "(java.sql.Date[])",
                                                     "JSONObject.parseObject(%s, java.sql.Date[].class)",
                                                     "JSON.toJSONString(%s)",
                                                     "java.sql.Date[]",
                                                     "java.sql.Date[]",
                                                     java.sql.Date[].class,
                                                     java.sql.Date[].class));
        dbToJavaMap.putIfAbsent("daterange",
                                new DbTypeToJavaType("(java.sql.Date[])",
                                                     "(java.sql.Date[])",
                                                     "JSONObject.parseObject(%s, java.sql.Date[].class)",
                                                     "JSON.toJSONString(%s)",
                                                     "java.sql.Date[]",
                                                     "java.sql.Date[]",
                                                     java.sql.Date[].class,
                                                     java.sql.Date[].class));
        dbToJavaMap.putIfAbsent("daterange[]",
                                new DbTypeToJavaType("(java.sql.Date[][])",
                                                     "(java.sql.Date[][])",
                                                     "JSONObject.parseObject(%s, java.sql.Date[].class)",
                                                     "JSON.toJSONString(%s)",
                                                     "java.sql.Date[][]",
                                                     "java.sql.Date[][]",
                                                     java.sql.Date[][].class,
                                                     java.sql.Date[][].class));
        dbToJavaMap.putIfAbsent("time",
                                new DbTypeToJavaType("(java.sql.Time)",
                                                     "(java.sql.Time)",
                                                     "new java.sql.Time(Long.parseLong(%s))",
                                                     "Long.toString(%s.getTime())",
                                                     "java.sql.Time",
                                                     "java.sql.Time",
                                                     java.sql.Time.class,
                                                     java.sql.Time.class));
        dbToJavaMap.putIfAbsent("time[]",
                                new DbTypeToJavaType("(java.sql.Time[])",
                                                     "(java.sql.Time[])",
                                                     "JSONObject.parseObject(%s, java.sql.Time[].class)",
                                                     "JSON.toJSONString(%s)",
                                                     "java.sql.Time[]",
                                                     "java.sql.Time[]",
                                                     java.sql.Time[].class,
                                                     java.sql.Time[].class));
        dbToJavaMap.putIfAbsent("timestamp",
                                new DbTypeToJavaType("(Date)",
                                                     "(Date)",
                                                     "new Date(Long.parseLong(%s))",
                                                     "Long.toString(%s.getTime())",
                                                     "Date",
                                                     "Date",
                                                     Date.class,
                                                     Date.class));
        dbToJavaMap.putIfAbsent("timestamp[]",
                                new DbTypeToJavaType("(Date[])",
                                                     "(Date[])",
                                                     "JSONObject.parseObject(%s, Date[].class)",
                                                     "JSON.toJSONString(%s)",
                                                     "Date[]",
                                                     "Date[]",
                                                     Date[].class,
                                                     Date[].class));

        dbToJavaMap.putIfAbsent("bytea",
                                new DbTypeToJavaType("(byte[])",
                                                     "(byte[])",
                                                     "Base64.getDecoder().decode(%s.getBytes(StandardCharsets.UTF_8))",
                                                     "new String(Base64.getEncoder().encode(%s), StandardCharsets.UTF_8)",
                                                     "byte[]",
                                                     "byte[]",
                                                     byte[].class,
                                                     byte[].class));

        dbToJavaMap.putIfAbsent("bit",
                                new DbTypeToJavaType("(BitArray)",
                                                     "(BitArray)",
                                                     "StringExtension.ToBitArray(%s)",
                                                     "StringExtension.ToBitString(%s)",
                                                     "BitArray",
                                                     "BitArray",
                                                     BitArray.class,
                                                     BitArray.class));

        dbToJavaMap.putIfAbsent("json",
                                new DbTypeToJavaType("(JSONObject)",
                                                     "(JSONObject)",
                                                     "JSONObject.parseObject(%s)",
                                                     "JSON.toJSONString(%s)",
                                                     "BitArray",
                                                     "BitArray",
                                                     JSONObject.class,
                                                     JSONObject.class));
        dbToJavaMap.putIfAbsent("json[]",
                                new DbTypeToJavaType("(JSONArray)",
                                                     "(JSONArray)",
                                                     "JSONArray.parseArray(%s)",
                                                     "JSONArray.toJSONString(%s)",
                                                     "BitArray",
                                                     "BitArray",
                                                     JSONArray.class,
                                                     JSONArray.class));

        dbToJavaMap.putIfAbsent("char",
                                new DbTypeToJavaType("(char)",
                                                     "(Character)",
                                                     "%s.charAt(0)",
                                                     "Character.toString(%s)",
                                                     "char",
                                                     "Character",
                                                     char.class,
                                                     Character.class));

        dbToJavaMap.putIfAbsent("char[]",
                                new DbTypeToJavaType("(char[])",
                                                     "(Character[])",
                                                     "JSONObject.parseObject(%s, Character[].class)",
                                                     "JSON.toJSONString(%s)",
                                                     "char",
                                                     "Character",
                                                     char[].class,
                                                     Character[].class));

        dbToJavaMap.putIfAbsent("character varying",
                                new DbTypeToJavaType("(String)",
                                                     "(String)",
                                                     "%s",
                                                     "%s",
                                                     "String",
                                                     "String",
                                                     String.class,
                                                     String.class));

        dbToJavaMap.putIfAbsent("character varying[]",
                                new DbTypeToJavaType("(String)",
                                                     "(String)",
                                                     "%s",
                                                     "%s",
                                                     "String",
                                                     "String",
                                                     String.class,
                                                     String.class));
        dbToJavaMap.putIfAbsent("uuid",
                                new DbTypeToJavaType("(UUID)",
                                                     "(UUID)",
                                                     "UUID.fromString(%s)",
                                                     "%s.toString()",
                                                     "UUID",
                                                     "UUID",
                                                     UUID.class,
                                                     UUID.class));
        dbToJavaMap.putIfAbsent("uuid[]",
                                new DbTypeToJavaType("(UUID[])",
                                                     "(UUID[])",
                                                     "JSONObject.parseObject(%s, UUID[].class)",
                                                     "JSON.toJSONString(%s)",
                                                     "UUID[]",
                                                     "UUID[]",
                                                     UUID[].class,
                                                     UUID[].class));

        dbToJavaMap.putIfAbsent("unknown",
                                new DbTypeToJavaType("(Object)",
                                                     "(Object)",
                                                     "(Object)%s",
                                                     "%s.toString()",
                                                     "Object",
                                                     "Object",
                                                     Object.class,
                                                     Object.class));
        dbToJavaMap.putIfAbsent("unknown[]",
                                new DbTypeToJavaType("(Object[])",
                                                     "(Object[])",
                                                     "JSONObject.parseObject(%s, Object[].class)",
                                                     "JSON.toJSONString(%s)",
                                                     "Object[]",
                                                     "Object[]",
                                                     Object[].class,
                                                     Object[].class));
    }

    /**
     * 检查并设置映射表
     *
     * @param column 列信息
     */
    private void checkAndSetupJavaMap(DbColumnInfo column) {
        if (dbToJavaMap.containsKey(column.getDbTypeTextFull()))
            return;

        String dbTypeText = column.getDbTypeText();
        dbTypeText = dbTypeText == null
                     ? ""
                     : dbTypeText.toLowerCase(Locale.ROOT);

        boolean isArray = dbTypeText.endsWith("[]");

        if (isArray) dbTypeText = dbTypeText.substring(0,
                                                       dbTypeText.length() - 2);

        String copyDbType;

        switch (dbTypeText) {
            case "boolean":
                copyDbType = "boolean";
                break;
            case "smallint":
            case "smallserial":
                copyDbType = "smallint";
                break;
            case "integer":
            case "serial":
                copyDbType = "integer";
                break;
            case "int4range":
                copyDbType = "int4range";
                break;
            case "bigint":
            case "bigserial":
                copyDbType = "bigint";
                break;
            case "int8range":
                copyDbType = "int8range";
                break;
            case "real":
                copyDbType = "real";
                break;
            case "double precision":
                copyDbType = "double precision";
                break;
            case "numeric":
            case "money":
                copyDbType = "numeric";
                break;
            case "numrange":
                copyDbType = "numrange";
                break;
            case "date":
                copyDbType = "date";
                break;
            case "daterange":
                copyDbType = "daterange";
                break;
            case "time":
            case "time with time zone":
            case "time without time zone":
                copyDbType = "time";
                break;
            case "timestamp":
            case "timestamp with time zone":
            case "timestamp without time zone":
                copyDbType = "timestamp";
                break;
            case "bytea":
                copyDbType = "bytea";
                break;
            case "bit":
            case "bit varying":
                copyDbType = "bit";
                break;
            case "char":
                copyDbType = "char";
                break;
            case "varchar":
            case "character":
            case "character varying":
            case "text":
            case "name":
            case "cidr":
            case "inet":
            case "macaddr":
            case "macaddr8":
                copyDbType = "character varying";
                break;
            case "json":
            case "jsonb":
                copyDbType = "json";
                break;
            case "uuid":
                copyDbType = "uuid";
                break;
            default:
                copyDbType = "unknown";
                break;
        }

        copyAndPutDbToJavaMap(column.getDbTypeTextFull(),
                              String.format("%s%s",
                                            copyDbType,
                                            isArray
                                            ? "[]"
                                            : ""));
    }

    /**
     * 查询表信息
     *
     * @param tbName     表名
     * @param ignoreCase 忽略大小写
     * @return 表信息
     */
    private List<Map<String, Object>> selectTableInfo(String[] tbName,
                                                      boolean ignoreCase)
            throws
            ModuleException {
        String sql = String.format("%s select \r\n"
                                           + "b.nspname || '.' || a.tablename as \"F1\",\r\n"
                                           + "a.schemaname as \"F2\",\r\n"
                                           + "a.tablename as \"F3\",\r\n"
                                           + "d.description as \"F4\",\r\n"
                                           + "'TABLE' as \"F5\" \r\n"
                                           + "from pg_tables a \r\n"
                                           + "inner join pg_namespace b on b.nspname = a.schemaname \r\n"
                                           + "inner join pg_class c on c.relnamespace = b.oid and c.relname = a.tablename \r\n"
                                           + "left join pg_description d on d.objoid = c.oid and objsubid = 0 \r\n"
                                           + "where a.schemaname not in ('pg_catalog', 'information_schema', 'topology') \r\n"
                                           + "and b.nspname || '.' || a.tablename not in ('public.spatial_ref_sys') \r\n"
                                           + "union all \r\n"
                                           + "select \r\n"
                                           + "b.nspname || '.' || a.relname, \r\n"
                                           + "b.nspname, \r\n"
                                           + "a.relname, \r\n"
                                           + "d.description, \r\n"
                                           + "'VIEW' \r\n"
                                           + "from pg_class a \r\n"
                                           + "inner join pg_namespace b on b.oid = a.relnamespace \r\n"
                                           + "left join pg_description d on d.objoid = a.oid and objsubid = 0 \r\n"
                                           + "where b.nspname not in ('pg_catalog', 'information_schema') and a.relkind in ('m','v')  \r\n"
                                           + "and b.nspname || '.' || a.relname not in ('public.geography_columns','public.geometry_columns','public.raster_columns','public.raster_overviews') \r\n"
                                           + "%s \r\n",
                                   tbName == null
                                   ? ""
                                   : "select * from (",
                                   tbName == null
                                   ? ""
                                   : String.format(") ft_dbf where %s%s and %s%s",
                                                   ignoreCase
                                                   ? "lower(schemaname) "
                                                   : "schemaname ",
                                                   StringUtils.hasText(tbName[0])
                                                   ? String.format("='%s'",
                                                                   tbName[0])
                                                   : " is null",
                                                   ignoreCase
                                                   ? "lower(tablename) "
                                                   : "tablename ",
                                                   StringUtils.hasText(tbName[1])
                                                   ? String.format("='%s'",
                                                                   tbName[1])
                                                   : " is null"));

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
                                           + "ns.nspname || '.' || c.relname as \"F1\",\r\n"
                                           + "cc.column_name as \"F2\",\r\n"
                                           + "cc.data_type || '.' || t.typname as \"F3\",\r\n"
                                           + "case when a.atttypmod > 0 and a.atttypmod < 32767 then a.atttypmod - 4 else a.attlen end as \"F4\",\r\n"
                                           + "cc.numeric_precision as \"F5\",\r\n"
                                           + "cc.numeric_scale as \"F6\",\r\n"
                                           + "null as \"F7\",\r\n"
                                           + "case cc.is_nullable when 'NO' then 0 else 1 end as \"F8\",\r\n"
                                           + "case cc.is_identity when 'NO' then 0 else 1 end as \"F9\",\r\n"
                                           + "d.description as \"F10\",\r\n"
                                           + "cc.column_default as \"F11\" \r\n"
                                           + "from pg_class c \r\n"
                                           + "inner join pg_attribute a on a.attnum > 0 and a.attrelid = c.oid \r\n"
                                           + "left join information_schema.columns cc on cc.column_name = a.attname \r\n"
                                           + "left join pg_constraint const on const.conrelid = c.oid and const.contype = 'p' and a.attnum = const.conkey[1] \r\n"
                                           + "inner join pg_type t on t.oid = a.atttypid \r\n"
                                           + "left join pg_type t2 on t2.oid = t.typelem \r\n"
                                           + "left join pg_description d on d.objoid = a.attrelid and d.objsubid = a.attnum \r\n"
                                           + "left join pg_attrdef e on e.adrelid = a.attrelid and e.adnum = a.attnum \r\n"
                                           + "inner join pg_namespace ns on ns.oid = c.relnamespace \r\n"
                                           + "inner join pg_namespace ns2 on ns2.oid = t.typnamespace \r\n"
                                           + "where %s in (%s) \r\n"
                                           + "and %s",
                                   ignoreCase
                                   ? "lower(cc.table_catalog) "
                                   : "cc.table_catalog ",
                                   inDatabase,
                                   tablesMatcher);

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
     * @param tablesMatcher 用来匹配表名的sql语句
     * @return 索引/唯一键信息
     */
    private List<Map<String, Object>> selectIndexInfo(String tablesMatcher)
            throws
            ModuleException {
        String sql = String.format("select\n"
                                           + "ns.nspname || '.' || c.relname as table_id as \"F1\", \r\n"
                                           + "attr.attname as \"F2\", \r\n"
                                           + "b.relname as \"F3\", \r\n"
                                           + "case when a.indisunique then 1 else 0 end as \"F4\", \r\n"
                                           + "case when a.indisprimary then 1 else 0 end as \"F5\", \r\n"
                                           + "case when a.indisclustered then 0 else 1 end as \"F6\", \r\n"
                                           + "case when pg_index_column_has_property(b.oid, attr.attnum, 'desc') = 't' then 1 else 0 end as \"F7\", \r\n"
                                           + "a.indkey::text as \"F8\", \r\n"
                                           + "attr.attnum as \"F9\" \r\n"
                                           + "from pg_index a\n"
                                           + "inner join pg_class b on b.oid = a.indexrelid\n"
                                           + "inner join pg_attribute attr on attr.attnum > 0 and attr.attrelid = b.oid\n"
                                           + "inner join pg_namespace ns on ns.oid = b.relnamespace\n"
                                           + "inner join pg_class c on c.oid = a.indrelid \r\n"
                                           + "where %s ",
                                   tablesMatcher);

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
     * @param tablesMatcher 用来匹配表名的sql语句
     * @param ignoreCase    忽略大小写
     * @return 外键信息
     */
    private List<Map<String, Object>> selectForeignInfo(String tablesMatcher,
                                                        boolean ignoreCase)
            throws
            ModuleException {
        String sql = String.format("select \r\n"
                                           + "ns.nspname || '.' || c.relname as \"F1\", \r\n"
                                           + "array(select attname from pg_attribute where attrelid = a.conrelid and attnum = any(a.conkey)) as \"F2\", \r\n"
                                           + "a.conname as \"F3\", \r\n"
                                           + "ns2.nspname || '.' || b.relname as \"F4\", \r\n"
                                           + "1 as \"F5\", \r\n"
                                           + "array(select attname from pg_attribute where attrelid = a.confrelid and attnum = any(a.confkey)) as \"F6\" \r\n"
                                           + "from  pg_constraint a \r\n"
                                           + "inner join pg_class c on c.oid = a.conrelid \r\n"
                                           + "inner join pg_class b on b.oid = a.confrelid \r\n"
                                           + "inner join pg_namespace ns on ns.oid = c.relnamespace \r\n"
                                           + "inner join pg_namespace ns2 on ns2.oid = b.relnamespace \r\n"
                                           + "where %s ",
                                   tablesMatcher);

        return this.ado.selectMapList(getSqlSession(),
                                      getMSId(),
                                      sql,
                                      null,
                                      null,
                                      null,
                                      null,
                                      config.getNameConvertType());
    }

//    /**
//     * 获取用户标识
//     *
//     * @param lower 转小写
//     * @return 用户标识
//     */
//    private String getUserId(boolean lower)
//            throws
//            ModuleException {
//        String userId = this.config.getProperties()
//                                   .getProperty(DruidDataSourceFactory.PROP_USERNAME);
//        if (lower)
//            return userId.toLowerCase(Locale.ROOT);
//        else
//            return userId;
//    }

    /**
     * 从数据库查询模式名
     *
     * @param lower 转小写
     * @return 模式名
     */
    private String getSchemaNameFromDatabase(boolean lower)
            throws
            ModuleException {
        //从数据库中查询
        String sql = " select \"schema_name\" from information_schema.schemata where \"schema_owner\"=(select current_user)";
        String userId = this.ado.selectOne(getSqlSession(),
                                           getMSId(),
                                           sql,
                                           null,
                                           null,
                                           String.class,
                                           null,
                                           null,
                                           config.getNameConvertType());
        if (lower)
            return userId.toLowerCase(Locale.ROOT);
        else
            return userId;
    }

    /**
     * 从连接字符串中获取数据库名
     *
     * @param lower 转小写
     * @return 数据库名
     */
    private String getDatabaseFromConnectionString(boolean lower)
            throws
            ModuleException {
        Matcher matcher = Pattern.compile("jdbc:postgresql://(.*?)/(.*?)\\?",
                                          Pattern.CASE_INSENSITIVE)
                                 .matcher(config.getProperties()
                                                .getProperty(DruidDataSourceFactory.PROP_URL));
        if (!matcher.find())
            throw new ModuleException(Strings.getCanNotGetDbNameFromUrl("jdbc:postgresql://(.*?)/(.*?)\\?"));

        if (lower)
            return matcher.group(2)
                          .toLowerCase(Locale.ROOT);
        else
            return matcher.group(2);
    }

    /**
     * 获取建表时的数据库数据类型
     */
    private String getSqlTypeName(String dataType,
                                  String pgType) {
        if (dataType.equals("array")) {
            switch (pgType) {
                case "_bool":
                    pgType = "boolean";
                    break;
                case "_int2":
                    pgType = "smallint";
                    break;
                case "_int4":
                    pgType = "integer";
                    break;
                case "_int8":
                    pgType = "bigint";
                    break;
                case "_float4":
                    pgType = "real";
                    break;
                case "_float8":
                    pgType = "double precision";
                    break;
                case "_char":
                    pgType = "\"char\"";
                    break;
                case "_bpchar":
                    pgType = "character";
                    break;
                case "_varchar":
                    pgType = "character varying";
                    break;
                case "_time":
                    pgType = "time without time zone";
                    break;
                case "_timetz":
                    pgType = "time with time zone";
                    break;
                case "_timestamp":
                    pgType = "timestamp without time zone";
                    break;
                case "_timestamptz":
                    pgType = "timestamp with time zone";
                    break;
                default:
                    pgType = pgType.substring(1);
                    break;
            }

            return String.format("%s[]",
                                 pgType);
        } else if (dataType.equals("char"))
            return "\"char\"";
        else
            return dataType;
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
        String sqlType = type.toLowerCase(Locale.ROOT);

        if (sqlType.startsWith("timestamp with time zone") && length > 0)
            sqlType = String.format("timestamp(%s) with time zone",
                                    length);
        else if (sqlType.startsWith("timestamp without time zone") && length > 0)
            sqlType = String.format("timestamp(%s) without time zone",
                                    length);
        else if (sqlType.startsWith("time with time zone") && length > 0)
            sqlType = String.format("time(%s) with time zone",
                                    length);
        else if (sqlType.startsWith("time without time zone") && length > 0)
            sqlType = String.format("time(%s) without time zone",
                                    length);
        else if (precision > 0)
            sqlType += String.format("(%s, %s)",
                                     precision,
                                     scale);
        else if (length > 0)
            sqlType += String.format("(%s)",
                                     length);

        return sqlType;
    }

    @Override
    protected String[] splitTableName(String name,
                                      boolean lower) {
        String[] tbName = getSplitTableNames(name,
                                             '\"',
                                             '\"',
                                             2);

        if (tbName == null)
            return null;

        if (lower)
            for (int i = 0; i < tbName.length; i++)
                tbName[i] = tbName[i].toLowerCase(Locale.ROOT);

        return tbName;
    }

    /**
     * 获取数据库表结构信息
     *
     * @param database   数据库名
     * @param tablename  表名
     * @param ignoreCase 忽略大小写
     * @return 数据库表结构信息集合
     */
    @SuppressWarnings("DuplicatedCode")
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
        String[] tbName = null;

        //当前数据库
        String currentDatabase = getDatabaseFromConnectionString(ignoreCase);

        if (StringUtils.hasText(tablename)) {
            tbName = splitTableName(tablename,
                                    ignoreCase);
            if (tbName == null)
                throw new ModuleException(Strings.getUnknownValue("tablename",
                                                                  tablename));
            if (tbName.length == 1) {
                String schemaName = getSchemaNameFromDatabase(false);
                tbName = new String[]{schemaName,
                                      tbName[0]};
            }
            if (ignoreCase)
                tbName = new String[]{tbName[0].toLowerCase(Locale.ROOT),
                                      tbName[1].toLowerCase(Locale.ROOT)};
            database = new String[]{currentDatabase};
        }
        //如果没有指定数据库，则获取当前数据库为指定数据库
        else if (database == null || database.length == 0)
            database = new String[]{currentDatabase};

        //用来在sql语句中筛选出当前操作涉及的数据库
        String inDatabase = Arrays.stream(database)
                                  .filter(StringUtils::hasText)
                                  .map(x -> String.format("'%s'",
                                                          x))
                                  .collect(Collectors.joining(","));

        //查询表结构信息
        List<Map<String, Object>> dataMap = selectTableInfo(tbName,
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
                                       ? "lower(ns.nspname || '.' || c.relname) in ("
                                       : "ns.nspname || '.' || c.relname in (");

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
            //数据库建表时的数据类型
            String[] types = type.toLowerCase(Locale.ROOT)
                                 .split("\\.");
            type = getSqlTypeName(types[0],
                                  types[1]);

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
            String sqlType = getSqlTypeFullName(type,
                                                length,
                                                precision,
                                                scale);
            //数据的最大长度
            int max_length = length;

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

            //如果数据的最大长度为0，则判定为可变长度的数据类型（TEXT, ARRAY......）
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
        dataMap = selectIndexInfo(tablesMatcher);

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
//            //索引
//            String[] indKeys = String.valueOf(d.get("F8"))
//                                     .split(" ");
//            Tuple2<Boolean, Integer> attnum_result = NumericExtension.tryParseInt(String.valueOf(d.get("F9")));
//            int indkey = 0;
//            if (attnum_result.a) {
//                Tuple2<Boolean, Integer> indkey_result = NumericExtension.tryParseInt(indKeys[attnum_result.b - 1]);
//                if (indkey_result.a)
//                    indkey = indkey_result.b;
//            }

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
        if (tbName == null) {
            dataMap = selectForeignInfo(tablesMatcher,
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
                column = column.substring(1,
                                          column.length() - 1);
                //外键名
                String fk_id = String.valueOf(d.get("F3"));
                //关联表名
                String ref_table_id = String.valueOf(d.get("F4"));
//                //是否为外键
//                boolean is_foreign_key = String.valueOf(d.get("F5"))
//                                               .equals("1");
                //关联列名
                String ref_column = String.valueOf(d.get("F6"))
                                          .trim();
                ref_column = ref_column.substring(1,
                                                  ref_column.length() - 1);

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
                Map<String, DbForeignInfo> foreign = new HashMap<>();
                DbForeignInfo foreignInfo = new DbForeignInfo(tableWithIds_cache.get(table_id),
                                                              ref_table);
                if (!fkColumns.containsKey(table_id))
                    fkColumns.put(table_id,
                                  foreign);
                else
                    foreign = fkColumns.get(table_id);

                if (!foreign.containsKey(fk_id))
                    foreign.put(fk_id,
                                foreignInfo);

                //添加外键相关的列信息
                foreignInfo.getColumns()
                           .add(columnInfo);
                foreignInfo.getReferencedColumns()
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
        String sql = " select datname from pg_database where datname not in ('template1', 'template0')";
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
        String[] tbName = splitTableName(name,
                                         ignoreCase);
        if (tbName == null)
            throw new ModuleException(Strings.getUnknownValue("name",
                                                              name));

        //获取当前模式名，也就是用户标识
        if (tbName.length == 1) {
            String schemaName = getSchemaNameFromDatabase(ignoreCase);
            tbName = new String[]{schemaName,
                                  tbName[0]};
        }

        String sql = String.format(" select case when count(1) > 0 then 1 else 0 end from pg_tables a inner join pg_namespace b on b.nspname = a.schemaname where %s%s and %s%s",
                                   ignoreCase
                                   ? "lower(b.nspname)"
                                   : "b.nspname",
                                   StringUtils.hasText(tbName[0])
                                   ? String.format("='%s'",
                                                   tbName[0])
                                   : " is null",
                                   ignoreCase
                                   ? "lower(a.tablename)"
                                   : "a.tablename",
                                   StringUtils.hasText(tbName[1])
                                   ? String.format("='%s'",
                                                   tbName[1])
                                   : " is null");

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
        String dbTypeText = column.getDbTypeText();
        dbTypeText = dbTypeText == null
                     ? ""
                     : dbTypeText.toLowerCase(Locale.ROOT);

        boolean isArray = dbTypeText.endsWith("[]");

        if (isArray)
            return JDBCType.ARRAY;

        switch (dbTypeText) {
            case "boolean":
                return JDBCType.BOOLEAN;
            case "smallint":
            case "smallserial":
                return JDBCType.SMALLINT;
            case "integer":
            case "serial":
                return JDBCType.INTEGER;
            case "bigint":
            case "bigserial":
                return JDBCType.BIGINT;
            case "real":
                return JDBCType.FLOAT;
            case "double precision":
                return JDBCType.DOUBLE;
            case "numeric":
            case "money":
                return JDBCType.DECIMAL;

            case "date":
                return JDBCType.DATE;
            case "time":
            case "time without time zone":
                return JDBCType.TIME;
            case "time with time zone":
                return JDBCType.TIME_WITH_TIMEZONE;
            case "timestamp":
            case "timestamp without time zone":
                return JDBCType.TIMESTAMP;
            case "timestamp with time zone":
                return JDBCType.TIMESTAMP_WITH_TIMEZONE;

            case "bytea":
                return JDBCType.LONGVARBINARY;
            case "bit":
                return JDBCType.BINARY;
            case "bit varying":
                return JDBCType.VARBINARY;

            case "char":
                return JDBCType.CHAR;
            case "varchar":
            case "character":
            case "character varying":
            case "name":
            case "cidr":
            case "inet":
            case "macaddr":
            case "macaddr8":
            case "uuid":
            default:
                return JDBCType.VARCHAR;
            case "text":
            case "json":
            case "jsonb":
                return JDBCType.LONGVARCHAR;
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
