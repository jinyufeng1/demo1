package com.example.demo1.module.entity;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class Coach {
    @ExcelProperty("id")
    private Long id;
    @ExcelProperty("name")
    private String name;
    @ExcelProperty("pics")
    private String pics;
    @ExcelProperty("speciality")
    private String speciality;
    @ExcelProperty("intro")
    private String intro;
    @ExcelProperty("categoryId")
    private Long categoryId;
    @ExcelProperty("createTime")
    private Integer createTime;
    @ExcelProperty("updateTime")
    private Integer updateTime;
    @ExcelProperty("isDeleted")
    private Integer isDeleted;
}
