package top.kuanghua.commonpom.exception;

import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import top.kuanghua.commonpom.entity.ResResult;

/*
 * 全局异常处理
 * */
@RestControllerAdvice
public class BaseExceptionHandler {
    /***
     * 异常处理
     * @param:
     * @return
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResResult MethodArgumentNotValidExceptionHandler(MethodArgumentNotValidException e) {
        ObjectError objectError = e.getBindingResult().getAllErrors().get(0);
        return new ResResult().error(objectError.getDefaultMessage());
    }

    @ExceptionHandler(value = Exception.class)
    public ResResult error(Exception e) {
        e.printStackTrace();
        return new ResResult().error(e.getMessage());
    }
}