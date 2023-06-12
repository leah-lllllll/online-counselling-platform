package com.technical.exchange.bean;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "t_reply")
public class Reply {
    @Id
    public String uuid;//主键
    public String post_uuid;//故事id
    public String username;//用户名
    public String content;//内容
    public long create_time;//发布时间
    public int view_count;//浏览数
}
