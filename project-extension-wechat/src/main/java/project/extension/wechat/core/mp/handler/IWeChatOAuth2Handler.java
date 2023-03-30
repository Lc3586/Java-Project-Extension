package project.extension.wechat.core.mp.handler;

import me.chanjar.weixin.common.bean.WxOAuth2UserInfo;
import me.chanjar.weixin.common.bean.oauth2.WxOAuth2AccessToken;
import org.springframework.lang.Nullable;
import project.extension.wechat.config.MpConfig;

import javax.servlet.http.HttpServletRequest;

/**
 * 微信网页授权处理类
 *
 * @author LCTR
 * @date 2023-03-16
 */
public interface IWeChatOAuth2Handler {
    /**
     * 处理用户公众号唯一标识
     *
     * @param request     请求对象
     * @param config      公众号配置
     * @param accessToken 访问令牌
     * @param state       url中附带的state参数
     * @return 重定向地址
     */
    String Handler(HttpServletRequest request,
                   MpConfig config,
                   WxOAuth2AccessToken accessToken,
                   @Nullable
                           String state);

    /**
     * 处理用户基础信息
     *
     * @param request  请求对象
     * @param config   公众号配置
     * @param userinfo 用户基础信息
     * @param state    url中附带的state参数
     * @return 重定向地址
     */
    String Handler(HttpServletRequest request,
                   MpConfig config,
                   WxOAuth2UserInfo userinfo,
                   @Nullable
                           String state);
}
