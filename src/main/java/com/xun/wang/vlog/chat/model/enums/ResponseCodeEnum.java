package com.xun.wang.vlog.chat.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ResponseCodeEnum {

    SUCCESS(200,"请求成功"),

    SERVLET_EXCEPTION(500,"服务器异常");

    private int code ;

    private String message;



}
