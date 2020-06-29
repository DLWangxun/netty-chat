package com.xun.wang.vlog.chat.model.domain;

import lombok.Data;

/**
 * @ClassName ChatSuggest
 * @Description ChatSuggest
 * @Author xun.d.wang
 * @Date 2020/6/28 15:30
 * @Version 1.0
 **/
@Data
public class ChatSuggest {
    private String input;
    private int weight = 10; //默认权重
}
