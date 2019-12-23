package com.tensquare.user.controller;

import java.util.HashMap;
import java.util.Map;

import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.tensquare.user.pojo.User;
import com.tensquare.user.service.UserService;

import entity.PageResult;
import entity.Result;
import entity.StatusCode;
import util.JwtUtil;

import javax.servlet.http.HttpServletRequest;

/**
 * 控制器层
 *
 * @author Administrator
 */
@RestController
@CrossOrigin
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private JwtUtil jwtUtil;

    /**
     * 更新好友粉丝数和用户关注数
     * @param userid
     * @param friendid
     * @param x
     */
    @RequestMapping(value = "/{userid}/{friendid}/{x}", method = RequestMethod.PUT)
    public void updateincfanscountAndincfollowcount(@PathVariable String userid,@PathVariable String friendid,@PathVariable Integer x){
        System.out.println(userid+"####"+friendid+"$$$$"+x);
    userService.updateIncFanscountAndIncFollowcounts(x,userid,friendid);
    }


    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public Result login(@RequestBody Map<String,String> loginMap) {
        User user = userService.findByMobileAndPassword(loginMap.get("mobile"), loginMap.get("password"));
        if (user != null) {
            String token = jwtUtil.createJWT(user.getId(), user.getNickname(), "user");
            Map<String, Object> map = new HashMap<>();
            map.put("token", token);
            map.put("roles","user"); //给客户端的  其实作用不大，只是让前端的人看见是那个角色
            map.put("name", user.getNickname());//昵称            
            map.put("avatar", user.getAvatar());//头像
            return new Result(true, StatusCode.OK, "登陆成功",map);
        } else {
            return new Result(false, StatusCode.LOGINERROR, "用户名或密码错 误");

        }
    }


    /**
     * 发送短信验证码
     *
     * @param mobile 手机号
     * @return
     */
    @RequestMapping(value = "/sendsms/{mobile}", method = RequestMethod.POST)
    public Result sendsms(@PathVariable String mobile) {
        userService.sendSms(mobile);
        return new Result(true, StatusCode.OK, "发送成功");
    }

    /**
     * @param user 用户写的信息
     * @param code 验证码
     * @return
     */
    @RequestMapping(value = "/register/{code}", method = RequestMethod.POST)
    public Result register(@RequestBody User user, @PathVariable String code) {
        //判断验证码是否正确
        String syscode = (String) redisTemplate.opsForValue().get("smscode_" + user.getMobile());
        //提取系统正确的验证码syscode==null  缓存没有
        if (syscode == null) {
            return new Result(false, StatusCode.ERROR, "请点击获取短信验证码");
        }
        if (!syscode.equals(code)) {
            return new Result(false, StatusCode.ERROR, "验证码输入不正确");
        }
        userService.add(user);
        return new Result(true, StatusCode.OK, "注册成功");
    }


    /**
     * 查询全部数据
     *
     * @return
     */
    @RequestMapping(method = RequestMethod.GET)
    public Result findAll() {
        return new Result(true, StatusCode.OK, "查询成功", userService.findAll());
    }

    /**
     * 根据ID查询
     *
     * @param id ID
     * @return
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public Result findById(@PathVariable String id) {
        return new Result(true, StatusCode.OK, "查询成功", userService.findById(id));
    }


    /**
     * 分页+多条件查询
     *
     * @param searchMap 查询条件封装
     * @param page      页码
     * @param size      页大小
     * @return 分页结果
     */
    @RequestMapping(value = "/search/{page}/{size}", method = RequestMethod.POST)
    public Result findSearch(@RequestBody Map searchMap, @PathVariable int page, @PathVariable int size) {
        Page<User> pageList = userService.findSearch(searchMap, page, size);
        return new Result(true, StatusCode.OK, "查询成功", new PageResult<User>(pageList.getTotalElements(), pageList.getContent()));
    }

    /**
     * 根据条件查询
     *
     * @param searchMap
     * @return
     */
    @RequestMapping(value = "/search", method = RequestMethod.POST)
    public Result findSearch(@RequestBody Map searchMap) {
        return new Result(true, StatusCode.OK, "查询成功", userService.findSearch(searchMap));
    }

    /**
     * 增加
     *
     * @param user
     */
    @RequestMapping(method = RequestMethod.POST)
    public Result add(@RequestBody User user) {
        userService.add(user);
        return new Result(true, StatusCode.OK, "增加成功");
    }

    /**
     * 修改
     *
     * @param user
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public Result update(@RequestBody User user, @PathVariable String id) {
        user.setId(id);
        userService.update(user);
        return new Result(true, StatusCode.OK, "修改成功");
    }

    /**
     * 删除
     * 必须有admin角色才可以删除
     *
     * @param id
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public Result delete(@PathVariable String id, HttpServletRequest request) {
        Claims claims = (Claims) request.getAttribute("admin_claims");
        /*
        claims 不为空 声明拦截器已经把值拿到了 并且token是存在的是正确的
        如果不正确解析会报异常 ，所有我们在这里只判断为不为空就可以了
         */
        if (claims == null) {
            return new Result(false, StatusCode.ACCESSERROR, "无权访问");
        }
        userService.deleteById(id);
        return new Result(true, StatusCode.OK, "删除成功");
    }

}
