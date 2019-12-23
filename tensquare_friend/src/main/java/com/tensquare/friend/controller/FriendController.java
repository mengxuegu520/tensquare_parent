package com.tensquare.friend.controller;

import com.tensquare.friend.client.UserClient;
import com.tensquare.friend.service.FriendService;
import entity.Result;
import entity.StatusCode;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/friend")
public class FriendController {

    @Autowired
    private FriendService friendService;
    @Autowired
    private UserClient userClient;

    /**
     *      *  添加好友
     *      * @param friendid 对方用户ID
     *      * @param type  1：喜欢 0：不喜欢
     *      * @return
     */
    @RequestMapping(value = "/like/{friendid}/{type}", method = RequestMethod.PUT)
    public Result addFriend(@PathVariable String friendid, @PathVariable String type, HttpServletRequest request) {
        //验证是否登陆
        Claims claims = (Claims) request.getAttribute("user_claims");
        if (claims == null) {
            return new Result(false, StatusCode.ACCESSERROR, "无权访问");
        }
        //判断添加好友还是非好友
        if (type != null) {
            //如果是喜欢  添加好友
            if (type.equals("1")) {
                int flag = friendService.addFriend(claims.getId(), friendid);
                if (flag == 0) {
                    return new Result(false, StatusCode.REPERROR, "已经添加此好友");
                }
                if (flag == 1) {
                    //这里回去调用别的微服务 来增加粉丝数 和 关注数
                    userClient.updateincfanscountAndincfollowcount(claims.getId(),friendid,1);
                    return new Result(true, StatusCode.OK, "添加好友成功");
                }
            } else if (type.equals("2")) {
                //不喜欢 添加非好友
                int flag = friendService.addNoFriend(claims.getId(), friendid);
                if (flag == 0) {
                    return new Result(false, StatusCode.REPERROR, "不能重复添加");
                }
                if (flag == 1) {
                    return new Result(true, StatusCode.OK, "添加非好友成功");
                }

            }
            return new Result(false, StatusCode.ERROR, "参数异常");
        } else {
            return new Result(false, StatusCode.ERROR, "参数异常");
        }
    }


    /**
     *      * 删除好友
     *      * @param friendid
     *      * @return
     *      
     */
    @RequestMapping(value = "/{friendid}", method = RequestMethod.DELETE)
    public Result remove(@PathVariable String friendid,HttpServletRequest request) {
        Claims claims = (Claims) request.getAttribute("user_claims");
        if (claims == null) {
            return new Result(false, StatusCode.ACCESSERROR, "无权访问");
        }
        friendService.deleteFriend(claims.getId(), friendid);
        userClient.updateincfanscountAndincfollowcount(claims.getId(),friendid,-1);
        return new Result(true, StatusCode.OK, "删除成功");
    }

}
