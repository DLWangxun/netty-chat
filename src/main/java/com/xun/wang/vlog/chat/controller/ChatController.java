package com.xun.wang.vlog.chat.controller;



import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;

import com.xun.wang.vlog.chat.model.domain.ChatMsg;
import com.xun.wang.vlog.chat.model.domain.SearchCondition;
import com.xun.wang.vlog.chat.model.vo.ResponseResult;
import com.xun.wang.vlog.chat.server.search.SearchService;
import com.xun.wang.vlog.chat.server.service.ChatService;
import com.xun.wang.vlog.chat.util.ResultUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
　* @description: 页面 controller
　* @author Lucas
　* @date 2020/5/19 16:05
  */
@Controller
@RequestMapping("/chat")
public class ChatController {

    @Autowired
    private ChatService chatService;

    @Autowired
    private SearchService searchService;

    private final Map<String, String> friendMap = new HashMap<String, String>() {{
        put("lucas", "jenifer");
        put("jenifer", "lucas");
    }};

    @GetMapping
    public String chat(HttpServletRequest req) {
        return "chat";
    }


    @GetMapping("/record")
    @ResponseBody
    public Object getChatRecord(@ModelAttribute SearchCondition searchCondition) {
        Map<String, Object> result = new HashMap<String, Object>() {{
            List<ChatMsg> chatMsgs = chatService.findChatRecordBySendIdAndReceiverId(searchCondition);
            put("chatRecord", chatMsgs);
        }};
        return result;
    }

    @PatchMapping("/record")
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

    @GetMapping("/autocomplete")
    @ResponseBody
    public ResponseResult<String> autocomplete(@RequestParam(value = "prefix", required = true) String prefix){
        return  ResultUtil.success(searchService.suggest(prefix));
    }






}
