package com.tensquare.qa.Interceptor;

import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import util.JwtUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class JwtLnterceptor extends HandlerInterceptorAdapter {
    @Autowired
    private JwtUtil jwtUtil;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        System.out.println("经过了拦截器");
        //客户端发来 请求头  我们去request拿
        String authHeader = request.getHeader("Authorization");
        //1:拿到的不是空 并且 格式正确
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            try {
                //2 ：截取 完整的token
                final String token = authHeader.substring(7);
                //解析token  如果没用可以报异常 所有cry一下
                Claims claims = jwtUtil.parseJWT(token);
                if (claims != null) {
                    if ("admin".equals(claims.get("rolse"))) {//如果是管理员
                        request.setAttribute("admin_claims", claims);
                    }
                    if ("user".equals(claims.get("roles"))) {//如果是用户                
                        request.setAttribute("user_claims", claims);
                    }
                }
            } catch (Exception e) {

            }
        }
        return true;
    }
}
