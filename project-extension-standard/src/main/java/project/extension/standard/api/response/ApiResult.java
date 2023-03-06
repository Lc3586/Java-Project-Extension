package project.extension.standard.api.response;

import org.springframework.http.HttpStatus;

/**
 * 接口返回数据
 *
 * @author LCTR
 * @date 2023-03-06
 */
public class ApiResult {
    public ApiResult(HttpStatus code,
                     String msg) {
        setCode(code);
        setMsg(msg);
    }

    /**
     * 状态码
     *
     * @see HttpStatus
     */
    private HttpStatus code;

    /**
     * 信息
     */
    private String msg;

    /**
     * 状态码
     *
     * @see HttpStatus
     */
    public HttpStatus getCode() {
        return code;
    }

    public void setCode(HttpStatus code) {
        this.code = code;
    }

    /**
     * 信息
     */
    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    /**
     * 成功
     */
    public static ApiResult success() {
        return new ApiResult(HttpStatus.OK,
                             null);
    }

    /**
     * 成功
     *
     * @param msg 信息
     */
    public static ApiResult success(String msg) {
        return new ApiResult(HttpStatus.OK,
                             msg);
    }

    /**
     * 失败
     *
     * @param msg 信息
     */
    public static ApiResult error(String msg) {
        return new ApiResult(HttpStatus.INTERNAL_SERVER_ERROR,
                             msg);
    }

    /**
     * 失败
     *
     * @param code 编码
     * @param msg  信息
     */
    public static ApiResult error(HttpStatus code,
                                  String msg) {
        return new ApiResult(code,
                             msg);
    }
}
