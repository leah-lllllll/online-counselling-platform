package com.technical.exchange.controller;

import com.technical.exchange.bean.*;
import com.technical.exchange.dao.FollowDao;
import com.technical.exchange.dao.LikeDao;
import com.technical.exchange.dao.PostDao;
import com.technical.exchange.dao.UserDao;
import com.technical.exchange.util.EmailSender;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.mail.MessagingException;
import java.security.GeneralSecurityException;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@RequestMapping("/user/")
@RestController
public class UserController {

    @Resource
    UserDao userDao;//用户操作
    @Resource
    FollowDao followDao;//关注、粉丝操作
    @Resource
    PostDao postDao;//咨询师操作

    //查询是否关注了某用户
    @GetMapping("isFollow")
    //@RequestParam接收的参数是来自HTTP请求体或请求url的QueryString中
    public ApiResponse<Boolean> isFollow(@RequestParam("username") String username, @RequestParam("follow_username") String follow_username) {
        Follow follow = followDao.isFollow(username, follow_username);
        return new ApiResponse<Boolean>().data(follow != null);
    }

    //关注某用户
    @GetMapping("follow")
    public ApiResponse<String> follow(@RequestParam("username") String username, @RequestParam("follow_username") String follow_username) {
        Follow follow = new Follow();
        follow.username = username;//我的用户名
        follow.follow_username = follow_username;//被关注人的用户名
        //save为JpaRepository自带方法，用于存到数据库中
        followDao.save(follow);//保存
        return new ApiResponse<String>().message("关注成功");
    }

    //取消关注谋用户
    @GetMapping("removeFollow")
    public ApiResponse<String> removeFollow(@RequestParam("username") String username, @RequestParam("follow_username") String follow_username) {
        followDao.delete(username, follow_username);//取消关注
        return new ApiResponse<String>().message("取消关注成功");
    }

    //移除粉丝
    @GetMapping("removeFollowMe")
    public ApiResponse<String> removeFollowMe(@RequestParam("username") String username, @RequestParam("follow_username") String follow_username) {
        followDao.delete(follow_username, username);//移除粉丝
        return new ApiResponse<String>().message("移除粉丝成功");
    }


    //获取粉丝列表
    @GetMapping("getFollowMeUser")
    //使用ApiResponse原因:data和message方便传递
    public ApiResponse<List<User>> getFollowMeUser(@RequestParam("username") String username) {

        return new ApiResponse<List<User>>().data(
                userDao.getFollowMeUser(username)//获取粉丝列表
        ).message("获取成功");
    }


    //获取我关注的人的列表
    @GetMapping("getMyFollowUser")
    public ApiResponse<List<User>> getMyFollowUser(@RequestParam("username") String username) {
        return new ApiResponse<List<User>>().data(
                userDao.getMyFollowUser(username)//获取粉丝列表
        ).message("获取成功");
    }

    @Resource
    private LikeDao likeDao;//点赞操作

    //查询点赞状态
    @GetMapping("isLike")
    public ApiResponse<Boolean> isLike(@RequestParam("doctor_username") String doctor_username, @RequestParam("username") String username) {
        Like like = likeDao.isLike(doctor_username, username);
        return new ApiResponse<Boolean>().data(like != null).message("");
    }


    //点赞
    @GetMapping("like")
    public ApiResponse<String> like(@RequestParam("doctor_username") String doctor_username, @RequestParam("username") String username) {
        Like like = new Like();
        like.username = username;//用户名
        like.doctor_username = doctor_username;//咨询师id
        //保存like实体
        likeDao.save(like);
        return new ApiResponse<String>().message("点赞成功");
    }

    //取消点赞
    @GetMapping("unLike")
    public ApiResponse<String> unLike(@RequestParam("doctor_username") String doctor_username, @RequestParam("username") String username) {
        likeDao.unLike(doctor_username, username);
        return new ApiResponse<String>().message("取消成功");
    }

    //获取点赞数量
    @GetMapping("likeCount")
    public ApiResponse<Integer> likeCount(@RequestParam("doctor_username") String doctor_username) {
        //点赞数量
        return new ApiResponse<Integer>().data(likeDao.getLikeCount(doctor_username)).message("取消成功");
    }

    //用户登录
    @PostMapping("login")
    public ApiResponse<User> login(@RequestBody User user) {
        User user1 = userDao.login(user.username, user.password);
        if (user1 != null) {
            return new ApiResponse<User>().message("登录成功").data(user1);//返回用户信息
        } else {
            return new ApiResponse<User>().message("用户名或密码错误").fail();
        }
    }

    //用户邮箱验证码登录
    @PostMapping("login_email")
    public ApiResponse<User> login_email(@RequestBody User user) {
        Email email = smsHashMap.get(user.mail);
        //还没获取验证码，直接返回错误信息
        if (email == null || !email.normal()) {
            return new ApiResponse<User>().message("验证码错误").fail();
        }
        //验证码错误
        if (!email.code.equals(user.mail_code)) {
            return new ApiResponse<User>().message("验证码错误").fail();
        }
        //通过邮箱获取用户
        User user1 = userDao.login_sms(user.mail);
        if (email.normal()) {
            return new ApiResponse<User>().message("登录成功").data(user1);//返回用户信息
        } else {
            return new ApiResponse<User>().message("验证码过期").fail();
        }
    }

    // 用户注册
    @RequestMapping("register")
    public ApiResponse<String> register(@RequestBody User user) {
        //检查用户名是否被占用，
        if (!userDao.checkUserName(user.username).isEmpty()) {
            return new ApiResponse<String>().message("用户名已存在，请更换").fail();
        }
        //检查邮箱是否被占用，
        if (!userDao.checkEmail(user.mail).isEmpty()) {
            return new ApiResponse<String>().message("邮箱已被占用，请更换").fail();
        }
        Email email = smsHashMap.get(user.username);
        if (email == null || !email.normal()) {
            return new ApiResponse<String>().message("验证码错误").fail();
        } else {
            if (!email.code.equals(user.mail_code)) {
                return new ApiResponse<String>().message("验证码错误").fail();
            }
            //未被占用，进行注册
            userDao.save(user);
            return new ApiResponse<String>().message("注册成功");
        }
    }

    // 查询所有用户
    @GetMapping("list")
    public ApiResponse<List<User>> list() {
        List<User> list = userDao.getAllUser();
        return new ApiResponse<List<User>>().data(list).message("查询成功");
    }

    //删除用户
    //未实现
    @GetMapping("delete")
    public ApiResponse<String> list(@RequestParam("username") String username) {
        userDao.deleteById(username);
        return new ApiResponse<String>().message("删除成功");
    }

    //更新资料
    @PostMapping("update")
    public ApiResponse<User> update(@RequestBody User user) {
        User user1 = userDao.findById(user.username).get();//查找用户
        if (user.password.equals(user1.password)) {
            //验证用户
            userDao.save(user);//更新
            return new ApiResponse<User>().data(user).message("修改成功");
        } else {
            return new ApiResponse<User>().fail().message("密码错误，无法修改");
        }
    }

    //修改密码
    @GetMapping("changePassword")
    public ApiResponse<User> changePassword(@RequestParam("username") String username, @RequestParam("oldpwd") String oldpwd, @RequestParam("newpwd") String newpwd) {
        User user1 = userDao.findById(username).get();//查找用户
        //如果原密码正确
        if (user1.password.equals(oldpwd)) {
            //验证用户
            user1.password = newpwd;//更新密码
            userDao.save(user1);//存库
            return new ApiResponse<User>().data(user1).message("修改成功");
        } else {
            return new ApiResponse<User>().fail().message("密码错误，无法修改");
        }
    }

    //线程安全的hashmap
    private static final ConcurrentHashMap<String, Email> smsHashMap = new ConcurrentHashMap<String, Email>();//验证码队列
    private static final long EXP = 5 * 60 * 1000;//5分钟有效期
    private static final long TIME = 60000;//验证码发送最下间隔

    //获取邮箱验证码
    @GetMapping("getCode")
    public ApiResponse<String> getCode(@RequestParam(value = "username",defaultValue = "") String username, @RequestParam("email") String email) {
        //此类中包含验证码，有效时间
        Email emaill;
        //用户登录时的情况，只有邮箱
        if (username.equals("")) {
            if ((emaill = smsHashMap.get(email)) == null || !emaill.normal()) {
                //验证码为空，或者过期
                String code = (int) ((Math.random() * 9 + 1) * 100000) + "";//验证码
                emaill = new Email();
                emaill.code = code;
                emaill.time = EXP + System.currentTimeMillis();//过期时间
                smsHashMap.put(email, emaill);
                System.out.println("邮箱：" + email + "的验证码是" + code);
                new Thread(() -> {
                    try {
                        EmailSender.sendEmail(email, "[话心]登录验证码", "您的验证码为" + code + "(5分钟内有效)。用于安全验证，请不要告诉他人。");
                    } catch (Exception e) {
                        e.printStackTrace();
                        System.out.println("验证码发送异常");
                    }
                }).start();

                return new ApiResponse<String>().message("发送成功");//应答
            } else {
                return new ApiResponse<String>().message("发送过于频繁").fail();
            }
            //注册的情况，此时有用户名
        } else {
            if ((emaill = smsHashMap.get(username)) == null || !emaill.normal()) {
                //验证码为空,或者过期,此时可以发送
                String code = (int) ((Math.random() * 9 + 1) * 100000) + "";//验证码
                emaill = new Email();
                emaill.code = code;
                emaill.time = TIME + System.currentTimeMillis();//过期时间
                smsHashMap.put(username, emaill);
                System.out.println("邮箱：" + email + "的验证码是" + code);
                new Thread(() -> {
                    try {
                        EmailSender.sendEmail(email, "[话心]注册验证码", "您的验证码为" + code + "(5分钟内有效)。用于安全验证，请不要告诉他人。");
                    } catch (Exception e) {
                        e.printStackTrace();
                        System.out.println("验证码发送异常");
                    }
                }).start();

                return new ApiResponse<String>().message("发送成功");//应答
            } else {
                return new ApiResponse<String>().message("发送过于频繁").fail();
            }
        }
    }
}
