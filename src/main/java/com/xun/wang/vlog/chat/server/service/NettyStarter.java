package com.xun.wang.vlog.chat.server.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import com.xun.wang.vlog.chat.server.ChatServer;

@Component
public class NettyStarter implements ApplicationListener<ContextRefreshedEvent>{

    @Autowired
    private ChatServer chatServer;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if(event.getApplicationContext().getParent() == null) {
            try {
                chatServer.start();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
