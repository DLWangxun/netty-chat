package com.xun.wang.vlog.chat.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ResponseCodeEnum {

    SUCCESS(10000,"请求成功"),

    BUSINESS_FAILED(10001,"业务处理失败"),

    SERVLET_EXCEPTION(20000,"服务器异常");


    private int code ;

    private String message;



}
