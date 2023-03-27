package project.extension.wechat.core.mp.standard;

import me.chanjar.weixin.common.bean.WxJsapiSignature;
import me.chanjar.weixin.common.bean.WxOAuth2UserInfo;
import me.chanjar.weixin.mp.api.WxMpMessageRouter;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.template.WxMpTemplateMessage;
import me.chanjar.weixin.mp.config.WxMpConfigStorage;
import project.extension.wechat.model.*;

/**
 * 微信公众号服务
 *
 * <p>使用此方法获取默认的公众号服务org.springframework.beans.factory.BeanFactory#getBean(INaiveWeChatServiceProvider.DEFAULT_MP_SERVICE_IOC_NAME, IWeChatMpService.class)</p>
 *
 * @author LCTR
 * @date 2023-03-14
 */
public interface IWeChatMpService {
    /**
     * 获取原生的微信服务对象
     */
    WxMpService getMpService();

    /**
     * 获取微信客户端配置存储对象
     */
    void setMpConfigStorage(WxMpConfigStorage mpConfigStorage);

    /**
     * 获取微信客户端配置存储对象
     */
    WxMpConfigStorage getMpConfigStorage();

    /**
     * 设置消息路由
     */
    void setWxMpMessageRouter(WxMpMessageRouter router);

    /**
     * 获取消息路由
     */
    WxMpMessageRouter getWxMpMessageRouter();

    /**
     * 获取Token
     */
    String getAccessToken();

    /**
     * 获取签名
     */
    WxJsapiSignature getSign(String url);

    /**
     * 获取基础授权地址
     */
    String getBaseOAuth2Url();

    /**
     * 获取用户信息授权地址
     */
    String getUserInfoOAuth2Url();

    /**
     * 获取用户信息
     *
     * @param openId 用户唯一标识
     */
    WxOAuth2UserInfo getUserInfo(String openId);

    /**
     * 获取用户信息
     *
     * @param code code作为换取access_token的票据，每次用户授权带上的code将不一样，code只能使用一次，5分钟未被使用自动过期。
     */
    WxOAuth2UserInfo getUserInfoByCode(String code);

    /**
     * 发送模板消息
     *
     * @param parameter 参数
     */
    SendTemplateMessageResult sendTemplateMessage(WxMpTemplateMessage parameter);
}
