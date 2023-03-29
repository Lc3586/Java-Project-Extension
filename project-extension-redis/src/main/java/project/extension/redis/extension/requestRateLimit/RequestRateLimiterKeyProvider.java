package project.extension.redis.extension.requestRateLimit;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.expression.MethodBasedEvaluationContext;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * 限制请求速率标识构造器
 *
 * @author LCTR
 * @date 2023-03-29
 */
@Component
public class RequestRateLimiterKeyProvider {
    private final ParameterNameDiscoverer discoverer = new DefaultParameterNameDiscoverer();

    private final ExpressionParser parser = new SpelExpressionParser();

    private final Logger logger = LoggerFactory.getLogger(RequestRateLimiterAspectHandler.class);

    public String getKey(JoinPoint joinPoint,
                         RequestRateLimiter requestRateLimiter) {
        Method method = getMethod(joinPoint);
        List<String> definitionKeys = getSpelDefinitionKey(requestRateLimiter.keys(),
                                                           method,
                                                           joinPoint.getArgs());
        List<String> keyList = new ArrayList<>(definitionKeys);
        return StringUtils.collectionToDelimitedString(keyList,
                                                       ".",
                                                       "",
                                                       "");
    }

    private Method getMethod(JoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        if (method.getDeclaringClass()
                  .isInterface()) {
            try {
                method = joinPoint.getTarget()
                                  .getClass()
                                  .getDeclaredMethod(signature.getName(),
                                                     method.getParameterTypes());
            } catch (Exception e) {
                logger.error(null,
                             e);
            }
        }
        return method;
    }

    private List<String> getSpelDefinitionKey(String[] definitionKeys,
                                              Method method,
                                              Object[] parameterValues) {
        List<String> definitionKeyList = new ArrayList<>();
        for (String definitionKey : definitionKeys) {
            if (definitionKey != null && !definitionKey.isEmpty()) {
                EvaluationContext context = new MethodBasedEvaluationContext(null,
                                                                             method,
                                                                             parameterValues,
                                                                             discoverer);
                String key = parser.parseExpression(definitionKey)
                                   .getValue(context)
                                   .toString();
                definitionKeyList.add(key);
            }
        }
        return definitionKeyList;
    }
}
