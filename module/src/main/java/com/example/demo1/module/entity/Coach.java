package com.example.demo1.module.entity;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class Coach {
    private Long id;
    private String name;
    private String pics;
    private String speciality;
    private String intro;
    private Integer createTime;
    private Integer updateTime;
    private Integer isDeleted;
    private Long categoryId;
}
