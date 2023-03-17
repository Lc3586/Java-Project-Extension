package project.extension.wechat.core;

import project.extension.wechat.core.mp.standard.IWeChatMPService;
import project.extension.wechat.core.pay.standard.IWeChatPayService;

import java.util.List;
import java.util.Map;

/**
 * 微信服务构造器
 *
 * @author LCTR
 * @date 2023-03-15
 */
public interface INaiveWeChatServiceProvider {
    /**
     * 默认公众号
     */
    String DEFAULT_MP = "master";

    /**
     * 默认商户号
     */
    String DEFAULT_PAY = "master";

    /**
     * 默认微信公众号服务在IOC容器中的名称
     */
    String DEFAULT_MP_SERVICE_IOC_NAME = "weChatMPService";

    /**
     * 默认微信支付服务在IOC容器中的名称
     */
    String DEFAULT_PAY_SERVICE_IOC_NAME = "weChatPayService";

    /**
     * 微信公众号服务在IOC容器中的名称前缀
     */
    String MP_SERVICE_IOC_PREFIX = "weChatMPService#";

    /**
     * 微信支付服务在IOC容器中的名称前缀
     */
    String PAY_SERVICE_IOC_PREFIX = "weChatPayService#";

    /**
     * 中间件在IOC容器中的名称前缀
     */
    String MIDDLEWARE_IOC_PREFIX = "middleware#";

    /**
     * 获取默认的公众号
     */
    String defaultMP();

    /**
     * 公众号是否存在
     *
     * @param mp 公众号
     */
    boolean isMPExists(String mp);

    /**
     * 公众号是否启用
     *
     * @param mp 公众号
     */
    boolean isMPEnable(String mp);

    /**
     * 获取所有的公众号
     *
     * @param enabledOnly 仅获取启用的公众号
     */
    List<String> allMP(boolean enabledOnly);

    /**
     * 加载所有的微信公众号服务
     */
    Map<String, IWeChatMPService> loadAllWeChatMPService();

    /**
     * 获取默认微信公众号服务
     */
    IWeChatMPService getDefaultWeChatMPService();

    /**
     * 获取微信公众号服务
     *
     * @param mp 公众号
     */
    IWeChatMPService getWeChatMPService(String mp);

    /**
     * 获取默认的商户号
     */
    String defaultPay();

    /**
     * 商户号是否存在
     *
     * @param pay 商户号
     */
    boolean isPayExists(String pay);

    /**
     * 商户号是否启用
     *
     * @param pay 商户号
     */
    boolean isPayEnable(String pay);

    /**
     * 获取所有的商户号
     *
     * @param enabledOnly 仅获取启用的商户号
     */
    List<String> allPay(boolean enabledOnly);

    /**
     * 加载所有的微信支付服务
     */
    Map<String, IWeChatPayService> loadAllWeChatPayService();

    /**
     * 获取默认微信支付服务
     */
    IWeChatPayService getDefaultWeChatPayService();

    /**
     * 获取微信支付服务
     *
     * @param pay 商户号
     */
    IWeChatPayService getWeChatPayService(String pay);
}
