package com.xun.wang.vlog.chat.model.domain;

import java.util.HashMap;

import com.xun.wang.vlog.chat.model.enums.SingleEnum;

import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;

/**
 * @ClassName ChatChannelRef
 * @Description TODO
 * @Author xun.d.wang
 * @Date 2020/4/16 13:51
 * @Version 1.0
 **/
@Slf4j
public class ChatChannelRef {

    private HashMap<String, Channel> manager = new HashMap<String, Channel>();

    public void put(String senderId, Channel channel) {
        manager.put(senderId, channel);
    }

    public Channel get(String senderId) {
        return manager.get(senderId);
    }

    public static ChatChannelRef getInstance() {
        return SingleEnum.INSTANCE.getChatChannelRef();
    }


    public void output() {
        manager.forEach((key, value) -> {
            log.info("管理管道详情,UserId:{},ChannelId:{}", key, value.id().asLongText());
        });

    }
}
