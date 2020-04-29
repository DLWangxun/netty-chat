package com.xun.wang.vlog.chat.server.service;

import com.xun.wang.vlog.chat.model.domain.Chat;

import io.netty.channel.Channel;

public interface CommunicationHandler {

    boolean handler(Channel channel, Chat chat);
}
