package com.xun.wang.vlog.chat.server.handler.strategy;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.xun.wang.vlog.chat.model.domain.Chat;

import io.netty.channel.Channel;

public interface CommunicationStrategy {

    boolean handler(Channel channel, Chat chat) throws JsonProcessingException;
}
