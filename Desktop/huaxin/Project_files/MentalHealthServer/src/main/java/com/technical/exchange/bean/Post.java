package com.technical.exchange.bean;

import javax.persistence.*;
import java.util.List;

//故事 实体
@Entity
@Table(name = "t_post")
public class Post {
    @Id
    public String uuid;//主键
    public String title;//标题
    public String content;//内容
    public String username;//发布人
    public String images;//图片
    public long create_time;//发布时间
    public int view_count;//浏览数

    @Transient//忽略，数据库无此字段
    public String avatar;//头像
    
    @Transient//忽略，数据库无此字段
    public List<Reply> replyList;//回复列表

    public Post() {

    }

}

