package com.example.demo1.app.domain;

import lombok.Data;

@Data
public class ImageVo {
    private String src;
    private Float ar = 1.0f; // 默认值0.1
}
