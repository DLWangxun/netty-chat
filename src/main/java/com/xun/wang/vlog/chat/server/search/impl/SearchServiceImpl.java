package com.xun.wang.vlog.chat.server.search.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.xun.wang.vlog.chat.model.document.ChatDoc;
import com.xun.wang.vlog.chat.model.domain.ChatSuggest;
import com.xun.wang.vlog.chat.model.domain.SearchCondition;
import com.xun.wang.vlog.chat.model.domain.ServiceMultiResult;
import com.xun.wang.vlog.chat.model.enums.ChatIndexKey;
import com.xun.wang.vlog.chat.model.enums.MsgFlagEnum;
import com.xun.wang.vlog.chat.server.search.SearchService;
import org.apache.commons.lang3.ArrayUtils;
import org.elasticsearch.action.admin.indices.analyze.AnalyzeAction;
import org.elasticsearch.action.admin.indices.analyze.AnalyzeRequestBuilder;
import org.elasticsearch.action.admin.indices.analyze.AnalyzeResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.reindex.BulkByScrollResponse;
import org.elasticsearch.index.reindex.DeleteByQueryAction;
import org.elasticsearch.index.reindex.DeleteByQueryRequestBuilder;
import org.elasticsearch.index.reindex.UpdateByQueryAction;
import org.elasticsearch.index.reindex.UpdateByQueryRequestBuilder;
import org.elasticsearch.rest.RestStatus;
import org.elasticsearch.script.Script;
import org.elasticsearch.script.ScriptType;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.elasticsearch.search.sort.SortOrder;
import org.elasticsearch.search.suggest.Suggest;
import org.elasticsearch.search.suggest.SuggestBuilder;
import org.elasticsearch.search.suggest.SuggestBuilders;
import org.elasticsearch.search.suggest.completion.CompletionSuggestion;
import org.elasticsearch.search.suggest.completion.CompletionSuggestionBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

/**
 * @ClassName SearchServiceImpl
 * @Description TODO
 * @Author xun.d.wang
 * @Date 2020/5/19 19:19
 * @Version 1.0
 **/
@Slf4j
@Service
public class SearchServiceImpl implements SearchService {

    @Autowired
    private TransportClient esClient;

    @Autowired
    private ObjectMapper objectMapper;

    private static final String INDEX_NAME = "chat";

    private static final String INDEX_TYPE = "chat_record";

    private static final int SUGGEST_MAX_SIZE = 5;

    @Override
    public boolean create(ChatDoc chatDoc) {
        try {
            //构建suggest
            AnalyzeRequestBuilder requestBuilder = new AnalyzeRequestBuilder(
                    this.esClient, AnalyzeAction.INSTANCE, INDEX_NAME, chatDoc.getMsg());
            requestBuilder.setAnalyzer("ik_smart");
            AnalyzeResponse analyzeResponse = requestBuilder.get();
            List<AnalyzeResponse.AnalyzeToken> tokens = analyzeResponse.getTokens();
            if(tokens == null){
                log.warn("Can not analyze token for chat: " + chatDoc.getId());
                return  false;
            }
           List<ChatSuggest> chatSuggests = tokens.stream().filter( token ->!(
                   "<NUM>".equals(token.getType()) || token.getTerm().length() < 2)).map(token ->{
                ChatSuggest suggest = new ChatSuggest();
                suggest.setInput(token.getTerm());
                return  suggest;
            }).collect(Collectors.toList());
            chatDoc.setSuggests(chatSuggests);
            //存入文档
            IndexResponse indexResponse = esClient.prepareIndex(INDEX_NAME, INDEX_TYPE).setSource(objectMapper.writeValueAsBytes(chatDoc)).get();
            log.info("Create index with chat: " + chatDoc.getId());
            if (indexResponse.status() == RestStatus.CREATED) {
                return true;
            } else {
                return false;
            }
        } catch (JsonProcessingException e) {
            log.error("Error to index chat " + chatDoc.getId(), e);
            return false;
        }
    }

    @Override
    public long searchCount(long id) {
        SearchRequestBuilder requestBuilder = this.esClient.prepareSearch(INDEX_NAME).setTypes(INDEX_TYPE)
                .setQuery(QueryBuilders.termQuery(ChatIndexKey.ID.getCode(), id));
        log.info("Whether the record exists, result:" + requestBuilder.toString());
        SearchResponse searchResponse = requestBuilder.get();
        return searchResponse.getHits().getTotalHits();

    }

    @Override
    public boolean deleteAndCreate(long totalHit, ChatDoc chatDoc) {
        DeleteByQueryRequestBuilder builder = DeleteByQueryAction.INSTANCE
                .newRequestBuilder(esClient)
                .filter(QueryBuilders.termQuery(ChatIndexKey.ID.getCode(), chatDoc.getId()))
                .source(INDEX_NAME);

        log.debug("Delete by query for chat: " + builder);

        BulkByScrollResponse response = builder.get();
        long deleted = response.getDeleted();
        if (deleted != totalHit) {
            log.warn("Need delete {}, but {} was deleted!", totalHit, deleted);
            return false;
        } else {
            return create(chatDoc);
        }
    }

    @Override
    public ServiceMultiResult<ChatDoc> query(SearchCondition condition) {
        BoolQueryBuilder lastBoolQuery = QueryBuilders.boolQuery();
        BoolQueryBuilder senderBoolQuery = QueryBuilders.boolQuery();
        senderBoolQuery.must(QueryBuilders.termQuery(ChatIndexKey.SENDERID.getCode(), condition.getSenderId()));
        senderBoolQuery.must(QueryBuilders.termQuery(ChatIndexKey.RECEIVERID.getCode(), condition.getReceiverId()));
        BoolQueryBuilder recieveBoolQuery = QueryBuilders.boolQuery();
        recieveBoolQuery.must(QueryBuilders.termQuery(ChatIndexKey.SENDERID.getCode(), condition.getReceiverId()));
        recieveBoolQuery.must(QueryBuilders.termQuery(ChatIndexKey.RECEIVERID.getCode(), condition.getSenderId()));
        lastBoolQuery.should(senderBoolQuery).should(recieveBoolQuery);
        if (condition.getMsg() != null && !"*".equals(condition.getMsg())) {
            lastBoolQuery.must(QueryBuilders.matchQuery(ChatIndexKey.MSG.getCode(), condition.getMsg()));
        }
        lastBoolQuery.must(QueryBuilders.matchQuery(ChatIndexKey.EFFECTIVE.getCode(),"1"));
        HighlightBuilder highlightBuilder = new HighlightBuilder(); //生成高亮查询器
        highlightBuilder.field(ChatIndexKey.MSG.getCode());    //高亮查询字段
        highlightBuilder.preTags("<span style=\"color:green\">");   //高亮设置
        highlightBuilder.postTags("</span>");
        highlightBuilder.fragmentSize(800000); //最大高亮分片数

        SearchRequestBuilder requestBuilder = this.esClient.prepareSearch(INDEX_NAME)
                .setTypes(INDEX_TYPE)
                .setQuery(lastBoolQuery)
                .addSort(
                        ChatIndexKey.CDATE.getCode(),
                        SortOrder.fromString("asc")
                )
                .setTrackScores(true)             //避免分页之后相关性乱了
                .highlighter(highlightBuilder)     //配置高亮
                .setFrom(condition.getPageNumber())
                .setSize(10);



        SearchResponse response = requestBuilder.get();
        List<ChatDoc> chatDocs = new ArrayList<>();
        if (response.status() == RestStatus.OK) {
            response.getHits().forEach(item -> {
                try {
                    //获取结果值
                    ChatDoc chatDoc = objectMapper.readValue(item.getSourceAsString(), ChatDoc.class);
                    //获取高亮字段
                    Map<String, HighlightField> highlightFields = item.getHighlightFields();
                    HighlightField highlightField = highlightFields.get("msg");

                    if(highlightField != null){
                        Text[] fragments = highlightField.fragments();
                        String msg = ArrayUtils.toString(fragments);
                        chatDoc.setMsg(msg);
                    }
                    chatDocs.add(chatDoc);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });

        }
        return new ServiceMultiResult<>(response.getHits().getTotalHits(), chatDocs);
    }

    public List<String> suggest(String prefix) {
               CompletionSuggestionBuilder suggestion =
                SuggestBuilders.completionSuggestion("suggests").prefix(prefix).size(5);
        SuggestBuilder suggestBuilder = new SuggestBuilder();
        suggestBuilder.addSuggestion("autocomplete", suggestion);
        SearchRequestBuilder requestBuilder = this.esClient.prepareSearch(INDEX_NAME)
                .setTypes(INDEX_TYPE)
                .suggest(suggestBuilder);
        log.debug(requestBuilder.toString());
        SearchResponse response = requestBuilder.get();
        Suggest suggest = response.getSuggest();
        Suggest.Suggestion  result  = suggest.getSuggestion("autocomplete");
        Set<String> results = new HashSet<>();
        int maxSuggest = 0;
        for (Object term : result.getEntries()) {
            if (term instanceof CompletionSuggestion.Entry) {
                CompletionSuggestion.Entry item = (CompletionSuggestion.Entry) term;

                if (item.getOptions().isEmpty()) {
                     continue;
                }

                for (CompletionSuggestion.Entry.Option option : item.getOptions()) {
                    String tip = option.getText().string();
                    if (results.contains(tip)) {
                        continue;
                    }
                    results.add(tip);
                    maxSuggest++;
                }
            }

            if (maxSuggest > 5) {
                break;

            }
        }
        return results.stream().collect(Collectors.toList());
    }

    @Override
    public void sigenedByIds(List<String> ids) {
        UpdateByQueryRequestBuilder updateByQuery =  UpdateByQueryAction.INSTANCE.newRequestBuilder(this.esClient);
        Map<String,Object> params = new HashMap<String,Object>();
        params.put("signMark", MsgFlagEnum.SIGNED.getCode());
        ScriptType type = ScriptType.INLINE;
        String lang = "painless";
        String code = "ctx._source.signMark=params.signMark";
        Script script = new Script(type,lang,code,params);
        BulkByScrollResponse response = updateByQuery.source(INDEX_NAME).script(script)
                .filter(QueryBuilders.termsQuery("id", ids))
                .abortOnVersionConflict(false)
                .get();
        long rows = response.getUpdated();
        if(rows == ids.size()){
            log.error("消息签收失败,ids:{}",ids.toString());
        }
        log.info("消息签收成功,ids:{}",ids.toString());
    }

}
