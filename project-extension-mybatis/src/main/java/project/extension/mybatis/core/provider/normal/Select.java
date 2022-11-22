package project.extension.mybatis.core.provider.normal;

import org.apache.ibatis.session.SqlSession;
import project.extension.cryptography.MD5Utils;
import project.extension.mybatis.config.BaseConfig;
import project.extension.mybatis.core.driver.NaiveSqlSession;
import project.extension.mybatis.core.provider.*;
import project.extension.mybatis.core.provider.standard.*;
import project.extension.mybatis.extention.RepositoryExtension;
import project.extension.mybatis.extention.SqlSessionExtension;
import project.extension.mybatis.model.*;
import project.extension.tuple.Tuple2;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 数据查询对象
 *
 * @param <T> 实体类型
 * @author LCTR
 * @date 2022-03-31
 */
public abstract class Select<T>
        implements ISelect<T> {
    private final BaseConfig config;

    protected final SqlProvider sqlProvider;

    protected final AopProvider aop;

    protected final boolean withTransactional;

    protected final ExecutorDTO executor;

    protected final Class<T> entityType;

    private final String msIdPrefix;

    protected final IWhere<T, IWhereSource<T>> where;

    protected final IOrderBy<T, IOrderBySource<T>> orderBy;

    public Select(BaseConfig config,
                  SqlProvider sqlProvider,
                  IAop aop,
                  Class<T> entityType,
                  boolean withTransactional) {
        this.config = config;
        this.sqlProvider = sqlProvider;
        this.aop = (AopProvider) aop;
        this.withTransactional = withTransactional;
        this.executor = new ExecutorDTO();
        this.entityType = entityType;
        this.msIdPrefix = String.format("Select:%s",
                                        entityType.getTypeName());
        this.where = new WhereProvider<>(this);
        this.orderBy = new OrderByProvider<>(this);
        initialization();
    }

    /**
     * 初始化
     */
    protected void initialization() {
        Tuple2<String, String> table = RepositoryExtension.getTableName(entityType,
                                                                        config.getNameConvertType());
        executor.setSchema(table.a);
        executor.setTableName(table.b);
        executor.setEntityType(entityType);
        executor.setDtoType(entityType);
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
     * 是否需要分页
     *
     * @return 脚本
     */
    protected boolean needPaging() {
        Pagination pagination = executor.getPagination();
        return pagination != null && !pagination.getNope() && !pagination.getUserPageHelper();
    }

    /**
     * 分页
     *
     * @return 添加了分页的Sql语句
     */
    protected String paging()
            throws
            Exception {
        if (needPaging()) {
            executor.getPagination()
                    .setRecordCount(count());

            return currentScript(true,
                                 false,
                                 false);

//            String sql = sqlProvider.originalSql2CountSql(originalSql);
//            executor.getPagination()
//                    .setRecordCount(aop.invokeWithAop(() -> SqlSessionExtension.selectOne(
//                                                              getSqlSession(),
//                                                              getMSId(MD5Utils.hash(sql),
//                                                                      entityType.getTypeName(),
//                                                                      Long.class.getTypeName()),
//                                                              sql,
//                                                              executor.getParameter(),
//                                                              Long.class),
//                                                      CurdType.查询,
//                                                      sql,
//                                                      executor.getParameter(),
//                                                      executor.getEntityType(),
//                                                      executor.getDtoType()));

//            return sqlProvider.pagination2Sql(executor.getPagination(),
//                                              currentScript(true,
//                                                            false,
//                                                            false));
        }

        return currentScript(false,
                             false,
                             false);
    }

    /**
     * 设置查询设置
     */
    protected void setupExecutor() {
        setupExecutor(true,
                      true,
                      true);
    }

    /**
     * 设置查询设置
     *
     * @param clear  清理历史参数
     * @param filter 过滤条件
     * @param order  排序条件
     */
    protected void setupExecutor(boolean clear,
                                 boolean filter,
                                 boolean order) {
        if (clear)
            executor.getParameter()
                    .clear();
        if (filter) {
            executor.setDynamicFilters(where.getDynamicFilters());
            if (where.getParameters()
                     .size() > 0)
                executor.getCustomParameter()
                        .putAll(where.getParameters());
        }
        if (order)
            executor.setDynamicOrder(orderBy.getDynamicOrder());
    }

    /**
     * 返回当前的Sql语句
     *
     * @param paging         包括分页
     * @param noParameter    无参数语句
     * @param withoutOrderBy 排除排序语句
     * @return Sql语句
     */
    protected String currentScript(boolean paging,
                                   boolean noParameter,
                                   boolean withoutOrderBy)
            throws
            Exception {
        setupExecutor();
        return sqlProvider.executor2Sql(executor,
                                        paging,
                                        noParameter,
                                        withoutOrderBy);
    }

    /**
     * 获取Sql会话
     */
    protected SqlSession getSqlSession() {
        return withTransactional
               ? NaiveSqlSession.current()
               : null;
    }

    @Override
    public ISelect<T> where(String sql) {
        executor.getWithWhereSQLs()
                .add(sql);
        return this;
    }

    @Override
    public final ISelect<T> where(String sql,
                                  List<Tuple2<String, Object>> parameter) {
        executor.getWithWhereSQLs()
                .add(sql);
        if (parameter != null && parameter.size() > 0)
            executor.getCustomParameter()
                    .putAll(parameter.stream()
                                     .collect(Collectors.toMap(k -> k.a,
                                                               v -> v.b)));
        return this;
    }

    @Override
    public ISelect<T> where(String sql,
                            Map<String, Object> parameter) {
        executor.getWithWhereSQLs()
                .add(sql);
        if (parameter != null && parameter.size() > 0)
            executor.getCustomParameter()
                    .putAll(parameter);
        return this;
    }

    @Override
    public ISelect<T> where(IWhereAction<T, IWhereSource<T>> action) {
        action.invoke(where);
        return this;
    }

    @Override
    public IWhere<T, IWhereSource<T>> newWhere(IWhereAction<T, IWhereSource<T>> action) {
        return action.invoke(new WhereProvider<>(this));
    }

    @Override
    public ISelect<T> withSql(String sql) {
        executor.setWithSQL(sql);
        return this;
    }

    @Override
    public ISelect<T> withSql(String sql,
                              List<Tuple2<String, Object>> parameter) {
        executor.setWithSQL(sql);
        if (parameter != null && parameter.size() > 0)
            executor.getCustomParameter()
                    .putAll(parameter.stream()
                                     .collect(Collectors.toMap(k -> k.a,
                                                               v -> v.b)));
        return this;
    }

    @Override
    public ISelect<T> withSql(String sql,
                              Map<String, Object> parameter) {
        executor.setWithSQL(sql);
        if (parameter != null && parameter.size() > 0)
            executor.getCustomParameter()
                    .putAll(parameter);
        return this;
    }

    @Override
    public ISelect<T> groupBy(String... fieldNames) {
        return groupBy(Arrays.stream(fieldNames)
                             .collect(Collectors.toList()));
    }

    @Override
    public ISelect<T> groupBy(Collection<String> fieldNames) {
        executor.getGroupByFieldNames()
                .addAll(fieldNames);
        return this;
    }

    @Override
    public ISelect<T> orderBy(String sql) {
        executor.setWithOrderBySQL(sql);
        return this;
    }

    @Override
    public ISelect<T> orderBy(String sql,
                              List<Tuple2<String, Object>> parameter) {
        executor.setWithOrderBySQL(sql);
        if (parameter != null && parameter.size() > 0)
            executor.getCustomParameter()
                    .putAll(parameter.stream()
                                     .collect(Collectors.toMap(k -> k.a,
                                                               v -> v.b)));
        return this;
    }

    @Override
    public ISelect<T> orderBy(String sql,
                              Map<String, Object> parameter) {
        executor.setWithOrderBySQL(sql);
        if (parameter != null && parameter.size() > 0)
            executor.getCustomParameter()
                    .putAll(parameter);
        return this;
    }

    @Override
    public ISelect<T> orderBy(IOrderByAction<T, IOrderBySource<T>> action) {
        action.invoke(orderBy);
        return this;
    }

    @Override
    public IOrderBy<T, IOrderBySource<T>> newOrderBy(IOrderByAction<T, IOrderBySource<T>> action) {
        return action.invoke(new OrderByProvider<>(this));
    }

    @Override
    public ISelect<T> pagination(Pagination pagination) {
        executor.setPagination(pagination);
        return this;
    }

    @Override
    public <T2> ISelect<T> leftJoin(Class<T2> tableType) {
        //#TODO
        return this;
    }

    @Override
    public <T2> ISelect<T> leftJoin(Class<T2> tableType,
                                    String alias) {
        //#TODO
        return this;
    }

    @Override
    public <T2> ISelect<T> rightJoin(Class<T2> tableType) {
        //#TODO
        return this;
    }

    @Override
    public <T2> ISelect<T> rightJoin(Class<T2> tableType,
                                     String alias) {
        //#TODO
        return this;
    }

    @Override
    public ISelect<T> include(String fieldName) {
        //#TODO
        return this;
    }

    @Override
    public ISelect<T> include(String fieldName,
                              String alias) {
        //#TODO
        return this;
    }

    @Override
    public <T2> ISelect<T> include(Class<T2> tableType,
                                   String sql) {
        //#TODO
        return this;
    }

    @Override
    public <T2> ISelect<T> include(Class<T2> tableType,
                                   String sql,
                                   String alias) {
        //#TODO
        return this;
    }

    @Override
    public <T2> ISelect<T> where(Class<T2> tableType,
                                 String sql) {
        //#TODO
        return this;
    }

    @Override
    public ISelect<T> where(String alias,
                            String sql) {
        //#TODO
        return this;
    }

    @Override
    public <T2> ISelect<T> where(Class<T2> tableType,
                                 DynamicFilter filter) {
        //#TODO
        return this;
    }

    @Override
    public <T2> ISelect<T> where(Class<T2> tableType,
                                 List<DynamicFilter> filters) {
        //#TODO
        return this;
    }

    @Override
    public ISelect<T> where(String alias,
                            DynamicFilter filter) {
        //#TODO
        return this;
    }

    @Override
    public ISelect<T> where(String alias,
                            Collection<DynamicFilter> filters) {
        //#TODO
        return this;
    }

    @Override
    public <T2> ISelect<T> withSql(Class<T2> tableType,
                                   String sql) {
        //#TODO
        return this;
    }

    @Override
    public ISelect<T> withSql(String alias,
                              String sql) {
        //#TODO
        return this;
    }

    @Override
    public Boolean any()
            throws
            Exception {
        executor.setDtoType(entityType);
        executor.setAllColumns(true);

        String sql = sqlProvider.originalSql2AnySql(currentScript(false,
                                                                  false,
                                                                  true));

        return aop.invokeWithAop(() -> SqlSessionExtension.selectOne(
                                         getSqlSession(),
                                         getMSId(MD5Utils.hash(sql),
                                                 entityType.getTypeName(),
                                                 Boolean.class.getTypeName()),
                                         sql,
                                         executor.getParameter(),
                                         Boolean.class,
                                         config.getNameConvertType()),
                                 CurdType.查询,
                                 config.getDataSource(),
                                 sql,
                                 executor.getParameter(),
                                 executor.getEntityType(),
                                 executor.getDtoType());
    }

    @Override
    public ISelect<T> columns(String... fieldNames) {
        return columns(Arrays.stream(fieldNames)
                             .collect(Collectors.toList()));
    }

    @Override
    public ISelect<T> columns(Collection<String> fieldNames) {
        executor.getCustomFieldNames()
                .addAll(fieldNames);
        return this;
    }

    @Override
    public List<T> toList()
            throws
            Exception {
        executor.setDtoType(entityType);
        executor.setAllColumns(false);
        String sql = paging();

        return aop.invokeWithAop(() -> SqlSessionExtension.selectList(getSqlSession(),
                                                                      getMSId(MD5Utils.hash(sql),
                                                                              entityType.getTypeName()),
                                                                      sql,
                                                                      executor.getParameter(),
                                                                      entityType,
                                                                      config.getNameConvertType()),
                                 CurdType.查询,
                                 config.getDataSource(),
                                 sql,
                                 executor.getParameter(),
                                 executor.getEntityType(),
                                 executor.getDtoType());
    }

    @Override
    public <T2> List<T2> toList(Class<T2> dtoType)
            throws
            Exception {
        executor.setDtoType(dtoType);
        executor.setAllColumns(false);
        String sql = paging();

        return aop.invokeWithAop(() -> SqlSessionExtension.selectList(getSqlSession(),
                                                                      getMSId(MD5Utils.hash(sql),
                                                                              entityType.getTypeName(),
                                                                              dtoType.getTypeName()),
                                                                      sql,
                                                                      executor.getParameter(),
                                                                      dtoType,
                                                                      executor.getMainTagLevel(),
                                                                      executor.getCustomTags(),
                                                                      config.getNameConvertType()),
                                 CurdType.查询,
                                 config.getDataSource(),
                                 sql,
                                 executor.getParameter(),
                                 executor.getEntityType(),
                                 executor.getDtoType());
    }

    @Override
    public List<Map<String, Object>> toMapList()
            throws
            Exception {
        executor.setAllColumns(false);
        String sql = paging();

        return aop.invokeWithAop(() -> SqlSessionExtension.selectMapList(getSqlSession(),
                                                                         getMSId(MD5Utils.hash(sql),
                                                                                 entityType.getTypeName(),
                                                                                 HashMap.class.getTypeName()),
                                                                         sql,
                                                                         executor.getParameter(),
                                                                         config.getNameConvertType()),
                                 CurdType.查询,
                                 config.getDataSource(),
                                 sql,
                                 executor.getParameter(),
                                 executor.getEntityType(),
                                 executor.getDtoType());
    }

    @Override
    public T first()
            throws
            Exception {
        executor.setDtoType(entityType);
        executor.setAllColumns(false);
        String sql = sqlProvider.originalSql2LimitSql(currentScript(false,
                                                                    false,
                                                                    false),
                                                      0,
                                                      1);

        return aop.invokeWithAop(() -> SqlSessionExtension.selectOne(getSqlSession(),
                                                                     getMSId(MD5Utils.hash(sql),
                                                                             entityType.getTypeName()),
                                                                     sql,
                                                                     executor.getParameter(),
                                                                     entityType,
                                                                     config.getNameConvertType()),
                                 CurdType.查询,
                                 config.getDataSource(),
                                 sql,
                                 executor.getParameter(),
                                 executor.getEntityType(),
                                 executor.getDtoType());
    }

    @Override
    public <T2> T2 first(Class<T2> dtoType)
            throws
            Exception {
        executor.setDtoType(dtoType);
        executor.setAllColumns(false);
        String sql = sqlProvider.originalSql2LimitSql(currentScript(false,
                                                                    false,
                                                                    false),
                                                      0,
                                                      1);

        return aop.invokeWithAop(() -> SqlSessionExtension.selectOne(getSqlSession(),
                                                                     getMSId(MD5Utils.hash(sql),
                                                                             entityType.getTypeName(),
                                                                             dtoType.getTypeName()),
                                                                     sql,
                                                                     executor.getParameter(),
                                                                     dtoType,
                                                                     executor.getMainTagLevel(),
                                                                     executor.getCustomTags(),
                                                                     config.getNameConvertType()),
                                 CurdType.查询,
                                 config.getDataSource(),
                                 sql,
                                 executor.getParameter(),
                                 executor.getEntityType(),
                                 executor.getDtoType());
    }

    @Override
    public <C> C first(String fieldName,
                       Class<C> memberType)
            throws
            Exception {
        executor.setAllColumns(false);
        executor.getCustomFieldNames()
                .clear();
        executor.getCustomFieldNames()
                .add(fieldName);
        String sql = sqlProvider.originalSql2LimitSql(currentScript(false,
                                                                    false,
                                                                    false),
                                                      0,
                                                      1);

        return aop.invokeWithAop(() -> SqlSessionExtension.selectOne(
                                         getSqlSession(),
                                         getMSId(MD5Utils.hash(sql),
                                                 entityType.getTypeName(),
                                                 fieldName),
                                         sql,
                                         executor.getParameter(),
                                         memberType,
                                         config.getNameConvertType()),
                                 CurdType.查询,
                                 config.getDataSource(),
                                 sql,
                                 executor.getParameter(),
                                 executor.getEntityType(),
                                 executor.getDtoType());
    }

    @Override
    public Map<String, Object> firstMap()
            throws
            Exception {
        executor.setAllColumns(false);
        String sql = sqlProvider.originalSql2LimitSql(currentScript(false,
                                                                    false,
                                                                    false),
                                                      0,
                                                      1);

        return aop.invokeWithAop(() -> SqlSessionExtension.selectMap(getSqlSession(),
                                                                     getMSId(MD5Utils.hash(sql),
                                                                             entityType.getTypeName(),
                                                                             HashMap.class.getTypeName()),
                                                                     sql,
                                                                     executor.getParameter(),
                                                                     config.getNameConvertType()),
                                 CurdType.查询,
                                 config.getDataSource(),
                                 sql,
                                 executor.getParameter(),
                                 executor.getEntityType(),
                                 executor.getDtoType());
    }

    @Override
    public Long count()
            throws
            Exception {
        setupExecutor(true,
                      true,
                      false);
        String sql = sqlProvider.executor2CountSql(executor,
                                                   false,
                                                   true);

        return aop.invokeWithAop(() -> SqlSessionExtension.selectOne(
                                         getSqlSession(),
                                         getMSId(MD5Utils.hash(sql),
                                                 entityType.getTypeName(),
                                                 Long.class.getTypeName()),
                                         sql,
                                         executor.getParameter(),
                                         Long.class,
                                         config.getNameConvertType()),
                                 CurdType.查询,
                                 config.getDataSource(),
                                 sql,
                                 executor.getParameter(),
                                 executor.getEntityType(),
                                 executor.getDtoType());
    }

    @Override
    public <C> C max(String fieldName,
                     Class<C> memberType)
            throws
            Exception {
        executor.getCustomFieldNames()
                .clear();
        executor.getCustomFieldNames()
                .add(fieldName);
        setupExecutor(true,
                      true,
                      false);

        String sql = sqlProvider.executor2MaxSql(executor,
                                                 false,
                                                 true);

        return aop.invokeWithAop(() -> SqlSessionExtension.selectOne(
                                         getSqlSession(),
                                         getMSId(MD5Utils.hash(sql),
                                                 entityType.getTypeName(),
                                                 fieldName),
                                         sql,
                                         executor.getParameter(),
                                         memberType,
                                         config.getNameConvertType()),
                                 CurdType.查询,
                                 config.getDataSource(),
                                 sql,
                                 executor.getParameter(),
                                 executor.getEntityType(),
                                 executor.getDtoType());
    }

    @Override
    public <C> C min(String fieldName,
                     Class<C> memberType)
            throws
            Exception {
        executor.getCustomFieldNames()
                .clear();
        executor.getCustomFieldNames()
                .add(fieldName);
        setupExecutor(true,
                      true,
                      false);

        String sql = sqlProvider.executor2MinSql(executor,
                                                 false,
                                                 true);

        return aop.invokeWithAop(() -> SqlSessionExtension.selectOne(
                                         getSqlSession(),
                                         getMSId(MD5Utils.hash(sql),
                                                 entityType.getTypeName(),
                                                 fieldName),
                                         sql,
                                         executor.getParameter(),
                                         memberType,
                                         config.getNameConvertType()),
                                 CurdType.查询,
                                 config.getDataSource(),
                                 sql,
                                 executor.getParameter(),
                                 executor.getEntityType(),
                                 executor.getDtoType());
    }

    @Override
    public <C> C avg(String fieldName,
                     Class<C> memberType)
            throws
            Exception {
        executor.getCustomFieldNames()
                .clear();
        executor.getCustomFieldNames()
                .add(fieldName);
        setupExecutor(true,
                      true,
                      false);

        String sql = sqlProvider.executor2AvgSql(executor,
                                                 false);

        return aop.invokeWithAop(() -> SqlSessionExtension.selectOne(
                                         getSqlSession(),
                                         getMSId(MD5Utils.hash(sql),
                                                 entityType.getTypeName(),
                                                 fieldName),
                                         sql,
                                         executor.getParameter(),
                                         memberType,
                                         config.getNameConvertType()),
                                 CurdType.查询,
                                 config.getDataSource(),
                                 sql,
                                 executor.getParameter(),
                                 executor.getEntityType(),
                                 executor.getDtoType());
    }

    @Override
    public <C> C sum(String fieldName,
                     Class<C> memberType)
            throws
            Exception {
        executor.getCustomFieldNames()
                .clear();
        executor.getCustomFieldNames()
                .add(fieldName);
        setupExecutor(true,
                      true,
                      false);

        String sql = sqlProvider.executor2SumSql(executor,
                                                 false);

        return aop.invokeWithAop(() -> SqlSessionExtension.selectOne(
                                         getSqlSession(),
                                         getMSId(MD5Utils.hash(sql),
                                                 entityType.getTypeName(),
                                                 fieldName),
                                         sql,
                                         executor.getParameter(),
                                         memberType,
                                         config.getNameConvertType()),
                                 CurdType.查询,
                                 config.getDataSource(),
                                 sql,
                                 executor.getParameter(),
                                 executor.getEntityType(),
                                 executor.getDtoType());
    }

    @Override
    public ISelect<T> asTable(String tableName) {
        executor.setTableName(tableName);
        return this;
    }

    @Override
    public ISelect<T> as(String alias) {
        executor.setAlias(alias);
        return this;
    }

    @Override
    public Class<T> getEntityType() {
        return entityType;
    }

    @Override
    public ISelect<T> mainTagLevel(Integer level) {
        executor.setMainTagLevel(level);
        return this;
    }

    @Override
    public ISelect<T> withAnOtherTag(String... tag) {
        executor.getCustomTags()
                .addAll(Arrays.asList(tag));
        return this;
    }

    @Override
    public String toSqlWithNoParameter()
            throws
            Exception {
        return currentScript(true,
                             true,
                             false);
    }

    @Override
    public Tuple2<String, Map<String, Object>> toSql()
            throws
            Exception {
        return new Tuple2<>(currentScript(true,
                                          false,
                                          false),
                            executor.getParameter());
    }
}
