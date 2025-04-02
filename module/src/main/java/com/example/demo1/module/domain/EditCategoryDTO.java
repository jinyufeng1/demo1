package com.example.demo1.module.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@AllArgsConstructor
public class EditCategoryDTO {
    private Long id;
    private String name;
    private String pic;
    private Long parentId;
}
