package com.technical.exchange.bean;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

//消息
@Entity
@Table(name = "t_messages")
public class Messages {
    @Id
    public String uuid;//主键

    public String receive_user_id;//接收人
    public String send_user_id;//发送人
    public int status;//0未读，1已读
    public String content;//消息内容
    public String del_username;//消息删除者
    public long create_time;//发送时间
}
