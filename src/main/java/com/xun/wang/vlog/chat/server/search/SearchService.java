package com.xun.wang.vlog.chat.server.search;

import java.util.List;

import com.xun.wang.vlog.chat.model.document.ChatDoc;
import com.xun.wang.vlog.chat.model.domain.SearchCondition;
import com.xun.wang.vlog.chat.model.domain.ServiceMultiResult;

public interface SearchService {

    /**
     * 创建文档类
     * @param chatDoc
     * @return
     */
    boolean create (ChatDoc chatDoc);

    /**
     * 根据id判断index是否已建立
     * @param id
     * @return
     */
    long searchCount(long id);

    /**
     * 删除并创建index
     * @param totalHit
     * @param chatDoc
     * @return
     */
    boolean deleteAndCreate(long totalHit,ChatDoc chatDoc);

    /**
     * 查询聊天记录
     * @param condition
     * @return
     */
    ServiceMultiResult<ChatDoc> query(SearchCondition condition);

    /**
     * 自动补全日志
     * @param prefix
     * @return
     */
    List<String> suggest(String prefix);





}
