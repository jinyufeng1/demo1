package com.example.demo1.app.controller;

import com.example.demo1.app.domain.CategoryItemListVo;
import com.example.demo1.app.domain.CategoryItemVo;
import com.example.demo1.module.entity.Category;
import com.example.demo1.module.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    @RequestMapping("/category/list")
    public CategoryItemListVo getCategoryList(@RequestParam(name = "keyword", required = false) String keyword) {
        CategoryItemListVo categoryItemListVo = new CategoryItemListVo();
        List<Category> list = categoryService.getList(keyword, null, null);
        if (list.isEmpty()) {
            categoryItemListVo.setList(new ArrayList<>());
            return categoryItemListVo;
        }

        // 一级分类
        List<Category> firstList = list.stream().filter(e -> null == e.getParentId()).collect(Collectors.toList());
        if (firstList.isEmpty()) {
            throw new RuntimeException("分类信息异常，无一级分类信息！");
        }

        // 二级分类
        List<Category> secondList = list.stream().filter(e -> null != e.getParentId()).collect(Collectors.toList());

        //构建所属关系
        Map<Long, List<Category>> map = new HashMap<>();
        for (Category category : secondList) {
            List<Category> categories = map.computeIfAbsent(category.getParentId(), e -> new ArrayList<>());
            categories.add(category);
        }

        //构建层级列表
        List<CategoryItemVo> retList = firstList.stream().map(e -> {
            CategoryItemVo categoryItemVo = new CategoryItemVo();
            categoryItemVo.setId(e.getId());
            categoryItemVo.setName(e.getName());
            categoryItemVo.setIcon(e.getPic());
            List<Category> categories = map.get(e.getId());
            if (!CollectionUtils.isEmpty(categories)) {
                categoryItemVo.setChildren(new ArrayList<>());

                for (Category category : categories) {
                    CategoryItemVo child = new CategoryItemVo();
                    child.setId(category.getId());
                    child.setName(category.getName());
                    child.setIcon(category.getPic());
                    categoryItemVo.getChildren().add(child);
                }
            }

            return categoryItemVo;
        }).collect(Collectors.toList());

        categoryItemListVo.setList(retList);
        return categoryItemListVo;
    }
}
