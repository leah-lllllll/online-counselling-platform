package com.technical.exchange.bean;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.UUID;

//关注、粉丝表
@Entity
@Table(name = "t_follow")
public class Follow {
    @Id
    public String uuid;//用户名
    public String username;//用户名
    public String follow_username;//关注的人用户名

    public Follow() {
        uuid = UUID.randomUUID().toString();//主键
    }

}

