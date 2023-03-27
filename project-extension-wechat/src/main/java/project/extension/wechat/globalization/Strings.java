package project.extension.wechat.globalization;

import org.springframework.context.i18n.LocaleContextHolder;

import java.util.Locale;

/**
 * 字符串资源
 *
 * @author LCTR
 * @date 2023-03-15
 */
public class Strings {
    /**
     * 本土化信息
     */
    private static Locale locale = LocaleContextHolder.getLocale();

    /**
     * 获取当前的本地化信息
     *
     * @return 本土化信息
     */
    public static Locale getLocale() {
        return Strings.locale;
    }

    /**
     * 设置本地化信息
     * <p>设置中文：Locale.SIMPLIFIED_CHINESE</p>
     * <p>设置英文：Locale.US</p>
     *
     * @param locale 本土化信息
     */
    public static void setLocale(Locale locale) {
        Strings.locale = locale;
    }

    /**
     * 获取字符串
     *
     * @param code 编码
     * @param args 参数
     * @return 字符串
     */
    public static String getString(String code,
                                   Object... args) {
        String message;
        if (Locale.CHINESE.equals(locale) || Locale.SIMPLIFIED_CHINESE.equals(locale)
                || Locale.TRADITIONAL_CHINESE.equals(locale)) message = String.format(Strings_zh_CN.getValue(code),
                                                                                      args);
        else message = String.format(Strings_en_US.getValue(code),
                                     args);

        return String.format("WeChat: %s",
                             message);
    }

    /**
     * 在配置文件中没有为公众号配置名称
     */
    public static String getConfigMpNameUndefined() {
        return getString("ConfigMpNameUndefined");
    }

    /**
     * 找不到指定的公众号配置
     *
     * @param name 公众号名称
     */
    public static String getConfigMpUndefined(String name) {
        return getString("ConfigMpUndefined",
                         name);
    }

    /**
     * 公众号配置重复
     *
     * @param name 公众号名称
     */
    public static String getConfigMpRepeat(String name) {
        return getString("ConfigMpRepeat",
                         name);
    }

    /**
     * 公众号配置未启用
     *
     * @param name 公众号名称
     */
    public static String getConfigMpNotActive(String name) {
        return getString("ConfigMpNotActive",
                         name);
    }

    /**
     * 公众号配置项缺失
     *
     * @param name   公众号名称
     * @param option 配置项名称
     */
    public static String getConfigMpOptionUndefined(String name,
                                                    String option) {
        return getString("ConfigMpOptionUndefined",
                         name,
                         option);
    }

    /**
     * 公众号配置项无效
     *
     * @param name   公众号名称
     * @param option 配置项名称
     * @param value  配置项值
     */
    public static String getConfigMpOptionInvalid(String name,
                                                  String option,
                                                  String value) {
        return getString("ConfigMpOptionInvalid",
                         name,
                         option,
                         value);
    }

    /**
     * 未启用服务时此配置不可为空
     *
     * @param name   服务名称
     * @param option 配置名称
     */
    public static String getConfigRequiredWhenDisabledServlet(String name,
                                                              String option) {
        return getString("ConfigRequiredWhenDisabledServlet",
                         name,
                         option);
    }

    /**
     * 两个配置的值不可相同
     *
     * @param option1 配置名称1
     * @param option2 配置名称2
     */
    public static String getConfigIdentical(String option1,
                                            String option2) {
        return getString("ConfigIdentical",
                         option1,
                         option2);
    }

    /**
     * 创建指定的微信公众号服务失败
     *
     * @param name 公众号名称
     */
    public static String getCreateWeChatMpServiceFailed(String name) {
        return getString("CreateWeChatMpServiceFailed",
                         name);
    }

    /**
     * 找不到指定版本的微信公众号服务
     *
     * @param name    公众号名称
     * @param version 接口版本
     */
    public static String getWeChatMpService4VersionUndefined(String name,
                                                             String version) {
        return getString("WeChatMpService4VersionUndefined",
                         name,
                         version);
    }

    /**
     * 请使用INaiveWeChatServiceProvider.getDefaultWeChatMpService方法获取默认的微信公众号服务
     */
    public static String getUseDefaultWeChatMpServiceMethod() {
        return getString("UseDefaultWeChatMpServiceMethod");
    }

    /**
     * 在配置文件中没有为商户号配置名称
     */
    public static String getConfigPayNameUndefined() {
        return getString("ConfigPayNameUndefined");
    }

    /**
     * 找不到指定的商户号配置
     *
     * @param name 商户号名称
     */
    public static String getConfigPayUndefined(String name) {
        return getString("ConfigPayUndefined",
                         name);
    }

    /**
     * 商户号配置重复
     *
     * @param name 商户号名称
     */
    public static String getConfigPayRepeat(String name) {
        return getString("ConfigPayRepeat",
                         name);
    }

    /**
     * 商户号配置未启用
     *
     * @param name 商户号名称
     */
    public static String getConfigPayNotActive(String name) {
        return getString("ConfigPayNotActive",
                         name);
    }

    /**
     * 商户号配置项缺失
     *
     * @param name   商户号名称
     * @param option 配置项名称
     */
    public static String getConfigPayOptionUndefined(String name,
                                                     String option) {
        return getString("ConfigPayOptionUndefined",
                         name,
                         option);
    }

    /**
     * 商户号配置项无效
     *
     * @param name   商户号名称
     * @param option 配置项名称
     * @param value  配置项值
     */
    public static String getConfigPayOptionInvalid(String name,
                                                   String option,
                                                   String value) {
        return getString("ConfigPayOptionInvalid",
                         name,
                         option,
                         value);
    }

    /**
     * 创建指定的微信支付服务失败
     *
     * @param name 商户号名称
     */
    public static String getCreateWeChatPayServiceFailed(String name) {
        return getString("CreateWeChatPayServiceFailed",
                         name);
    }

    /**
     * 找不到指定版本的微信支付服务
     *
     * @param name    商户号名称
     * @param version 接口版本
     */
    public static String getWeChatPayService4VersionUndefined(String name,
                                                              String version) {
        return getString("WeChatPayService4VersionUndefined",
                         name,
                         version);
    }

    /**
     * 请使用INaiveWeChatServiceProvider.getDefaultWeChatPayService方法获取默认的微信支付服务
     */
    public static String getUseDefaultWeChatPayServiceMethod() {
        return getString("UseDefaultWeChatPayServiceMethod");
    }

    /**
     * 设置中间件失败
     *
     * @param name 中间件名称
     */
    public static String getSetupMiddlewareFailed(String name) {
        return getString("SetupMiddlewareFailed",
                         name);
    }

    /**
     * 服务处理失败
     *
     * @param name 中间件名称
     */
    public static String getServletHandleFailed(String name) {
        return getString("ServletHandleFailed",
                         name);
    }

    /**
     * 找不到指定处理类
     *
     * @param name 服务名称
     */
    public static String getHandlerUndefined(String name) {
        return getString("HandlerUndefined",
                         name);
    }

    /**
     * 找不到指定公众号的消息路由
     *
     * @param mp 公众号
     */
    public static String getWxMpMessageRouterUndefined(String mp) {
        return getString("WxMpMessageRouterUndefined",
                         mp);
    }

    /**
     * 方法尚未实现
     */
    public static String getFunctionNotImplemented() {
        return getString("FunctionNotImplemented");
    }

    /**
     * 创建实例失败
     *
     * @param instanceName 实例名称
     */
    public static String getCreateInstanceFailed(String instanceName) {
        return getString("CreateInstanceFailed",
                         instanceName);
    }

    /**
     * 实例参数缺失
     *
     * @param instanceName 实例名称
     * @param params       参数名称
     */
    public static String getInstanceParamsUndefined(String instanceName,
                                                    String params) {
        return getString("InstanceParamsUndefined",
                         instanceName,
                         params);
    }

    /**
     * 不支持的日期数据类型
     *
     * @param typeName 类型名称
     */
    public static String getUnsupportedDateType(String typeName) {
        return getString("UnsupportedDateType",
                         typeName);
    }

    /**
     * 格式化日期失败
     *
     * @param value  日期值
     * @param format 格式
     */
    public static String getFormatDateFailed(String value,
                                             String format) {
        return getString("FormatDateFailed",
                         value,
                         format);
    }

    /**
     * 数据格式不规范
     *
     * @param object 指定对象
     * @param value  指定值
     * @param format 格式
     */
    public static String getDataFormatNonStandard(String object,
                                                  String value,
                                                  String format) {
        return getString("DataFormatNonStandard",
                         object,
                         value,
                         format);
    }

    /**
     * 无效的值
     *
     * @param name  值名称
     * @param value 值
     */
    public static String getUnknownValue(String name,
                                         String value) {
        return getString("UnknownValue",
                         name,
                         value);
    }

    /**
     * 不支持此操作
     *
     * @param operation 操作
     */
    public static String getUnSupportOperation(String operation) {
        return getString("UnSupportOperation",
                         operation);
    }

    /**
     * 数据不存在或已被移除
     */
    public static String getDataUndefined() {
        return getString("DataUndefined");
    }

}
