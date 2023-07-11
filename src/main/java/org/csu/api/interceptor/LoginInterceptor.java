package org.csu.api.interceptor;


import org.csu.api.common.CONSTANT;
import org.csu.api.common.CommonResponse;
import org.csu.api.common.ResponseCode;
import org.csu.api.util.RedisCache;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.servlet.HandlerInterceptor;


import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Author: RQG
 * @Description: TODO
 * @Date 2023-03-20 21:22:31
 * @Version 1.0.0
 **/
//@Component
public class LoginInterceptor implements HandlerInterceptor {
    @Resource
    private RedisCache redisCache;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        String token = request.getHeader("Authorization");

        System.out.println("token");
        System.out.println(request.getHeader("Authorization"));
        if (HttpMethod.OPTIONS.toString().equals(request.getMethod())){
            System.out.println("options请求，放行");
            return true;
        }
        if (token == null || redisCache.getCacheObject(token) == null) {
//
            return false;
        }
        return true;
    }
}
