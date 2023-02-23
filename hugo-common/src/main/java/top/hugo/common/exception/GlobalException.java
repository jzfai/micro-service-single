package top.hugo.common.exception;

/**
 * 全局异常
 *
 * @author ruoyi
 */
public class GlobalException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    /**
     * 错误提示
     */
    private String message;

    private String detailMessage;

    /**
     * 空构造方法，避免反序列化问题
     */
    public GlobalException() {
    }

    public GlobalException(String message) {
        this.message = message;
    }

    public String getDetailMessage() {
        return detailMessage;
    }

    public GlobalException setDetailMessage(String detailMessage) {
        this.detailMessage = detailMessage;
        return this;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public GlobalException setMessage(String message) {
        this.message = message;
        return this;
    }
}
