package com.example.demo1.module.entity;

import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 分类信息表
 * </p>
 *
 * @author jobob
 * @since 2025-03-28
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class Category implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    private String name;

    private String pic;

    private Integer createTime;

    private Integer updateTime;

    private Integer isDeleted;


}
