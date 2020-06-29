package com.xun.wang.vlog.chat.server.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import com.xun.wang.vlog.chat.model.document.ChatDoc;
import com.xun.wang.vlog.chat.model.domain.ChatMsg;
import com.xun.wang.vlog.chat.model.domain.SearchCondition;
import com.xun.wang.vlog.chat.model.domain.ServiceMultiResult;
import com.xun.wang.vlog.chat.model.enums.MsgFlagEnum;
import com.xun.wang.vlog.chat.server.repository.ChatMsgRepository;
import com.xun.wang.vlog.chat.server.search.SearchService;
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

    @Autowired
    private SearchService searchService;

    @Override
    public boolean signedMsg(List<Long> ids) {
        return chatMsgRepository.updateflagByIds(MsgFlagEnum.SIGNED.getCode(), ids) == ids.size() ? true : false;
    }

    @Override
    public boolean unSignMsg(List<Long> ids) {
        return false;
    }

    @Override
    public List<ChatMsg> findChatRecordBySendIdAndReceiverId(SearchCondition searchCondition) {
        ServiceMultiResult<ChatDoc> serviceMultiResult = searchService.query(searchCondition);
        return convertChatMsgs(serviceMultiResult.getResult(),ChatDoc.class);
    }


    /**
     * entity convert dto
     *
     * @param targets
     * @return
     */
    public List<ChatMsg> convertChatMsgs(List<?> targets,Class targetClass) {
        return targets.stream().map(target -> {
            ChatMsg chatMsg = new ChatMsg();
            BeanCopier beanCopier = BeanCopier.create(targetClass, ChatMsg.class,
                    false);
            beanCopier.copy(target, chatMsg, null);
            return chatMsg;
        }).collect(Collectors.toList());

    }
}
