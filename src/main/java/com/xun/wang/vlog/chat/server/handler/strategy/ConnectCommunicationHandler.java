package com.xun.wang.vlog.chat.server.handler.strategy;


import com.xun.wang.vlog.chat.model.domain.Chat;
import com.xun.wang.vlog.chat.model.domain.ChatChannelRef;
import org.springframework.stereotype.Component;

import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;

/**
 * @ClassName ConnectMessageHandler
 * @Description 连接消息处理
 * @Author xun.d.wang
 * @Date 2020/4/16 13:57
 * @Version 1.0
 **/
@Component
@Slf4j
public class ConnectCommunicationHandler implements CommunicationHandler {

    public boolean handler(Channel channel, Chat chat) {
        // 判断执行结果
        boolean result = false;
        // 当websocket 第一次open的时候，初始化channel，把用的channel和userid关联起来
        ChatChannelRef.getInstance().put(chat.getChatMsg().getSenderId(), channel);
        ChatChannelRef.getInstance().output();

        result = true;
        return result;
    }

}
