package com.xun.wang.vlog.chat.server;

import com.xun.wang.vlog.chat.model.enums.SingleEnum;
import com.xun.wang.vlog.chat.server.handler.ChatHandler;
import com.xun.wang.vlog.chat.server.handler.HeartBeatHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.stream.ChunkedWriteHandler;
import io.netty.handler.timeout.IdleStateHandler;

/**
 * @ClassName ChatServer
 * @Description netty websocket服务端
 * @Author xun.d.wang
 * @Date 2020/4/16 8:51
 * @Version 1.0
 **/
@Component
public class ChatServer {

    private EventLoopGroup mainGroup;
    private EventLoopGroup subGroup;
    private ServerBootstrap server;
    private ChannelFuture future;

    @Autowired
    private HeartBeatHandler heartBeatHandler;

    @Autowired
    private ChatHandler chatHandler;

    public static ChatServer getInstance() {
        return SingleEnum.INSTANCE.getChatServer();
    }

    public void start() {
        this.future = server.bind(8011);
    }

    public ChatServer(){
        mainGroup = new NioEventLoopGroup();
        subGroup = new NioEventLoopGroup();
        server = new ServerBootstrap();
        server.group(mainGroup, subGroup);
        server.channel(NioServerSocketChannel.class);

        server.childHandler(new ChannelInitializer<SocketChannel>(){
            @Override
            protected void initChannel(SocketChannel socketChannel) throws Exception {
                // websocket 基于http协议，所以要有http编解码器
                socketChannel.pipeline().addLast(new HttpServerCodec());
                // 对写大数据流的支持
                socketChannel.pipeline().addLast(new ChunkedWriteHandler());
                // 几乎在netty中的编程，都会使用到此hanler
                socketChannel.pipeline().addLast(new HttpObjectAggregator(1024*64));
                // ====================== 以上是用于支持http协议    ======================
                // ====================== 增加心跳支持 start    ======================
                // 针对客户端，如果在1分钟时没有向服务端发送读写心跳(ALL)，则主动断开
                // 自定义的handler
                socketChannel.pipeline().addLast(chatHandler);
                // 如果是读空闲或者写空闲，不处理
                socketChannel.pipeline().addLast(new IdleStateHandler(20, 30, 40));
                // 自定义的空闲状态检测
                socketChannel.pipeline().addLast(heartBeatHandler);
                // ====================== 增加心跳支持 end    ======================
                // ====================== 以下是支持httpWebsocket ======================

                /**
                 * websocket 服务器处理的协议，用于指定给客户端连接访问的路由 : /ws
                 * 本handler会帮你处理一些繁重的复杂的事
                 * 会帮你处理握手动作： handshaking（close, ping, pong） ping + pong = 心跳
                 * 对于websocket来讲，都是以frames进行传输的，不同的数据类型对应的frames也不同
                 */
                socketChannel.pipeline().addLast(new WebSocketServerProtocolHandler("/ws"));


            }
        });

    }

}
