package com.technical.exchange.controller;

import com.technical.exchange.bean.*;
import com.technical.exchange.dao.MessagesDao;
import com.technical.exchange.dao.ServiceRecordDao;
import com.technical.exchange.dao.UserDao;
import com.technical.exchange.dao.UserRelationDao;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.*;

//消息操作
//@Controller
//@ResponseBody
@RestController
@RequestMapping("/app/message")
public class MessageController {
    @Resource
    private UserDao userDao;//用户数据库操作
    @Resource
    private UserRelationDao userRelationDao;//用户关系数据库操作
    @Resource
    private MessagesDao messagesDao;//消息数据库操作

    //用户获取消息列表
    @GetMapping("getMyMessageList")
    public ApiResponse<List<NewMessage>> getMyMessageList(@RequestParam("user_name") String user_name) {
        List<NewMessage> newMessages = new ArrayList<>();
        List<User> users = userDao.getMyFriends(user_name);//获取我的x消息列表
        for (User u : users) {
            NewMessage newMessage = new NewMessage();
            newMessage.user = u;
            newMessage.unReadMessagesList = messagesDao.getUnReadMessages(user_name, u.username);//获取与该人未读聊天记录
            newMessage.messagesList = messagesDao.getMessages(user_name, u.username);//消息记录
            //初始化，加入时间，为了排序
            newMessage.init();
            newMessages.add(newMessage);//添加
        }
        //按最新消息倒序排列
        Collections.sort(newMessages);
        return new ApiResponse<List<NewMessage>>().data(newMessages);
    }


    //用户删除消息
    @GetMapping("deleteMessage")
    public ApiResponse<String> deleteMessage(@RequestParam("user_name") String user_name, @RequestParam("friend_name") String friend_name) {
        //删除消息列表关联
        //先是删除二人之间的单向朋友关系
        userRelationDao.deleteFriend(user_name, friend_name);
        //
        //messagesDao.msgDeleteAll(user_name, friend_name);
        //表中加入删除人信息
        messagesDao.msgDelete(user_name, friend_name);
        return new ApiResponse<String>().message("删除成功");
    }

    @Resource
    private ServiceRecordDao serviceRecordDao;//服务记录

    //发送消息
    @PostMapping("sendMessages")
    public ApiResponse<String> sendMessages(@RequestBody Messages messages) {
        messages.uuid = UUID.randomUUID().toString();//主键
        messages.create_time = System.currentTimeMillis();//时间
        messagesDao.save(messages);
        String user_name = messages.send_user_id;
        try {
            //增加服务记录
            if (serviceRecordDao.getServiceRecord(messages.send_user_id, messages.receive_user_id) == null) {
                ServiceRecord serviceRecord = new ServiceRecord();
                serviceRecord.uuid = UUID.randomUUID().toString();//主键
                serviceRecord.doctor_username = messages.receive_user_id;
                serviceRecord.username = messages.send_user_id;
                serviceRecordDao.save(serviceRecord);
            }
        } catch (Exception e) {
        }
        String friend_name = messages.receive_user_id;
        try {
            //建立关系
            UserRelation userRelation = new UserRelation();
            userRelation.uuid = UUID.randomUUID().toString();
            userRelation.user_name = user_name;
            userRelation.friend_name = friend_name;
            userRelationDao.save(userRelation);
        } catch (Exception e) {
        }
        try {
            //再添加反向消息关系
            UserRelation userRelation1 = new UserRelation();
            userRelation1.uuid = UUID.randomUUID().toString();
            userRelation1.friend_name = user_name;
            userRelation1.user_name = friend_name;
            userRelationDao.save(userRelation1);
        } catch (Exception e) {
        }
        return new ApiResponse<String>().message("发送成功");

    }

    //获取聊天记录
    @GetMapping("getMessages")
    public ApiResponse<List<Messages>> getMessages(@RequestParam("user_name") String
                                                           user_name, @RequestParam("friend_name") String friend_name) {
        List<Messages> messages = messagesDao.getMessages(user_name, friend_name);//获取与该人聊天记录
        return new ApiResponse<List<Messages>>().data(messages).message("获取成功");
    }

    //获取聊天记录（未读）
    @GetMapping("getUnReadMessages")
    public ApiResponse<List<Messages>> getUnReadMessages(@RequestParam("user_name") String
                                                                 user_name, @RequestParam("friend_name") String friend_name) {
        List<Messages> messages = messagesDao.getUnReadMessages(user_name, friend_name);//获取与该人未读聊天记录
        return new ApiResponse<List<Messages>>().data(messages).message("获取成功");
    }

    //将消息标记已读
    @GetMapping("read")
    public ApiResponse<String> read(@RequestParam("user_name") String
                                            user_name, @RequestParam("friend_name") String friend_name) {
        messagesDao.read(user_name, friend_name);//标记已读
        return new ApiResponse<String>().message("标记成功");
    }
}
