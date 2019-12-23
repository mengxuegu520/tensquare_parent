package com.tensquare.manager.filter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import util.JwtUtil;

import javax.servlet.http.HttpServletRequest;

public class ManagerFilter extends ZuulFilter {
    @Autowired
    private JwtUtil jwtUtil;
    /*
    pre ：可以在请求被路由之前调用
    route ：在路由请求时候被调用
    post ：在route和error过滤器之后被调用
    error ：处理请求时发生错误时被调用
     */
    @Override
    public String filterType() {
        return "pre"; // 前置过滤器
    }

    //返回值越低越先执行
    @Override
    public int filterOrder() {
        return 0; // 优先级为0，数字越大，优先级越低
    }

    /*
    返回一个boolean类型来判断该过滤器是否要执行，所以通过此函数可
  实现过滤器的开关。在上例中，我们直接返回true，所以该过滤器总是生效
     */
    @Override
    public boolean shouldFilter() {
        return true;
    }

    /*
    run ：过滤器的具体逻辑
     */
    @Override
    public Object run() throws ZuulException {
        System.out.println("zuul过滤器----manager");
        //zuul过滤器 会丢失 头信息  所有要在过滤器执行前拿到头信息 并转发
        //向header中添加鉴权令牌
        //拿到上下文
        RequestContext requestContext = RequestContext.getCurrentContext();
        //拿到 request 作用域
        HttpServletRequest request = requestContext.getRequest();
        if (request.getMethod().equals("OPTIONS")) {
            return null;
        }
        String url = request.getRequestURL().toString();
        if (url.indexOf("/admin/login") > 0) {
            System.out.println("登陆页面" + url);
            return null;
        }
        String authHeader = request.getHeader("Authorization");//获取头信息
        //如果不为空 并且以 Bearer加空格  开头
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);  //拿到token值
            //解析token值
            Claims claims = jwtUtil.parseJWT(token);
            if (claims != null) {
                if ("admin".equals(claims.get("roles"))) {
                    requestContext.addZuulRequestHeader("Authorization", authHeader);
                    System.out.println("token 验证通过，添加了头信息" + authHeader);
                    return null;
                }
            }
        }
        //authHeader 为空  没用权限访问
        requestContext.setSendZuulResponse(false);//终止运行
        requestContext.setResponseStatusCode(401);//http状态码
        //requestContext.setResponseBody("无权访问"); 传的是json
        //requestContext.getResponse().setContentType 也要改为 application/json
        requestContext.setResponseBody("无权访问");
        requestContext.getResponse().setContentType("text/html;charset=UTF‐8");
        return null;
    }
}
