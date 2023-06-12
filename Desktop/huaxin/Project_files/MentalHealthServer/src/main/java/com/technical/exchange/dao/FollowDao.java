package com.technical.exchange.dao;

import com.technical.exchange.bean.Follow;
import com.technical.exchange.bean.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


//关注、粉丝
@Repository
public interface FollowDao extends JpaRepository<Follow, String> {

    //检查是否关注了某用户
    @Query(value = "select * from t_follow where username=?1 and follow_username=?2", nativeQuery = true)
    Follow isFollow(String username, String follow_username);


    //解除关系，移除粉丝或者取消关注
    @Query(value = "delete from t_follow where username=?1 and follow_username=?2", nativeQuery = true)
    @Modifying
    @Transactional
    void delete(String username, String follow_username);


}
