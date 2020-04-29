package com.xun.wang.vlog.chat.model.domain;

import java.io.Serializable;

import lombok.Data;



@Data
public class ChatMsg implements Serializable {

	private static final long serialVersionUID = -7695651820196843562L;

	private String senderId;

	private String receiverId;

	private String msg;

	private Long msgId;

	private Integer signType;


}
