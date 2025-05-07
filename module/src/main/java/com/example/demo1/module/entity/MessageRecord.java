package com.example.demo1.module.entity;

import lombok.Data;

/**
 * <p>
 * 短信记录表
 * </p>
 *
 * @author 我叫小呆呆
 * @since 2025-05-07
 */
@Data
public class MessageRecord {
    private Long id;
    private String phone;
    private String content;
    private Integer code;
    private String reason;
    private Integer createTime;
    private Integer updateTime;
    private Integer isDeleted;
}
