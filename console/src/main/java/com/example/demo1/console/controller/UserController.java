package com.example.demo1.console.controller;

import com.alibaba.fastjson.JSON;
import com.example.demo1.module.entity.User;
import com.example.demo1.module.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.Base64;
import java.util.List;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @RequestMapping("/user/login")
    public Boolean login(@RequestParam("name") String name, @RequestParam("password") String password, HttpServletResponse response) {
        List<User> users = userService.getByProperty(new User(null, name, password, null, null, null, null, null));
        if (users.isEmpty()) {
            return false;
        }

        // 实体转json
        String jsonString = JSON.toJSONString(users.get(0));
        // Base64编码
        String token = Base64.getUrlEncoder().encodeToString(jsonString.getBytes());
        Cookie cookie = new Cookie("token", token);
        cookie.setMaxAge(60 * 60 * 24 * 7); // 设置Cookie的过期时间为7天
        cookie.setPath("/"); // 设置Cookie的作用路径
        response.addCookie(cookie); // 将Cookie添加到响应中
        return true;
    }
}
