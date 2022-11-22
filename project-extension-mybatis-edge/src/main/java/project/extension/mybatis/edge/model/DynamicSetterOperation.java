package project.extension.mybatis.edge.model;

/**
 * 动态更新操作条件
 *
 * @author LCTR
 * @date 2022-04-11
 */
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

    /**
     * 操作符号
     */
    public OperationSymbol getOperationSymbol() {
        return operationSymbol;
    }

    public void setOperationSymbol(OperationSymbol operationSymbol) {
        this.operationSymbol = operationSymbol;
    }

    /**
     * 操作符号
     */
    public DynamicSetterTarget getTarget() {
        return target;
    }

    public void setTarget(DynamicSetterTarget target) {
        this.target = target;
    }
}
