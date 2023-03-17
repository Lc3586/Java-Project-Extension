package project.extension.wechat.core.pay.normal;

import com.github.binarywang.wxpay.bean.order.WxPayAppOrderResult;
import com.github.binarywang.wxpay.bean.order.WxPayMpOrderResult;
import com.github.binarywang.wxpay.bean.order.WxPayMwebOrderResult;
import com.github.binarywang.wxpay.bean.order.WxPayNativeOrderResult;
import com.github.binarywang.wxpay.bean.request.*;
import com.github.binarywang.wxpay.bean.result.*;
import com.github.binarywang.wxpay.bean.result.enums.TradeTypeEnum;
import com.github.binarywang.wxpay.config.WxPayConfig;
import com.github.binarywang.wxpay.constant.WxPayConstants;
import com.github.binarywang.wxpay.service.WxPayService;
import com.github.binarywang.wxpay.service.impl.WxPayServiceImpl;
import org.apache.commons.lang3.StringUtils;
import project.extension.standard.exception.ModuleException;
import project.extension.wechat.config.BaseConfig;
import project.extension.wechat.config.PayConfig;
import project.extension.wechat.core.pay.standard.IWeChatPayService;

/**
 * 微信支付服务
 *
 * @author LCTR
 * @date 2023-03-15
 */
public class WeChatPayService
        implements IWeChatPayService {
    public WeChatPayService(BaseConfig baseConfig,
                            PayConfig payConfig) {
        this.baseConfig = baseConfig;
        this.payConfig = payConfig;
        this.payService = createPayService();
    }

    private final BaseConfig baseConfig;

    private final PayConfig payConfig;

    private final WxPayService payService;

    private WxPayService createPayService() {
        WxPayService payService = new WxPayServiceImpl();
        WxPayConfig payConfig = new WxPayConfig();
        payConfig.setAppId(StringUtils.trimToNull(this.payConfig.getAppId()));
        payConfig.setMchId(StringUtils.trimToNull(this.payConfig.getMchId()));
        payConfig.setMchKey(StringUtils.trimToNull(this.payConfig.getMchKey()));
        payConfig.setSubAppId(StringUtils.trimToNull(this.payConfig.getSubAppId()));
        payConfig.setSubMchId(StringUtils.trimToNull(this.payConfig.getSubMchId()));
        payConfig.setKeyPath(StringUtils.trimToNull(this.payConfig.getCertFilePath()));
        //以下是apiV3以及支付分相关
        payConfig.setServiceId(StringUtils.trimToNull(this.payConfig.getServiceId()));
        payConfig.setPayScoreNotifyUrl(StringUtils.trimToNull(this.baseConfig.getPayScoreNotifyV3Url()));
        payConfig.setPrivateKeyPath(StringUtils.trimToNull(this.payConfig.getPrivateKeyPath()));
        payConfig.setPrivateCertPath(StringUtils.trimToNull(this.payConfig.getPrivateCertPath()));
        payConfig.setCertSerialNo(StringUtils.trimToNull(this.payConfig.getCertSerialNo()));
        payConfig.setApiV3Key(StringUtils.trimToNull(this.payConfig.getApiV3Key()));
        payService.setConfig(payConfig);
        return payService;
    }

    @Override
    public WxPayService getPayService() {
        return payService;
    }

    @Override
    public WxPayNativeOrderResult createOrder4Native(WxPayUnifiedOrderRequest parameter) {
        try {
            parameter.setNotifyUrl(baseConfig.getPayNotifyUrl());
            return payService.createOrder(WxPayConstants.TradeType.Specific.NATIVE,
                                          parameter);
        } catch (Exception ex) {
            throw new ModuleException(ex.getMessage(),
                                      ex);
        }
    }

    @Override
    public WxPayAppOrderResult createOrder4App(WxPayUnifiedOrderRequest parameter) {
        try {
            parameter.setNotifyUrl(baseConfig.getPayNotifyUrl());
            return payService.createOrder(WxPayConstants.TradeType.Specific.APP,
                                          parameter);
        } catch (Exception ex) {
            throw new ModuleException(ex.getMessage(),
                                      ex);
        }
    }

    @Override
    public WxPayMpOrderResult createOrder4JsApi(WxPayUnifiedOrderRequest parameter) {
        try {
            parameter.setNotifyUrl(baseConfig.getPayNotifyUrl());
            return payService.createOrder(WxPayConstants.TradeType.Specific.JSAPI,
                                          parameter);
        } catch (Exception ex) {
            throw new ModuleException(ex.getMessage(),
                                      ex);
        }
    }

    @Override
    public WxPayMwebOrderResult createOrder4MWeb(WxPayUnifiedOrderRequest parameter) {
        try {
            parameter.setNotifyUrl(baseConfig.getPayNotifyUrl());
            return payService.createOrder(WxPayConstants.TradeType.Specific.MWEB,
                                          parameter);
        } catch (Exception ex) {
            throw new ModuleException(ex.getMessage(),
                                      ex);
        }
    }

    @Override
    public WxPayMicropayResult createOrder4MicroPay(WxPayUnifiedOrderRequest parameter) {
        try {
            parameter.setNotifyUrl(baseConfig.getPayNotifyUrl());
            return payService.createOrder(WxPayConstants.TradeType.Specific.MICROPAY,
                                          parameter);
        } catch (Exception ex) {
            throw new ModuleException(ex.getMessage(),
                                      ex);
        }
    }

    @Override
    public String createOrderV34Native(WxPayUnifiedOrderV3Request parameter) {
        try {
            parameter.setNotifyUrl(baseConfig.getPayNotifyV3Url());
            return payService.createOrderV3(TradeTypeEnum.NATIVE,
                                            parameter);
        } catch (Exception ex) {
            throw new ModuleException(ex.getMessage(),
                                      ex);
        }
    }

    @Override
    public WxPayUnifiedOrderV3Result.AppResult createOrderV34App(WxPayUnifiedOrderV3Request parameter) {
        try {
            parameter.setNotifyUrl(baseConfig.getPayNotifyV3Url());
            return payService.createOrderV3(TradeTypeEnum.APP,
                                            parameter);
        } catch (Exception ex) {
            throw new ModuleException(ex.getMessage(),
                                      ex);
        }
    }

    @Override
    public WxPayUnifiedOrderV3Result.JsapiResult createOrderV34JsApi(WxPayUnifiedOrderV3Request parameter) {
        try {
            parameter.setNotifyUrl(baseConfig.getPayNotifyV3Url());
            return payService.createOrderV3(TradeTypeEnum.JSAPI,
                                            parameter);
        } catch (Exception ex) {
            throw new ModuleException(ex.getMessage(),
                                      ex);
        }
    }

    @Override
    public String createOrderV34H5(WxPayUnifiedOrderV3Request parameter) {
        try {
            parameter.setNotifyUrl(baseConfig.getPayNotifyV3Url());
            return payService.createOrderV3(TradeTypeEnum.H5,
                                            parameter);
        } catch (Exception ex) {
            throw new ModuleException(ex.getMessage(),
                                      ex);
        }
    }

    @Override
    public WxPayRefundResult refund(WxPayRefundRequest parameter) {
        try {
            parameter.setNotifyUrl(baseConfig.getRefundNotifyUrl());
            return payService.refund(parameter);
        } catch (Exception ex) {
            throw new ModuleException(ex.getMessage(),
                                      ex);
        }
    }

    @Override
    public WxPayRefundResult refundV2(WxPayRefundRequest parameter) {
        try {
            parameter.setNotifyUrl(baseConfig.getRefundNotifyUrl());
            return payService.refundV2(parameter);
        } catch (Exception ex) {
            throw new ModuleException(ex.getMessage(),
                                      ex);
        }
    }

    @Override
    public WxPayRefundV3Result refundV3(WxPayRefundV3Request parameter) {
        try {
            parameter.setNotifyUrl(baseConfig.getRefundNotifyV3Url());
            return payService.refundV3(parameter);
        } catch (Exception ex) {
            throw new ModuleException(ex.getMessage(),
                                      ex);
        }
    }

    @Override
    public WxPayRefundQueryResult refundQuery(WxPayRefundQueryRequest parameter) {
        try {
            return payService.refundQuery(parameter);
        } catch (Exception ex) {
            throw new ModuleException(ex.getMessage(),
                                      ex);
        }
    }

    @Override
    public WxPayRefundQueryResult refundQueryV2(WxPayRefundQueryRequest parameter) {
        try {
            return payService.refundQueryV2(parameter);
        } catch (Exception ex) {
            throw new ModuleException(ex.getMessage(),
                                      ex);
        }
    }

    @Override
    public WxPayRefundQueryV3Result refundQueryV3(WxPayRefundQueryV3Request parameter) {
        try {
            return payService.refundQueryV3(parameter);
        } catch (Exception ex) {
            throw new ModuleException(ex.getMessage(),
                                      ex);
        }
    }
}
