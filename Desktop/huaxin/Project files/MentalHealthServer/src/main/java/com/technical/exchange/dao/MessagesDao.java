package com.technical.exchange.dao;

import com.technical.exchange.bean.Messages;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

//消息
@Repository
public interface MessagesDao extends JpaRepository<Messages, String> {

    //获取与某用户的聊天内容
    //且此消息未被删
    @Query(value = "select * from t_messages where ((receive_user_id=?1 and send_user_id=?2) or (receive_user_id=?2 and send_user_id=?1)) and(ISNULL(del_username) or del_username<>?1) order by create_time asc", nativeQuery = true)
    List<Messages> getMessages(String user_name, String friend_name);

    //获取与某用户的聊天内容(未读)
    @Query(value = "select * from t_messages where (receive_user_id=?1 and send_user_id=?2) and status=0", nativeQuery = true)
    List<Messages> getUnReadMessages(String user_name, String friend_name);


    //消息标记已读
    @Query(value = "update  t_messages set status=1 where (receive_user_id=?1 and send_user_id=?2)", nativeQuery = true)
    @Modifying
    @Transactional
    void read(String user_name, String friend_name);

//    //消息删除(双方都删除了)
//    @Query(value = "delete from t_messages  where (send_user_id =?1 and receive_user_id=?2) and del_username!= ?1", nativeQuery = true)
//    @Modifying
//    @Transactional
//    void msgDeleteAll(String user_name, String friend_name);

    //消息删除
    @Query(value = "update  t_messages set del_username=?1 ,status=1 where (send_user_id =?1 and receive_user_id=?2) or((send_user_id =?2 and receive_user_id=?1))", nativeQuery = true)
    @Modifying
    @Transactional
    void msgDelete(String user_name, String friend_name);

}
