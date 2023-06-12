package com.technical.exchange.bean;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.UUID;

@Entity
@Table(name = "t_like")
public class Like {
    @Id
    public String uuid;//主键
    public String doctor_username;//咨询师id
    public String username;//用户名

    public Like() {
        this.uuid = UUID.randomUUID().toString();
    }
}
