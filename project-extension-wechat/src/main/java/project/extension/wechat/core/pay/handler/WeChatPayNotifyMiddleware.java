package project.extension.wechat.core.pay.handler;

import com.github.binarywang.wxpay.bean.notify.WxPayOrderNotifyResult;
import com.github.binarywang.wxpay.bean.notify.WxPayOrderNotifyV3Result;
import com.github.binarywang.wxpay.bean.notify.WxPayRefundNotifyResult;
import com.github.binarywang.wxpay.bean.notify.WxPayRefundNotifyV3Result;
import org.apache.commons.io.IOUtils;
import org.springframework.http.HttpMethod;
import org.springframework.lang.Nullable;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import project.extension.ioc.IOCExtension;
import project.extension.standard.exception.ModuleException;
import project.extension.tuple.Tuple4;
import project.extension.wechat.config.BaseConfig;
import project.extension.wechat.config.PayConfig;
import project.extension.wechat.core.pay.standard.IWeChatPayService;
import project.extension.wechat.extension.SignatureHelper;
import project.extension.wechat.globalization.Strings;
import project.extension.wechat.model.WeChatPayApiVersion;
import project.extension.wechat.model.WeChatPayNotifyType;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * 微信收付通通知中间件
 *
 * @author LCTR
 * @date 2023-03-16
 */
public class WeChatPayNotifyMiddleware
        implements HandlerInterceptor {
    public WeChatPayNotifyMiddleware(BaseConfig baseConfig) {
        this.baseConfig = baseConfig;
    }

    private final BaseConfig baseConfig;

    private IWeChatPayNotifyHandler payNotifyHandler;

    private final static ConcurrentMap<String, Tuple4<WeChatPayNotifyType, WeChatPayApiVersion, PayConfig, IWeChatPayService>> keyMap = new ConcurrentHashMap<>();

    private final static ConcurrentMap<String, String> urlMap = new ConcurrentHashMap<>();

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
                payConfig.setPayNotifyUrl(String.format("/wechat/pay/notify/%s",
                                                        UUID.randomUUID()));
            urlMap.putIfAbsent(getUrlKey(payConfig,
                                         payService,
                                         WeChatPayNotifyType.Pay,
                                         WeChatPayApiVersion.NORMAL),
                               payConfig.getPayNotifyUrl());

            if (!StringUtils.hasText(payConfig.getPayNotifyV3Url()))
                //自动生成支付通知回调地址
                payConfig.setPayNotifyV3Url(String.format("/wechat/pay/notify/v3/%s",
                                                          UUID.randomUUID()));
            urlMap.putIfAbsent(getUrlKey(payConfig,
                                         payService,
                                         WeChatPayNotifyType.Pay,
                                         WeChatPayApiVersion.V3),
                               payConfig.getPayNotifyV3Url());

            if (!StringUtils.hasText(payConfig.getPayScoreNotifyV3Url()))
                //自动生成支付分通知回调地址
                payConfig.setPayScoreNotifyV3Url(String.format("/wechat/pay/score/notify/%s",
                                                               UUID.randomUUID()));
            urlMap.putIfAbsent(getUrlKey(payConfig,
                                         payService,
                                         WeChatPayNotifyType.PayScore,
                                         WeChatPayApiVersion.NORMAL),
                               payConfig.getPayScoreNotifyV3Url());

            if (!StringUtils.hasText(payConfig.getRefundNotifyUrl()))
                //自动生成退款通知回调地址
                payConfig.setRefundNotifyUrl(String.format("/wechat/pay/refund/notify/%s",
                                                           UUID.randomUUID()));
            urlMap.putIfAbsent(getUrlKey(payConfig,
                                         payService,
                                         WeChatPayNotifyType.Refund,
                                         WeChatPayApiVersion.NORMAL),
                               payConfig.getRefundNotifyUrl());

            if (!StringUtils.hasText(payConfig.getRefundNotifyV3Url()))
                //自动生成退款通知回调地址
                payConfig.setRefundNotifyV3Url(String.format("/wechat/pay/refund/notify/v3/%s",
                                                             UUID.randomUUID()));
            urlMap.putIfAbsent(getUrlKey(payConfig,
                                         payService,
                                         WeChatPayNotifyType.Refund,
                                         WeChatPayApiVersion.V3),
                               payConfig.getRefundNotifyV3Url());
        } catch (Exception ex) {
            throw new ModuleException(Strings.getSetupMiddlewareFailed(WeChatPayNotifyMiddleware.class.getName()),
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
            //输出信息
            String responseData = "";

            String uri = request.getRequestURI();
            if (request.getMethod()
                       .equals(HttpMethod.POST.toString())
                    && StringUtils.hasText(uri)) {
                for (String key : urlMap.keySet()) {
                    String url = urlMap.get(key);
                    if (!url.contains(uri))
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

            if (end)
                response.getWriter()
                        .println(responseData);

            return !end;
        } catch (Exception ex) {
            throw new ModuleException(Strings.getMiddlewarePreHandleFailed(WeChatPayNotifyMiddleware.class.getName()),
                                      ex);
        }
    }
}
