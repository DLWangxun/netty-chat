package com.xun.wang.vlog.chat.server.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import com.xun.wang.vlog.chat.model.domain.ChatMsg;
import com.xun.wang.vlog.chat.model.entity.ChatMsgEntity;
import com.xun.wang.vlog.chat.model.enums.MsgSignFlagEnum;
import com.xun.wang.vlog.chat.server.repository.ChatMsgRepository;
import com.xun.wang.vlog.chat.server.service.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.beans.BeanCopier;
import org.springframework.stereotype.Service;

/**
 * @ClassName ChatServiceImpl
 * @Description TODO
 * @Author xun.d.wang
 * @Date 2020/4/27 13:43
 * @Version 1.0
 **/
@Service
public class ChatServiceImpl implements ChatService {

    @Autowired
    private ChatMsgRepository chatMsgRepository;

    @Override
    public boolean signedMsg(List<Long> ids) {
        return chatMsgRepository.updateflagByIds(MsgSignFlagEnum.signed.getType(), ids) == ids.size() ? true : false;
    }

    @Override
    public boolean unSignMsg(List<Long> ids) {
        return false;
    }

    @Override
    public List<ChatMsg> findChatRecordBySendIdAndRecieverId(String senderId, String recieverId) {
        return convertChatMsgs(chatMsgRepository.findChatRecord(senderId, recieverId));
    }

    /**
     * entity convert dto
     *
     * @param chatMsgEntities
     * @return
     */
    public List<ChatMsg> convertChatMsgs(List<ChatMsgEntity> chatMsgEntities) {
        return chatMsgEntities.stream().map(chatMsgEntity -> {
            ChatMsg chatMsg = new ChatMsg();
            BeanCopier beanCopier = BeanCopier.create(ChatMsgEntity.class, ChatMsg.class,
                    false);
            beanCopier.copy(chatMsgEntity, chatMsg, null);
            chatMsg.setMsgId(chatMsgEntity.getId());
            chatMsg.setSignType(chatMsgEntity.getEffective());
            return chatMsg;
        }).collect(Collectors.toList());

    }
}
