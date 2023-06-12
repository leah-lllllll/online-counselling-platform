package com.technical.exchange.dao;

import com.technical.exchange.bean.Like;
import com.technical.exchange.bean.Reply;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

//点赞操作
@Repository
public interface LikeDao extends JpaRepository<Like, String> {

    //根据用户名获取某咨询师的点赞数量
    @Query(value = "select count(doctor_username) from t_like where doctor_username=?1", nativeQuery = true)
    int getLikeCount(String doctor_username);

    //根据用户名和用户名，查询用户是否点赞了该咨询师
    @Query(value = "select * from t_like where doctor_username=?1 and username=?2", nativeQuery = true)
    Like isLike(String doctor_username, String username);

    //根据用户名和用户名，取消点赞
    @Transactional
    @Query(value = "delete  from t_like where doctor_username=?1 and username=?2", nativeQuery = true)
    @Modifying
    void unLike(String doctor_username, String username);
}
