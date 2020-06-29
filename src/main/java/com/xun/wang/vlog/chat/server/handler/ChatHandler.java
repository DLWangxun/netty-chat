package com.xun.wang.vlog.chat.server.handler;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Map;

import com.xun.wang.vlog.chat.model.domain.Chat;
import com.xun.wang.vlog.chat.model.enums.MsgActionEnum;
import com.xun.wang.vlog.chat.server.handler.strategy.CommunicationHandler;
import com.xun.wang.vlog.chat.util.EnumUtils;
import com.xun.wang.vlog.chat.util.JsonUtils;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.util.concurrent.GlobalEventExecutor;
import lombok.extern.slf4j.Slf4j;

/**
 * @ClassName ChatHandler
 * @Description 处理主要逻辑
 * @Author xun.d.wang
 * @Date 2020/4/16 10:18
 * @Version 1.0
 **/
@Slf4j
@Component
@ChannelHandler.Sharable
public class ChatHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {

    @Autowired
    private Map<String, CommunicationHandler> communicationHandlerMap;

    // 用于记录和管理所有客户端的channle
    public static ChannelGroup users = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    private final String HANDLER_NAME_SUFFIX = "CommunicationHandler";


    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame textWebSocketFrame) throws Exception {
        String content = textWebSocketFrame.text();
        Channel currentChannel = ctx.channel();
        // 1. 获取客户端发来的消息
        log.info("context:{}",content);
        Chat chat = JsonUtils.jsonToPojo(content, Chat.class);
        String handlerName = getHandlerName(chat.getAction());
        LocalDateTime startTime = LocalDateTime.now();
        log.info("通讯开始时间:{}", parseTimeToString(startTime,"yyyy-MM-dd HH:mm:ss"));
        boolean result = communicationHandlerMap.get(handlerName).handler(currentChannel, chat);
        LocalDateTime endTime = LocalDateTime.now();
        log.info("通讯开始{},结束时间:{},时间间隔:{}",result?"成功":"失败",endTime, Duration.between(startTime, endTime).get(ChronoUnit.SECONDS));

    }

    private String  getHandlerName(Integer typeCode){
        String prefix = EnumUtils.getByCode(typeCode, MsgActionEnum.class).name();
        String handlerName = StringUtils.lowerCase(prefix).concat(HANDLER_NAME_SUFFIX);
        return handlerName;
    }

    private String parseTimeToString(LocalDateTime localDateTime,String format){
        DateTimeFormatter dateToStrFormatter = DateTimeFormatter.ofPattern(format);
        return localDateTime.format(dateToStrFormatter);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        users.add(ctx.channel());
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        users.remove(ctx.channel());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.channel().close();
        users.remove(ctx.channel());
    }
}
