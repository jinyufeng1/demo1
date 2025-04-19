package com.example.demo1.module.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * <p>
 * 用户信息表
 * </p>
 *
 * @author 我叫小呆呆
 * @since 2025-04-19
 */
@Data
@AllArgsConstructor
public class User {
    private Long id;
    private String name;
    private String password;
    private String phone;
    private String avatar;
    private Integer createTime;
    private Integer updateTime;
    private Integer isDeleted;
}
