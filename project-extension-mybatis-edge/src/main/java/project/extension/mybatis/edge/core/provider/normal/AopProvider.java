package project.extension.mybatis.edge.core.provider.normal;

import org.apache.commons.lang3.time.StopWatch;
import org.springframework.stereotype.Component;
import project.extension.action.IAction1;
import project.extension.func.IFunc0;
import project.extension.mybatis.edge.core.provider.standard.IAop;
import project.extension.mybatis.edge.model.CurdAfterEventArg;
import project.extension.mybatis.edge.model.CurdBeforeEventArg;
import project.extension.mybatis.edge.model.CurdType;
import project.extension.mybatis.edge.model.MappedStatementArg;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * AOP构造器
 *
 * @author LCTR
 * @date 2022-07-14
 */
@Component
public class AopProvider
        implements IAop {
    /**
     * 监听执行增删改查操作的命令之前触发的事件集合
     */
    private final List<IAction1<CurdBeforeEventArg>> curdBeforeActions = new ArrayList<>();

    /**
     * 监听执行增删改查操作的命令之后触发的事件集合
     */
    private final List<IAction1<CurdAfterEventArg>> curdAfterActions = new ArrayList<>();

    /**
     * 监听获取映射对象之后触发的事件集合
     */
    private final List<IAction1<MappedStatementArg>> mappedStatementActions = new ArrayList<>();

    @Override
    public IAop curdBeforeAddListener(IAction1<CurdBeforeEventArg> action) {
        if (!curdBeforeActions.contains(action))
            curdBeforeActions.add(action);
        return this;
    }

    @Override
    public IAop curdBeforeRemoveListener(IAction1<CurdBeforeEventArg> action) {
        curdBeforeActions.remove(action);
        return this;
    }

    @Override
    public IAop curdAfterAddListener(IAction1<CurdAfterEventArg> action) {
        if (!curdAfterActions.contains(action))
            curdAfterActions.add(action);
        return this;
    }

    @Override
    public IAop curdAfterRemoveListener(IAction1<CurdAfterEventArg> action) {
        curdAfterActions.remove(action);
        return this;
    }

    @Override
    public IAop mappedStatementAddListener(IAction1<MappedStatementArg> action) {
        if (!mappedStatementActions.contains(action))
            mappedStatementActions.add(action);
        return this;
    }

    @Override
    public IAop mappedStatementRemoveListener(IAction1<MappedStatementArg> action) {
        mappedStatementActions.remove(action);
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
                                           CurdType type,
                                           String dataSource,
                                           String sql,
                                           Map<String, Object> parameter,
                                           Class<?> entityType,
                                           Class<?> dtoType)
            throws
            Exception {
        CurdBeforeEventArg beforeEventArg =
                this.curdBefore(new CurdBeforeEventArg(UUID.randomUUID(),
                                                       new StopWatch(),
                                                       type,
                                                       entityType,
                                                       dtoType,
                                                       dataSource,
                                                       sql,
                                                       parameter,
                                                       null));
        Exception exception = null;
        TResult result = null;

        try {
            result = fun.invoke();
            return result;
        } catch (Exception ex) {
            exception = ex;
            throw ex;
        } finally {
            this.curdAfter(new CurdAfterEventArg(beforeEventArg,
                                                 exception,
                                                 result));
        }
    }

    /**
     * 执行增删改查操作的命令之前
     *
     * @param arg 参数
     */
    public CurdBeforeEventArg curdBefore(CurdBeforeEventArg arg) {
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
    public CurdAfterEventArg curdAfter(CurdAfterEventArg arg) {
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
    public MappedStatementArg mappedStatement(MappedStatementArg arg) {
        mappedStatementActions.forEach(x -> {
            try {
                x.invoke(arg);
            } catch (Exception ignored) {

            }
        });
        return arg;
    }
}
