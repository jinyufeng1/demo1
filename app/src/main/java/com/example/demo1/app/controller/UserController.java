package com.example.demo1.app.controller;

import com.alibaba.fastjson.JSON;
import com.example.demo1.app.domain.UserVo;
import com.example.demo1.module.common.Response;
import com.example.demo1.module.domain.EditUserDTO;
import com.example.demo1.module.domain.Sign;
import com.example.demo1.module.entity.User;
import com.example.demo1.module.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Base64;

@Slf4j
@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @RequestMapping("/user/register")
    public Response<Long> add(@RequestParam(name = "name", required = false) String name, @RequestParam("password") String password,
                             @RequestParam(name = "phone") String phone , @RequestParam(name = "avatar", required = false) String avatar) {
        Long userId = userService.edit(new EditUserDTO(null, name, password, phone, avatar));
        return new Response<>(1001, userId);
    }

    @RequestMapping("/user/login")
    public Response<UserVo> login(@RequestParam("phone") String phone, @RequestParam("password") String password) {
        User user = userService.getByPhone(phone);
        if (null == user) {
            return new Response<>(2001);
        }

        // 检查原始密码和存储的哈希值是否匹配。返回true或false。
        if (!BCrypt.checkpw(password, user.getPassword())) {
            return new Response<>(1003);
        }

        //构建sign
        Sign sign = new Sign(user.getId(), System.currentTimeMillis() + 3600000L);   // 一小时以后
        String jsonString = JSON.toJSONString(sign);  // 实体转json
        String signString = Base64.getUrlEncoder().encodeToString(jsonString.getBytes());

        UserVo userVo = new UserVo(user.getName(), user.getPhone(), user.getAvatar(), signString);

        return new Response<>(1001, userVo);
    }
}
