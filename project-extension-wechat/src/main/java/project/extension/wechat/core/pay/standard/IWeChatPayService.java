package project.extension.wechat.core.pay.standard;

import com.github.binarywang.wxpay.bean.order.WxPayAppOrderResult;
import com.github.binarywang.wxpay.bean.order.WxPayMpOrderResult;
import com.github.binarywang.wxpay.bean.order.WxPayMwebOrderResult;
import com.github.binarywang.wxpay.bean.order.WxPayNativeOrderResult;
import com.github.binarywang.wxpay.bean.request.*;
import com.github.binarywang.wxpay.bean.result.*;
import com.github.binarywang.wxpay.service.WxPayService;

/**
 * 微信支付服务
 *
 * @author LCTR
 * @date 2023-03-14
 */
public interface IWeChatPayService {
    /**
     * 获取原生的微信支付服务对象
     */
    WxPayService getPayService();

    /**
     * 创建扫码支付订单
     *
     * @param parameter 参数
     */
    WxPayNativeOrderResult createOrder4Native(WxPayUnifiedOrderRequest parameter);

    /**
     * 创建APP支付订单
     *
     * @param parameter 参数
     */
    WxPayAppOrderResult createOrder4App(WxPayUnifiedOrderRequest parameter);

    /**
     * 创建公众号支付订单
     *
     * @param parameter 参数
     */
    WxPayMpOrderResult createOrder4JsApi(WxPayUnifiedOrderRequest parameter);

    /**
     * 创建微信H5支付订单
     *
     * @param parameter 参数
     */
    WxPayMwebOrderResult createOrder4MWeb(WxPayUnifiedOrderRequest parameter);

    /**
     * 创建刷卡支付订单
     *
     * @param parameter 参数
     */
    WxPayMicropayResult createOrder4MicroPay(WxPayUnifiedOrderRequest parameter);

    /**
     * 创建V3版本扫码支付订单
     *
     * @param parameter 参数
     */
    String createOrderV34Native(WxPayUnifiedOrderV3Request parameter);

    /**
     * 创建V3版本APP支付订单
     *
     * @param parameter 参数
     */
    WxPayUnifiedOrderV3Result.AppResult createOrderV34App(WxPayUnifiedOrderV3Request parameter);

    /**
     * 创建V3版本公众号支付订单
     *
     * @param parameter 参数
     */
    WxPayUnifiedOrderV3Result.JsapiResult createOrderV34JsApi(WxPayUnifiedOrderV3Request parameter);

    /**
     * 创建V3版本H5支付订单
     *
     * @param parameter 参数
     */
    String createOrderV34H5(WxPayUnifiedOrderV3Request parameter);

    /**
     * 申请退款
     */
    WxPayRefundResult refund(WxPayRefundRequest parameter);

    /**
     * 申请退款（支持单品）
     */
    WxPayRefundResult refundV2(WxPayRefundRequest parameter);

    /**
     * 申请退款（支持单品）
     */
    WxPayRefundV3Result refundV3(WxPayRefundV3Request parameter);

    /**
     * 查询退款
     *
     * @param parameter 参数
     */
    WxPayRefundQueryResult refundQuery(WxPayRefundQueryRequest parameter);

    /**
     * 查询退款（支持单品）
     *
     * @param parameter 参数
     */
    WxPayRefundQueryResult refundQueryV2(WxPayRefundQueryRequest parameter);

    /**
     * 查询退款
     *
     * @param parameter 参数
     */
    WxPayRefundQueryV3Result refundQueryV3(WxPayRefundQueryV3Request parameter);
}
