package com.example.demo1.app.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class WpVo {
    private Integer page;
    private String keyword;
    private String firstTime;
}
