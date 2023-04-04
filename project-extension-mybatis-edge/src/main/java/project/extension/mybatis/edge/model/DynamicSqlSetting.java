package project.extension.mybatis.edge.model;

import lombok.Data;
import project.extension.mybatis.edge.annotations.ExecutorSetting;

/**
 * 动态SQL设置
 *
 * @param <T> 实体类型
 * @author LCTR
 * @date 2022-03-26
 */
@ExecutorSetting
@Data
public class DynamicSqlSetting<T> {
    public DynamicSqlSetting() {

    }

    public DynamicSqlSetting(Class<T> entityType) {
        this(entityType,
             null);
    }

    public DynamicSqlSetting(Class<T> entityType,
                             Class<?> resultType) {
        this.entityType = entityType;
        this.resultType = resultType;
    }

    /**
     * 实体类型
     */
    @ExecutorSetting(ExecutorParameter.实体类型)
    private Class<T> entityType;

    /**
     * 数据类型
     */
    @ExecutorSetting(ExecutorParameter.数据类型)
    private Class<?> dataType;

    /**
     * 返回值类型
     */
    @ExecutorSetting(ExecutorParameter.返回值类型)
    private Class<?> resultType;
}
