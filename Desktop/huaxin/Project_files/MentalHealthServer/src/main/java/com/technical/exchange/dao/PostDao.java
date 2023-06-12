package com.technical.exchange.dao;

import com.technical.exchange.bean.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

//故事
@Repository
public interface PostDao extends JpaRepository<Post, String> {

    //获取所有故事，按浏览次数倒序
    @Query(value = "select * from t_post order by view_count desc", nativeQuery = true)
    List<Post> getAllPost();


    //获取故事
    @Query(value = "select * from t_post where uuid=?1", nativeQuery = true)
    Post getPost(String uuid);

    //搜索故事，按发布时间倒序
    @Query(value = "select * from t_post where title like %?1% or content like %?1% order by create_time desc", nativeQuery = true)
    List<Post> searchPost(String key);

    //获取我发布的故事，按发布时间倒序
    @Query(value = "select * from t_post where username=?1 order by create_time desc", nativeQuery = true)
    List<Post> getMyPost(String username);


}
