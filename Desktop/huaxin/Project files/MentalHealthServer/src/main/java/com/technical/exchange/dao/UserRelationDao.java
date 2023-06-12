package com.technical.exchange.dao;

import com.technical.exchange.bean.UserRelation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

//用户消息关系
@Repository
public interface UserRelationDao extends JpaRepository<UserRelation, String> {
    //查询二人的记录关联
    @Query(value = "select * from t_user_relation where user_name=?1 and friend_name=?2", nativeQuery = true)
    UserRelation isFriend(String user_name, String friend_name);

    //删除记录
    @Query(value = "delete from t_user_relation  where user_name=?1 and friend_name=?2", nativeQuery = true)
    @Modifying
    @Transactional
    void deleteFriend(String user_name, String friend_name);
}
