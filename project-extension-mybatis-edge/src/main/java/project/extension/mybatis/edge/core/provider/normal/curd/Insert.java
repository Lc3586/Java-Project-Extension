package project.extension.mybatis.edge.core.provider.normal.curd;

import org.apache.ibatis.session.SqlSession;
import project.extension.collections.CollectionsExtension;
import project.extension.ioc.IOCExtension;
import project.extension.mybatis.edge.aop.NaiveAopProvider;
import project.extension.mybatis.edge.config.DataSourceConfig;
import project.extension.mybatis.edge.core.ado.INaiveAdo;
import project.extension.mybatis.edge.aop.INaiveAop;
import project.extension.mybatis.edge.core.mapper.EntityTypeHandler;
import project.extension.mybatis.edge.core.provider.normal.SqlProvider;
import project.extension.mybatis.edge.core.provider.standard.curd.IInsert;
import project.extension.mybatis.edge.globalization.Strings;
import project.extension.mybatis.edge.model.CurdType;
import project.extension.mybatis.edge.model.InserterDTO;
import project.extension.standard.exception.ModuleException;
import project.extension.tuple.Tuple2;
import project.extension.tuple.Tuple3;

import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 数据插入对象
 *
 * @param <T> 实体类型
 * @author LCTR
 * @date 2022-03-31
 */
public abstract class Insert<T>
        implements IInsert<T> {
    protected final DataSourceConfig config;

    protected final SqlProvider sqlProvider;

    protected final INaiveAdo ado;

    protected final NaiveAopProvider aop;

    protected final InserterDTO inserter;

    protected final Class<T> entityType;

    protected final String msIdPrefix;

    public Insert(DataSourceConfig config,
                  SqlProvider sqlProvider,
                  INaiveAdo ado,
                  Class<T> entityType) {
        this.config = config;
        this.sqlProvider = sqlProvider;
        this.ado = ado;
        this.aop = (NaiveAopProvider) IOCExtension.applicationContext.getBean(INaiveAop.class);
        this.inserter = new InserterDTO();
        this.entityType = entityType;
        this.msIdPrefix = String.format("Insert:%s",
                                        entityType.getTypeName());
        initialization();
    }

    /**
     * 初始化
     */
    protected void initialization() {
        Tuple2<String, String> table = EntityTypeHandler.getTableName(entityType,
                                                                      config.getNameConvertType());
        inserter.setSchema(table.a);
        inserter.setTableName(table.b);
        inserter.setEntityType(entityType);
        inserter.setDtoType(entityType);
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
        inserter.getParameter()
                .clear();
        return sqlProvider.inserter2Script(inserter,
                                           noParameter,
                                           data);
    }

    /**
     * 插入数据
     * <p>支持一般数据库的自增列</p>
     *
     * @param msId   标识
     * @param script 脚本
     * @return 数据库受影响行数
     */
    protected int insert(String msId,
                         String script)
            throws
            ModuleException {
        Tuple3<Boolean, Field, String> identityFieldAndColumn = getIdentityFieldAndColumn();

        if (identityFieldAndColumn.a)
            inserter.getOutParameter()
                    .put(identityFieldAndColumn.b.getName(),
                         identityFieldAndColumn.b);

        return ado.insert(getSqlSession(),
                          msId,
                          script,
                          identityFieldAndColumn.a,
                          identityFieldAndColumn.a
                          ? identityFieldAndColumn.b.getName()
                          : null,
                          identityFieldAndColumn.a
                          ? identityFieldAndColumn.c
                          : null,
                          false,
                          null,
                          null,
                          null,
                          inserter.getParameter(),
                          inserter.getOutParameter(),
                          inserter.getInOutParameter(),
                          config.getNameConvertType());
    }

    /**
     * 将输出参数的值设置给数据
     *
     * @param data 数据
     */
    protected void setOutputValue(Object data) {
        if (CollectionsExtension.anyPlus(inserter.getOutParameter())) {
            for (Field field : inserter.getOutParameter()
                                       .values()) {
                setOutputParameterValue2Field(field,
                                              data);
            }
        }

        if (CollectionsExtension.anyPlus(inserter.getInOutParameter())) {
            for (Field field : inserter.getOutParameter()
                                       .values()) {
                setOutputParameterValue2Field(field,
                                              data);
            }
        }
    }

    /**
     * 获取自增列
     *
     * @return a: 字段, b: 列名, c: 类型
     */
    protected Tuple3<Boolean, Field, String> getIdentityFieldAndColumn() {
        Field identityField = EntityTypeHandler.getIdentityKeyField(inserter.getEntityType());
        if (identityField == null) return new Tuple3<>(false,
                                                       null,
                                                       null);
        return new Tuple3<>(true,
                            identityField,
                            EntityTypeHandler.getColumn(identityField,
                                                        config.getNameConvertType()));
    }

    /**
     * 将数据库返回的输出参数的值设置到指定字段
     *
     * @param field      字段
     * @param dataObject 数据对象
     */
    protected void setOutputParameterValue2Field(Field field,
                                                 Object dataObject) {
        try {
            field.setAccessible(true);
            Object value = inserter.getParameter()
                                   .get(field.getName());
            Class<?> fieldType = field.getType();
            Class<?> type = value.getClass();
            if (!fieldType.equals(type)) {
                if (fieldType.equals(byte.class) || fieldType.equals(Byte.class))
                    value = Byte.parseByte(value.toString());
                else if (fieldType.equals(short.class) || fieldType.equals(Short.class))
                    value = Short.parseShort(value.toString());
                else if (fieldType.equals(int.class) || fieldType.equals(Integer.class))
                    value = Integer.parseInt(value.toString());
                else if (fieldType.equals(long.class) || fieldType.equals(Long.class))
                    value = Long.parseLong(value.toString());
                else if (fieldType.equals(char.class)
                        || fieldType.equals(Character.class))
                    value = value.toString()
                                 .charAt(0);
                else if (fieldType.equals(String.class))
                    value = value.toString();
            }
            field.set(dataObject,
                      value);
        } catch (Exception ex) {
            throw new ModuleException(Strings.getSetOutParameterValue2FieldFailed(field.getName(),
                                                                                  field.getName()),
                                      ex);
        }
    }

    /**
     * 获取Sql会话
     */
    protected SqlSession getSqlSession() {
        return ado.getOrCreateSqlSession();
    }

    @Override
    public <T2> IInsert<T> appendData(T2 data) {
        inserter.getDataList()
                .add(data);
        return this;
    }

    @Override
    public <T2> IInsert<T> appendData(Collection<T2> data) {
        inserter.getDataList()
                .addAll(data);
        return this;
    }

    @Override
    public IInsert<T> include(String... fieldNames) {
        return include(Arrays.stream(fieldNames)
                             .collect(Collectors.toList()));
    }

    @Override
    public IInsert<T> include(Collection<String> fieldNames) {
        inserter.getExceptionFieldNames()
                .addAll(fieldNames);
        return this;
    }

    @Override
    public IInsert<T> ignore(String... fieldNames) {
        return ignore(Arrays.stream(fieldNames)
                            .collect(Collectors.toList()));
    }

    @Override
    public IInsert<T> ignore(Collection<String> fieldNames) {
        inserter.getIgnoreFieldNames()
                .addAll(fieldNames);
        return this;
    }

    @Override
    public IInsert<T> asTable(String tableName) {
        inserter.setTableName(tableName);
        return this;
    }

    @Override
    public Class<T> getEntityType() {
        return entityType;
    }

    @Override
    public <T2> IInsert<T> asDto(Class<T2> dtoType) {
        inserter.setDtoType(dtoType);
        return this;
    }

    @Override
    public IInsert<T> mainTagLevel(Integer level) {
        inserter.setMainTagLevel(level);
        return this;
    }

    @Override
    public IInsert<T> withAnOtherTag(String... tag) {
        inserter.getCustomTags()
                .addAll(Arrays.asList(tag));
        return this;
    }

    @Override
    public List<String> toSqlWithNoParameter()
            throws
            ModuleException {
        List<String> sqlList = new ArrayList<>();
        for (Object data : inserter.getDataList()) {
            sqlList.add(currentScript(true,
                                      data));
        }
        return sqlList;
    }

    @Override
    public List<Tuple2<String, Map<String, Object>>> toSql()
            throws
            ModuleException {
        List<Tuple2<String, Map<String, Object>>> sqlList = new ArrayList<>();
        for (Object data : inserter.getDataList()) {
            sqlList.add(new Tuple2<>(currentScript(true,
                                                   data),
                                     inserter.getParameter()));
        }
        return sqlList;
    }

    @Override
    public int executeAffrows()
            throws
            ModuleException {
        if (inserter.getDataList()
                    .size() == 0) return 0;

        int rows = 0;

        //批量操作时需要启用事务
        boolean needTransaction = inserter.getDataList()
                                          .size() > 1
                && !ado.isTransactionAlreadyExisting();

        if (needTransaction)
            ado.beginTransaction();

        try {
            for (Object data : inserter.getDataList()) {
                String script = currentScript(false,
                                              data);
                String msId = getMSId();

                //批量插入
                int currentRows = aop.invokeWithAop(() -> insert(msId,
                                                                 script),
                                                    msId,
                                                    CurdType.插入,
                                                    config.getName(),
                                                    script,
                                                    inserter.getParameter(),
                                                    inserter.getEntityType(),
                                                    inserter.getDtoType());

                if (currentRows < 0) {
                    throw new ModuleException(Strings.getRowsDataException(currentRows));
                } else if (currentRows > 0) {
                    rows++;

                    setOutputValue(data);
                }
            }

            if (needTransaction)
                ado.transactionCommit();
        } catch (Exception ex) {
            if (needTransaction)
                ado.transactionRollback();

            throw ex;
        }

        return rows;
    }
}
