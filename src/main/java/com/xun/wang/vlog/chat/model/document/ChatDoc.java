package com.xun.wang.vlog.chat.model.document;

import java.util.Date;
import java.util.List;

import com.xun.wang.vlog.chat.model.domain.ChatSuggest;
import org.elasticsearch.search.suggest.Suggest;

import lombok.Data;

/**
 * @ClassName document
 * @Description chat 文档类
 * @Author xun.d.wang
 * @Date 2020/5/19 19:20
 * @Version 1.0
 **/
@Data
public class ChatDoc {

    private Long id;

    private Integer effective;

    private Date cdate;

    private String senderId;

    private String receiverId;

    private String msg;

    private Integer signMark;

    private List<ChatSuggest> suggests;

}
