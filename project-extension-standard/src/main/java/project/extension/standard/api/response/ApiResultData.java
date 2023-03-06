package project.extension.standard.api.response;

import org.springframework.http.HttpStatus;

/**
 * 接口返回数据
 *
 * @author LCTR
 * @date 2023-03-06
 */
public class ApiResultData<T>
        extends ApiResult {
    public ApiResultData(HttpStatus code,
                         String msg,
                         T data) {
        super(code,
              msg);
        setData(data);
    }

    /**
     * 数据
     */
    private T data;

    /**
     * 数据
     */
    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    /**
     * 成功
     *
     * @param data 数据
     */
    public static <T> ApiResultData<T> success(T data) {
        return new ApiResultData<>(HttpStatus.OK,
                                   null,
                                   data);
    }

    /**
     * 成功
     *
     * @param msg  信息
     * @param data 数据
     */
    public static <T> ApiResultData<T> success(String msg,
                                               T data) {
        return new ApiResultData<>(HttpStatus.OK,
                                   msg,
                                   data);
    }

    /**
     * 失败
     *
     * @param msg  信息
     * @param data 数据
     */
    public static <T> ApiResultData<T> error(String msg,
                                             T data) {
        return new ApiResultData<>(HttpStatus.INTERNAL_SERVER_ERROR,
                                   msg,
                                   data);
    }

    /**
     * 失败
     *
     * @param code 编码
     * @param msg  信息
     * @param data 数据
     */
    public static <T> ApiResultData<T> error(HttpStatus code,
                                             String msg,
                                             T data) {
        return new ApiResultData<>(code,
                                   msg,
                                   data);
    }
}
