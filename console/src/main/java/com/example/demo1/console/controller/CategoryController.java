package com.example.demo1.console.controller;

import com.example.demo1.console.domain.CategoryItemListVo;
import com.example.demo1.console.domain.CategoryItemVo;
import com.example.demo1.module.common.Constant;
import com.example.demo1.module.common.CustomUtils;
import com.example.demo1.module.common.Response;
import com.example.demo1.module.domain.EditCategoryDTO;
import com.example.demo1.module.entity.Category;
import com.example.demo1.module.entity.Coach;
import com.example.demo1.module.service.CategoryService;
import com.example.demo1.module.service.CoachService;
import lombok.extern.slf4j.Slf4j;
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

@Slf4j
@RestController
public class CategoryController {

    @Autowired
    private CoachService coachService;
    @Autowired
    private CategoryService categoryService;

    @RequestMapping("/category/add")
    public Response<Long> addCategory(@RequestParam("name") String name, @RequestParam(value = "icon", required = false) String icon, @RequestParam(value = "parentId") Long parentId) {
        Long categoryId = categoryService.edit(new EditCategoryDTO(null, name, icon, parentId));
        return new Response<>(1001, categoryId);
    }

    @RequestMapping("/category/del")
    public Response<Boolean> deleteCategory(@RequestParam("id") Long id) {
        Boolean ret = categoryService.deleteHierarchy(id);
        if (ret) {
            //为解决循环依赖，将coachService提升一级，删除依赖这个分类的教练数据
            Coach entity = new Coach();
            entity.setCategoryId(id);
            coachService.deleteByProperty(entity);
        }
        return new Response<>(1001, ret);
    }

    @RequestMapping("/category/update")
    public Response<Long> updateCategory(@RequestParam("id") Long id, @RequestParam(value = "name", required = false) String name, @RequestParam(value = "icon", required = false) String icon, @RequestParam(value = "parentId") Long parentId) {
        Long categoryId = categoryService.edit(new EditCategoryDTO(id, name, icon, parentId));
        return new Response<>(1001, categoryId);
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
    public Response<CategoryItemListVo> getCategoryTree() {
        CategoryItemListVo categoryItemListVo = new CategoryItemListVo();
        List<Category> list = categoryService.getList(null, null, null);
        if (list.isEmpty()) {
            log.info("console CategoryController 无分类信息");
            return new Response<>(1001, categoryItemListVo);
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
            log.info("console CategoryController 无一级分类信息");
            return new Response<>(1001, categoryItemListVo);
        }

        //构建层级列表
        List<CategoryItemVo> retList = firstList.stream().map(e -> shift(e, map)).collect(Collectors.toList());

        categoryItemListVo.setList(retList);
        return new Response<>(1001, categoryItemListVo);
    }
}
