package project.extension.wechat.configure;

import com.github.binarywang.wxpay.bean.notify.*;
import me.chanjar.weixin.common.bean.WxOAuth2UserInfo;
import me.chanjar.weixin.common.bean.oauth2.WxOAuth2AccessToken;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import project.extension.wechat.config.MPConfig;
import project.extension.wechat.config.PayConfig;
import project.extension.wechat.core.mp.handler.IWeChatOAuthHandler;
import project.extension.wechat.core.pay.handler.IWeChatPayNotifyHandler;

import javax.servlet.http.HttpServletRequest;

/**
 * 配置微信服务处理类
 *
 * @author LCTR
 * @date 2023-03-17
 */
@Configuration
public class NaiveWeChatHandlerConfigure {
    /**
     * 注册微信网页授权处理类
     */
    @Bean
    public IWeChatOAuthHandler registerWeChatOAuthHandler() {
        return new IWeChatOAuthHandler() {
            @Override
            public String Handler(HttpServletRequest request,
                                  MPConfig config,
                                  WxOAuth2AccessToken accessToken,
                                  String state) {
                return state;
            }

            @Override
            public String Handler(HttpServletRequest request,
                                  MPConfig config,
                                  WxOAuth2UserInfo userinfo,
                                  String state) {
                return state;
            }
        };
    }

    /**
     * 注册微信收付通通知处理类
     */
    @Bean
    public IWeChatPayNotifyHandler registerWeChatPayNotifyHandler() {
        return new IWeChatPayNotifyHandler() {
            @Override
            public String payNotify(PayConfig config,
                                    WxPayOrderNotifyResult notifyInfo) {
                return WxPayNotifyResponse.success(config.getName());
            }

            @Override
            public String payNotifyV3(PayConfig config,
                                      WxPayOrderNotifyV3Result notifyInfo) {
                return WxPayNotifyResponse.success(config.getName());
            }

            @Override
            public String refundNotifyV3(PayConfig config,
                                         WxPayRefundNotifyV3Result notifyInfo) {
                return WxPayNotifyResponse.success(config.getName());
            }

            @Override
            public String refundNotify(PayConfig config,
                                       WxPayRefundNotifyResult notifyInfo) {
                return WxPayNotifyResponse.success(config.getName());
            }
        };
    }
}
