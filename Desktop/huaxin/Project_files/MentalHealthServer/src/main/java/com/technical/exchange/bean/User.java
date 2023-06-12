package com.technical.exchange.bean;

import javax.persistence.*;

//用户表
@Entity
@Table(name = "t_user")
public class User {
    @Id
    public String username;//用户名
    public String password;//密码
    public String nickname;//昵称
    public int gender;//0=男，1=女
    public String avatar;//头像
    public String mail;//邮箱
    public String phone;//电话
    public String name;//姓名
    public String level;//专业等级
    public String edu;//教育背景
    public String good;//擅长领域
    public String style;//咨询风格
    public String history;//工作经验
    public String price;//价格
    public int user_type;//0=普通用户，1=咨询师，2=超级管理员

    @Transient
    public String mail_code;//短信验证码


    @Transient//序列化与反序列化时忽略
    public int publish_count;//发故事的数量

    @Transient//序列化与反序列化时忽略
    public int service_count;//咨询数量

    public User() {

    }

}

