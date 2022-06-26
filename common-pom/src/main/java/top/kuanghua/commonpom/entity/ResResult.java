package top.kuanghua.commonpom.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

/**
 * 描述
 *
 * @author kuanghua
 * @version 1.0
 * @package entity *
 * @since 1.0
 */
@Data
@AllArgsConstructor
public class ResResult<T> implements Serializable {
    private static final long serialVersionUID = 2344775856369318037L;
    private boolean flag;//是否成功
    private Integer code;//返回码
    private String msg;//返回消息
    private T data;//返回数据

    public ResResult success(Object data) {
        this.flag = true;
        this.code = top.kuanghua.commonpom.entity.StatusCode.OK;
        this.data = (T) data;
        return this;
    }

    public ResResult success() {
        this.flag = true;
        this.code = top.kuanghua.commonpom.entity.StatusCode.OK;
        this.msg = "操作成功！";
        return this;
    }

    public ResResult error(String msg) {
        this.flag = false;
        this.code = top.kuanghua.commonpom.entity.StatusCode.ERROR;
        this.msg = msg;
        return this;
    }

    public ResResult(String msg, Object data) {
        this.flag = true;
        this.code = top.kuanghua.commonpom.entity.StatusCode.OK;
        this.msg = msg;
        this.data = (T) data;
    }

    public ResResult(boolean flag, Integer code, String msg) {
        this.flag = flag;
        this.code = code;
        this.msg = msg;
    }

    public ResResult() {
        this.flag = true;
        this.code = top.kuanghua.commonpom.entity.StatusCode.OK;
        this.msg = "操作成功!";
    }

}
