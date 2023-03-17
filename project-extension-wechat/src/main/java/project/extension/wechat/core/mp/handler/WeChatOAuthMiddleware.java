package project.extension.wechat.core.mp.handler;

import me.chanjar.weixin.common.bean.WxOAuth2UserInfo;
import me.chanjar.weixin.common.bean.oauth2.WxOAuth2AccessToken;
import org.springframework.http.HttpMethod;
import org.springframework.lang.Nullable;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import project.extension.ioc.IOCExtension;
import project.extension.standard.exception.ModuleException;
import project.extension.tuple.Tuple3;
import project.extension.wechat.config.BaseConfig;
import project.extension.wechat.config.MPConfig;
import project.extension.wechat.core.mp.standard.IWeChatMPService;
import project.extension.wechat.globalization.Strings;
import project.extension.wechat.model.WeChatRedirectType;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * 微信网页授权中间件
 *
 * @author LCTR
 * @date 2023-03-16
 */
public class WeChatOAuthMiddleware
        implements HandlerInterceptor {
    public WeChatOAuthMiddleware(BaseConfig baseConfig) {
        this.baseConfig = baseConfig;
    }

    private final BaseConfig baseConfig;

    private IWeChatOAuthHandler oAuthHandler;

    private final static ConcurrentMap<String, Tuple3<WeChatRedirectType, MPConfig, IWeChatMPService>> keyMap = new ConcurrentHashMap<>();

    private final static ConcurrentMap<String, String> urlMap = new ConcurrentHashMap<>();

    public final static String BASE_SCOPE = "snsapi_base";
    public final static String USER_INFO_SCOPE = "snsapi_userinfo";
    public final static String STATE_PARAMETER = "state";
    public final static String CODE_PARAMETER = "code";

    /**
     * 获取处理类
     */
    private IWeChatOAuthHandler getOAuthHandler() {
        if (oAuthHandler == null)
            oAuthHandler = IOCExtension.tryGetBean(IWeChatOAuthHandler.class);
        if (oAuthHandler == null)
            throw new ModuleException(Strings.getHandlerUndefined(IWeChatOAuthHandler.class.getName()));
        return oAuthHandler;
    }

    /**
     * 获取地址标识
     *
     * @param mpConfig  公众号配置
     * @param mpService 微信公众号服务
     * @param type      类型
     */
    private static String getUrlKey(MPConfig mpConfig,
                                    @Nullable
                                            IWeChatMPService mpService,
                                    WeChatRedirectType type) {
        String key = String.format("%s-%s",
                                   mpConfig.getName(),
                                   type.toString());

        if (!keyMap.containsKey(key))
            keyMap.putIfAbsent(key,
                               new Tuple3<>(type,
                                            mpConfig,
                                            mpService));

        return key;
    }

    /**
     * 获取或创建请求中的state参数
     *
     * @param request     请求对象
     * @param create4Null 为空时自动创建
     */
    private static String getState(HttpServletRequest request,
                                   boolean create4Null) {
        String state = request.getParameter(STATE_PARAMETER);
        if (!StringUtils.hasText(state)) {
            if (create4Null)
                state = UUID.randomUUID()
                            .toString();
            else
                state = null;
        }
        return state;
    }

    /**
     * 获取或创建请求中的code参数
     *
     * @param request 请求对象
     */
    private static String getCode(HttpServletRequest request) {
        return request.getParameter(CODE_PARAMETER);
    }

    /**
     * 获取完整的回调地址
     *
     * @param redirectUrl 回调地址
     */
    private String getFullRedirectUrl(String redirectUrl) {
        return String.format("%s%s",
                             baseConfig.getRootUrl(),
                             redirectUrl);
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
            if (!StringUtils.hasText(mpConfig.getOAuthBaseUrl()))
                //自动生成基础授权接口地址
                mpConfig.setOAuthBaseUrl(String.format("/wechat/oauth2/base/api/%s",
                                                       UUID.randomUUID()));
            urlMap.putIfAbsent(getUrlKey(mpConfig,
                                         mpService,
                                         WeChatRedirectType.OAUTH2_BASE_API),
                               mpConfig.getOAuthBaseUrl());
            //自动生成基础授权回调地址
            urlMap.putIfAbsent(getUrlKey(mpConfig,
                                         mpService,
                                         WeChatRedirectType.OAUTH2_BASE_RETURN),
                               String.format("/wechat/oauth2/base/return/%s",
                                             UUID.randomUUID()));

            if (!StringUtils.hasText(mpConfig.getOAuthUserInfoUrl()))
                //自动生成身份信息授权接口地址
                mpConfig.setOAuthUserInfoUrl(String.format("/wechat/oauth2/userinfo/api/%s",
                                                           UUID.randomUUID()));
            urlMap.putIfAbsent(getUrlKey(mpConfig,
                                         mpService,
                                         WeChatRedirectType.OAUTH2_USER_INFO_API),
                               mpConfig.getOAuthUserInfoUrl());
            //自动生成身份信息授权回调地址
            urlMap.putIfAbsent(getUrlKey(mpConfig,
                                         mpService,
                                         WeChatRedirectType.OAUTH2_USER_INFO_RETURN),
                               String.format("/wechat/oauth2/userinfo/return/%s",
                                             UUID.randomUUID()));
        } catch (Exception ex) {
            throw new ModuleException(Strings.getSetupMiddlewareFailed(WeChatOAuthMiddleware.class.getName()),
                                      ex);
        }
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
            //重定向地址
            String redirectUrl = "";

            String uri = request.getRequestURI();
            if (request.getMethod()
                       .equals(HttpMethod.GET.toString())
                    && StringUtils.hasText(uri)) {
                for (String key : urlMap.keySet()) {
                    String url = urlMap.get(key);
                    if (!url.contains(uri))
                        continue;

                    Tuple3<WeChatRedirectType, MPConfig, IWeChatMPService> keyInfo = keyMap.get(key);

                    switch (keyInfo.a) {
                        case OAUTH2_BASE_API:
                            String base_redirectUrl = getFullRedirectUrl(urlMap.get(getUrlKey(keyInfo.b,
                                                                                              null,
                                                                                              WeChatRedirectType.OAUTH2_BASE_RETURN)));

                            redirectUrl = keyInfo.c.getMPService()
                                                   .getOAuth2Service()
                                                   .buildAuthorizationUrl(base_redirectUrl,
                                                                          BASE_SCOPE,
                                                                          getState(request,
                                                                                   true));

                            end = true;
                            break;
                        case OAUTH2_BASE_RETURN:
                            WxOAuth2AccessToken base_accessToken = keyInfo.c.getMPService()
                                                                            .getOAuth2Service()
                                                                            .getAccessToken(getCode(request));

                            redirectUrl = getOAuthHandler().Handler(request,
                                                                    keyInfo.b,
                                                                    base_accessToken,
                                                                    getState(request,
                                                                             false));
                            end = true;
                            break;
                        case OAUTH2_USER_INFO_API:
                            String userInfo_redirectUrl = getFullRedirectUrl(urlMap.get(getUrlKey(keyInfo.b,
                                                                                                  null,
                                                                                                  WeChatRedirectType.OAUTH2_USER_INFO_RETURN)));

                            redirectUrl = keyInfo.c.getMPService()
                                                   .getOAuth2Service()
                                                   .buildAuthorizationUrl(userInfo_redirectUrl,
                                                                          USER_INFO_SCOPE,
                                                                          getState(request,
                                                                                   true));

                            end = true;
                            break;
                        case OAUTH2_USER_INFO_RETURN:
                            WxOAuth2AccessToken userInfo_accessToken = keyInfo.c.getMPService()
                                                                                .getOAuth2Service()
                                                                                .getAccessToken(getCode(request));

                            WxOAuth2UserInfo userInfo = keyInfo.c.getMPService()
                                                                 .getOAuth2Service()
                                                                 .getUserInfo(userInfo_accessToken,
                                                                              keyInfo.b.getLanguage());

                            redirectUrl = getOAuthHandler().Handler(request,
                                                                    keyInfo.b,
                                                                    userInfo,
                                                                    getState(request,
                                                                             false));
                            end = true;
                            break;
                    }
                }
            }

            if (end)
                response.sendRedirect(redirectUrl);

            return !end;
        } catch (Exception ex) {
            throw new ModuleException(Strings.getMiddlewarePreHandleFailed(WeChatOAuthMiddleware.class.getName()),
                                      ex);
        }
    }
}
