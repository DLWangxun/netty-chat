package com.xun.wang.vlog.chat.controller;


import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;

import com.xun.wang.vlog.chat.model.domain.ChatMsg;
import com.xun.wang.vlog.chat.server.service.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class PageController {

    @Autowired
    private ChatService chatService;

    private final Map<String, String> friendMap = new HashMap<String, String>() {{
        put("lucas", "jenifer");
        put("jenifer", "lucas");
    }};

    @RequestMapping("/chat")
    public String chat(HttpServletRequest req) {
        return "chat";
    }


    @GetMapping("/chat/record")
    @ResponseBody
    public Object getChatRecord(@RequestParam("senderId") String senderId, @RequestParam("recieverId") String recieverId) {
        Map<String, Object> result = new HashMap<String, Object>() {{
            List<ChatMsg> chatMsgs = chatService.findChatRecordBySendIdAndRecieverId(senderId, recieverId);
            put("chatRecord", chatMsgs);
            put("unSignMsgIds",chatMsgs.stream().filter(chatMsg ->
                    chatMsg.getSignType() == 0).map(chatMsg -> {
                return chatMsg.getMsgId();
            }).collect(Collectors.toList()));
        }};
        return result;
    }

    @PatchMapping("/chat/record")
    public Object signedChatMsg(@RequestBody List<Long> ids) {
        Map<String, Object> result = new HashMap<String, Object>() {{
            if (chatService.signedMsg(ids)) {
                put("result", "SUCCESS");
            } else {
                put("result", "FAIL");
            }

        }};
        return result;
    }


    @GetMapping("/friendId/{id}")
    @ResponseBody
    public Object index(@PathVariable("id") String id) {
        Map<String, String> result = new HashMap<String, String>() {{
            put("friendId", friendMap.get(id));
        }};
        return result;
    }

}
