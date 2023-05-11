package top.hugo.common.exception.user;


import top.hugo.common.exception.base.BaseException;

/**
 * 用户信息异常类
 *
 * @author hugo
 */
public class UserException extends BaseException {
    private static final long serialVersionUID = 1L;

    public UserException(String code, Object... args) {
        super("user", code, args, null);
    }
}
