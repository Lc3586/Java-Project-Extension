package project.extension.mybatis.edge.core.ado;

import org.apache.ibatis.builder.xml.XMLMapperEntityResolver;
import org.apache.ibatis.executor.parameter.ParameterHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlSource;
import org.apache.ibatis.parsing.PropertyParser;
import org.apache.ibatis.parsing.XNode;
import org.apache.ibatis.parsing.XPathParser;
import org.apache.ibatis.scripting.LanguageDriver;
import org.apache.ibatis.scripting.defaults.DefaultParameterHandler;
import org.apache.ibatis.scripting.defaults.RawSqlSource;
import org.apache.ibatis.scripting.xmltags.DynamicSqlSource;
import org.apache.ibatis.scripting.xmltags.XMLScriptBuilder;
import org.apache.ibatis.session.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import project.extension.mybatis.edge.extention.ExecutorExtension;
import project.extension.mybatis.edge.model.DynamicMethod;

/**
 * 初级语言驱动
 *
 * @author LCTR
 * @date 2022-03-14
 * @deprecated 请使用RepositoryProvider来操作数据库
 */
@Deprecated
public class NaiveMixedLanguageDriver implements LanguageDriver {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public NaiveMixedLanguageDriver() {

    }

    /**
     * 分配配套的参数处理器
     *
     * @param mappedStatement
     * @param parameterObject
     * @param boundSql
     * @return
     */
    @Override
    public ParameterHandler createParameterHandler(MappedStatement mappedStatement, Object parameterObject, BoundSql boundSql) {
//        System.out.println("createParameterHandler");
        return new DefaultParameterHandler(mappedStatement, parameterObject, boundSql);
    }

    /**
     * 处理xml文件中的配置
     * <p>1、根据xml配置中的trim节点的属性自动生成sql语句的where、order by等部分</p>
     *
     * @param configuration
     * @param script
     * @param parameterType
     * @return
     */
    @Override
    public SqlSource createSqlSource(Configuration configuration, XNode script, Class<?> parameterType) {
//        System.out.println("createSqlSource XNode");

        XMLScriptBuilder builder = new XMLScriptBuilder(configuration, script, parameterType);
        return builder.parseScriptNode();

        //TODO 无法更改xml配置的内容，只能通过添加自定义的节点处理器的方式来实现
//        Node node = script.getNode();
//        String body = node.getTextContent();
//        Matcher matcher = pattern.matcher(body);
//        if (matcher.find()) {
//            StringBuilder sb = new StringBuilder("select id, category, keyword, `enable`, create_time from test_mapper_demo");
//            String changed = matcher.replaceAll(sb.toString());
//            node.setTextContent(changed);
//        }
//
//        NodeList children = script.getNode().getChildNodes();
//
//        for (int i = 0; i < children.getLength(); ++i) {
//            XNode child = script.newXNode(children.item(i));
//            Short type = child.getNode().getNodeType();
//            String content = child.getStringBody();
//            if (!child.getNode().getNodeName().equals("trim"))
//                continue;
//
//            switch (child.getStringAttribute("prefix")) {
//                case "dynamic":
//                    switch (child.getStringAttribute("suffix")) {
//                        case "select":
//                            SqlNode sqlNode = new StaticTextSqlNode("select id, category, keyword, `enable`, create_time from test_mapper_demo");
//
////                            script.getNode().replaceChild( ,child.getNode());
//                            break;
//                        default:
//                            continue;
//                    }
//                    break;
//                default:
//                    continue;
//            }
//        }
    }

    /**
     * 处理注解中的配置
     * <p>会根据以下注解自动生成sql语句</p>
     * <p>1、project.extension.annotations.OpenApiMainTag 设置主标签（可选）</p>
     * <p>1.1、可在@DynamicSetting注解的outherTags方法中指定其他主标签（可选）</p>
     * <p>1.2、可在添加了@ExecutorSetting注解并标记未ExecutorParameter.其他标签的字段中中指定其他主标签（可选）</p>
     * <p>2、project.extension.annotations.OpenApiSubTag 设置附属标签（可选）</p>
     * <p>3、project.extension.annotations.OpenApiSuperModel 标记架构父类模型（可选）</p>
     * <p>4、project.extension.annotations.OpenApiSchemaStrictMode 设置架构严格模式（可选，和project.extension.annotations.OpenApiSchema配合使用）</p>
     * <p>4、javax.persistence.Table 设置表名（可选，未设置时将使用类名）、架构（可选）</p>
     * <p>5、org.apache.ibatis.type.Alias 设置表名别名（可选，可在@Select的参数中特别指定）</p>
     * <p>6、tk.mybatis.mapper.annotation.ColumnType 设置列名（可选，未设置时将使用字段名）</p>
     *
     * <p>7、@MapperSetting注解使用示例：</p>
     * <p>7.1、@DynamicSetting(List.class)</p>
     * <p>7.2、@DynamicSetting(
     * <P>     resultType = List.class,</P>
     * <P>     outherTags = {"MainTagA", "MainTagB"})</p>
     *
     * @param configuration 配置
     * @param script        @Select中的值
     * @param parameterType 参数类型
     * @return
     */
    @Override
    public SqlSource createSqlSource(Configuration configuration, String script, Class<?> parameterType) {
//        System.out.println("createSqlSource String");

        switch (script) {
//            case DynamicMethod.Configuration:
//                if (!CollectionsExtension.anyPlus(configuration.getInterceptors(), x -> x.getClass().equals(NaivePlugin.class)))
////                    configuration.addInterceptor(new NaivePlugin());
//                return null;
            case DynamicMethod.BaseQueryList:
            case DynamicMethod.BaseSingle:
            case DynamicMethod.BaseQuerySingle:
            case DynamicMethod.BaseSingleByKey:
            case DynamicMethod.BaseInsert:
            case DynamicMethod.BaseBatchInsert:
            case DynamicMethod.BaseQueryInsert:
            case DynamicMethod.BaseUpdate:
            case DynamicMethod.BaseBatchUpdate:
            case DynamicMethod.BaseQueryUpdate:
            case DynamicMethod.BaseDelete:
            case DynamicMethod.BaseBatchDelete:
            case DynamicMethod.BaseQueryDelete:
            case DynamicMethod.BaseDeleteByKey:
            case DynamicMethod.BaseDeleteByKeys:

            case DynamicMethod.QueryList:
            case DynamicMethod.Single:
            case DynamicMethod.QuerySingle:
            case DynamicMethod.SingleByKey:
            case DynamicMethod.Insert:
            case DynamicMethod.BatchInsert:
            case DynamicMethod.QueryInsert:
            case DynamicMethod.Update:
            case DynamicMethod.BatchUpdate:
            case DynamicMethod.QueryUpdate:
            case DynamicMethod.Delete:
            case DynamicMethod.BatchDelete:
            case DynamicMethod.QueryDelete:
            case DynamicMethod.DeleteByKey:
            case DynamicMethod.DeleteByKeys:
                //动态查询语句占位符
                script = ExecutorExtension.getParameterString(script);
                break;
            default:
                break;
        }

        if (script.startsWith("<script>")) {
            XPathParser parser = new XPathParser(script, false, configuration.getVariables(), new XMLMapperEntityResolver());
            return this.createSqlSource(configuration, parser.evalNode("/script"), parameterType);
        } else {
            script = PropertyParser.parse(script, configuration.getVariables());
            NaiveTextSqlNode textSqlNode = new NaiveTextSqlNode(script);
            return (textSqlNode.isDynamic() ? new DynamicSqlSource(configuration, textSqlNode) : new RawSqlSource(configuration, script, parameterType));
        }
    }
}