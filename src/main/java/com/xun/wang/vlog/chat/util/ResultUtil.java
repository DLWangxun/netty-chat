package com.xun.wang.vlog.chat.util;

import com.xun.wang.vlog.chat.model.enums.ResponseCodeEnum;
import com.xun.wang.vlog.chat.model.vo.ResponseResult;

/**
 * @ClassName ResultUtil
 * @Description 返回响应结果
 * @Author xun.d.wang
 * @Date 2020/6/18 13:51
 * @Version 1.0
 **/
public class ResultUtil {

    /**
     * 响应成功
     * @param data
     * @return
     */
    public static ResponseResult success(Object data){
       return getResponseResult(ResponseCodeEnum.SUCCESS,data);
    }

    /**
     * 响应成功
     * @return
     */
    public static ResponseResult success(){
        return getResponseResult(ResponseCodeEnum.SUCCESS,null);
    }



    /**
     * 设置ResponseResult 各项值
     * @param codeEnum
     * @param data
     * @return
     */
    public static ResponseResult getResponseResult(ResponseCodeEnum codeEnum, Object data){
        ResponseResult responseResult = new ResponseResult();
        responseResult.setCode(codeEnum.getCode());
        responseResult.setMessage(codeEnum.getMessage());
        responseResult.setData(data);
        return  responseResult;
    }
}
