package com.xun.wang.vlog.chat.model.enums;

import lombok.Getter;

/**
 * 
 * @Description: 消息签收状态 枚举
 */
@Getter
public enum MsgFlagEnum {
	
	UNSIGN(0, "未签收"),
	SIGNED(1, "已签收"),
	INVALID(0,"无效"),
	EFFECTIVE(1,"有效");
	
	public final Integer code;
	public final String content;
	
	MsgFlagEnum(Integer code, String content){
		this.code = code;
		this.content = content;
	}
}
