package project.extension.wechat.globalization;

import java.util.HashMap;
import java.util.Map;

/**
 * 字符串资源zh_CN
 *
 * @author LCTR
 * @date 2023-03-15
 */
public class Strings_zh_CN {
    private static Map<String, String> values = null;

    /**
     * 获取值
     *
     * @param code 编码
     * @return 值
     */
    public static String getValue(String code) {
        if (values == null) {
            values = new HashMap<>();
            //公众号相关错误信息
            values.put("ConfigMPNameUndefined",
                       "在配置文件的project.extension.mybatis.multiDataSource中没有为数据源配置名称");
            values.put("ConfigMPUndefined",
                       "配置文件project.extension.wechat.multiMP中未找到名称为%1$2s的公众号配置");
            values.put("ConfigMPRepeat",
                       "配置文件project.extension.wechat.multiMP中存在多个名称为%1$2s的公众号");
            values.put("ConfigMPOptionUndefined",
                       "配置文件project.extension.wechat.multiMP - name为%1$2s的公众号缺失%2$2s设置，并且也没有设置默认的%2$2s");
            values.put("ConfigMPNotActive",
                       "未启用配置文件中的project.extension.wechat.multiMP.%1$2s公众号");
            values.put("ConfigMPOptionInvalid",
                       "配置文件project.extension.wechat.multiMP - name为%1$2s的公众号的%2$2s设置无效，设置的值：%3$2s");
            values.put("CreateWeChatMPServiceFailed",
                       "创建微信公众号服务失败，配置来源于project.extension.wechat.multiMP中名称为%1$2s的公众号配置");
            values.put("WeChatMPService4VersionUndefined",
                       "找不到%1$2s版本的名称为%2$2s的微信公众号服务");
            values.put("UseDefaultWeChatMPServiceMethod",
                       "请使用请使用INaiveWeChatServiceProvider.getDefaultWeChatMPService方法获取默认的微信公众号服务");

            //商户号相关错误信息
            values.put("ConfigPayNameUndefined",
                       "在配置文件的project.extension.wechat.multiPay中没有为商户号配置名称");
            values.put("ConfigPayUndefined",
                       "配置文件project.extension.wechat.multiPay中未找到名称为%1$2s的商户号配置");
            values.put("ConfigPayRepeat",
                       "配置文件project.extension.wechat.multiPay中存在多个名称为%1$2s的商户号");
            values.put("ConfigPayOptionUndefined",
                       "配置文件project.extension.wechat.multiPay - name为%1$2s的商户号缺失%2$2s设置，并且也没有设置默认的%2$2s");
            values.put("ConfigPayNotActive",
                       "未启用配置文件中的project.extension.wechat.multiPay.%1$2s商户号");
            values.put("ConfigPayOptionInvalid",
                       "配置文件project.extension.wechat.multiPay - name为%1$2s的商户号的%2$2s设置无效，设置的值：%3$2s");
            values.put("CreateWeChatPayServiceFailed",
                       "创建微信支付服务失败，配置来源于project.extension.wechat.multiPay中名称为%1$2s的商户号配置");
            values.put("WeChatPayService4VersionUndefined",
                       "找不到%1$2s版本的名称为%2$2s的微信支付服务");
            values.put("UseDefaultWeChatPayServiceMethod",
                       "请使用请使用INaiveWeChatServiceProvider.getDefaultWeChatPayService方法获取默认的微信支付服务");

            //通用错误信息
            values.put("ConfigIdentical",
                       "%1$2s配置和%2$2s配置禁止拥有相同的值");
            values.put("ConfigRequiredWhenDisabledMiddleware",
                       "未启用%1$2s中间件时%2$2s配置不可为空");
            values.put("SetupMiddlewareFailed",
                       "设置%1$2s中间件失败");
            values.put("MiddlewarePreHandleFailed",
                       "%1$2s中间件预处理失败");
            values.put("HandlerUndefined",
                       "找不到%1$2s处理类，请检查是否正确配置了该处理类");
            values.put("FunctionNotImplemented",
                       "方法尚未实现");
            values.put("CreateInstanceFailed",
                       "创建%1$2s实例失败");
            values.put("InstanceParamsUndefined",
                       "%1$2s构造函数%2$2s参数不可为空");
            values.put("UnsupportedDateType",
                       "暂不支持%1$2s类型的日期");
            values.put("FormatDateFailed",
                       "格式化日期%1$2s到%2$2s格式失败");
            values.put("DateFormatNonStandard",
                       "%1$2s 要求 %2$2s 格式必须为：%3$2s其中一种");
            values.put("UnknownValue",
                       "%1$2s的值%2$2s无效");
            values.put("UnSupportOperation",
                       "不支持%1$2s操作");
            values.put("DataUndefined",
                       "数据不存在或已被移除");
        }

        return values.get(code);
    }
}
