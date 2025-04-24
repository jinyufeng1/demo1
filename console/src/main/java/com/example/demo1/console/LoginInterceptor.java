package com.example.demo1.console;

import com.alibaba.fastjson.JSON;
import com.example.demo1.module.common.Response;
import com.example.demo1.module.domain.Sign;
import com.example.demo1.module.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Base64;

@Component
public class LoginInterceptor implements HandlerInterceptor {

    @Autowired
    private UserService userService;

    // 通过这样的方式在拦截器返回Response
    private void writeResponse(HttpServletResponse response, Integer code) throws IOException {
        Response objectResponse = new Response<>(code);
        response.setContentType("application/json;charset=UTF-8"); // 设置内容类型和字符编码
        String jsonString = JSON.toJSONString(objectResponse);
        response.getWriter().write(jsonString);
    }

    // 返回 true 表示继续处理请求，返回 false 表示中断请求
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 登录判断
        Cookie[] cookies = request.getCookies();
        String signStr = null;
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("sign".equals(cookie.getName())) {
                    signStr = cookie.getValue();
                    break;
                }
            }
        }

        if (null == signStr) {
            writeResponse(response, 1002);
            return false;
        }

        //Base64解码
        String decode = new String(Base64.getUrlDecoder().decode(signStr));
        //获取json转实体
        Sign sign = JSON.parseObject(decode, Sign.class);

        Long expirationTime = sign.getExpirationTime();
        if (expirationTime < System.currentTimeMillis()) {
            writeResponse(response, 1004);
            return false;
        }

        if (ObjectUtils.isEmpty(userService.getById(sign.getId()))) {
            writeResponse(response, 1005);
            return false;
        }

        return true;
    }
}