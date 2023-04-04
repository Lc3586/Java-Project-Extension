package project.extension.mybatis.edge.model;

import lombok.Data;

/**
 * 动态过滤操作条件
 *
 * @author LCTR
 * @date 2022-05-17
 */
@Data
public class DynamicFilterOperation {
    public DynamicFilterOperation() {

    }

    /**
     * @param operationSymbol 操作符
     * @param target          操作目标
     */
    public DynamicFilterOperation(OperationSymbol operationSymbol,
                                  DynamicFilterTarget target) {
        this.operationSymbol = operationSymbol;
        this.target = target;
    }

    /**
     * 操作符号
     */
    private OperationSymbol operationSymbol;

    /**
     * 操作目标
     */
    private DynamicFilterTarget target;
}
