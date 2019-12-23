package com.tensquare.web.filter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;

import javax.servlet.http.HttpServletRequest;

public class WebFilter extends ZuulFilter {
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
        System.out.println("zuul过滤器----web");
        //zuul过滤器 会丢失 头信息  所有要在过滤器执行前拿到头信息 并转发
        //向header中添加鉴权令牌
        //拿到上下文
        RequestContext requestContext = RequestContext.getCurrentContext();
        //拿到 request 作用域
        HttpServletRequest request = requestContext.getRequest();
        String authorization = request.getHeader("Authorization");
        if (authorization != null) {
            requestContext.addZuulRequestHeader("Authorization", authorization);
        }
        return null;
    }
}
