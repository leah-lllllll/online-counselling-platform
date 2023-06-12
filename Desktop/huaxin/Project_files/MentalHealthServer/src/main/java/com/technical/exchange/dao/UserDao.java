package com.technical.exchange.dao;

import com.technical.exchange.bean.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface UserDao extends JpaRepository<User, String> {
    //返回满足此用户名用户，用于检查是否存在用户
    @Query(value = "select * from t_user where username=?1", nativeQuery = true)
    List<User> checkUserName(String name);

    //检查邮箱是否可用
    @Query(value = "select * from t_user where mail=?1", nativeQuery = true)
    List<User> checkEmail(String email);

    //登录验证
    @Query(value = "select * from t_user where username=?1 and password=?2", nativeQuery = true)
    User login(String name, String pwd);

    //登录验证
    @Query(value = "select * from t_user where mail=?1", nativeQuery = true)
    User login_sms(String email);

    //获取咨询师列表
    @Query(value = "select * from t_user where user_type=1", nativeQuery = true)
    List<User> getDoctors();

    //获取我关注的人
    @Query(value = "select * from t_user where username in (select follow_username from t_follow where username=?1)", nativeQuery = true)
    List<User> getMyFollowUser(String username);

    //获取关注我的人(粉丝)
    @Query(value = "select * from t_user where username in (select username from t_follow where follow_username=?1)", nativeQuery = true)
    List<User> getFollowMeUser(String username);

    //获取所有用户
    @Query(value = "select * from t_user where user_type !=2", nativeQuery = true)
    List<User> getAllUser();

    //获取我的消息列表
    @Query(value = "select * from t_user where username in (select friend_name from t_user_relation where user_name=?1)", nativeQuery = true)
    List<User> getMyFriends(String user_name);
}
