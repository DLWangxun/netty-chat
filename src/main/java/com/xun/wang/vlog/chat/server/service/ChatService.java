package com.xun.wang.vlog.chat.server.service;

import java.util.List;

import com.xun.wang.vlog.chat.model.domain.ChatMsg;

public interface ChatService {

    /**
     * 标记消息
     * @param ids
     * @return
     */
    boolean signedMsg(List<Long> ids);

    /**
     * 标记消息
     * @param ids
     * @return
     */
    boolean unSignMsg(List<Long> ids);


    /**
     * 查询聊天记录
     * @param senderId
     * @param recieverId
     * @return
     */
    List<ChatMsg> findChatRecordBySendIdAndRecieverId(String senderId,String recieverId);
}
