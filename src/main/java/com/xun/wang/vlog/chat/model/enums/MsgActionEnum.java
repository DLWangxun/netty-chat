package com.xun.wang.vlog.chat.model.enums;

import lombok.Getter;

/**
 * 
 * @Description: 发送消息的动作 枚举
 */
@Getter
public enum MsgActionEnum implements CodeEnum {

	CONNECT(1, "第一次(或重连)初始化连接"),
	CHAT(2, "聊天消息"),
	SIGNED(3, "消息签收"),
	ALIVE(4, "客户端保持心跳"),
	PULL_FRIEND(5, "拉取好友");

	public final Integer code;
	public final String content;

	MsgActionEnum(Integer code, String content) {
		this.code = code;
		this.content = content;
	}
}
	

