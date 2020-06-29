package com.xun.wang.vlog.chat.model.enums;

import lombok.Getter;

/**
 * @ClassName ChatIndexKey
 * @Description TODO
 * @Author xun.d.wang
 * @Date 2020/5/20 8:53
 * @Version 1.0
 **/
@Getter
public enum ChatIndexKey {
    ID("id"),
    MSG("msg"),
    RECEIVERID("receiverId"),
    SENDERID("senderId"),
    EFFECTIVE("effective"),
    CDATE("cdate");

    ChatIndexKey(String code) {
        this.code = code;
    }

    private String code;

}
