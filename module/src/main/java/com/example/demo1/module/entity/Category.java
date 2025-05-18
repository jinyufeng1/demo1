package com.example.demo1.module.entity;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

/**
 * <p>
 * 分类信息表
 * </p>
 *
 * @author 我叫小呆呆
 * @since 2025-04-02
 */
@Data
public class Category {
    @ExcelProperty("id")
    private Long id;
    @ExcelProperty("name")
    private String name;
    @ExcelProperty("pic")
    private String pic;
    @ExcelProperty("parentId")
    private Long parentId;
    @ExcelProperty("createTime")
    private Integer createTime;
    @ExcelProperty("updateTime")
    private Integer updateTime;
    @ExcelProperty("isDeleted")
    private Integer isDeleted;
}
