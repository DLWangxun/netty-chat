package com.xun.wang.vlog.chat.server.service.impl;

import com.xun.wang.vlog.chat.model.domain.Chat;
import com.xun.wang.vlog.chat.model.domain.ChatChannelRef;
import com.xun.wang.vlog.chat.model.domain.ChatMsg;
import com.xun.wang.vlog.chat.model.entity.ChatMsgEntity;
import com.xun.wang.vlog.chat.model.enums.MsgSignFlagEnum;
import com.xun.wang.vlog.chat.server.repository.ChatMsgRepository;
import com.xun.wang.vlog.chat.server.handler.ChatHandler;
import com.xun.wang.vlog.chat.server.service.CommunicationHandler;
import com.xun.wang.vlog.chat.util.JsonUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.beans.BeanCopier;
import org.springframework.stereotype.Service;

import io.netty.channel.Channel;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;

/**
 * @ClassName ChattingHandler
 * @Description 交流消息处理
 * @Author xun.d.wang
 * @Date 2020/4/16 14:37
 * @Version 1.0
 **/
@Service
public class ChatCommunicationHandler implements CommunicationHandler {

    @Autowired
    private ChatMsgRepository repository;

    public boolean handler(Channel channel, Chat chat) {
        // 判断执行结果
        boolean result = false;
        // 聊天类型的消息，把聊天记录保存到数据库，同时标记消息的签收状态[未签收]
        ChatMsgEntity chatMsgEntity = setChatMsgEntity(chat.getChatMsg());
        ChatMsgEntity resultEntity = repository.save(chatMsgEntity);
        chat.getChatMsg().setMsgId(resultEntity.getId());
        // 发送消息
        // 从全局用户Channel关系中获取接受方的channel
        Channel receiverChannel = ChatChannelRef.getInstance().get(chat.getChatMsg().getReceiverId());

        if (receiverChannel == null) {
            // TODO channel为空代表用户离线，推送消息（JPush，个推，小米推送）
        } else {
            // 当receiverChannel不为空的时候，从ChannelGroup去查找对应的channel是否存在
           // Channel findChannel = ChatHandler.users.find(receiverChannel.id());
            //if (findChannel != null) {
                // 用户在线
                receiverChannel.writeAndFlush(
                        new TextWebSocketFrame(
                                JsonUtils.objectToJson(chat)));
            //} else {
                // 用户离线 TODO 推送消息
            //}
        }
        result = true;
        return result;
    }

    private ChatMsgEntity setChatMsgEntity(ChatMsg chatMsg) {
        ChatMsgEntity chatMsgEntity = new ChatMsgEntity();
        BeanCopier beanCopier = BeanCopier.create(ChatMsg.class,
                ChatMsgEntity.class, false);
        beanCopier.copy(chatMsg, chatMsgEntity, null);
        chatMsgEntity.setEffective(MsgSignFlagEnum.unsign.getType());
        return chatMsgEntity;
    }
}
