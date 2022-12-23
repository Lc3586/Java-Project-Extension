package project.extension.mybatis.edge.aop;

import org.apache.commons.lang3.time.StopWatch;
import project.extension.mybatis.edge.model.CurdType;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * 执行增删改查操作的命令之前触发的事件的参数
 *
 * @author LCTR
 * @date 2022-07-14
 */
public class CurdBeforeEventArgs {
    public CurdBeforeEventArgs(CurdType curdType,
                               Class<?> entityType,
                               Class<?> dtoType,
                               String dataSource,
                               String sql,
                               Map<String, Object> parameter) {
        this(UUID.randomUUID(),
             new StopWatch(),
             curdType,
             entityType,
             dtoType,
             dataSource,
             sql,
             parameter,
             new HashMap<>());
    }

    protected CurdBeforeEventArgs(UUID identifier,
                                  StopWatch watch,
                                  CurdType curdType,
                                  Class<?> entityType,
                                  Class<?> dtoType,
                                  String dataSource,
                                  String sql,
                                  Map<String, Object> parameter,
                                  Map<String, Object> states) {
        this.identifier = identifier;
        this.watch = watch;
        if (!this.watch.isStarted())
            this.watch.start();
        this.curdType = curdType;
        this.entityType = entityType;
        this.dtoType = dtoType;
        this.dataSource = dataSource;
        this.sql = sql;
        this.parameter = parameter;
        this.states = states;
    }

    /**
     * 标识
     * <p>可将 TraceBeforeEventArgs 与 TraceAfterEventArgs 进行匹配</p>
     */
    private final UUID identifier;

    /**
     * 计时器
     */
    private final StopWatch watch;

    /**
     * 操作类型
     */
    private final CurdType curdType;

    /**
     * 实体类型
     */
    private final Class<?> entityType;

    /**
     * 业务模型类型
     */
    private final Class<?> dtoType;

    /**
     * 数据源
     */
    public final String dataSource;

    /**
     * 执行的SQL语句
     */
    private final String sql;

    /**
     * SQL语句中的参数
     */
    private final Map<String, Object> parameter;

    /**
     * 状态
     */
    private final Map<String, Object> states;

    /**
     * 标识
     */
    public UUID getIdentifier() {
        return identifier;
    }

    /**
     * 计时器
     */
    protected StopWatch getWatch() {
        return watch;
    }

    /**
     * 操作类型
     */
    public CurdType getCurdType() {
        return curdType;
    }

    /**
     * 实体类型
     */
    public Class<?> getEntityType() {
        return entityType;
    }

    /**
     * 业务模型类型
     */
    public Class<?> getDtoType() {
        return dtoType;
    }

    /**
     * 数据源
     */
    public String getDataSource() {
        return dataSource;
    }

    /**
     * 执行的SQL语句
     */
    public String getSql() {
        return sql;
    }

    /**
     * SQL语句中的参数
     */
    public Map<String, Object> getParameter() {
        return parameter;
    }

    /**
     * 状态
     */
    public Map<String, Object> getStates() {
        return states;
    }
}
