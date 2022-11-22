package project.extension.mybatis.core.provider.normal;

import org.apache.ibatis.session.SqlSession;
import project.extension.collections.CollectionsExtension;
import project.extension.cryptography.MD5Utils;
import project.extension.mybatis.config.BaseConfig;
import project.extension.mybatis.core.driver.NaiveSqlSession;
import project.extension.mybatis.core.provider.*;
import project.extension.mybatis.core.provider.standard.*;
import project.extension.mybatis.extention.RepositoryExtension;
import project.extension.mybatis.extention.SqlSessionExtension;
import project.extension.mybatis.model.CurdType;
import project.extension.mybatis.model.UpdaterDTO;
import project.extension.tuple.Tuple2;

import java.util.*;
import java.util.stream.Collectors;

/**
 * MySql数据更新对象
 *
 * @param <T> 实体类型
 * @author LCTR
 * @date 2022-03-31
 */
public abstract class Update<T>
        implements IUpdate<T> {
    private final BaseConfig config;

    private final SqlProvider sqlProvider;

    private final AopProvider aop;

    private final boolean withTransactional;

    private final UpdaterDTO updater;

    private final Class<T> entityType;

    private final String msIdPrefix;

    private final IWhere<T, IWhereSource<T>> where;

    public Update(BaseConfig config,
                  SqlProvider sqlProvider,
                  IAop aop,
                  Class<T> entityType,
                  boolean withTransactional) {
        this.config = config;
        this.sqlProvider = sqlProvider;
        this.aop = (AopProvider) aop;
        this.withTransactional = withTransactional;
        this.updater = new UpdaterDTO();
        this.entityType = entityType;
        this.msIdPrefix = String.format("Update:%s",
                                        entityType.getTypeName());
        this.where = new WhereProvider<>(this);
        initialization();
    }

    /**
     * 初始化
     */
    protected void initialization() {
        Tuple2<String, String> table = RepositoryExtension.getTableName(entityType,
                                                                        config.getNameConvertType());
        updater.setSchema(table.a);
        updater.setTableName(table.b);
        updater.setEntityType(entityType);
        updater.setDtoType(entityType);
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
     * 返回当前的脚本
     *
     * @param noParameter 无参数语句
     * @param data        数据
     * @return 脚本
     */
    protected String currentScript(boolean noParameter,
                                   Object data)
            throws
            Exception {
        updater.getParameter()
               .clear();
        updater.setDynamicFilter(where.getDynamicFilters());
        if (where.getParameters()
                 .size() > 0)
            updater.getCustomParameter()
                   .putAll(where.getParameters());
        return sqlProvider.updater2Script(updater,
                                          noParameter,
                                          data);
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
    public <T2> IUpdate<T> setSource(T2 data) {
        updater.getDataList()
               .add(data);
        return this;
    }

    @Override
    public <T2> IUpdate<T> setSource(Collection<T2> data) {
        updater.getDataList()
               .addAll(data);
        return this;
    }

    @Override
    public IUpdate<T> setWithSql(String fieldName,
                                 String sql) {
        updater.getCustomSetByFieldNameWithSql()
               .put(fieldName,
                    sql);
        return this;
    }

    @Override
    public IUpdate<T> setWithSql(String fieldName,
                                 String sql,
                                 List<Tuple2<String, Object>> parameter) {
        updater.getCustomSetByFieldNameWithSql()
               .put(fieldName,
                    sql);
        if (parameter != null && parameter.size() > 0)
            updater.getCustomParameter()
                   .putAll(parameter.stream()
                                    .collect(Collectors.toMap(k -> k.a,
                                                              v -> v.b)));
        return this;
    }

    @Override
    public IUpdate<T> setWithSql(String fieldName,
                                 String sql,
                                 Map<String, Object> parameter) {
        updater.getCustomSetByFieldNameWithSql()
               .put(fieldName,
                    sql);
        if (parameter != null && parameter.size() > 0)
            updater.getCustomParameter()
                   .putAll(parameter);
        return this;
    }

    @Override
    public IUpdate<T> set(String fieldName,
                          Object value) {
        updater.getCustomSetByFieldName()
               .put(fieldName,
                    value);
        return this;
    }

    @Override
    public <TMember> IUpdate<T> set(String fieldName,
                                    Class<TMember> memberType,
                                    ISetAction<T, TMember> action) {
        updater.getDynamicSetters()
               .add(action.invoke(new SetProvider<>(this,
                                                    fieldName,
                                                    memberType))
                          .getSetter());
        return this;
    }

    @Override
    public <TMember> ISet<T, TMember> newSet(String fieldName,
                                             Class<TMember> memberType,
                                             ISetAction<T, TMember> action) {
        return action.invoke(new SetProvider<>(this,
                                               fieldName,
                                               memberType));
    }

    @Override
    public IUpdate<T> whitTempKey(String... fieldNames) {
        return whitTempKey(Arrays.stream(fieldNames)
                                 .collect(Collectors.toList()));
    }

    @Override
    public IUpdate<T> whitTempKey(Collection<String> fieldNames) {
        updater.getTempKeyFieldNames()
               .addAll(fieldNames);
        return this;
    }

    @Override
    public IUpdate<T> where(String sql) {
        updater.getWithWhereSQLs()
               .add(sql);
        return this;
    }

    @Override
    public IUpdate<T> where(String sql,
                            List<Tuple2<String, Object>> parameter) {
        updater.getWithWhereSQLs()
               .add(sql);
        if (parameter != null && parameter.size() > 0)
            updater.getCustomParameter()
                   .putAll(parameter.stream()
                                    .collect(Collectors.toMap(k -> k.a,
                                                              v -> v.b)));
        return this;
    }

    @Override
    public IUpdate<T> where(String sql,
                            Map<String, Object> parameter) {
        updater.getWithWhereSQLs()
               .add(sql);
        if (parameter != null && parameter.size() > 0)
            updater.getCustomParameter()
                   .putAll(parameter);
        return this;
    }

    @Override
    public IUpdate<T> where(IWhereAction<T, IWhereSource<T>> action) {
        action.invoke(where);
        return this;
    }

    @Override
    public IWhere<T, IWhereSource<T>> newWhere(IWhereAction<T, IWhereSource<T>> action) {
        return action.invoke(new WhereProvider<>(this));
    }

    @Override
    public IUpdate<T> include(String... fieldNames) {
        return include(Arrays.stream(fieldNames)
                             .collect(Collectors.toList()));
    }

    @Override
    public IUpdate<T> include(Collection<String> fieldNames) {
        updater.getExceptionFieldNames()
               .addAll(fieldNames);
        return this;
    }

    @Override
    public IUpdate<T> ignore(String... fieldNames) {
        return ignore(Arrays.stream(fieldNames)
                            .collect(Collectors.toList()));
    }

    @Override
    public IUpdate<T> ignore(Collection<String> fieldNames) {
        updater.getIgnoreFieldNames()
               .addAll(fieldNames);
        return this;
    }

    @Override
    public IUpdate<T> asTable(String tableName) {
        updater.setTableName(tableName);
        return this;
    }

    @Override
    public Class<T> getEntityType() {
        return entityType;
    }

    @Override
    public <T2> IUpdate<T> asDto(Class<T2> dtoType) {
        updater.setDtoType(dtoType);
        return this;
    }

    @Override
    public IUpdate<T> mainTagLevel(Integer level) {
        updater.setMainTagLevel(level);
        return this;
    }

    @Override
    public IUpdate<T> withAnOtherTag(String... tag) {
        updater.getCustomTags()
               .addAll(Arrays.asList(tag));
        return this;
    }

    @Override
    public List<String> toSqlWithNoParameter()
            throws
            Exception {
        List<String> sqlList = new ArrayList<>();
        if (updater.getDataList()
                   .size() > 0) {
            //更新指定数据的sql语句
            for (Object data : updater.getDataList()) {
                sqlList.add(currentScript(true,
                                          data));
            }
        } else if (CollectionsExtension.anyPlus(updater.getCustomSetByFieldName())) {
            //根据条件更新指定列的sql语句
            sqlList.add(currentScript(true,
                                      null));
        }
        return sqlList;
    }

    @Override
    public List<Tuple2<String, Map<String, Object>>> toSql()
            throws
            Exception {
        List<Tuple2<String, Map<String, Object>>> sqlList = new ArrayList<>();
        if (updater.getDataList()
                   .size() > 0) {
            //更新指定数据的sql语句
            for (Object data : updater.getDataList()) {
                sqlList.add(new Tuple2<>(currentScript(false,
                                                       data),
                                         updater.getParameter()));
            }
        } else if (CollectionsExtension.anyPlus(updater.getCustomSetByFieldName())) {
            //根据条件更新指定列的sql语句
            sqlList.add(new Tuple2<>(currentScript(false,
                                                   null),
                                     updater.getParameter()));
        }
        return sqlList;
    }

    @Override
    public int executeAffrows()
            throws
            Exception {
        int rows = 0;

        if (updater.getDataList()
                   .size() > 0) {
            //更新指定数据
            for (Object data : updater.getDataList()) {
                String script = currentScript(false,
                                              data);
                String msId = getMSId(MD5Utils.hash(script),
                                      updater.getDtoType()
                                             .getTypeName());

                int currentRows = aop.invokeWithAop(() -> SqlSessionExtension.update(
                                                            getSqlSession(),
                                                            msId,
                                                            script,
                                                            updater.getParameter(),
                                                            config.getNameConvertType()),
                                                    CurdType.更新,
                                                    config.getDataSource(),
                                                    script,
                                                    updater.getParameter(),
                                                    updater.getEntityType(),
                                                    updater.getDtoType());
                if (currentRows < 0) {
                    throw new Exception("数据更新失败");
                } else if (currentRows > 0) rows++;
            }
        } else if (CollectionsExtension.anyPlus(updater.getCustomSetByFieldName())
                || CollectionsExtension.anyPlus(updater.getDynamicSetters())) {
            //根据条件更新指定列
            if (updater.getWithWhereSQLs()
                       .size() == 0
                    && where.getDynamicFilters()
                            .size() == 0)
                throw new Exception("更新指定列时，必须设置WHERE条件");

            String script = currentScript(false,
                                          null);
            String msId = getMSId(MD5Utils.hash(script),
                                  updater.getDtoType()
                                         .getTypeName());

            int currentRows = aop.invokeWithAop(() -> SqlSessionExtension.update(
                                                        getSqlSession(),
                                                        msId,
                                                        script,
                                                        updater.getParameter(),
                                                        config.getNameConvertType()),
                                                CurdType.更新,
                                                config.getDataSource(),
                                                script,
                                                updater.getParameter(),
                                                updater.getEntityType(),
                                                updater.getDtoType());
            if (currentRows < 0) {
                return currentRows;
            } else if (currentRows > 0) rows++;
        }

        return rows;
    }
}
