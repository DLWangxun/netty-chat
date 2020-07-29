package com.xun.wang.vlog.chat.controller;



import java.util.HashMap;
import java.util.Map;
import com.xun.wang.vlog.chat.model.domain.SearchCondition;
import com.xun.wang.vlog.chat.model.vo.ResponseResult;
import com.xun.wang.vlog.chat.server.search.SearchService;
import com.xun.wang.vlog.chat.server.service.ChatService;
import com.xun.wang.vlog.chat.util.ResultUtil;
import org.n3r.idworker.Sid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
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

    /**
     * 获取视图
     * @return
     */
    @GetMapping
    public String chat() {
        return "chat";
    }


    /**
     * 查询消息记录
     * @param searchCondition
     * @return
     */
    @GetMapping("/record")
    @ResponseBody
    public Object getChatRecord(@ModelAttribute SearchCondition searchCondition) {
        return  ResultUtil.success(chatService.findChatRecordBySendIdAndReceiverId(searchCondition));
    }

    /**
     * 根据userid查询好友
     * @param id
     * @return
     */
    @GetMapping("/friendId/{id}")
    @ResponseBody
    public Object index(@PathVariable("id") String id) {
        return ResultUtil.success(friendMap.get(id));
    }

    /**
     * 自动补全
     * @param prefix
     * @return
     */
    @GetMapping("/autocomplete")
    @ResponseBody
    public ResponseResult<String> autocomplete(@RequestParam(value = "prefix", required = true) String prefix){
        return  ResultUtil.success(searchService.suggest(prefix));
    }

    /**
     * 获取id
     * @return
     */
    @GetMapping("/id")
    @ResponseBody
    public Long getId(){
        return Sid.next();
    }


}
