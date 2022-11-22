package project.extension.openapi.fastjson;

import com.alibaba.fastjson2.JSONWriter;
import project.extension.collections.CollectionsExtension;
import com.alibaba.fastjson2.filter.PropertyPreFilter;
import project.extension.openapi.extention.SchemaExtension;

import java.util.Hashtable;
import java.util.List;

/**
 * Json属性预过滤器
 *
 * @author LCTR
 * @date 2022-03-22
 */
public class JsonPropertyPreFilter
        implements PropertyPreFilter,
                   com.alibaba.fastjson.serializer.PropertyPreFilter {
    private final Hashtable<String, List<String>> propertyDic;

    /**
     * @param type 架构类型
     */
    public JsonPropertyPreFilter(Class<?> type) {
        super();
        this.propertyDic = SchemaExtension.getOrNullForPropertyDic(type,
                                                                   true);
    }

    /**
     * @param type            架构类型
     * @param exceptionFields 特别输出的字段
     * @param ignoreFields    特别忽略的字段
     */
    public JsonPropertyPreFilter(Class<?> type,
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
     * @param writer 序列化器
     * @param source 源对象
     * @param name   字段名称
     * @return 是否创建该属性
     */
    @Override
    public boolean process(JSONWriter writer,
                           Object source,
                           String name) {
//        System.out.println(name);
        if (source == null) return true;

        String typeName = source.getClass()
                                .getTypeName();
        if (!CollectionsExtension.anyPlus(propertyDic,
                                          x -> x.equals(typeName))) return true;

//        System.out.println(propertyDic.get(typeName).contains(name));
        return propertyDic.get(typeName)
                          .contains(name);
    }
}
