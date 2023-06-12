package com.technical.exchange.dao;

import com.technical.exchange.bean.Comments;
import com.technical.exchange.bean.Like;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

//评价操作
@Repository
public interface CommentsDao extends JpaRepository<Comments, String> {

    //获取咨询师的评价列表，按时间倒序
    @Query(value = "select c.* ,u.avatar from t_comments c left join t_user u on c.username = u.username where c.doctor_username=?1", nativeQuery = true)
    List<Comments> getComments(String doctor_username);

}
