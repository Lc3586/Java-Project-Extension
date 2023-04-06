package project.extension.wechat.core;

import project.extension.wechat.core.mp.standard.IWeChatMpService;
import project.extension.wechat.core.pay.standard.IWeChatPayService;

import java.util.List;
import java.util.Map;

/**
 * 微信服务构造器
 *
 * @author LCTR
 * @date 2023-03-15
 */
public interface INaiveWeChatService {
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
    String DEFAULT_MP_SERVICE_IOC_NAME = "weChatMpService";

    /**
     * 默认微信支付服务在IOC容器中的名称
     */
    String DEFAULT_PAY_SERVICE_IOC_NAME = "weChatPayService";

    /**
     * 微信公众号服务在IOC容器中的名称前缀
     */
    String MP_SERVICE_IOC_PREFIX = "weChatMpService#";

    /**
     * 微信支付服务在IOC容器中的名称前缀
     */
    String PAY_SERVICE_IOC_PREFIX = "weChatPayService#";

    /**
     * 服务在IOC容器中的名称前缀
     */
    String SERVLET_IOC_PREFIX = "servlet#";

    /**
     * 获取默认的公众号
     */
    String defaultMp();

    /**
     * 公众号是否存在
     *
     * @param mp 公众号
     */
    boolean isMpExists(String mp);

    /**
     * 公众号是否启用
     *
     * @param mp 公众号
     */
    boolean isMpEnable(String mp);

    /**
     * 获取所有的公众号
     *
     * @param enabledOnly 仅获取启用的公众号
     */
    List<String> allMp(boolean enabledOnly);

    /**
     * 加载所有的微信公众号服务
     */
    Map<String, IWeChatMpService> loadAllWeChatMpService();

    /**
     * 获取默认微信公众号服务
     */
    IWeChatMpService getDefaultWeChatMpService();

    /**
     * 获取微信公众号服务
     *
     * @param mp 公众号
     */
    IWeChatMpService getWeChatMpService(String mp);

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
