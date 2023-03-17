package project.extension.wechat.core.mp.normal;

import me.chanjar.weixin.common.bean.WxJsapiSignature;
import me.chanjar.weixin.common.bean.WxOAuth2UserInfo;
import me.chanjar.weixin.common.bean.oauth2.WxOAuth2AccessToken;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.api.impl.WxMpServiceImpl;
import me.chanjar.weixin.mp.bean.template.WxMpTemplateMessage;
import me.chanjar.weixin.mp.config.impl.WxMpDefaultConfigImpl;
import org.apache.commons.lang3.StringUtils;
import project.extension.standard.exception.ModuleException;
import project.extension.wechat.config.MPConfig;
import project.extension.wechat.core.mp.standard.IWeChatMPService;
import project.extension.wechat.model.SendTemplateMessageResult;

/**
 * 微信公众号服务
 *
 * @author LCTR
 * @date 2023-03-15
 */
public class WeChatMPService
        implements IWeChatMPService {
    public WeChatMPService(MPConfig mpConfig) {
        this.mpConfig = mpConfig;
        this.mpService = createMPService();
    }

    private final MPConfig mpConfig;

    private final WxMpService mpService;

    private WxMpService createMPService() {
        WxMpService wxMpService = new WxMpServiceImpl();
        WxMpDefaultConfigImpl configStorage = new WxMpDefaultConfigImpl();
        configStorage.setAppId(StringUtils.trimToNull(this.mpConfig.getAppId()));
        configStorage.setSecret(StringUtils.trimToNull(this.mpConfig.getAppSecret()));
        configStorage.setToken(StringUtils.trimToNull(this.mpConfig.getToken()));
        configStorage.setAesKey(StringUtils.trimToNull(this.mpConfig.getAesKey()));
        wxMpService.setWxMpConfigStorage(configStorage);
        return wxMpService;
    }

    @Override
    public WxMpService getMPService() {
        return mpService;
    }

    @Override
    public String getAccessToken() {
        try {
            return mpService.getAccessToken();
        } catch (Exception ex) {
            throw new ModuleException(ex.getMessage(),
                                      ex);
        }
    }

    @Override
    public WxJsapiSignature getSign(String url) {
        try {
            return mpService.createJsapiSignature(url);
        } catch (Exception ex) {
            throw new ModuleException(ex.getMessage(),
                                      ex);
        }
    }

    @Override
    public String getBaseOAuth2Url() {
        return mpConfig.getOAuthBaseUrl();
    }

    @Override
    public String getUserInfoOAuth2Url() {
        return mpConfig.getOAuthUserInfoUrl();
    }

    @Override
    public WxOAuth2UserInfo getUserInfo(String openId) {
        try {
            WxOAuth2AccessToken accessToken = new WxOAuth2AccessToken();
            accessToken.setAccessToken(getAccessToken());
            accessToken.setOpenId(openId);

            return mpService.getOAuth2Service()
                            .getUserInfo(accessToken,
                                         mpConfig.getLanguage());
        } catch (Exception ex) {
            throw new ModuleException(ex.getMessage(),
                                      ex);
        }
    }

    @Override
    public WxOAuth2UserInfo getUserInfoByCode(String code) {
        try {
            final WxOAuth2AccessToken accessToken = mpService.getOAuth2Service()
                                                             .getAccessToken(code);
            return mpService.getOAuth2Service()
                            .getUserInfo(accessToken,
                                         mpConfig.getLanguage());
        } catch (Exception ex) {
            throw new ModuleException(ex.getMessage(),
                                      ex);
        }
    }

    @Override
    public SendTemplateMessageResult sendTemplateMessage(WxMpTemplateMessage parameter) {
        try {
            String msgId = mpService.getTemplateMsgService()
                                    .sendTemplateMsg(parameter);
            SendTemplateMessageResult result = new SendTemplateMessageResult();
            result.setMsgId(msgId);
            return result;
        } catch (Exception ex) {
            throw new ModuleException(ex.getMessage(),
                                      ex);
        }
    }
}
