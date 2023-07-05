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
    private final List<IAction1<CurdBeforeEventArgs>> curdBeforeActions = new ArrayList<>();

    /**
     * 监听执行增删改查操作的命令之后触发的事件集合
     */
    private final List<IAction1<CurdAfterEventArgs>> curdAfterActions = new ArrayList<>();

    /**
     * 监听获取映射对象之后触发的事件集合
     */
    private final List<IAction1<MappedStatementArgs>> mappedStatementActions = new ArrayList<>();

    /**
     * 监听执行事务操作之前触发的事件集合
     */
    private final List<IAction1<TraceBeforeEventArgs>> traceBeforeActions = new ArrayList<>();

    /**
     * 监听执行事务操作之后触发的事件集合
     */
    private final List<IAction1<TraceAfterEventArgs>> traceAfterActions = new ArrayList<>();

    @Override
    public INaiveAop curdBeforeAddListener(IAction1<CurdBeforeEventArgs> action) {
        if (!curdBeforeActions.contains(action))
            curdBeforeActions.add(action);
        return this;
    }

    @Override
    public INaiveAop curdBeforeRemoveListener(IAction1<CurdBeforeEventArgs> action) {
        curdBeforeActions.remove(action);
        return this;
    }

    @Override
    public INaiveAop curdAfterAddListener(IAction1<CurdAfterEventArgs> action) {
        if (!curdAfterActions.contains(action))
            curdAfterActions.add(action);
        return this;
    }

    @Override
    public INaiveAop curdAfterRemoveListener(IAction1<CurdAfterEventArgs> action) {
        curdAfterActions.remove(action);
        return this;
    }

    @Override
    public INaiveAop mappedStatementAddListener(IAction1<MappedStatementArgs> action) {
        if (!mappedStatementActions.contains(action))
            mappedStatementActions.add(action);
        return this;
    }

    @Override
    public INaiveAop mappedStatementRemoveListener(IAction1<MappedStatementArgs> action) {
        mappedStatementActions.remove(action);
        return this;
    }

    @Override
    public INaiveAop traceBeforeAddListener(IAction1<TraceBeforeEventArgs> action) {
        if (!traceBeforeActions.contains(action))
            traceBeforeActions.add(action);
        return this;
    }

    @Override
    public INaiveAop traceBeforeRemoveListener(IAction1<TraceBeforeEventArgs> action) {
        traceBeforeActions.remove(action);
        return this;
    }

    @Override
    public INaiveAop traceAfterAddListener(IAction1<TraceAfterEventArgs> action) {
        if (!traceAfterActions.contains(action))
            traceAfterActions.add(action);
        return this;
    }

    @Override
    public INaiveAop traceAfterRemoveListener(IAction1<TraceAfterEventArgs> action) {
        traceAfterActions.remove(action);
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
        CurdBeforeEventArgs beforeEventArg =
                this.curdBefore(new CurdBeforeEventArgs(msId,
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
            this.curdAfter(new CurdAfterEventArgs(beforeEventArg,
                                                  exception,
                                                  result));
        }
    }

    /**
     * 执行增删改查操作的命令之前
     *
     * @param arg 参数
     */
    public CurdBeforeEventArgs curdBefore(CurdBeforeEventArgs arg) {
        curdBeforeActions.forEach(x -> {
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
    public CurdAfterEventArgs curdAfter(CurdAfterEventArgs arg) {
        curdAfterActions.forEach(x -> {
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
    public TraceBeforeEventArgs traceBefore(TraceBeforeEventArgs arg) {
        traceBeforeActions.forEach(x -> {
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
    public TraceAfterEventArgs traceAfter(TraceAfterEventArgs arg) {
        traceAfterActions.forEach(x -> {
            try {
                x.invoke(arg);
            } catch (Exception ignored) {

            }
        });
        return arg;
    }


}
