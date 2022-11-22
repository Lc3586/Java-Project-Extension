package project.extension.mybatis.model;

/**
 * 动态过滤操作条件
 *
 * @author LCTR
 * @date 2022-05-17
 */
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
    public DynamicFilterTarget getTarget() {
        return target;
    }

    public void setTarget(DynamicFilterTarget target) {
        this.target = target;
    }
}
