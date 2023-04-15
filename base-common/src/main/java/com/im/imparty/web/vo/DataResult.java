package com.im.imparty.web.vo;

import java.util.HashMap;

/**
 * 统一返回值
 */
public class DataResult extends HashMap<String,Object> {

    private static final long serialVersionUID = 1L;

    //状态码
    private int code;
    //返回内容
    private String msg;
    //数据对象
    private Object data;

    /**
     * 状态码
     */
    public static final String CODE_TAG = "code";

    /**
     * 返回内容
     */
    public static final String MSG_TAG = "msg";

    /**
     * 数据对象
     */
    public static final String DATA_TAG = "data";

    /**
     * 初始化一个新创建的 DataResult 对象，使其表示一个空消息。
     * @param
     */
    public DataResult() {
    }

    /**
     * 初始化一个新创建的 DataResult 对象
     *
     * @param code 状态码
     * @param msg  返回内容
     */
    public DataResult(int code, String msg) {
        super.put(CODE_TAG, code);
        super.put(MSG_TAG, msg);
        this.code=code;
        this.msg=msg;
    }

    /**
     * 初始化一个新创建的 DataResult 对象
     *
     * @param code 状态码
     * @param msg  返回内容
     * @param data 数据对象
     */
    public DataResult(int code, String msg, Object data) {
        super.put(CODE_TAG, code);
        super.put(MSG_TAG, msg);
        this.code=code;
        this.msg=msg;
        if (!(data==null)) {
            super.put(DATA_TAG, data);
            this.data=data;
        }
    }

    /**
     * 返回成功消息
     *
     * @return 成功消息
     */
    public static DataResult success() {
        return DataResult.success("操作成功!");
    }

    /**
     * 返回成功消息
     *
     * @param msg 返回内容
     * @return 成功消息
     */
    public static DataResult success(String msg) {
        return DataResult.success(msg, null);
    }

    /**
     * 返回成功数据
     *
     * @return 成功消息
     */
    public static DataResult success(Object data) {
        return DataResult.success("操作成功!", data);
    }


    /**
     * 返回成功消息
     *
     * @param msg  返回内容
     * @param data 数据对象
     * @return 成功消息
     */
    public static DataResult success(String msg, Object data) {
        return new DataResult(200, msg, data);
    }

    /**
     * 返回错误消息
     *
     * @return
     */
    public static DataResult error() {
        return DataResult.error("操作失败!");
    }

    /**
     * 返回错误消息
     *
     * @param msg 返回内容
     * @return 警告消息
     */
    public static DataResult error(String msg) {
        return DataResult.error(msg, null);
    }

    /**
     * 返回错误消息
     *
     * @param msg  返回内容
     * @param data 数据对象
     * @return 警告消息
     */
    public static DataResult error(String msg, Object data) {
        return new DataResult(500, msg, data);
    }

    /**
     * 返回错误消息
     *
     * @param code 状态码
     * @param msg  返回内容
     * @return 警告消息
     */
    public static DataResult error(int code, String msg) {
        return new DataResult(code, msg, null);
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}

