package com.example.demo1.module.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigInteger;



//mybatis-plus会自动将类名去掉首字母大写，当表名。类名表名不一致时使用，不是必须的，@TableFiled同理
//@TableName
@Data
@Accessors(chain = true)
public class Coach {
    @TableId(value = "id", type = IdType.AUTO)
    private BigInteger id;  //如果属性名是id，就会被mp当成主键
    private String name;
    private String pics;
    private String speciality;
    private String intro;
    @TableField(fill = FieldFill.INSERT)
    private Integer createTime;
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Integer updateTime;
//    @TableLogic(value = "0", delval = "1") //在springboot配置中加了全局配置就可以不用加了
    private Integer isDeleted;
}
