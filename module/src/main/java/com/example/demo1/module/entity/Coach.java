package com.example.demo1.module.entity;

import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigInteger;

@Data
@Accessors(chain = true)
public class Coach {
    private BigInteger id;
    private String name;
    private String pics;
    private String speciality;
    private String intro;
    private Integer createTime;
    private Integer updateTime;
    private Integer isDeleted;
}
