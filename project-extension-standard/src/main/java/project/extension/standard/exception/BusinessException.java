package project.extension.standard.exception;

/**
 * 业务层级的异常
 *
 * @author LCTR
 * @date 2022-12-08
 */
public class BusinessException
        extends RuntimeException {
    /**
     * 消息
     */
    private String message;

    /**
     * 内部异常
     */
    private Throwable innerException;

    public BusinessException() {
    }

    public BusinessException(String message) {
        this(message,
             null);
    }

    public BusinessException(String message,
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
