package com.technical.exchange.dao;

import com.technical.exchange.bean.Reply;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

//故事
@Repository
public interface ReplyDao extends JpaRepository<Reply, String> {

    //获取某故事的回复列表，根据故事uuid
    @Query(value = "select * from t_reply where post_uuid=?1", nativeQuery = true)
    List<Reply> getReplyByPostUUID(String postUUID);

    @Modifying
    @Transactional
    @Query(value = "delete from t_reply where post_uuid=?1", nativeQuery = true)
    void deleteByPostUUID(String post_uuid);
}
