package com.xun.wang.vlog.chat.server.handler.strategy;

import java.util.Date;

import com.xun.wang.vlog.chat.model.document.ChatDoc;
import com.xun.wang.vlog.chat.model.domain.Chat;
import com.xun.wang.vlog.chat.model.domain.ChatChannelRef;
import com.xun.wang.vlog.chat.model.domain.ChatMsg;
import com.xun.wang.vlog.chat.model.enums.MsgFlagEnum;
import com.xun.wang.vlog.chat.server.repository.ChatMsgRepository;
import com.xun.wang.vlog.chat.server.search.SearchService;
import com.xun.wang.vlog.chat.util.JsonUtils;
import org.n3r.idworker.Sid;
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

    @Autowired
    private SearchService searchService;

    public boolean handler(Channel channel, Chat chat) {
        // 判断执行结果
        boolean result = false;
        //添加index
        ChatDoc chatDoc = getChatDoc(chat.getChatMsg());
        searchService.create(chatDoc);
        // 发送消息
        Channel receiverChannel = ChatChannelRef.getInstance().get(chat.getChatMsg().getReceiverId());

        if (receiverChannel == null) {
            // TODO channel为空代表用户离线，推送消息（JPush，个推，小米推送）
        } else {
                receiverChannel.writeAndFlush(
                        new TextWebSocketFrame(
                                JsonUtils.objectToJson(chat)));
        }
        result = true;
        return result;
    }





    private ChatDoc getChatDoc(ChatMsg chatMsg){
        ChatDoc chatDoc = new ChatDoc();
        BeanCopier beanCopier = BeanCopier.create(ChatMsg.class,
                ChatDoc.class, false);
        beanCopier.copy(chatMsg, chatDoc, null);
        chatDoc.setId(Sid.next());
        chatDoc.setCdate(new Date());
        // 发送消息
        Channel receiverChannel = ChatChannelRef.getInstance().get(chatMsg.getReceiverId());
        if (receiverChannel == null) {
            chatDoc.setSignMark(MsgFlagEnum.UNSIGN.getCode());
        } else {
            chatDoc.setSignMark(MsgFlagEnum.SIGNED.getCode());
        }
        chatDoc.setEffective(MsgFlagEnum.EFFECTIVE.getCode());
        return chatDoc;
    }



}
