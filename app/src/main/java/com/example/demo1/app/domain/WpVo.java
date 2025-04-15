package com.example.demo1.app.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class WpVo {
    private Integer page;
    private String keyword;
    private String firstTime;
    List<Long> leafCategoryIds;
}
