package com.example.demo1.module.common;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class WpEntity {
    private Integer page;
    private String keyword;
}
