package com.xun.wang.vlog.chat.server.handler.strategy;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.xun.wang.vlog.chat.model.domain.Chat;
import org.springframework.beans.factory.annotation.Autowired;
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
public class AliveCommunicationStrategyImpl implements CommunicationStrategy {


    @Override
    public boolean handler(Channel channel, Chat chat) throws JsonProcessingException {
        boolean result = false;
        log.info("收到来自channel为{}的心跳包...", channel);
        // 用户在线
        channel.writeAndFlush(new TextWebSocketFrame("PONG"));
        result = true;
        return result;
    }
}
