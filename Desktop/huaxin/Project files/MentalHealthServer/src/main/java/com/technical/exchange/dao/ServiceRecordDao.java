package com.technical.exchange.dao;

import com.technical.exchange.bean.Reply;
import com.technical.exchange.bean.ServiceRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

//服务记录
@Repository
public interface ServiceRecordDao extends JpaRepository<ServiceRecord, String> {

    //查询是否服务过某用户
    @Query(value = "select * from t_service_record where doctor_username=?1 and username=?2", nativeQuery = true)
    ServiceRecord getServiceRecord(String doctor_username, String username);

    //查询服务过几人
    @Query(value = "select count(username) from t_service_record where doctor_username=?1 ", nativeQuery = true)
    int getServiceRecordCount(String doctor_username);


}
