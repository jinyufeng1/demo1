package com.example.demo1.module.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigInteger;

@Data
@Accessors(chain = true)
//@TableName //不是必须的
public class Coach {
    private BigInteger id;
    private String name;
    private String pics;
    private String speciality;
    private String intro;
    @TableField(fill = FieldFill.INSERT)
    private Integer createTime;
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Integer updateTime;
//    @TableLogic //在springboot配置中加了全局配置就可以不用加了
    private Integer isDeleted;
}
