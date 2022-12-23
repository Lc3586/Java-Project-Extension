package project.extension.mybatis.edge.aop;

import org.springframework.stereotype.Repository;
import project.extension.action.IAction1;

/**
 * 支持AOP编程
 *
 * @author LCTR
 * @date 2022-07-14
 */
@Repository
public interface INaiveAop {
    /**
     * 监听执行增删改查操作的命令之前触发的事件
     *
     * @param action 事件处理逻辑
     */
    INaiveAop curdBeforeAddListener(IAction1<CurdBeforeEventArgs> action);

    /**
     * 取消监听执行增删改查操作的命令之前触发的事件
     *
     * @param action 事件处理逻辑
     */
    INaiveAop curdBeforeRemoveListener(IAction1<CurdBeforeEventArgs> action);

    /**
     * 监听执行增删改查操作的命令之后触发的事件
     *
     * @param action 事件处理逻辑
     */
    INaiveAop curdAfterAddListener(IAction1<CurdAfterEventArgs> action);

    /**
     * 取消监听执行增删改查操作的命令之后触发的事件
     *
     * @param action 事件处理逻辑
     */
    INaiveAop curdAfterRemoveListener(IAction1<CurdAfterEventArgs> action);

    /**
     * 监听获取映射对象之后触发的事件
     *
     * @param action 事件处理逻辑
     */
    INaiveAop mappedStatementAddListener(IAction1<MappedStatementArgs> action);

    /**
     * 取消监听获取映射对象之后触发的事件
     *
     * @param action 事件处理逻辑
     */
    INaiveAop mappedStatementRemoveListener(IAction1<MappedStatementArgs> action);

    /**
     * 监听执行事务操作之前触发的事件
     *
     * @param action 事件处理逻辑
     */
    INaiveAop traceBeforeAddListener(IAction1<TraceBeforeEventArgs> action);

    /**
     * 取消监听执行事务操作之前触发的事件
     *
     * @param action 事件处理逻辑
     */
    INaiveAop traceBeforeRemoveListener(IAction1<TraceBeforeEventArgs> action);

    /**
     * 监听执行事务操作之后触发的事件
     *
     * @param action 事件处理逻辑
     */
    INaiveAop traceAfterAddListener(IAction1<TraceAfterEventArgs> action);

    /**
     * 取消监听执行事务操作之后触发的事件
     *
     * @param action 事件处理逻辑
     */
    INaiveAop traceAfterRemoveListener(IAction1<TraceAfterEventArgs> action);
}
