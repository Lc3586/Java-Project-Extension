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
    INaiveAop addListenerBeforeCurd(IAction1<BeforeCurdEventArgs> action);

    /**
     * 取消监听执行增删改查操作的命令之前触发的事件
     *
     * @param action 事件处理逻辑
     */
    INaiveAop removeListenerBeforeCurd(IAction1<BeforeCurdEventArgs> action);

    /**
     * 监听执行增删改查操作的命令之后触发的事件
     *
     * @param action 事件处理逻辑
     */
    INaiveAop addListenerAfterCurd(IAction1<AfterCurdEventArgs> action);

    /**
     * 取消监听执行增删改查操作的命令之后触发的事件
     *
     * @param action 事件处理逻辑
     */
    INaiveAop removeListenerAfterCurd(IAction1<AfterCurdEventArgs> action);

    /**
     * 监听获取映射对象之后触发的事件
     *
     * @param action 事件处理逻辑
     */
    INaiveAop addListenerMappedStatement(IAction1<MappedStatementArgs> action);

    /**
     * 取消监听获取映射对象之后触发的事件
     *
     * @param action 事件处理逻辑
     */
    INaiveAop removeListenerMappedStatement(IAction1<MappedStatementArgs> action);

    /**
     * 监听执行事务操作之前触发的事件
     *
     * @param action 事件处理逻辑
     */
    INaiveAop addListenerBeforeTrace(IAction1<BeforeTraceEventArgs> action);

    /**
     * 取消监听执行事务操作之前触发的事件
     *
     * @param action 事件处理逻辑
     */
    INaiveAop removeListenerBeforeTrace(IAction1<BeforeTraceEventArgs> action);

    /**
     * 监听执行事务操作之后触发的事件
     *
     * @param action 事件处理逻辑
     */
    INaiveAop addListenerAfterTrace(IAction1<AfterTraceEventArgs> action);

    /**
     * 取消监听执行事务操作之后触发的事件
     *
     * @param action 事件处理逻辑
     */
    INaiveAop removeListenerAfterTrace(IAction1<AfterTraceEventArgs> action);

    /**
     * 监听数据源切换之后触发的事件
     *
     * @param action 事件处理逻辑
     */
    INaiveAop addListenerDataSourceChanged(IAction1<DataSourceChangedEventArgs> action);

    /**
     * 取消监听数据源切换之后触发的事件
     *
     * @param action 事件处理逻辑
     */
    INaiveAop removeListenerDataSourceChanged(IAction1<DataSourceChangedEventArgs> action);
}
