package project.extension.mybatis.edge.model;

/**
 * 返回结果为空
 *
 * @author LCTR
 * @date 2022-04-07
 */
public class NullResultException extends RuntimeException {
    public NullResultException(String message) {
        this.setMessage(message);
    }

    private String message;

    /**
     * 信息
     */
    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
