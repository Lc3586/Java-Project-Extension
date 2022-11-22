package project.extension.mybatis.core.provider.normal;

import org.apache.ibatis.session.SqlSession;
import project.extension.cryptography.MD5Utils;
import project.extension.mybatis.config.BaseConfig;
import project.extension.mybatis.core.driver.NaiveSqlSession;
import project.extension.mybatis.core.provider.standard.IAop;
import project.extension.mybatis.core.provider.standard.IInsert;
import project.extension.mybatis.extention.RepositoryExtension;
import project.extension.mybatis.extention.SqlSessionExtension;
import project.extension.mybatis.model.CurdType;
import project.extension.mybatis.model.InserterDTO;
import project.extension.tuple.Tuple2;

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
    private final BaseConfig config;

    private final SqlProvider sqlProvider;

    private final AopProvider aop;

    private final boolean withTransactional;

    private final InserterDTO inserter;

    private final Class<T> entityType;

    private final String msIdPrefix;

    public Insert(BaseConfig config,
                  SqlProvider sqlProvider,
                  IAop aop,
                  Class<T> entityType,
                  boolean withTransactional) {
        this.config = config;
        this.sqlProvider = sqlProvider;
        this.aop = (AopProvider) aop;
        this.withTransactional = withTransactional;
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
        Tuple2<String, String> table = RepositoryExtension.getTableName(entityType,
                                                                        config.getNameConvertType());
        inserter.setSchema(table.a);
        inserter.setTableName(table.b);
        inserter.setEntityType(entityType);
        inserter.setDtoType(entityType);
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
        inserter.getParameter()
                .clear();
        return sqlProvider.inserter2Script(inserter,
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
            Exception {
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
            Exception {
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
            Exception {
        if (inserter.getDataList()
                    .size() == 0)
            return 0;

        int rows = 0;

        for (Object data : inserter.getDataList()) {
            String script = currentScript(false,
                                          data);
            String msId = getMSId(MD5Utils.hash(script),
                                  inserter.getDtoType()
                                          .getTypeName());

            //批量插入
            int currentRows = aop.invokeWithAop(() -> SqlSessionExtension.insert(
                                                        getSqlSession(),
                                                        msId,
                                                        script,
                                                        inserter.getParameter(),
                                                        config.getNameConvertType()),
                                                CurdType.插入,
                                                config.getDataSource(),
                                                script,
                                                inserter.getParameter(),
                                                inserter.getEntityType(),
                                                inserter.getDtoType());
            if (currentRows < 0) {
                return currentRows;
            } else if (currentRows > 0) rows++;
        }

        return rows;
    }
}
