package project.extension.openapi.fastjson;

import project.extension.collections.CollectionsExtension;
import com.alibaba.fastjson.serializer.PropertyFilter;
import project.extension.openapi.extention.SchemaExtension;

import java.util.Hashtable;
import java.util.List;

/**
 * Json属性过滤器
 *
 * @author LCTR
 * @date 2022-03-22
 */
public class JsonPropertyFilter
        implements PropertyFilter {
    private final Hashtable<String, List<String>> propertyDic;

    /**
     * @param type 架构类型
     */
    public JsonPropertyFilter(Class<?> type) {
        super();
        this.propertyDic = SchemaExtension.getOrNullForPropertyDic(type,
                                                                   true);
    }

    /**
     * @param type            架构类型
     * @param exceptionFields 特别输出的字段
     * @param ignoreFields    特别忽略的字段
     */
    public JsonPropertyFilter(Class<?> type,
                              Hashtable<String, List<String>> exceptionFields,
                              Hashtable<String, List<String>> ignoreFields) {
        super();
        this.propertyDic = SchemaExtension.getOrNullForPropertyDic(type,
                                                                   true,
                                                                   exceptionFields,
                                                                   ignoreFields);
    }

    /**
     * 执行判断
     *
     * @param source 源对象
     * @param name   字段名称
     * @param value  字段值
     * @return 是否创建该属性
     */
    @Override
    public boolean apply(Object source,
                         String name,
                         Object value) {
        if (source == null) return true;

        String typeName = source.getClass()
                                .getTypeName();
        if (!CollectionsExtension.anyPlus(propertyDic,
                                          x -> x.equals(typeName))) return true;

        return propertyDic.get(typeName)
                          .contains(name);
    }
}
