package com.xun.wang.vlog.chat.model.entity;

import java.io.Serializable;

import javax.persistence.AttributeOverride;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "t_chat_msg")
@AttributeOverride(name="effective", column=@Column(name="signFlag"))
public class ChatMsgEntity extends BaseEntity implements Serializable {

	private static final long serialVersionUID = -7695651820196843562L;

	@Column(nullable = false, columnDefinition = "varchar(50) COMMENT '发送人Id'")
	private String senderId;

	@Column(nullable = false, columnDefinition = "varchar(50) COMMENT '接收人Id'")
	private String receiverId;

	@Basic(fetch= FetchType.LAZY)
	@Column(columnDefinition="TEXT COMMENT '发送内容'")
	private String msg;
	

}
