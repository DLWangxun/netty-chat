package com.xun.wang.vlog.chat.server.handler.strategy;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import com.xun.wang.vlog.chat.model.domain.Chat;
import com.xun.wang.vlog.chat.model.enums.MsgFlagEnum;
import com.xun.wang.vlog.chat.server.repository.ChatMsgRepository;
import org.apache.commons.beanutils.ConvertUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import io.netty.channel.Channel;

/**
 * @ClassName SignedCommunicationHandler
 * @Description 签收消息处理
 * @Author xun.d.wang
 * @Date 2020/4/17 9:52
 * @Version 1.0
 **/
@Service
public class SignedCommunicationHandler implements CommunicationHandler {

    @Autowired
    private ChatMsgRepository repository;

    @Override
    public boolean handler(Channel channel, Chat chat) {
        boolean result = false;
        String[] msgIdsStrArray = chat.getExtand().split(",");
        Long[] msgIdsLongArray = (Long[]) ConvertUtils.convert(msgIdsStrArray, Long.class);
        List<Long> msgIdsList = Stream.of(msgIdsLongArray).collect(Collectors.toList());
        if (!CollectionUtils.isEmpty(msgIdsList)) {
            repository.updateflagByIds(MsgFlagEnum.SIGNED.getCode(), msgIdsList);
        }
        result = true;
        return result;
    }
}
