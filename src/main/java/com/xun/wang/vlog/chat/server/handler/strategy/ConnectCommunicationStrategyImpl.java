package com.xun.wang.vlog.chat.server.handler.strategy;


import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.xun.wang.vlog.chat.model.domain.Chat;
import com.xun.wang.vlog.chat.model.domain.ChatChannelRef;
import com.xun.wang.vlog.chat.server.search.SearchService;
import com.xun.wang.vlog.chat.util.JsonUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import io.netty.channel.Channel;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
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
public class ConnectCommunicationStrategyImpl implements CommunicationStrategy {

    @Autowired
    private SearchService searchService;

    @Autowired
    private ObjectMapper objectMapper;

    public boolean handler(Channel channel, Chat chat) {
        // 判断执行结果
        boolean result = false;
        // 当websocket 第一次open的时候，初始化channel，把用的channel和userid关联起来
        ChatChannelRef.getInstance().put(chat.getChatMsg().getSenderId(), channel);
        ChatChannelRef.getInstance().output();
        //签收消息
        if(StringUtils.isNotBlank(chat.getExtand())&& ChatChannelRef.getInstance().get(chat.getChatMsg().getReceiverId())!= null){
            List<String> unreadMsgIds =  Arrays.asList(chat.getExtand().split(",")).stream().map(s ->s.trim()).collect(Collectors.toList());
            searchService.sigenedByIds(unreadMsgIds);
            Channel receiverChannel = ChatChannelRef.getInstance().get(chat.getChatMsg().getReceiverId());
            receiverChannel.writeAndFlush(new TextWebSocketFrame(JsonUtils.objectToJson(unreadMsgIds)));
        }
        result = true;
        return result;
    }

}
