package project.extension.mybatis.edge.core.driver;

import org.apache.ibatis.parsing.GenericTokenParser;
import org.apache.ibatis.parsing.TokenHandler;
import org.apache.ibatis.scripting.ScriptingException;
import org.apache.ibatis.scripting.xmltags.DynamicContext;
import org.apache.ibatis.scripting.xmltags.SqlNode;
import org.apache.ibatis.type.SimpleTypeRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import project.extension.mybatis.edge.extention.SqlExtension;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

/**
 * 初级Sql文本节点处理器
 *
 * @author LCTR
 * @date 2022-03-24
 * @deprecated 请使用RepositoryProvider来操作数据库
 */
@Deprecated
public class NaiveTextSqlNode
        implements SqlNode {
    private final String text;
    private final Pattern injectionFilter;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public NaiveTextSqlNode(String text) {
        this(text,
                null);
    }

    public NaiveTextSqlNode(String text,
                            Pattern injectionFilter) {
        this.text = text;
        this.injectionFilter = injectionFilter;
    }

    public boolean isDynamic() {
        NaiveTextSqlNode.DynamicCheckerTokenParser checker = new NaiveTextSqlNode.DynamicCheckerTokenParser();
        List<GenericTokenParser> parsers = this.createParser(checker);
        for (GenericTokenParser parser : parsers) {
            parser.parse(this.text);
            if (checker.isDynamic())
                return true;
        }
        return false;
    }

    public boolean apply(DynamicContext context) {
        List<GenericTokenParser> parsers = this.createParser(new NaiveTextSqlNode.BindingTokenParser(context,
                this.injectionFilter,
                this.logger));

        String sql = this.text;
        for (GenericTokenParser parser : parsers) {
            sql = parser.parse(sql);
        }

        context.appendSql(sql);

        return true;
    }

    private List<GenericTokenParser> createParser(TokenHandler handler) {
        return Arrays.asList(
                new GenericTokenParser("${",
                        "}",
                        handler),
                new GenericTokenParser("￥{",
                        "}",
                        handler));
    }

    private static class DynamicCheckerTokenParser
            implements TokenHandler {
        private boolean isDynamic;

        public DynamicCheckerTokenParser() {
        }

        public boolean isDynamic() {
            return this.isDynamic;
        }

        public String handleToken(String content) {
            this.isDynamic = true;
            return null;
        }
    }

    private static class BindingTokenParser
            implements TokenHandler {
        private final DynamicContext context;
        private final Pattern injectionFilter;
        private final Logger logger;

        public BindingTokenParser(DynamicContext context,
                                  Pattern injectionFilter,
                                  Logger logger) {
            this.context = context;
            this.injectionFilter = injectionFilter;
            this.logger = logger;
        }

        public String handleToken(String content) {
            Object methodParameter = this.context.getBindings()
                    .get("_parameter");
            if (methodParameter == null) {
                this.context.getBindings()
                        .put("value",
                                null);
            } else if (SimpleTypeRegistry.isSimpleType(methodParameter.getClass())) {
                this.context.getBindings()
                        .put("value",
                                methodParameter);
            }

            String srtValue = null;

            try {
                srtValue = SqlExtension.getDynamicSql(content,
                                                      methodParameter);
            } catch (Throwable ex) {
                Exception newEx = new Exception(String.format("处理DynamicMethod失败, content: %s",
                        content),
                        ex);
                logger.error(newEx.getMessage(),
                        newEx);
            }

            if (!StringUtils.hasText(srtValue))
                srtValue = "  ";

            this.checkInjection(srtValue);
            return srtValue;
        }

        private void checkInjection(String value) {
            if (this.injectionFilter != null && !this.injectionFilter.matcher(value)
                    .matches()) {
                throw new ScriptingException("Invalid input. Please conform to regex" + this.injectionFilter.pattern());
            }
        }
    }
}
