package project.extension.openapi.model;

import java.util.List;

/**
 * 任何接口架构类型的基础接口
 *
 * @author LCTR
 * @date 2022-03-18
 */
public interface IOpenApiAny {
    /**
     * 获取数据结构类型
     */
    AnyType getAnyType();

    /**
     * 获取注释
     */
    List<String> getDescription();

    /**
     * 添加注释
     */
    void addDescription(String description);
}
