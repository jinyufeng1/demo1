package com.example.demo1.app.controller;

import com.example.demo1.app.domain.CategoryItemListVo;
import com.example.demo1.app.domain.CategoryItemVo;
import com.example.demo1.module.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    @RequestMapping("/category/list")
    public CategoryItemListVo getCategoryList(@RequestParam(name = "keyword", required = false) String keyword) {
        List<CategoryItemVo> list = categoryService.getList(keyword, null).stream().map(e -> {
            CategoryItemVo categoryItemVo = new CategoryItemVo();
            categoryItemVo.setId(e.getId());
            categoryItemVo.setName(e.getName());
            categoryItemVo.setIcon(e.getPic());
            return categoryItemVo;
        }).collect(Collectors.toList());

        CategoryItemListVo categoryItemListVo = new CategoryItemListVo();
        categoryItemListVo.setList(list);
        return categoryItemListVo;
    }
}
