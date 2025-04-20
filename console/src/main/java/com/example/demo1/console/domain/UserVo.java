package com.example.demo1.console.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserVo {
    private String name;
    private String phone;
    private String avatar;
}
