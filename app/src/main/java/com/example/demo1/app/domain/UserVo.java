package com.example.demo1.app.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserVo {
    private String name;
    private String phone;
    private String avatar;
    private String sign;
}
