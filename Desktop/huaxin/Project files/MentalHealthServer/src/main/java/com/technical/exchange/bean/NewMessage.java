package com.technical.exchange.bean;

import java.util.List;

//消息列表，包含已读和未读
//用户获取消息时新建
public class NewMessage implements Comparable<NewMessage> {
    public User user;//用户
    public List<Messages> messagesList;//消息列表
    public List<Messages> unReadMessagesList;//未读消息列表
    public long lastMessageTime = 0;//最新消息的时间

    public void init() {
        if (messagesList.size() > 0) {
            lastMessageTime = messagesList.get(messagesList.size() - 1).create_time;//最新消息时间
        }
    }


    //重写方法以排序
    @Override
    public int compareTo(NewMessage o) {
        return lastMessageTime - o.lastMessageTime > 0 ? -1 : (lastMessageTime - o.lastMessageTime < 0 ? 1 : 0);
    }
}
