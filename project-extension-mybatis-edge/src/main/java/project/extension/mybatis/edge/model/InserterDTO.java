package project.extension.mybatis.edge.model;

import lombok.Data;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 数据插入设置
 *
 * @author LCTR
 * @date 2022-04-02
 */
@Data
public class InserterDTO {
    /**
     * 模式名
     */
    private String schema;

    /**
     * 数据表名
     */
    private String tableName;

    /**
     * 数据表别名
     */
    private String alias;

    /**
     * 包括的字段
     */
    private final List<String> exceptionFieldNames = new ArrayList<>();

    /**
     * 忽略的字段
     */
    private final List<String> ignoreFieldNames = new ArrayList<>();

    /**
     * 实体类型
     */
    private Class<?> entityType;

    /**
     * 业务模型类型
     */
    private Class<?> dtoType;

    /**
     * 主标签等级
     */
    private Integer mainTagLevel = 0;

    /**
     * 自定义标签
     */
    private final List<String> customTags = new ArrayList<>();

    /**
     * 数据集合
     */
    private final List<Object> dataList = new ArrayList<>();

    /**
     * 全部参数
     * <p>不可手动设置null值</p>
     */
    private final Map<String, Object> parameter = new HashMap<>();

    /**
     * 输出参数
     */
    private final Map<String, Field> outParameter = new HashMap<>();

    /**
     * 输入输出参数
     */
    private final Map<String, Field> inOutParameter = new HashMap<>();
}
