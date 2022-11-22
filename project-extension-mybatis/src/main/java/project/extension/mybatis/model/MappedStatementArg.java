package project.extension.mybatis.model;

import org.apache.ibatis.mapping.MappedStatement;

/**
 * 获取映射对象之后触发的事件的参数
 *
 * @author LCTR
 * @date 2022-07-14
 */
public class MappedStatementArg {
    public MappedStatementArg(Boolean preexisting,
                              MappedStatement mappedStatement) {
        this.preexisting = preexisting;
        this.mappedStatement = mappedStatement;
    }

    /**
     * 是否为先前就已创建的
     */
    private final Boolean preexisting;

    /**
     * 映射对象
     */
    private final MappedStatement mappedStatement;

    /**
     * 是否为先前就已创建的
     */
    public Boolean getPreexisting() {
        return preexisting;
    }

    /**
     * 映射对象
     */
    public MappedStatement getMappedStatement() {
        return mappedStatement;
    }
}
