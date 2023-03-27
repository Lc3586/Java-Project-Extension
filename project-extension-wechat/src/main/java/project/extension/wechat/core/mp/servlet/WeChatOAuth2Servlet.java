package project.extension.wechat.core.mp.servlet;

import me.chanjar.weixin.common.bean.WxOAuth2UserInfo;
import me.chanjar.weixin.common.bean.oauth2.WxOAuth2AccessToken;
import org.springframework.http.HttpMethod;
import org.springframework.lang.Nullable;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.StringUtils;
import project.extension.ioc.IOCExtension;
import project.extension.standard.exception.ModuleException;
import project.extension.string.StringExtension;
import project.extension.tuple.Tuple3;
import project.extension.wechat.config.BaseConfig;
import project.extension.wechat.config.MpConfig;
import project.extension.wechat.core.mp.handler.IWeChatOAuthHandler;
import project.extension.wechat.core.mp.standard.IWeChatMpService;
import project.extension.wechat.globalization.Strings;
import project.extension.wechat.model.WeChatRedirectType;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * 微信网页授权服务
 *
 * @author LCTR
 * @date 2023-03-21
 */
public class WeChatOAuth2Servlet
        extends HttpServlet {
    public WeChatOAuth2Servlet() {
        this.baseConfig = IOCExtension.applicationContext.getBean(BaseConfig.class);
    }

    private final BaseConfig baseConfig;

    private IWeChatOAuthHandler oAuthHandler;

    private final static ConcurrentMap<String, Tuple3<WeChatRedirectType, MpConfig, IWeChatMpService>> keyMap = new ConcurrentHashMap<>();
    private final static ConcurrentMap<String, String> urlPatternMap = new ConcurrentHashMap<>();
    private final static AntPathMatcher PATH_MATCHER = new AntPathMatcher();

    public final static String URL_PREFIX = "/wechat/oauth2";
    public final static String URL_MAPPING = "/wechat/oauth2/*";
    public final static String BASE_SCOPE = "snsapi_base";
    public final static String USER_INFO_SCOPE = "snsapi_userinfo";
    public final static String STATE_PARAMETER = "state";
    public final static String CODE_PARAMETER = "code";
    public final static String REDIRECT_URI_PARAMETER = "redirect_uri";

    /**
     * 测试模式
     */
    public static boolean testMode = false;

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
    private static String getUrlKey(MpConfig mpConfig,
                                    @Nullable
                                            IWeChatMpService mpService,
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
        return StringExtension.getUrl(baseConfig.getRootUrlScheme(),
                                      baseConfig.getRootUrlHost(),
                                      redirectUrl);
    }

    /**
     * 设置
     *
     * @param mpConfig  公众号配置
     * @param mpService 微信公众号服务
     */
    public static void setup(MpConfig mpConfig,
                             IWeChatMpService mpService) {
        try {
            if (!StringUtils.hasText(mpConfig.getOAuthBaseUrl()))
                //自动生成基础授权接口地址
                mpConfig.setOAuthBaseUrl(String.format("%s/base/api/%s",
                                                       URL_PREFIX,
                                                       UUID.randomUUID()));
            urlPatternMap.putIfAbsent(getUrlKey(mpConfig,
                                                mpService,
                                                WeChatRedirectType.OAUTH2_BASE_API),
                                      mpConfig.getOAuthBaseUrl());
            //自动生成基础授权回调地址
            urlPatternMap.putIfAbsent(getUrlKey(mpConfig,
                                                mpService,
                                                WeChatRedirectType.OAUTH2_BASE_RETURN),
                                      String.format("%s/base/return/%s",
                                                    URL_PREFIX,
                                                    UUID.randomUUID()));

            if (!StringUtils.hasText(mpConfig.getOAuthUserInfoUrl()))
                //自动生成身份信息授权接口地址
                mpConfig.setOAuthUserInfoUrl(String.format("%s/userinfo/api/%s",
                                                           URL_PREFIX,
                                                           UUID.randomUUID()));
            urlPatternMap.putIfAbsent(getUrlKey(mpConfig,
                                                mpService,
                                                WeChatRedirectType.OAUTH2_USER_INFO_API),
                                      mpConfig.getOAuthUserInfoUrl());
            //自动生成身份信息授权回调地址
            urlPatternMap.putIfAbsent(getUrlKey(mpConfig,
                                                mpService,
                                                WeChatRedirectType.OAUTH2_USER_INFO_RETURN),
                                      String.format("%s/userinfo/return/%s",
                                                    URL_PREFIX,
                                                    UUID.randomUUID()));
        } catch (Exception ex) {
            throw new ModuleException(Strings.getSetupMiddlewareFailed(WeChatOAuth2Servlet.class.getName()),
                                      ex);
        }
    }

    /**
     * 获取当前所使用的全部的地址
     */
    public static List<String> getAllUrlPattern() {
        return new ArrayList<>(urlPatternMap.values());
    }

    @Override
    protected void service(HttpServletRequest request,
                           HttpServletResponse response) {
        try {
            //是否结束请求
            boolean end = false;
            //重定向地址
            String redirectUrl = "";

            String uri = request.getRequestURI();
            if (request.getMethod()
                       .equals(HttpMethod.GET.toString())
                    && StringUtils.hasText(uri)) {
                for (String key : urlPatternMap.keySet()) {
                    if (end)
                        break;

                    String urlPattern = urlPatternMap.get(key);
                    if (!PATH_MATCHER.match(urlPattern,
                                            uri))
                        continue;

                    Tuple3<WeChatRedirectType, MpConfig, IWeChatMpService> keyInfo = keyMap.get(key);

                    switch (keyInfo.a) {
                        case OAUTH2_BASE_API:
                            String base_redirectUrl = getFullRedirectUrl(urlPatternMap.get(getUrlKey(keyInfo.b,
                                                                                                     null,
                                                                                                     WeChatRedirectType.OAUTH2_BASE_RETURN)));

                            redirectUrl = keyInfo.c.getMpService()
                                                   .getOAuth2Service()
                                                   .buildAuthorizationUrl(base_redirectUrl,
                                                                          BASE_SCOPE,
                                                                          getState(request,
                                                                                   true));

                            end = true;
                            break;
                        case OAUTH2_BASE_RETURN:
                            if (testMode)
                                redirectUrl = getOAuthHandler().Handler(request,
                                                                        keyInfo.b,
                                                                        new WxOAuth2AccessToken(),
                                                                        getState(request,
                                                                                 false));
                            else {
                                WxOAuth2AccessToken base_accessToken = keyInfo.c.getMpService()
                                                                                .getOAuth2Service()
                                                                                .getAccessToken(getCode(request));

                                redirectUrl = getOAuthHandler().Handler(request,
                                                                        keyInfo.b,
                                                                        base_accessToken,
                                                                        getState(request,
                                                                                 false));
                            }

                            end = true;
                            break;
                        case OAUTH2_USER_INFO_API:
                            String userInfo_redirectUrl = getFullRedirectUrl(urlPatternMap.get(getUrlKey(keyInfo.b,
                                                                                                         null,
                                                                                                         WeChatRedirectType.OAUTH2_USER_INFO_RETURN)));

                            redirectUrl = keyInfo.c.getMpService()
                                                   .getOAuth2Service()
                                                   .buildAuthorizationUrl(userInfo_redirectUrl,
                                                                          USER_INFO_SCOPE,
                                                                          getState(request,
                                                                                   true));

                            end = true;
                            break;
                        case OAUTH2_USER_INFO_RETURN:
                            if (testMode)
                                redirectUrl = getOAuthHandler().Handler(request,
                                                                        keyInfo.b,
                                                                        new WxOAuth2UserInfo(),
                                                                        getState(request,
                                                                                 false));
                            else {
                                WxOAuth2AccessToken userInfo_accessToken = keyInfo.c.getMpService()
                                                                                    .getOAuth2Service()
                                                                                    .getAccessToken(getCode(request));

                                WxOAuth2UserInfo userInfo = keyInfo.c.getMpService()
                                                                     .getOAuth2Service()
                                                                     .getUserInfo(userInfo_accessToken,
                                                                                  keyInfo.b.getLanguage());

                                redirectUrl = getOAuthHandler().Handler(request,
                                                                        keyInfo.b,
                                                                        userInfo,
                                                                        getState(request,
                                                                                 false));
                            }
                            end = true;
                            break;
                    }
                }
            }

            if (end)
                response.sendRedirect(redirectUrl);
            else
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        } catch (Exception ex) {
            throw new ModuleException(Strings.getServletHandleFailed(WeChatOAuth2Servlet.class.getName()),
                                      ex);
        }
    }
}
