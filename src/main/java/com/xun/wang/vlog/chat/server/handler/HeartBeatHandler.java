package com.xun.wang.vlog.chat.server.handler;

import com.xun.wang.vlog.chat.model.domain.ChatChannelRef;
import org.springframework.stereotype.Component;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import lombok.extern.slf4j.Slf4j;

/**
 * @ClassName HeartBeatHandler
 * @Description 检测心跳
 * @Author xun.d.wang
 * @Date 2020/4/16 10:13
 * @Version 1.0
 **/
@Slf4j
@Component
@ChannelHandler.Sharable
public class HeartBeatHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        // 判断evt是否是IdleStateEvent（用于触发用户事件，包含 读空闲/写空闲/读写空闲 ）
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent event = (IdleStateEvent) evt;        // 强制类型转换
            if (event.state() == IdleState.ALL_IDLE) {
                log.info("channel关闭前，users的数量为:{}", ChatHandler.users.size());
                Channel channel = ctx.channel();
                // 关闭无用的channel，以防资源浪费
                channel.close();
                log.info("channel关闭后，users的数量为：" + ChatHandler.users.size());
            }
        }
    }
}
