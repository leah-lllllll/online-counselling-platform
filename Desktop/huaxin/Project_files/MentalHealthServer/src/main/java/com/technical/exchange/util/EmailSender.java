package com.technical.exchange.util;

import com.sun.mail.util.MailSSLSocketFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.security.GeneralSecurityException;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

//邮件发送
public class EmailSender {
    //主机地址
    static final String SMTP_HOST = "smtp.office365.com";
    //连接方式
    static final String PROTOCOL = "smtp";
    //用户名
    static final String USERNAME = "communicatebyhearts@outlook.com";
    //密码
    static final String PASSWORD = "Yuyan0526";
    //端口
    static final int PORT = 587;
    //开启ttl
    static boolean starttls = true;

    public static void sendEmail(String email, String title, String content) throws GeneralSecurityException, MessagingException {
        //创建一个配置文件并保存
        Properties properties = new Properties();
        properties.setProperty("mail.host", SMTP_HOST);
        properties.setProperty("mail.port", PORT + "");
        //使用的协议是smtp
        properties.setProperty("mail.transport.protocol", PROTOCOL);
        properties.setProperty("mail.smtp.auth", "true");
        properties.setProperty("mail.smtp.connectiontimeout", "60000");//超时时间
        properties.setProperty("mail.smtp.timeout", "60000");
        properties.setProperty("mail.smtp.writetimeout", "60000");//超时时间

        MailSSLSocketFactory sf = new MailSSLSocketFactory();
        sf.setTrustAllHosts(true);
        if (starttls) {
            properties.put("mail.smtp.starttls.enable", "true");
            properties.put("mail.smtp.starttls.socketFactory", sf);
        } else {
            properties.put("mail.smtp.ssl.socketFactory", sf);
            properties.put("mail.smtp.ssl.enable", "true");
        }
        //
        //根据配置创建会话对象, 用于和邮件服务器交互
        Session session = Session.getDefaultInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(USERNAME, PASSWORD);
            }
        });
        session.setDebug(true);
        //根据 Session 获取邮件传输对象
        Transport transport = session.getTransport(PROTOCOL);
        transport.connect(SMTP_HOST, PORT, USERNAME, PASSWORD);
        //创建邮件
        MimeMessage mimeMessage = new MimeMessage(session);
        mimeMessage.setFrom(new InternetAddress(USERNAME));
        mimeMessage.setRecipient(Message.RecipientType.TO, new InternetAddress(email));
        mimeMessage.setSubject(title);
        mimeMessage.setContent(content, "text/html;charset=UTF-8");
        //发送邮件, 发到所有的收件地址, message.getAllRecipients() 获取到的是在创建邮件对象时添加的所有收件人, 抄送人, 密送人
        transport.sendMessage(mimeMessage, mimeMessage.getAllRecipients());
        //关闭连接
        transport.close();
    }

    private static final String EMAIL_REGX = "[\\w!#$%&'*+/=?^_`{|}~-]+(?:\\.[\\w!#$%&'*+/=?^_`{|}~-]+)*@(?:[\\w](?:[\\w-]*[\\w])?\\.)+[\\w](?:[\\w-]*[\\w])?";

    //验证邮箱合法性
    public static boolean isEmail(String email) {
        if (email == null || email.isEmpty()) {
            return false;
        }
        Pattern p = Pattern.compile(EMAIL_REGX);
        Matcher m = p.matcher(email);
        return m.matches();
    }

    public static String ellipsisEmailAddress(String email) {
        if (isEmail(email)) {
            char[] chars = email.toCharArray();
            for (int i = 0; i < chars.length; i++) {
                if (chars[i] == '@') {
                    break;
                }
                if (i != 0) {
                    chars[i] = '*';
                }
            }
            return new String(chars);
        }
        return "email address format error";
    }
}
