package com.technical.exchange.controller;

import com.technical.exchange.bean.*;
import com.technical.exchange.dao.LikeDao;
import com.technical.exchange.dao.PostDao;
import com.technical.exchange.dao.ReplyDao;
import com.technical.exchange.dao.UserDao;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.UUID;

//故事控制器
@RestController
@RequestMapping("/app/post")
public class PostController {
    @Resource
    private PostDao postDao;//故事数据库操作
    @Resource
    private ReplyDao replyDao;//故事回复数据库操作
    @Resource
    UserDao userDao;//用户操作
    //发布故事
    @PostMapping("post")
    public ApiResponse<String> post(@RequestBody Post post) {
        //接收post之后加入主键以及时间属性
        post.uuid = UUID.randomUUID().toString();//主键
        post.create_time = System.currentTimeMillis();//时间
        postDao.save(post);
        return new ApiResponse<String>().message("发布成功");//应答
    }

    //回复
    @PostMapping("reply")
    public ApiResponse<String> reply(@RequestBody Reply reply) {
        reply.uuid = UUID.randomUUID().toString();//主键
        reply.create_time = System.currentTimeMillis();//时间
        replyDao.save(reply);
        return new ApiResponse<String>().message("回复成功");//应答
    }

    //获取所有故事，按浏览量多到少排序
    @GetMapping("getAllPost")
    public ApiResponse<List<Post>> getAllPost() {
        List<Post> postList = postDao.getAllPost();//获取所有故事
        for (Post post : postList) {
            setDetail(post);//设置故事的其他信息
        }
        return new ApiResponse<List<Post>>().data(postList);//应答
    }



    //根据故事uuid获取故事的回复列表
    @GetMapping("getReplyByPostUUID")
    public ApiResponse<List<Reply>> getReplyByPostUUID(@RequestParam("post_uuid") String post_uuid) {
        List<Reply> replyList = replyDao.getReplyByPostUUID(post_uuid);//获取回复列表
        return new ApiResponse<List<Reply>>().data(replyList);//应答
    }

    //根据故事id获取故事，并增加浏览次数
    @GetMapping("view")
    public ApiResponse<Post> view(@RequestParam("post_uuid") String post_uuid) {
        Post post = postDao.getPost(post_uuid);
        post.view_count++;
        postDao.save(post);
        setDetail(post);//设置故事的其他信息
        return new ApiResponse<Post>().data(post).message("获取成功");
    }

    //删除故事
    @GetMapping("delete")
    public ApiResponse<String> delete(@RequestParam("post_uuid") String post_uuid) {
        replyDao.deleteByPostUUID(post_uuid);//删除回复
        postDao.deleteById(post_uuid);//删除主记录
        return new ApiResponse<String>().message("删除成功");
    }

    //我的故事
    @GetMapping("getMyPost")
    public ApiResponse<List<Post>> getMyPost(@RequestParam("username") String username) {
        List<Post> postList = postDao.getMyPost(username);
        for (Post post : postList) {
            setDetail(post);//设置详细信息
        }
        return new ApiResponse<List<Post>>().data(postList);
    }



    //设置故事的其他信息
    //即不在数据库中存储的信息
    private void setDetail(Post post) {
        post.replyList = replyDao.getReplyByPostUUID(post.uuid);//查询此故事的回复数量
//        post.like = likeDao.getLikeCountByPostUUID(post.uuid);//点赞数
        List<User> users = userDao.checkUserName(post.username);//获取用户
        if (users == null || users.isEmpty()) {
            post.avatar = "";
        } else {
            post.avatar = users.get(0).avatar;//设置发布人用户昵称
        }
    }

}
