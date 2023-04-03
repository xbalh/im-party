package com.im.imparty.web.vo;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;

@Data
public class BaseResult<T> {

    public BaseResult(int code) {
        this.code = code;
    }

    public BaseResult(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    private int code;

    private String msg;

    private T data;

    public static <T> BaseResult<T> ok(String msg) {
        return new BaseResult(200, msg);
    }

    public static <T> BaseResult<T> ok(String msg, T data) {
        return new BaseResult(200, msg).data(data);
    }
    public static <T> BaseResult<T> fail(String msg) {
        return new BaseResult(500, msg);
    }

    public BaseResult<T> data(T data) {
        this.data = data;
        return this;
    }

    public String toJSONString() {
        return JSONObject.toJSONString(this);
    }

}
