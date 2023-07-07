package project.extension.mybatis.edge.aop;

import org.springframework.stereotype.Component;
import project.extension.action.IAction1;
import project.extension.func.IFunc0;
import project.extension.mybatis.edge.model.CurdType;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * AOP构造器
 *
 * @author LCTR
 * @date 2022-07-14
 */
@Component
public class NaiveAopProvider
        implements INaiveAop {
    /**
     * 监听执行增删改查操作的命令之前触发的事件集合
     */
    private final List<IAction1<BeforeCurdEventArgs>> beforeCurdActions = new ArrayList<>();

    /**
     * 监听执行增删改查操作的命令之后触发的事件集合
     */
    private final List<IAction1<AfterCurdEventArgs>> afterCurdActions = new ArrayList<>();

    /**
     * 监听获取映射对象之后触发的事件集合
     */
    private final List<IAction1<MappedStatementArgs>> mappedStatementActions = new ArrayList<>();

    /**
     * 监听执行事务操作之前触发的事件集合
     */
    private final List<IAction1<BeforeTraceEventArgs>> beforeTraceActions = new ArrayList<>();

    /**
     * 监听执行事务操作之后触发的事件集合
     */
    private final List<IAction1<AfterTraceEventArgs>> afterTraceActions = new ArrayList<>();

    /**
     * 监听数据源切换之后触发的事件集合
     */
    private final List<IAction1<DataSourceChangedEventArgs>> dataSourceChangedActions = new ArrayList<>();

    @Override
    public INaiveAop addListenerBeforeCurd(IAction1<BeforeCurdEventArgs> action) {
        if (!beforeCurdActions.contains(action))
            beforeCurdActions.add(action);
        return this;
    }

    @Override
    public INaiveAop removeListenerBeforeCurd(IAction1<BeforeCurdEventArgs> action) {
        beforeCurdActions.remove(action);
        return this;
    }

    @Override
    public INaiveAop addListenerAfterCurd(IAction1<AfterCurdEventArgs> action) {
        if (!afterCurdActions.contains(action))
            afterCurdActions.add(action);
        return this;
    }

    @Override
    public INaiveAop removeListenerAfterCurd(IAction1<AfterCurdEventArgs> action) {
        afterCurdActions.remove(action);
        return this;
    }

    @Override
    public INaiveAop addListenerMappedStatement(IAction1<MappedStatementArgs> action) {
        if (!mappedStatementActions.contains(action))
            mappedStatementActions.add(action);
        return this;
    }

    @Override
    public INaiveAop removeListenerMappedStatement(IAction1<MappedStatementArgs> action) {
        mappedStatementActions.remove(action);
        return this;
    }

    @Override
    public INaiveAop addListenerBeforeTrace(IAction1<BeforeTraceEventArgs> action) {
        if (!beforeTraceActions.contains(action))
            beforeTraceActions.add(action);
        return this;
    }

    @Override
    public INaiveAop removeListenerBeforeTrace(IAction1<BeforeTraceEventArgs> action) {
        beforeTraceActions.remove(action);
        return this;
    }

    @Override
    public INaiveAop addListenerAfterTrace(IAction1<AfterTraceEventArgs> action) {
        if (!afterTraceActions.contains(action))
            afterTraceActions.add(action);
        return this;
    }

    @Override
    public INaiveAop removeListenerAfterTrace(IAction1<AfterTraceEventArgs> action) {
        afterTraceActions.remove(action);
        return this;
    }

    @Override
    public INaiveAop addListenerDataSourceChanged(IAction1<DataSourceChangedEventArgs> action) {
        if (!dataSourceChangedActions.contains(action))
            dataSourceChangedActions.add(action);
        return this;
    }

    @Override
    public INaiveAop removeListenerDataSourceChanged(IAction1<DataSourceChangedEventArgs> action) {
        afterTraceActions.remove(action);
        return this;
    }

    /**
     * 在支持AOP的方式下执行
     *
     * @param fun       方法
     * @param sql       SQL语句
     * @param <TResult> 返回数据类型
     * @return 返回数据
     */
    public <TResult> TResult invokeWithAop(IFunc0<TResult> fun,
                                           String msId,
                                           CurdType type,
                                           String dataSource,
                                           String sql,
                                           Map<String, Object> parameter,
                                           Class<?> entityType,
                                           Class<?> dtoType) {
        BeforeCurdEventArgs beforeEventArg =
                this.beforeCurd(new BeforeCurdEventArgs(msId,
                                                        type,
                                                        entityType,
                                                        dtoType,
                                                        dataSource,
                                                        sql,
                                                        parameter));
        Exception exception = null;
        TResult result = null;

        try {
            result = fun.invoke();
            return result;
        } catch (Exception ex) {
            exception = ex;
            throw ex;
        } finally {
            this.afterCurd(new AfterCurdEventArgs(beforeEventArg,
                                                  exception,
                                                  result));
        }
    }

    /**
     * 执行增删改查操作的命令之前
     *
     * @param arg 参数
     */
    public BeforeCurdEventArgs beforeCurd(BeforeCurdEventArgs arg) {
        beforeCurdActions.forEach(x -> {
            try {
                x.invoke(arg);
            } catch (Exception ignored) {

            }
        });
        return arg;
    }

    /**
     * 执行增删改查操作的命令之后
     *
     * @param arg 参数
     */
    public AfterCurdEventArgs afterCurd(AfterCurdEventArgs arg) {
        afterCurdActions.forEach(x -> {
            try {
                x.invoke(arg);
            } catch (Exception ignored) {

            }
        });
        return arg;
    }

    /**
     * 获取映射对象之后
     *
     * @param arg 参数
     */
    public MappedStatementArgs mappedStatement(MappedStatementArgs arg) {
        mappedStatementActions.forEach(x -> {
            try {
                x.invoke(arg);
            } catch (Exception ignored) {

            }
        });
        return arg;
    }

    /**
     * 执行事务操作之前
     *
     * @param arg 参数
     */
    public BeforeTraceEventArgs beforeTrace(BeforeTraceEventArgs arg) {
        beforeTraceActions.forEach(x -> {
            try {
                x.invoke(arg);
            } catch (Exception ignored) {

            }
        });
        return arg;
    }

    /**
     * 执行事务操作之后
     *
     * @param arg 参数
     */
    public AfterTraceEventArgs afterTrace(AfterTraceEventArgs arg) {
        afterTraceActions.forEach(x -> {
            try {
                x.invoke(arg);
            } catch (Exception ignored) {

            }
        });
        return arg;
    }

    /**
     * 切换数据源
     *
     * @param arg 参数
     */
    public DataSourceChangedEventArgs dataSourceChanged(DataSourceChangedEventArgs arg) {
        dataSourceChangedActions.forEach(x -> {
            try {
                x.invoke(arg);
            } catch (Exception ignored) {

            }
        });
        return arg;
    }
}
