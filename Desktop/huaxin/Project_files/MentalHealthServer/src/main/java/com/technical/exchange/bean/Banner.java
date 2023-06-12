package com.technical.exchange.bean;


import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

//轮播图
@Entity
@Table(name = "t_banner")
public class Banner {
    @Id
    public int id;//id
    public String image_path;//图片路径
}
