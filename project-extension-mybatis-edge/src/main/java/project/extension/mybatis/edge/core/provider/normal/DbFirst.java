package project.extension.mybatis.edge.core.provider.normal;

import org.apache.ibatis.session.SqlSession;
import org.springframework.util.StringUtils;
import project.extension.mybatis.edge.config.DataSourceConfig;
import project.extension.mybatis.edge.core.ado.INaiveAdo;
import project.extension.mybatis.edge.core.provider.standard.IDbFirst;
import project.extension.mybatis.edge.model.DbTypeToJavaType;
import project.extension.mybatis.edge.model.DbColumnInfo;
import project.extension.mybatis.edge.model.DbTableInfo;

import java.util.Comparator;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * DbFirst 开发模式相关功能
 *
 * @author LCTR
 * @date 2022-06-10
 */
public abstract class DbFirst
        implements IDbFirst {
    public DbFirst(DataSourceConfig config,
                   INaiveAdo ado,
                   String msIdPrefix) {
        this.config = config;
        this.ado = ado;
        this.msIdPrefix = msIdPrefix;
    }

    /**
     * 类型转换映射表
     */
    protected static final ConcurrentMap<String, DbTypeToJavaType> dbToJavaMap = new ConcurrentHashMap<>();

    protected final DataSourceConfig config;

    protected final INaiveAdo ado;

    private final String msIdPrefix;

    /**
     * 复制类型转换映射数据
     *
     * @param putDbType  新增的数据库类型
     * @param copyDbType 要复制的数据库类型
     */
    protected static void copyAndPutDbToJavaMap(String putDbType,
                                                String copyDbType) {
        dbToJavaMap.putIfAbsent(putDbType,
                                dbToJavaMap.get(copyDbType));
    }

    /**
     * 获取Sql会话
     */
    protected SqlSession getSqlSession() {
        return ado.getOrCreateSqlSession();
    }

    /**
     * 获取标识
     *
     * @param values 附加值
     * @return 标识
     */
    protected String getMSId(String... values) {
        return String.format("%s:%s",
                             msIdPrefix,
                             String.join("-",
                                         values));
    }

    /**
     * 使用分隔符获取模式名和表名
     *
     * @param name  名称
     * @param lower 转小写
     * @return 存有模式名和表名的数组
     */
    protected abstract String[] splitTableName(String name,
                                               boolean lower);

    /**
     * 使用分隔符获取模式名和表名
     *
     * @param name       名称
     * @param leftQuote  起始标识
     * @param rightQuote 结束标识
     * @param size       数量
     * @return 存有模式名和表名的数组
     */
    protected static String[] getSplitTableNames(String name,
                                                 char leftQuote,
                                                 char rightQuote,
                                                 int size) {
        if (!StringUtils.hasText(name)) return null;
        String[] result = name.split("[.]",
                                     size);

        if (name.indexOf(leftQuote) >= 0) {
            Pattern pattern = Pattern.compile((leftQuote == '['
                                               ? "\\"
                                               : "") +
                                                      leftQuote + "([^" + (rightQuote == ']'
                                                                           ? "\\"
                                                                           : "") + rightQuote + "]+)" +
                                                      (rightQuote == ']'
                                                       ? "\\"
                                                       : "") +
                                                      rightQuote);
            for (int i = 0; i < result.length; i++) {
                Matcher matcher = pattern.matcher(result[i]);
                if (matcher.find())
                    result[i] = matcher.group(1);
            }
        }

        return result;
    }

    /**
     * 表结构信息排序
     *
     * @param tableInfo 表结构信息
     * @return 表结构信息
     */
    protected static DbTableInfo sort(DbTableInfo tableInfo) {
        //主键列按名称（升序）排序
        tableInfo.getPrimaries()
                 .sort(Comparator.comparing(DbColumnInfo::getName));
        //所有列按是否为主键（升序）、是否为外键（降序）、名称（升序）排序
        tableInfo.getColumns()
                 .sort((c1, c2) -> {
                     int compare = Boolean.compare(c2.getIsPrimary(),
                                                   c1.getIsPrimary());
                     if (compare == 0) {
                         boolean b1 = tableInfo.getForeignsDict()
                                               .values()
                                               .stream()
                                               .anyMatch(fk -> fk.getColumns()
                                                                 .stream()
                                                                 .anyMatch(c3 -> c3.getName()
                                                                                   .equals(c1.getName())));
                         boolean b2 = tableInfo.getForeignsDict()
                                               .values()
                                               .stream()
                                               .anyMatch(fk -> fk.getColumns()
                                                                 .stream()
                                                                 .anyMatch(c3 -> c3.getName()
                                                                                   .equals(c2.getName())));
                         compare = Boolean.compare(b2,
                                                   b1);
                     }
                     if (compare == 0)
                         compare = c1.getName()
                                     .compareTo(c2.getName());
                     return compare;
                 });
        return tableInfo;
    }

    @Override
    public String getJavaTypeConvert(DbColumnInfo column) {
        DbTypeToJavaType dbTypeToJavaType = dbToJavaMap.getOrDefault(column.getDbTypeTextFull(),
                                                                     null);
        return dbTypeToJavaType == null
               ? null
               : column.getIsNullable()
                 ? dbTypeToJavaType.javaPackageTypeConvert
                 : dbTypeToJavaType.javaTypeConvert;
    }

    @Override
    public String getJavaType(DbColumnInfo column) {
        DbTypeToJavaType dbTypeToJavaType = dbToJavaMap.getOrDefault(column.getDbTypeTextFull(),
                                                                     null);
        return dbTypeToJavaType == null
               ? null
               : dbTypeToJavaType.javaType;
    }

    @Override
    public String getJavaPackageType(DbColumnInfo column) {
        DbTypeToJavaType dbTypeToJavaType = dbToJavaMap.getOrDefault(column.getDbTypeTextFull(),
                                                                     null);
        return dbTypeToJavaType == null
               ? null
               : dbTypeToJavaType.javaPackageType;
    }

    @Override
    public Class<?> getJavaTypeInfo(DbColumnInfo column) {
        DbTypeToJavaType dbTypeToJavaType = dbToJavaMap.getOrDefault(column.getDbTypeTextFull(),
                                                                     null);
        return dbTypeToJavaType == null
               ? null
               : dbTypeToJavaType.javaTypeInfo;
    }

    @Override
    public Class<?> getJavaPackageTypeInfo(DbColumnInfo column) {
        DbTypeToJavaType dbTypeToJavaType = dbToJavaMap.getOrDefault(column.getDbTypeTextFull(),
                                                                     null);
        return dbTypeToJavaType == null
               ? null
               : dbTypeToJavaType.javaPackageTypeInfo;
    }

    @Override
    public String getJavaStringify(DbColumnInfo column) {
        DbTypeToJavaType dbTypeToJavaType = dbToJavaMap.getOrDefault(column.getDbTypeTextFull(),
                                                                     null);
        return dbTypeToJavaType == null
               ? null
               : dbTypeToJavaType.javaStringify;
    }

    @Override
    public String getJavaParse(DbColumnInfo column) {
        DbTypeToJavaType dbTypeToJavaType = dbToJavaMap.getOrDefault(column.getDbTypeTextFull(),
                                                                     null);
        return dbTypeToJavaType == null
               ? null
               : dbTypeToJavaType.javaParse;
    }
}
