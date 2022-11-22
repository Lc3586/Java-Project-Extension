package project.extension.mybatis.model;

import project.extension.mybatis.annotations.ExecutorSetting;

/**
 * 动态SQL设置
 *
 * @param <T> 实体类型
 * @author LCTR
 * @date 2022-03-26
 */
@ExecutorSetting
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

    @ExecutorSetting(ExecutorParameter.实体类型)
    private Class<T> entityType;

    @ExecutorSetting(ExecutorParameter.数据类型)
    private Class<?> dataType;

    @ExecutorSetting(ExecutorParameter.返回值类型)
    private Class<?> resultType;

    /**
     * 实体类型
     */
    public Class<T> getEntityType() {
        return entityType;
    }

    public void setEntityType(Class<T> entityType) {
        this.entityType = entityType;
    }

    /**
     * 数据类型
     */
    public Class<?> getDataType() {
        return dataType;
    }

    public void setDataType(Class<?> dataType) {
        this.dataType = dataType;
    }

    /**
     * 返回值类型
     */
    public Class<?> getResultType() {
        return resultType;
    }

    public void setResultType(Class<?> resultType) {
        this.resultType = resultType;
    }
}
