package com.technical.exchange.bean;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

//评价
@Entity
@Table(name = "t_comments")
public class Comments {
    @Id
    public String uuid;//主键
    public String content;//内容
    public String username;//用户
    public String doctor_username;//咨询师用户名
    public long create_time;//发布时间

    //@Transient用于表示一个成员变量不是该对象序列化的一部分。当一个对象被序列化的时候，transient型变量的值不包括在序列化的结果中
    @Transient
    public String avatar;//头像


}
