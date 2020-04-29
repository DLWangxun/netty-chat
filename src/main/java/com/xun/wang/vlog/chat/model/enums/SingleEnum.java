package com.xun.wang.vlog.chat.model.enums;

import com.xun.wang.vlog.chat.model.domain.ChatChannelRef;
import com.xun.wang.vlog.chat.server.ChatServer;

/**
 * @ClassName ChatChannelRef
 * @Description 单例ChatChannelRef
 * @Author xun.d.wang
 * @Date 2020/4/16 13:44
 * @Version 1.0
 **/
public enum SingleEnum {

    INSTANCE;

    private ChatChannelRef instance;

    private ChatServer chatServer;

    SingleEnum(){
        instance = new ChatChannelRef();
        chatServer = new ChatServer();
    }

    public  ChatChannelRef getChatChannelRef(){
        return instance;
    }


    public  ChatServer getChatServer(){
        return chatServer;
    }




}
