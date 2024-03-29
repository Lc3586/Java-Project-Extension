package project.extension.mybatis.edge.aop;

import lombok.Data;
import org.apache.ibatis.mapping.MappedStatement;

/**
 * 创建映射对象之后触发的事件的参数
 *
 * @author LCTR
 * @date 2022-07-14
 */
@Data
public class MappedStatementArgs {
    public MappedStatementArgs(Boolean preexisting,
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
}
