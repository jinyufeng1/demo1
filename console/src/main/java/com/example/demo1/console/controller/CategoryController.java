package com.example.demo1.console.controller;

import com.example.demo1.console.domain.CategoryItemListVo;
import com.example.demo1.console.domain.CategoryItemVo;
import com.example.demo1.module.common.Constant;
import com.example.demo1.module.common.CustomUtils;
import com.example.demo1.module.domain.EditCategoryDTO;
import com.example.demo1.module.entity.Category;
import com.example.demo1.module.entity.Coach;
import com.example.demo1.module.service.CategoryService;
import com.example.demo1.module.service.CoachService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
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

    public CategoryItemVo shift(Category category, Map<Long, List<Category>> map) {
        CategoryItemVo categoryItemVo = new CategoryItemVo();
        categoryItemVo.setId(category.getId());
        categoryItemVo.setName(category.getName());
        categoryItemVo.setIcon(category.getPic());
        categoryItemVo.setCreateTime(CustomUtils.transformTimestamp(category.getCreateTime() * 1000L, Constant.DATE_PATTERN_1));
        categoryItemVo.setUpdateTime(CustomUtils.transformTimestamp(category.getUpdateTime() * 1000L, Constant.DATE_PATTERN_1));
        List<Category> children = map.get(category.getId());
        if (CollectionUtils.isEmpty(children)) {
            return categoryItemVo;
        }

        //添加子节点
        categoryItemVo.setChildren(new ArrayList<>());
        for (Category child : children) {
            categoryItemVo.getChildren().add(shift(child, map));
        }

        return categoryItemVo;
    }

    @RequestMapping("/category/ntree")
    public CategoryItemListVo getCategoryTree() {
        CategoryItemListVo categoryItemListVo = new CategoryItemListVo();
        List<Category> list = categoryService.getList(null, null, null);
        if (list.isEmpty()) {
            categoryItemListVo.setList(new ArrayList<>());
            return categoryItemListVo;
        }

        // 一级分类
        List<Category> firstList = new ArrayList<>();
        //分类所属关系 所有子节点
        Map<Long, List<Category>> map = new HashMap<>();
        for (Category category : list) {
            Long parentId = category.getParentId();
            if (ObjectUtils.isEmpty(parentId)) {
                firstList.add(category);
            } else {
                List<Category> categories = map.computeIfAbsent(parentId, e -> new ArrayList<>());
                categories.add(category);
            }
        }

        if (firstList.isEmpty()) {
            throw new RuntimeException("分类信息异常，无一级分类信息！");
        }

        //构建层级列表
        List<CategoryItemVo> retList = firstList.stream().map(e -> shift(e, map)).collect(Collectors.toList());

        categoryItemListVo.setList(retList);
        return categoryItemListVo;
    }
}
