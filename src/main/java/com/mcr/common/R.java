package com.mcr.common;

import lombok.Data;

import java.io.Serializable;

/**
 * 通用返回结果，服务端响应的数据最终都会封装成此对象
 *
 * @param <T>
 */
@Data
public class R<T> implements Serializable {
    private Integer code;
    private String message;
    private T data;
    private String version;
    private String mobileVersion;

    //    private Map<String, Object> map = new HashMap<>();
    public static <T> R<T> success(T object) {
        R<T> r = new R<>();
        r.message = "OK";
        r.data = object;
        r.code = 200;
        r.version = BaseContext.getVersion();
        r.mobileVersion = BaseContext.getMobileVersion();
        return r;
    }

    public static <T> R<T> error(String message) {
        R<T> r = new R<>();
        r.message = message;
        r.code = 0;
        return r;
    }

    public static <T> R<T> error(String message, T error) {
        R<T> r = new R<>();
        r.message = message;
        r.data = error;
        r.code = 0;
        return r;
    }
}
/*
    public R<T> add(String key, Object value) {
        this.map.put(key, value);
        return this;
    }
 */
/*
 * const successCode = '0,1,200,20000'
 * const noAuthCode = '401,403'
 */