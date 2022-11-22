package project.extension.mybatis.edge.core.provider.standard;

import project.extension.action.IAction1;
import project.extension.mybatis.edge.model.CurdAfterEventArg;
import project.extension.mybatis.edge.model.CurdBeforeEventArg;
import project.extension.mybatis.edge.model.MappedStatementArg;

/**
 * 支持AOP编程
 *
 * @author LCTR
 * @date 2022-07-14
 */
public interface IAop {
    /**
     * 监听执行增删改查操作的命令之前触发的事件
     *
     * @param action 事件处理逻辑
     */
    IAop curdBeforeAddListener(IAction1<CurdBeforeEventArg> action);

    /**
     * 取消监听执行增删改查操作的命令之前触发的事件
     *
     * @param action 事件处理逻辑
     */
    IAop curdBeforeRemoveListener(IAction1<CurdBeforeEventArg> action);

    /**
     * 监听执行增删改查操作的命令之后触发的事件
     *
     * @param action 事件处理逻辑
     */
    IAop curdAfterAddListener(IAction1<CurdAfterEventArg> action);

    /**
     * 取消监听执行增删改查操作的命令之后触发的事件
     *
     * @param action 事件处理逻辑
     */
    IAop curdAfterRemoveListener(IAction1<CurdAfterEventArg> action);

    /**
     * 监听获取映射对象之后触发的事件
     *
     * @param action 事件处理逻辑
     */
    IAop mappedStatementAddListener(IAction1<MappedStatementArg> action);

    /**
     * 取消监听获取映射对象之后触发的事件
     *
     * @param action 事件处理逻辑
     */
    IAop mappedStatementRemoveListener(IAction1<MappedStatementArg> action);
}
