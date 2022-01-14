package top.kuanghua.khcomomon.entity;

import java.io.Serializable;

/**
 * 描述
 *
 * @author 三国的包子
 * @version 1.0
 * @package entity *
 * @since 1.0
 */
public class Result<T> implements Serializable {
    private boolean flag;//是否成功
    private Integer code;//返回码
    private String msg;//返回消息
    private T data;//返回数据

    public Result(boolean flag, Integer code, String msg, Object data) {
        this.flag = flag;
        this.code = code;
        this.msg = msg;
        this.data = (T) data;
    }

    public Result(boolean flag, Integer code, String msg) {
        this.flag = flag;
        this.code = code;
        this.msg = msg;
    }

    public Result() {
        this.flag = true;
        this.code = StatusCode.OK;
        this.msg = "操作成功!";
    }

    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return msg;
    }

    public void setMessage(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
