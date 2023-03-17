package project.extension.wechat.extension;

import com.github.binarywang.wxpay.bean.notify.SignatureHeader;

import javax.servlet.http.HttpServletRequest;

/**
 * 签名帮助类
 *
 * @author LCTR
 * @date 2023-03-16
 */
public class SignatureHelper {
    public static final String RequestHeaderSerial = "Wechatpay-Serial";
    public static final String RequestHeaderTimeStamp = "Wechatpay-TimeStamp";
    public static final String RequestHeaderNonce = "Wechatpay-Nonce";
    public static final String RequestHeaderSignature = "Wechatpay-Signature";

    /**
     * 从请求头中获取签名信息
     *
     * @param request 请求对象
     * @return 签名信息，对象中的值可能为空
     */
    public static SignatureHeader getSignatureHeader(HttpServletRequest request) {
        SignatureHeader signatureHeader = new SignatureHeader();
        signatureHeader.setSerial(request.getHeader(RequestHeaderSerial));
        signatureHeader.setTimeStamp(request.getHeader(RequestHeaderTimeStamp));
        signatureHeader.setNonce(request.getHeader(RequestHeaderNonce));
        signatureHeader.setSignature(request.getHeader(RequestHeaderSignature));
        return signatureHeader;
    }
}
