package project.extension.exception;

/**
 * 工具模块的异常
 *
 * @author LCTR
 * @date 2022-01-09
 */
public class CommonException
        extends RuntimeException {
    /**
     * 消息
     */
    private String message;

    /**
     * 内部异常
     */
    private Throwable innerException;

    public CommonException() {
    }

    public CommonException(String message) {
        this(message,
             null);
    }

    public CommonException(String message,
                           Throwable innerException) {
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
