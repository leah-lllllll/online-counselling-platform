package com.technical.exchange.bean;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

//预约
@Entity
@Table(name = "t_appointment")
public class Appointment {
    @Id
    public String uuid;
    public String username;//用户名
    public String doctor_username;//咨询师id
    public String name;//姓名
    public String mobile;//手机
    public String content;//症状
    public long dtime;//预约时间

    @Transient
    public User doctor;//咨询师
}
