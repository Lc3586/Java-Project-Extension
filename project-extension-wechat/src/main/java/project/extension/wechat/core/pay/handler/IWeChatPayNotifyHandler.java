package project.extension.wechat.core.pay.handler;

import com.github.binarywang.wxpay.bean.notify.WxPayOrderNotifyResult;
import com.github.binarywang.wxpay.bean.notify.WxPayOrderNotifyV3Result;
import com.github.binarywang.wxpay.bean.notify.WxPayRefundNotifyResult;
import com.github.binarywang.wxpay.bean.notify.WxPayRefundNotifyV3Result;
import project.extension.wechat.config.PayConfig;

/**
 * 微信收付通通知处理类
 *
 * @author LCTR
 * @date 2023-03-16
 */
public interface IWeChatPayNotifyHandler {
    /**
     * 付款通知
     *
     * @param config     商户号配置
     * @param notifyInfo 通知信息
     * @return WxPayNotifyResponse.success()或WxPayNotifyResponse.fail()
     */
    String payNotify(PayConfig config,
                     WxPayOrderNotifyResult notifyInfo);

    /**
     * 付款通知，V3版本接口
     *
     * @param config     商户号配置
     * @param notifyInfo 通知信息
     * @return WxPayNotifyResponse.success()或WxPayNotifyResponse.fail()
     */
    String payNotifyV3(PayConfig config,
                       WxPayOrderNotifyV3Result notifyInfo);

    /**
     * 退款通知，V3版本接口
     *
     * @param config     商户号配置
     * @param notifyInfo 通知信息
     * @return WxPayNotifyResponse.success()或WxPayNotifyResponse.fail()
     */
    String refundNotifyV3(PayConfig config,
                          WxPayRefundNotifyV3Result notifyInfo);

    /**
     * 退款通知
     *
     * @param config     商户号配置
     * @param notifyInfo 通知信息
     * @return WxPayNotifyResponse.success()或WxPayNotifyResponse.fail()
     */
    String refundNotify(PayConfig config,
                        WxPayRefundNotifyResult notifyInfo);
}
