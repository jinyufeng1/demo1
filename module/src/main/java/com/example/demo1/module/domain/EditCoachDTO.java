package com.example.demo1.module.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.Accessors;


@Data
@Accessors(chain = true)
@AllArgsConstructor
public class EditCoachDTO {
    private Long id;
    private String pics;
    private String name;
    private String speciality;
    private String intro;
    private Long categoryId;
    private String tags;
}
