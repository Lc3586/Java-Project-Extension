package project.extension.mybatis.edge.model;

import lombok.Data;

/**
 * 动态更新操作条件
 *
 * @author LCTR
 * @date 2022-04-11
 */
@Data
public class DynamicSetterOperation {
    public DynamicSetterOperation() {

    }

    /**
     * @param operationSymbol 操作符
     * @param target          操作目标
     */
    public DynamicSetterOperation(OperationSymbol operationSymbol, DynamicSetterTarget target) {
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
    private DynamicSetterTarget target;
}
