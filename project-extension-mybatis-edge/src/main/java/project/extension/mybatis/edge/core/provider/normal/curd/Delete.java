package project.extension.mybatis.edge.core.provider.normal.curd;

import org.apache.ibatis.session.SqlSession;
import project.extension.ioc.IOCExtension;
import project.extension.mybatis.edge.aop.INaiveAop;
import project.extension.mybatis.edge.aop.NaiveAopProvider;
import project.extension.mybatis.edge.config.DataSourceConfig;
import project.extension.mybatis.edge.core.ado.INaiveAdo;
import project.extension.mybatis.edge.core.mapper.EntityTypeHandler;
import project.extension.mybatis.edge.core.provider.WhereProvider;
import project.extension.mybatis.edge.core.provider.normal.SqlProvider;
import project.extension.mybatis.edge.core.provider.standard.curd.IDelete;
import project.extension.mybatis.edge.core.provider.standard.curd.IWhere;
import project.extension.mybatis.edge.core.provider.standard.curd.IWhereAction;
import project.extension.mybatis.edge.core.provider.standard.curd.IWhereSource;
import project.extension.mybatis.edge.globalization.Strings;
import project.extension.mybatis.edge.model.CurdType;
import project.extension.mybatis.edge.model.DeleterDTO;
import project.extension.standard.exception.ModuleException;
import project.extension.tuple.Tuple2;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 数据删除对象
 *
 * @param <T> 实体类型
 * @author LCTR
 * @date 2022-03-31
 */
public abstract class Delete<T>
        implements IDelete<T> {
    private final DataSourceConfig config;

    private final SqlProvider sqlProvider;

    private final INaiveAdo ado;

    private final NaiveAopProvider aop;

    private final DeleterDTO deleter;

    private final Class<T> entityType;

    private final IWhere<T, IWhereSource<T>> where;

    public Delete(DataSourceConfig config,
                  SqlProvider sqlProvider,
                  INaiveAdo ado,
                  Class<T> entityType) {
        this.config = config;
        this.sqlProvider = sqlProvider;
        this.ado = ado;
        this.aop = (NaiveAopProvider) IOCExtension.applicationContext.getBean(INaiveAop.class);
        this.deleter = new DeleterDTO();
        this.entityType = entityType;
        this.where = new WhereProvider<>(this);
        initialization();
    }

    /**
     * 初始化
     */
    protected void initialization() {
        Tuple2<String, String> table = EntityTypeHandler.getTableName(entityType,
                                                                      config.getNameConvertType());
        deleter.setSchema(table.a);
        deleter.setTableName(table.b);
        deleter.setEntityType(entityType);
        deleter.setDtoType(entityType);
    }

    /**
     * 获取标识
     *
     * @return 标识
     */
    protected String getMSId() {
        return ado.getMSId();
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
            ModuleException {
        deleter.getParameter()
               .clear();
        deleter.setDynamicFilter(where.getDynamicFilters());
        if (where.getParameters()
                 .size() > 0)
            deleter.getCustomParameter()
                   .putAll(where.getParameters());
        return sqlProvider.deleter2Script(deleter,
                                          noParameter,
                                          data);
    }

    /**
     * 获取Sql会话
     */
    protected SqlSession getSqlSession() {
        return ado.getOrCreateSqlSession();
    }

    @Override
    public <T2> IDelete<T> setSource(T2 data) {
        deleter.getDataList()
               .add(data);
        return this;
    }

    @Override
    public <T2> IDelete<T> setSource(Collection<T2> data) {
        deleter.getDataList()
               .addAll(data);
        return this;
    }

    @Override
    public IDelete<T> whitTempKey(String... fieldNames) {
        return whitTempKey(Arrays.stream(fieldNames)
                                 .collect(Collectors.toList()));
    }

    @Override
    public IDelete<T> whitTempKey(Collection<String> fieldNames) {
        deleter.getTempKeyFieldNames()
               .addAll(fieldNames);
        return this;
    }

    @Override
    public IDelete<T> where(String sql) {
        deleter.getWithWhereSQLs()
               .add(sql);
        return this;
    }

    @Override
    public IDelete<T> where(String sql,
                            List<Tuple2<String, Object>> parameter) {
        deleter.getWithWhereSQLs()
               .add(sql);
        if (parameter != null && parameter.size() > 0)
            deleter.getCustomParameter()
                   .putAll(parameter.stream()
                                    .collect(Collectors.toMap(k -> k.a,
                                                              v -> v.b)));
        return this;
    }

    @Override
    public IDelete<T> where(String sql,
                            Map<String, Object> parameter) {
        deleter.getWithWhereSQLs()
               .add(sql);
        if (parameter != null && parameter.size() > 0)
            deleter.getCustomParameter()
                   .putAll(parameter);
        return this;
    }

    @Override
    public IDelete<T> where(IWhereAction<T, IWhereSource<T>> action) {
        action.invoke(where);
        return this;
    }

    @Override
    public IWhere<T, IWhereSource<T>> newWhere(IWhereAction<T, IWhereSource<T>> action) {
        return action.invoke(new WhereProvider<>(this));
    }

    @Override
    public IDelete<T> asTable(String tableName) {
        deleter.setTableName(tableName);
        return this;
    }

    @Override
    public Class<T> getEntityType() {
        return entityType;
    }

    @Override
    public <T2> IDelete<T> asDto(Class<T2> dtoType) {
        deleter.setDtoType(dtoType);
        return this;
    }

    @Override
    public List<String> toSqlWithNoParameter()
            throws
            ModuleException {
        List<String> sqlList = new ArrayList<>();
        if (deleter.getDataList()
                   .size() > 0) {
            //删除指定数据的sql语句
            for (Object data : deleter.getDataList()) {
                sqlList.add(currentScript(true,
                                          data));
            }
        } else if (deleter.getWithWhereSQLs()
                          .size() > 0
                || deleter.getDynamicFilter()
                          .size() != 0) {
            //根据条件删除指定的数据
            sqlList.add(currentScript(true,
                                      null));
        }
        return sqlList;
    }

    @Override
    public List<Tuple2<String, Map<String, Object>>> toSql()
            throws
            ModuleException {
        List<Tuple2<String, Map<String, Object>>> sqlList = new ArrayList<>();
        if (deleter.getDataList()
                   .size() > 0) {
            //删除指定数据的sql语句
            for (Object data : deleter.getDataList()) {
                sqlList.add(new Tuple2<>(currentScript(false,
                                                       data),
                                         deleter.getParameter()));
            }
        } else if (deleter.getWithWhereSQLs()
                          .size() > 0
                || deleter.getDynamicFilter()
                          .size() != 0) {
            //根据条件删除指定的数据
            sqlList.add(new Tuple2<>(currentScript(false,
                                                   null),
                                     deleter.getParameter()));
        }
        return sqlList;
    }

    @Override
    public int executeAffrows()
            throws
            ModuleException {
        int rows = 0;

        if (deleter.getDataList()
                   .size() > 0) {
            //批量操作时需要启用事务
            boolean needTransaction = deleter.getDataList()
                                             .size() > 1
                    && !ado.isTransactionAlreadyExisting();
            if (needTransaction)
                ado.beginTransaction();

            try {
                //删除指定数据
                for (Object data : deleter.getDataList()) {
                    String script = currentScript(false,
                                                  data);
                    String msId = getMSId();

                    int currentRows = aop.invokeWithAop(() -> this.ado.delete(getSqlSession(),
                                                                              msId,
                                                                              script,
                                                                              null,
                                                                              deleter.getParameter(),
                                                                              config.getNameConvertType()),
                                                        CurdType.删除,
                                                        config.getName(),
                                                        script,
                                                        deleter.getParameter(),
                                                        deleter.getEntityType(),
                                                        deleter.getDtoType());

                    if (currentRows < 0) {
                        throw new ModuleException(Strings.getRowsDataException(currentRows));
                    } else if (currentRows > 0) rows++;
                }

                if (needTransaction)
                    ado.transactionCommit();
            } catch (Exception ex) {
                if (needTransaction)
                    ado.transactionRollback();

                throw ex;
            }
        } else {
            //根据条件删除
            if (deleter.getWithWhereSQLs()
                       .size() == 0
                    && where.getDynamicFilters()
                            .size() == 0)
                throw new ModuleException(Strings.getDeleteOperationNeedDataOrCondition());

            String script = currentScript(false,
                                          null);
            String msId = getMSId();

            int currentRows = aop.invokeWithAop(() -> this.ado.delete(getSqlSession(),
                                                                      msId,
                                                                      script,
                                                                      null,
                                                                      deleter.getParameter(),
                                                                      config.getNameConvertType()),
                                                CurdType.删除,
                                                config.getName(),
                                                script,
                                                deleter.getParameter(),
                                                deleter.getEntityType(),
                                                deleter.getDtoType());
            if (currentRows < 0) {
                return currentRows;
            } else if (currentRows > 0) rows++;
        }

        return rows;
    }
}
