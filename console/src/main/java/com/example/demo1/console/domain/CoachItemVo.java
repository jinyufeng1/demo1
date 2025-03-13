package com.example.demo1.console.domain;

import lombok.Data;

import java.math.BigInteger;

//列表元素
@Data
public class CoachItemVo {
//    数据id
    private BigInteger id;

//    封面
    private String pic;

//    名称
    private String name;

//    专长
    private String speciality;
}
