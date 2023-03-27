package project.extension.wechat.core.mp.servlet;

import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;
import me.chanjar.weixin.mp.config.WxMpConfigStorage;
import org.springframework.lang.Nullable;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.StringUtils;
import project.extension.standard.exception.ModuleException;
import project.extension.tuple.Tuple2;
import project.extension.wechat.config.MpConfig;
import project.extension.wechat.core.mp.standard.IWeChatMpService;
import project.extension.wechat.globalization.Strings;
import project.extension.wechat.model.WeChatEncryptType;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * 微信终端服务
 *
 * @author LCTR
 * @date 2023-03-22
 */
public class WeChatMpEndpointServlet
        extends HttpServlet {
    private final static ConcurrentMap<String, Tuple2<MpConfig, IWeChatMpService>> keyMap = new ConcurrentHashMap<>();
    private final static ConcurrentMap<String, String> urlPatternMap = new ConcurrentHashMap<>();
    private final static AntPathMatcher PATH_MATCHER = new AntPathMatcher();

    public final static String SIGNATURE_PARAMETER = "signature";
    public final static String NONCE_PARAMETER = "nonce";
    public final static String TIMESTAMP_PARAMETER = "timestamp";
    public final static String ECHOSTR_PARAMETER = "echostr";
    public final static String MSG_SIGNATURE_PARAMETER = "msg_signature";
    public final static String ENCRYPT_TYPE_PARAMETER = "encrypt_type";
    public final static String RESPONSE_CONTENT_TYPE = "text/html;charset=utf-8";

    /**
     * 获取地址标识
     *
     * @param mpConfig  公众号配置
     * @param mpService 微信公众号服务
     */
    private static String getUrlKey(MpConfig mpConfig,
                                    @Nullable
                                            IWeChatMpService mpService) {
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
    public static void setup(MpConfig mpConfig,
                             IWeChatMpService mpService) {
        try {
            String key = getUrlKey(mpConfig,
                                   mpService);

            urlPatternMap.putIfAbsent(key,
                                      mpConfig.getMpEndpointUrl());
        } catch (Exception ex) {
            throw new ModuleException(Strings.getSetupMiddlewareFailed(WeChatMpEndpointServlet.class.getName()),
                                      ex);
        }
    }

    /**
     * 获取当前所使用的全部的地址
     */
    public static List<String> getAllUrlPattern() {
        return new ArrayList<>(urlPatternMap.values());
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
    protected void service(HttpServletRequest request,
                           HttpServletResponse response) {
        try {
            //是否结束请求
            boolean end = false;
            //输出信息
            String responseData = "";

            String uri = request.getRequestURI();

            if (StringUtils.hasText(uri)) {
                for (String key : urlPatternMap.keySet()) {
                    if (end)
                        break;
                    
                    String urlPattern = urlPatternMap.get(key);
                    if (!PATH_MATCHER.match(urlPattern,
                                            uri))
                        continue;

                    Tuple2<MpConfig, IWeChatMpService> keyInfo = keyMap.get(key);

                    String timestamp = getParameter(request,
                                                    TIMESTAMP_PARAMETER);
                    String nonce = getParameter(request,
                                                NONCE_PARAMETER);
                    if (!keyInfo.b.getMpService()
                                  .checkSignature(timestamp,
                                                  nonce,
                                                  getParameter(request,
                                                               SIGNATURE_PARAMETER))) {
                        //消息签名不正确，说明不是公众平台发过来的消息
                        response.setStatus(HttpServletResponse.SC_FORBIDDEN);

                        end = true;
                        continue;
                    }

                    //是否为微信服务器发送的token验证请求
                    String echostr = getParameter(request,
                                                  ECHOSTR_PARAMETER);
                    if (StringUtils.hasText(echostr)) {
                        //回显echostr
                        responseData = echostr;

                        end = true;
                        continue;
                    }


                    String encryptType = request.getParameter(ENCRYPT_TYPE_PARAMETER);
                    if (!StringUtils.hasText(encryptType))
                        encryptType = WeChatEncryptType.RAW.toString();

                    if (WeChatEncryptType.RAW.toString()
                                             .equals(encryptType)) {
                        // 明文传输的消息
                        WxMpXmlMessage inMessage = WxMpXmlMessage.fromXml(request.getInputStream());
                        WxMpXmlOutMessage outMessage = keyInfo.b.getWxMpMessageRouter()
                                                                .route(inMessage);
                        if (outMessage != null)
                            responseData = outMessage.toXml();

                        end = true;
                        continue;
                    }

                    if (WeChatEncryptType.AES.toString()
                                             .equals(encryptType)) {
                        // 是aes加密的消息
                        String msgSignature = getParameter(request,
                                                           MSG_SIGNATURE_PARAMETER);
                        WxMpConfigStorage mpConfigStorage = keyInfo.b.getMpService()
                                                                     .getWxMpConfigStorage();
                        WxMpXmlMessage inMessage = WxMpXmlMessage.fromEncryptedXml(request.getInputStream(),
                                                                                   mpConfigStorage,
                                                                                   timestamp,
                                                                                   nonce,
                                                                                   msgSignature);
                        WxMpXmlOutMessage outMessage = keyInfo.b.getWxMpMessageRouter()
                                                                .route(inMessage);
                        responseData = outMessage.toEncryptedXml(mpConfigStorage);

                        end = true;
                    }
                }
            }

            if (end && StringUtils.hasText(responseData)) {
                response.setContentType(RESPONSE_CONTENT_TYPE);
                response.setStatus(HttpServletResponse.SC_OK);
                response.getWriter()
                        .write(responseData);
            } else
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        } catch (Exception ex) {
            throw new ModuleException(Strings.getServletHandleFailed(WeChatMpEndpointServlet.class.getName()),
                                      ex);
        }
    }
}
