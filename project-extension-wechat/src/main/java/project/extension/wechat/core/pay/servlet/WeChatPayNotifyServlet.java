package project.extension.wechat.core.pay.servlet;

import com.github.binarywang.wxpay.bean.notify.WxPayOrderNotifyResult;
import com.github.binarywang.wxpay.bean.notify.WxPayOrderNotifyV3Result;
import com.github.binarywang.wxpay.bean.notify.WxPayRefundNotifyResult;
import com.github.binarywang.wxpay.bean.notify.WxPayRefundNotifyV3Result;
import org.apache.commons.io.IOUtils;
import org.springframework.http.HttpMethod;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.StringUtils;
import project.extension.ioc.IOCExtension;
import project.extension.standard.exception.ModuleException;
import project.extension.tuple.Tuple4;
import project.extension.wechat.config.BaseConfig;
import project.extension.wechat.config.PayConfig;
import project.extension.wechat.core.pay.handler.IWeChatPayNotifyHandler;
import project.extension.wechat.core.pay.standard.IWeChatPayService;
import project.extension.wechat.extension.SignatureHelper;
import project.extension.wechat.globalization.Strings;
import project.extension.wechat.model.WeChatPayApiVersion;
import project.extension.wechat.model.WeChatPayNotifyType;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * 微信收付通通知服务
 *
 * @author LCTR
 * @date 2023-03-22
 */
public class WeChatPayNotifyServlet
        extends HttpServlet {
    public WeChatPayNotifyServlet() {
        this.baseConfig = IOCExtension.applicationContext.getBean(BaseConfig.class);
    }

    private final BaseConfig baseConfig;

    private IWeChatPayNotifyHandler payNotifyHandler;

    private final static ConcurrentMap<String, Tuple4<WeChatPayNotifyType, WeChatPayApiVersion, PayConfig, IWeChatPayService>> keyMap = new ConcurrentHashMap<>();
    private final static ConcurrentMap<String, String> urlPatternMap = new ConcurrentHashMap<>();
    private final static AntPathMatcher PATH_MATCHER = new AntPathMatcher();

    public final static String URL_PREFIX = "/wechat/pay/notify";
    public final static String BASE_SCOPE = "snsapi_base";
    public final static String USER_INFO_SCOPE = "snsapi_userinfo";
    public final static String STATE_PARAMETER = "state";
    public final static String CODE_PARAMETER = "code";
    public final static String RESPONSE_CONTENT_TYPE = "text/html;charset=utf-8";

    /**
     * 获取处理类
     */
    private IWeChatPayNotifyHandler getPayNotifyHandler() {
        if (payNotifyHandler == null)
            payNotifyHandler = IOCExtension.tryGetBean(IWeChatPayNotifyHandler.class);
        if (payNotifyHandler == null)
            throw new ModuleException(Strings.getHandlerUndefined(IWeChatPayNotifyHandler.class.getName()));
        return payNotifyHandler;
    }

    /**
     * 获取地址标识
     *
     * @param payConfig  商户配置
     * @param type       类型
     * @param version    版本
     * @param payService 微信支付服务
     */
    private static String getUrlKey(PayConfig payConfig,
                                    IWeChatPayService payService,
                                    WeChatPayNotifyType type,
                                    WeChatPayApiVersion version) {
        String key = String.format("%s_%s",
                                   type.toString(),
                                   version.toString());

        keyMap.putIfAbsent(key,
                           new Tuple4<>(type,
                                        version,
                                        payConfig,
                                        payService));

        return key;
    }

    /**
     * 设置
     *
     * @param payConfig  商户配置
     * @param payService 微信支付服务
     */
    public static void setup(PayConfig payConfig,
                             IWeChatPayService payService) {
        try {
            if (!StringUtils.hasText(payConfig.getPayNotifyUrl()))
                //自动生成支付通知回调地址
                payConfig.setPayNotifyUrl(String.format("%s/%s",
                                                        URL_PREFIX,
                                                        UUID.randomUUID()));
            urlPatternMap.putIfAbsent(getUrlKey(payConfig,
                                                payService,
                                                WeChatPayNotifyType.Pay,
                                                WeChatPayApiVersion.NORMAL),
                                      payConfig.getPayNotifyUrl());

            if (!StringUtils.hasText(payConfig.getPayNotifyV3Url()))
                //自动生成支付通知回调地址
                payConfig.setPayNotifyV3Url(String.format("%s/v3/%s",
                                                          URL_PREFIX,
                                                          UUID.randomUUID()));
            urlPatternMap.putIfAbsent(getUrlKey(payConfig,
                                                payService,
                                                WeChatPayNotifyType.Pay,
                                                WeChatPayApiVersion.V3),
                                      payConfig.getPayNotifyV3Url());

            if (!StringUtils.hasText(payConfig.getPayScoreNotifyV3Url()))
                //自动生成支付分通知回调地址
                payConfig.setPayScoreNotifyV3Url(String.format("%s/score/%s",
                                                               URL_PREFIX,
                                                               UUID.randomUUID()));
            urlPatternMap.putIfAbsent(getUrlKey(payConfig,
                                                payService,
                                                WeChatPayNotifyType.PayScore,
                                                WeChatPayApiVersion.NORMAL),
                                      payConfig.getPayScoreNotifyV3Url());

            if (!StringUtils.hasText(payConfig.getRefundNotifyUrl()))
                //自动生成退款通知回调地址
                payConfig.setRefundNotifyUrl(String.format("%s/refund/%s",
                                                           URL_PREFIX,
                                                           UUID.randomUUID()));
            urlPatternMap.putIfAbsent(getUrlKey(payConfig,
                                                payService,
                                                WeChatPayNotifyType.Refund,
                                                WeChatPayApiVersion.NORMAL),
                                      payConfig.getRefundNotifyUrl());

            if (!StringUtils.hasText(payConfig.getRefundNotifyV3Url()))
                //自动生成退款通知回调地址
                payConfig.setRefundNotifyV3Url(String.format("%s/refund/v3/%s",
                                                             URL_PREFIX,
                                                             UUID.randomUUID()));
            urlPatternMap.putIfAbsent(getUrlKey(payConfig,
                                                payService,
                                                WeChatPayNotifyType.Refund,
                                                WeChatPayApiVersion.V3),
                                      payConfig.getRefundNotifyV3Url());
        } catch (Exception ex) {
            throw new ModuleException(Strings.getSetupMiddlewareFailed(WeChatPayNotifyServlet.class.getName()),
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
            //输出信息
            String responseData = "";

            String uri = request.getRequestURI();
            if (request.getMethod()
                       .equals(HttpMethod.POST.toString())
                    && StringUtils.hasText(uri)) {
                for (String key : urlPatternMap.keySet()) {
                    if (end)
                        break;

                    String urlPattern = urlPatternMap.get(key);
                    if (!PATH_MATCHER.match(urlPattern,
                                            uri))
                        continue;

                    Tuple4<WeChatPayNotifyType, WeChatPayApiVersion, PayConfig, IWeChatPayService> keyInfo = keyMap.get(key);

                    String bodyData = IOUtils.toString(request.getInputStream(),
                                                       baseConfig.getEncoding());

                    switch (keyInfo.a) {
                        case Pay:
                            switch (keyInfo.b) {
                                case NORMAL:
                                case V2:
                                    final WxPayOrderNotifyResult notifyResult = keyInfo.d.getPayService()
                                                                                         .parseOrderNotifyResult(bodyData);
                                    if (getPayNotifyHandler() != null) {
                                        responseData = getPayNotifyHandler().payNotify(keyInfo.c,
                                                                                       notifyResult);
                                        end = false;
                                    }
                                    break;
                                case V3:
                                    final WxPayOrderNotifyV3Result notifyV3Result = keyInfo.d.getPayService()
                                                                                             .parseOrderNotifyV3Result(bodyData,
                                                                                                                       SignatureHelper.getSignatureHeader(request));
                                    if (getPayNotifyHandler() != null) {
                                        responseData = getPayNotifyHandler().payNotifyV3(keyInfo.c,
                                                                                         notifyV3Result);
                                        end = true;
                                    }
                                    break;
                            }
                            break;
                        case Refund:
                            switch (keyInfo.b) {
                                case NORMAL:
                                case V2:
                                    final WxPayRefundNotifyResult notifyResult = keyInfo.d.getPayService()
                                                                                          .parseRefundNotifyResult(bodyData);
                                    if (getPayNotifyHandler() != null) {
                                        responseData = getPayNotifyHandler().refundNotify(keyInfo.c,
                                                                                          notifyResult);
                                        end = true;
                                    }
                                    break;
                                case V3:
                                    final WxPayRefundNotifyV3Result notifyV3Result = keyInfo.d.getPayService()
                                                                                              .parseRefundNotifyV3Result(bodyData,
                                                                                                                         SignatureHelper.getSignatureHeader(request));
                                    if (getPayNotifyHandler() != null) {
                                        responseData = getPayNotifyHandler().refundNotifyV3(keyInfo.c,
                                                                                            notifyV3Result);
                                        end = true;
                                    }
                                    break;
                            }
                            break;
                    }
                }
            }

            if (end) {
                response.setContentType(RESPONSE_CONTENT_TYPE);
                response.setStatus(HttpServletResponse.SC_OK);
                response.getWriter()
                        .write(responseData);
            } else
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        } catch (Exception ex) {
            throw new ModuleException(Strings.getServletHandleFailed(WeChatPayNotifyServlet.class.getName()),
                                      ex);
        }
    }
}
