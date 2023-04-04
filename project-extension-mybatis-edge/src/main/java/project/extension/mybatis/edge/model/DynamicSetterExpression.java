package project.extension.mybatis.edge.model;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * 动态更新器表达式
 *
 * @author LCTR
 * @date 2022-04-14
 */
@Data
public class DynamicSetterExpression {
    public DynamicSetterExpression() {

    }

    public DynamicSetterExpression(DynamicSetterTarget start) {
        this.start = start;
    }

    /**
     * 起始目标
     */
    private DynamicSetterTarget start;

    /**
     * 操作集合
     */
    private final List<DynamicSetterOperation> operations = new ArrayList<>();
}
