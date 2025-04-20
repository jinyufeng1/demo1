package com.example.demo1.app.controller;

import com.alibaba.fastjson.JSON;
import com.example.demo1.app.domain.UserVo;
import com.example.demo1.module.domain.Sign;
import com.example.demo1.module.entity.User;
import com.example.demo1.module.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Base64;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @RequestMapping("/user/add")
    public Boolean add(@RequestParam(name = "name", required = false) String name, @RequestParam("password") String password,
                       @RequestParam(name = "phone") String phone , @RequestParam(name = "avatar", required = false) String avatar) {
        User user = new User(null, null == name ? null : name.trim(), password, phone.trim(), avatar, null, null, null);
        return userService.insert(user);
    }

    @RequestMapping("/user/login")
    public UserVo login(@RequestParam("phone") String phone, @RequestParam("password") String password) {
        User user = userService.getByPhone(phone);
        if (null == user) {
            throw new RuntimeException("手机号 ： " + phone + "尚未注册系统账号！");
        }

        // 问题： 这个地方如果实现了加密，我是不是可以直接对比传进来的密文是否相等就行了？？？
        String password1 = user.getPassword();
        if (!password.equals(password1)) {
            throw new RuntimeException("密码错误，请核对后重新填写！");
        }

        //构建sign
        Sign sign = new Sign(user.getId(), System.currentTimeMillis() + 3600000L);   // 一小时以后
        String jsonString = JSON.toJSONString(sign);  // 实体转json
        String signString = Base64.getUrlEncoder().encodeToString(jsonString.getBytes());

        return new UserVo(user.getName(), user.getPhone(), user.getAvatar(), signString);
    }
}
