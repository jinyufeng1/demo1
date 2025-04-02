package com.example.demo1.console.controller;

import com.example.demo1.console.domain.CategoryItemListVo;
import com.example.demo1.console.domain.CategoryItemVo;
import com.example.demo1.module.common.CustomUtils;
import com.example.demo1.module.domain.EditCategoryDTO;
import com.example.demo1.module.entity.Category;
import com.example.demo1.module.entity.Coach;
import com.example.demo1.module.service.CategoryService;
import com.example.demo1.module.service.CoachService;
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
    private CoachService coachService;
    @Autowired
    private CategoryService categoryService;

    @RequestMapping("/category/add")
    public String addCategory(@RequestParam("name") String name, @RequestParam(value = "icon", required = false) String icon, @RequestParam(value = "parentId") Long parentId) {
        return categoryService.edit(new EditCategoryDTO(null, name, icon, parentId));
    }

    @RequestMapping("/category/del")
    public Boolean deleteCategory(@RequestParam("id") Long id) {
        Boolean ret = categoryService.deleteHierarchy(id);
        if (ret) {
            //为解决循环依赖，将coachService提升一级，删除依赖这个分类的教练数据
            Coach entity = new Coach();
            entity.setCategoryId(id);
            coachService.deleteByProperty(entity);
        }
        return ret;
    }

    @RequestMapping("/category/update")
    public String updateCategory(@RequestParam("id") Long id, @RequestParam(value = "name", required = false) String name, @RequestParam(value = "icon", required = false) String icon, @RequestParam(value = "parentId") Long parentId) {
        return categoryService.edit(new EditCategoryDTO(id, name, icon, parentId));
    }

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
            categoryItemVo.setCreateTime(CustomUtils.transformTimestamp(e.getCreateTime()));
            categoryItemVo.setUpdateTime(CustomUtils.transformTimestamp(e.getUpdateTime()));
            List<Category> categories = map.get(e.getId());
            if (!CollectionUtils.isEmpty(categories)) {
                categoryItemVo.setChildren(new ArrayList<>());

                for (Category category : categories) {
                    CategoryItemVo child = new CategoryItemVo();
                    child.setId(category.getId());
                    child.setName(category.getName());
                    child.setIcon(category.getPic());
                    child.setCreateTime(CustomUtils.transformTimestamp(category.getCreateTime()));
                    child.setUpdateTime(CustomUtils.transformTimestamp(category.getUpdateTime()));
                    categoryItemVo.getChildren().add(child);
                }
            }

            return categoryItemVo;
        }).collect(Collectors.toList());

        categoryItemListVo.setList(retList);
        return categoryItemListVo;
    }
}
