package com.xun.wang.vlog.chat.server.service.impl;

import com.xun.wang.vlog.chat.model.domain.Chat;
import com.xun.wang.vlog.chat.server.service.CommunicationHandler;
import org.springframework.stereotype.Service;
import io.netty.channel.Channel;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import lombok.extern.slf4j.Slf4j;

/**
 * @ClassName HeartBeatCommunicationHandler
 * @Description TODO
 * @Author xun.d.wang
 * @Date 2020/4/17 16:59
 * @Version 1.0
 **/
@Slf4j
@Service
public class AliveCommunicationHandler implements CommunicationHandler {

    @Override
    public boolean handler(Channel channel, Chat chat) {
        boolean result = false;
        log.info("收到来自channel为{}的心跳包...", channel);
        // 用户在线
        channel.writeAndFlush(
                new TextWebSocketFrame(
                       "PONG"));
        result = true;
        return result;
    }
}
