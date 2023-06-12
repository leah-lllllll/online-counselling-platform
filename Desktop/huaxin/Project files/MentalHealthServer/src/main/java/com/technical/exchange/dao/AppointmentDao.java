package com.technical.exchange.dao;

import com.technical.exchange.bean.Appointment;
import com.technical.exchange.bean.Like;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

//预约管理
@Repository
public interface AppointmentDao extends JpaRepository<Appointment, String> {

    //获取咨询师的预约列表
    @Query(value = "select * from t_appointment where doctor_username=?1 order by dtime desc", nativeQuery = true)
    List<Appointment> getDoctors(String doctor_username);

    //获取用户的预约列表
    @Query(value = "select * from t_appointment where username=?1 order by dtime desc", nativeQuery = true)
    List<Appointment> getUsers(String username);
}
