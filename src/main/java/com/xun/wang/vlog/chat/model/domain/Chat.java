package com.xun.wang.vlog.chat.model.domain;

import java.io.Serializable;



import lombok.Data;

/**
 * @ClassName MessageDTO
 * @Description
 * @Author xun.d.wang
 * @Date 2020/4/16 11:04
 * @Version 1.0
 **/
@Data
public class Chat implements Serializable {


    private static final long serialVersionUID = 84959983403471829L;

    private Integer action;
    private ChatMsg chatMsg;
    private String extand;
}
