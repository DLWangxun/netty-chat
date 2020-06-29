package com.xun.wang.vlog.chat.model.domain;

import lombok.Data;

/**
 * @ClassName ChatRecordSuggest
 * @Description 新增suggest
 * @Author xun.d.wang
 * @Date 2020/6/18 14:13
 * @Version 1.0
 **/
@Data
public class ChatRecordSuggest {

    private String  input;

    private int weight = 10; //默认权重
}
