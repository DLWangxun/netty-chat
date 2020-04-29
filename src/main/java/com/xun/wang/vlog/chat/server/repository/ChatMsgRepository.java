package com.xun.wang.vlog.chat.server.repository;

import java.lang.annotation.Native;
import java.util.Collection;
import java.util.List;

import com.xun.wang.vlog.chat.model.entity.ChatMsgEntity;
import org.hibernate.query.NativeQuery;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * @ClassName ChatMsgRespostity
 * @Description ChatMsg crud 方法
 * @Author xun.d.wang
 * @Date 2020/4/16 15:18
 * @Version 1.0
 **/
@Repository
public interface ChatMsgRepository extends BaseRepository<ChatMsgEntity> {

    @Transactional()
    @Modifying
    @Query(value = "update #{#entityName} set effective=:flag where  id in (:ids)")
    int updateflagByIds(@Param("flag") Integer flag, @Param("ids") Collection<Long> ids);



    @Query(nativeQuery = true,value = "SELECT\n" +
            "\tid,receiver_id ,sender_id  ,sign_flag,msg,cdate,edate,creator,editor\n" +
            "FROM\n" +
            "\t(\n" +
            "SELECT\n" +
            "\t*\n" +
            "FROM\n" +
            "\t`t_chat_msg` \n" +
            "WHERE\n" +
            "\tsender_id = :senderId\n" +
            "\tAND receiver_id = :recieverId UNION \n" +
            "SELECT\n" +
            "\t*\n" +
            "FROM\n" +
            "\t`t_chat_msg` \n" +
            "WHERE\n" +
            "\tsender_id = :recieverId \n" +
            "\tAND receiver_id = :senderId\n" +
            "\t) t \n" +
            "ORDER BY\n" +
            "\tt.cdate")
    List<ChatMsgEntity> findChatRecord(@Param("senderId") String senderId,@Param("recieverId") String recieverId);
}
