package project.extension.mybatis.edge.aop;

import lombok.Data;
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
@Data
public class CurdBeforeEventArgs {
    public CurdBeforeEventArgs(String msId,
                               CurdType curdType,
                               Class<?> entityType,
                               Class<?> dtoType,
                               String dataSource,
                               String sql,
                               Map<String, Object> parameter) {
        this(msId,
             new StopWatch(),
             curdType,
             entityType,
             dtoType,
             dataSource,
             sql,
             parameter,
             new HashMap<>());
    }

    protected CurdBeforeEventArgs(String msId,
                                  StopWatch watch,
                                  CurdType curdType,
                                  Class<?> entityType,
                                  Class<?> dtoType,
                                  String dataSource,
                                  String sql,
                                  Map<String, Object> parameter,
                                  Map<String, Object> states) {
        this.msId = msId;
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
    private final String msId;

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
}
