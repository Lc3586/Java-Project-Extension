package project.extension.mybatis.edge.model;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * 动态过滤器表达式
 *
 * @author LCTR
 * @date 2022-05-17
 */
@Data
public class DynamicFilterExpression {
    public DynamicFilterExpression() {

    }

    public DynamicFilterExpression(DynamicFilterTarget start) {
        this.start = start;
    }

    /**
     * 起始目标
     */
    private DynamicFilterTarget start;

    /**
     * 操作集合
     */
    private final List<DynamicFilterOperation> operations = new ArrayList<>();
}
