package project.extension.mybatis.edge.configure;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import project.extension.date.DateExtension;
import project.extension.mybatis.edge.aop.INaiveAop;

import java.util.stream.Collectors;

/**
 * mybatis拓展模块AOP支持类
 *
 * @author LCTR
 * @date 2023-03-02
 */
@Configuration
public class NaiveMybatisAop {
    /**
     * 日志组件
     */
    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * 添加监听
     *
     * @param aop AOP
     */
    @Autowired
    public void addListener(INaiveAop aop) {
        aop.curdBeforeAddListener(arg -> {
            //CURD操作执行之前触发
            logger.debug(String.format(
                    "\r\n\r\n>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>\r\n"
                            + "mybatis拓展模块：\r\n"
                            + "\t 即将执行\r\n"
                            + "\t 标识         : %s\r\n"
                            + "\t 操作类型     : %s\r\n"
                            + "\t 数据源       : %s\r\n"
                            + "\t SQL          : \r\n %s \r\n"
                            + "\t 参数         : \r\n %s \r\n"
                            + "\t 实体类型     : %s\r\n"
                            + "\t 业务模型类型 : %s\r\n"
                            + ">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>\r\n\r\n",
                    arg.getIdentifier()
                       .toString(),
                    arg.getCurdType()
                       .name(),
                    arg.getDataSource(),
                    arg.getSql(),
                    arg.getParameter()
                       .keySet()
                       .stream()
                       .map(x -> String.format("\t\t%s -> %s",
                                               x,
                                               arg.getParameter()
                                                  .get(x)
                                                  .toString()))
                       .collect(Collectors.joining("\r\n")),
                    arg.getEntityType()
                       .getTypeName(),
                    arg.getDtoType()
                       .getTypeName()));
        });

        aop.curdAfterAddListener(arg -> {
            //CURD操作执行之后触发
            logger.debug(String.format(
                    "\r\n\r\n√√√√√√√√√√√√√√√√√√√√√√√√√√√√√√√√√√√√√√√√√√√√√√√√√√√√√√√√√√√√√√√√√√√√√√√√√√√√√√√√√√√√√√√√√√√√√√√√√√√√\r\n"
                            + "mybatis拓展模块：\r\n"
                            + "\t 已经执行\r\n"
                            + "\t 标识         : %s\r\n"
                            + "\t 操作类型     : %s\r\n"
                            + "\t 耗时         : %s\r\n"
                            + "\t 数据源       : %s\r\n"
                            + "\t SQL          : \r\n\t\t %s \r\n"
                            + "\t 参数         : \r\n %s \r\n"
                            + "\t 实体类型     : %s\r\n"
                            + "\t 业务模型类型 : %s\r\n"
                            + "√√√√√√√√√√√√√√√√√√√√√√√√√√√√√√√√√√√√√√√√√√√√√√√√√√√√√√√√√√√√√√√√√√√√√√√√√√√√√√√√√√√√√√√√√√√√√√√√√√√√\r\n\r\n",
                    arg.getIdentifier()
                       .toString(),
                    arg.getCurdType()
                       .name(),
                    DateExtension.getTime(arg.getCosttime(),
                                          4),
                    arg.getDataSource(),
                    arg.getSql(),
                    arg.getParameter()
                       .keySet()
                       .stream()
                       .map(x -> String.format("\t\t%s -> %s",
                                               x,
                                               arg.getParameter()
                                                  .get(x)
                                                  .toString()))
                       .collect(Collectors.joining("\r\n")),
                    arg.getEntityType()
                       .getTypeName(),
                    arg.getDtoType()
                       .getTypeName()));
        });

        aop.mappedStatementAddListener(arg -> {
            //获取映射对象之后触发

        });
    }
}
