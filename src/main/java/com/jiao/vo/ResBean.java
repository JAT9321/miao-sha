package com.jiao.vo;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * auth:@highSky
 * create:2022/8/12
 * email:zgt9321@qq.com
 **/
@Data
public class ResBean {

    private long code;
    private String message;
    private Object object;

    public static ResBean success() {
        return new ResBean(ResBeanEnum.SUCCESS.getCode(), ResBeanEnum.SUCCESS.getMessage(), null);
    }

    public static ResBean success(Object object) {
        return new ResBean(ResBeanEnum.SUCCESS.getCode(), ResBeanEnum.SUCCESS.getMessage(), object);
    }

    public static ResBean error(ResBeanEnum ResBeanEnum) {
        return new ResBean(ResBeanEnum.getCode(), ResBeanEnum.getMessage(), null);
    }

    public static ResBean error(ResBeanEnum ResBeanEnum, Object object) {
        return new ResBean(ResBeanEnum.getCode(), ResBeanEnum.getMessage(), object);
    }

    public ResBean(long code, String message, Object object) {
        this.code = code;
        this.message = message;
        this.object = object;
    }

    public ResBean() {

    }

    public long getCode() {
        return code;
    }

    public void setCode(long code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getObject() {
        return object;
    }

    public void setObject(Object object) {
        this.object = object;
    }
}
