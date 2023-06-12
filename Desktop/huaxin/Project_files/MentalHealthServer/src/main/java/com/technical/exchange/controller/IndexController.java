package com.technical.exchange.controller;

import com.technical.exchange.bean.*;
import com.technical.exchange.dao.*;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

//主页控制器
@RestController
@RequestMapping("/app/index")
public class IndexController {
    @Resource
    private BannerDao bannerDao;//轮播图数据库操作

    //评价操作
    @Resource
    private CommentsDao commentsDao;

    //获取轮播图
    @GetMapping("getBanners")
    public ApiResponse<List<Banner>> getBanners() {
        //JPARepository自带接口
        return new ApiResponse<List<Banner>>().data(bannerDao.findAll()).message("获取成功");
    }

    @Resource
    private UserDao userDao;//用户操作
    @Resource
    private ServiceRecordDao serviceRecordDao;//服务记录

    //获取咨询师列表
    @GetMapping("getDoctors")
    public ApiResponse<List<User>> getDoctors() {
        List<User> users = userDao.getDoctors();
        for (User u : users) {
            u.service_count = serviceRecordDao.getServiceRecordCount(u.username);//查询服务次数
        }
        //对咨询师按照服务次数多少排序
        users.sort((o1, o2) -> Integer.compare(o2.service_count, o1.service_count));
        return new ApiResponse<List<User>>().data(users).message("获取成功");
    }

    //获取咨询师详情
    @GetMapping("getDoctor")
    public ApiResponse<User> getDoctors(@RequestParam("username") String username) {
        List<User> users = userDao.checkUserName(username);
        if (users.size() == 0) {
            return new ApiResponse<User>().fail().message("没有找到此用户");
        }
        return new ApiResponse<User>().data(users.get(0)).message("获取成功");
    }



    //获取评价列表
    @GetMapping("getComments")
    public ApiResponse<List<Comments>> getComments(@RequestParam("doctor_username") String doctor_username) {
        List<Comments> commentsList = commentsDao.getComments(doctor_username);
        for (Comments comments : commentsList) {
            //查询头像
            comments.avatar = userDao.checkUserName(comments.username).get(0).avatar;
        }
        return new ApiResponse<List<Comments>>().data(commentsList);
    }

    @PostMapping("addComments")
    public ApiResponse<String> addComments(@RequestBody Comments comments) {
        comments.uuid = UUID.randomUUID().toString();//主键
        comments.create_time = System.currentTimeMillis();//当前时间
        commentsDao.save(comments);
        return new ApiResponse<String>().message("发表成功");
    }

    @Resource
    private AppointmentDao appointmentDao;//预约管理

    //预约
    @PostMapping("addAppointment")
    public ApiResponse<String> addAppointment(@RequestBody Appointment appointment) {
        appointment.uuid = UUID.randomUUID().toString();//主键
        appointmentDao.save(appointment);
        return new ApiResponse<String>().message("预约成功");
    }


    //用户获取预约
    @GetMapping("getUserAppointment")
    public ApiResponse<List<Appointment>> getUserAppointment(@RequestParam("username") String username) {
        List<Appointment> appointmentList = appointmentDao.getUsers(username);
        //获取该用户的所有预约记录
        for (Appointment appointment : appointmentList) {
            //根据医生id获得医生对象
            appointment.doctor = userDao.checkUserName(appointment.doctor_username).get(0);
        }
        return new ApiResponse<List<Appointment>>().data(appointmentList);
    }

    //咨询师获取预约
    @GetMapping("getAppointment")
    public ApiResponse<List<Appointment>> getAppointment(@RequestParam("doctor_username") String doctor_username) {
        List<Appointment> appointmentList = appointmentDao.getDoctors(doctor_username);
        for (Appointment appointment : appointmentList) {
            appointment.doctor = userDao.checkUserName(appointment.doctor_username).get(0);
        }
        return new ApiResponse<List<Appointment>>().data(appointmentList);
    }

    //删除预约
    @GetMapping("deleteAppointment")
    public ApiResponse<String> deleteAppointment(@RequestParam("uuid") String uuid) {
        appointmentDao.deleteById(uuid);
        return new ApiResponse<String>().message("删除成功");
    }
}
