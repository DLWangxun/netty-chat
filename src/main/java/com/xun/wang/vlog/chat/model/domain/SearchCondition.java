package com.xun.wang.vlog.chat.model.domain;

import lombok.Data;

/**
 * @ClassName SearchCondition
 * @Description TODO
 * @Author xun.d.wang
 * @Date 2020/5/22 14:45
 * @Version 1.0
 **/
@Data
public class SearchCondition {
    private String msg;
    private String senderId;
    private String receiverId;
    private Integer effective;
    private Data cdate;
}
