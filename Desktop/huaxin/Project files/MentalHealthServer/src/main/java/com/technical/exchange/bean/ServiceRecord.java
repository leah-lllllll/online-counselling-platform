package com.technical.exchange.bean;


import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "t_service_record")
public class ServiceRecord {
    @Id
    public String uuid;
    public String doctor_username;//咨询师用户名
    public String username;//用户名
}
