package project.extension.wechat.core.mp.handler;

import org.springframework.http.HttpStatus;
import org.springframework.lang.Nullable;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import project.extension.collections.CollectionsExtension;
import project.extension.standard.exception.ModuleException;
import project.extension.tuple.Tuple2;
import project.extension.wechat.config.MPConfig;
import project.extension.wechat.core.mp.standard.IWeChatMPService;
import project.extension.wechat.globalization.Strings;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * 微信验证签名中间件
 *
 * @author LCTR
 * @date 2023-03-17
 */
public class WeChatSignatureVerificationMiddleware
        implements HandlerInterceptor {
    private final static ConcurrentMap<String, Tuple2<MPConfig, IWeChatMPService>> keyMap = new ConcurrentHashMap<>();

    private final static ConcurrentMap<String, List<Pattern>> urlMap = new ConcurrentHashMap<>();

    public final static String SIGNATURE_PARAMETER = "signature";
    public final static String NONCE_PARAMETER = "nonce";
    public final static String TIMESTAMP_PARAMETER = "timestamp";
    public final static String ECHOSTR_PARAMETER = "echostr";

    /**
     * 获取地址标识
     *
     * @param mpConfig  公众号配置
     * @param mpService 微信公众号服务
     */
    private static String getUrlKey(MPConfig mpConfig,
                                    @Nullable
                                            IWeChatMPService mpService) {
        String key = mpConfig.getName();

        if (!keyMap.containsKey(key))
            keyMap.putIfAbsent(key,
                               new Tuple2<>(mpConfig,
                                            mpService));

        return key;
    }

    /**
     * 设置
     *
     * @param mpConfig  公众号配置
     * @param mpService 微信公众号服务
     */
    public static void setup(MPConfig mpConfig,
                             IWeChatMPService mpService) {
        try {
            String key = getUrlKey(mpConfig,
                                   mpService);

            if (!urlMap.containsKey(key))
                urlMap.putIfAbsent(key,
                                   new ArrayList<>());

            urlMap.get(key)
                  .add(Pattern.compile(mpConfig.getTokenVerificationUrl()));

            if (CollectionsExtension.anyPlus(mpConfig.getSignatureVerificationUrls()))
                urlMap.get(key)
                      .addAll(mpConfig.getSignatureVerificationUrls()
                                      .stream()
                                      .map(Pattern::compile)
                                      .collect(Collectors.toList()));
        } catch (Exception ex) {
            throw new ModuleException(Strings.getSetupMiddlewareFailed(WeChatSignatureVerificationMiddleware.class.getName()),
                                      ex);
        }
    }

    /**
     * 获取请求中的参数
     *
     * @param request 请求对象
     * @param name    参数名
     */
    private static String getParameter(HttpServletRequest request,
                                       String name) {
        return request.getParameter(name);
    }

    @Override
    public boolean preHandle(
            @Nullable
                    HttpServletRequest request,
            @Nullable
                    HttpServletResponse response,
            @Nullable
                    Object handler) {
        try {
            //是否结束请求
            boolean end = false;
            //输出信息
            String responseData = "";

            String uri = request.getRequestURI();
            if (StringUtils.hasText(uri)) {
                for (String key : urlMap.keySet()) {
                    List<Pattern> urls = urlMap.get(key);
                    if (urls.stream()
                            .noneMatch(x -> x.matcher(uri)
                                             .find()))
                        continue;

                    end = true;

                    Tuple2<MPConfig, IWeChatMPService> keyInfo = keyMap.get(key);

                    if (!keyInfo.b.getMPService()
                                  .checkSignature(getParameter(request,
                                                               TIMESTAMP_PARAMETER),
                                                  getParameter(request,
                                                               NONCE_PARAMETER),
                                                  getParameter(request,
                                                               SIGNATURE_PARAMETER))) {
                        //消息签名不正确，说明不是公众平台发过来的消息
                        response.setStatus(HttpStatus.FORBIDDEN.value());
                    }

                    //微信服务器发送的token验证请求
                    if (uri.contains(keyInfo.a.getTokenVerificationUrl())) {
                        String echostr = getParameter(request,
                                                      ECHOSTR_PARAMETER);

                        if (StringUtils.hasText(echostr)) {
                            //回显echostr
                            response.setStatus(HttpStatus.OK.value());
                            responseData = echostr;
                        } else
                            response.setStatus(HttpStatus.NO_CONTENT.value());
                    }
                }
            }

            if (end && StringUtils.hasText(responseData)) {
                response.setContentType("text/html;charset=utf-8");
                response.getWriter()
                        .println(responseData);
            }

            return !end;
        } catch (Exception ex) {
            throw new ModuleException(Strings.getMiddlewarePreHandleFailed(WeChatSignatureVerificationMiddleware.class.getName()),
                                      ex);
        }
    }
}
