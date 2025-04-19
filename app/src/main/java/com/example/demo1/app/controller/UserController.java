package com.example.demo1.app.controller;

import com.alibaba.fastjson.JSON;
import com.example.demo1.module.entity.User;
import com.example.demo1.module.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Base64;
import java.util.List;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @RequestMapping("/user/add")
    public Boolean add(@RequestParam("name") String name, @RequestParam("password") String password,
                       @RequestParam(name = "phone", required = false) String phone , @RequestParam(name = "avatar", required = false) String avatar) {
        User user = new User(null, name, password, phone, avatar, null, null, null);
        return userService.insert(user);
    }

    @RequestMapping("/user/login")
    public String login(@RequestParam("name") String name, @RequestParam("password") String password) {
        List<User> users = userService.getByProperty(new User(null, name, password, null, null, null, null, null));
        if (users.isEmpty()) {
            return null;
        }

        // 实体转json
        String jsonString = JSON.toJSONString(users.get(0));
        // Base64编码 直接返回token
        return Base64.getUrlEncoder().encodeToString(jsonString.getBytes());
    }
}
