package com.technical.exchange.bean;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "t_user_relation")
public class UserRelation {
    @Id
    public String uuid;
    public String user_name;
    public String friend_name;
}
