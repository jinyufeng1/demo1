package com.example.demo1.console.domain;

import lombok.Data;

import java.util.List;

//详细信息
@Data
public class CoachDetailsVo {
//    轮播图
    private List<String> pics;

//    介绍
    private String intro;

//    创建时间
    private String createTime;

//    修改时间
    private String updateTime;
}
