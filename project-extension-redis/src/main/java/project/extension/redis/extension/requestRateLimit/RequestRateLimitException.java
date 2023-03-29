package project.extension.redis.extension.requestRateLimit;

/**
 * 请求速率超过限制的异常
 *
 * @author LCTR
 * @date 2023-03-29
 */
public class RequestRateLimitException
        extends RuntimeException {
    /**
     * 消息
     */
    private String message;

    /**
     * 内部异常
     */
    private Throwable innerException;

    public RequestRateLimitException() {
    }

    public RequestRateLimitException(String message) {
        this(message,
             null);
    }

    public RequestRateLimitException(String message,
                                     Throwable innerException) {
        super(message,
              innerException);
        this.message = message;
        this.setInnerException(innerException);
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Throwable getInnerException() {
        return innerException;
    }

    public void setInnerException(Throwable innerException) {
        this.innerException = innerException;
    }
}
