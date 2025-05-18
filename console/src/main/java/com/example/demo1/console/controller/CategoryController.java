package com.example.demo1.console.controller;

import com.example.demo1.console.domain.CategoryItemListVo;
import com.example.demo1.module.common.Response;
import com.example.demo1.module.domain.EditCategoryDTO;
import com.example.demo1.module.entity.Category;
import com.example.demo1.module.entity.Coach;
import com.example.demo1.module.service.CategoryService;
import com.example.demo1.module.service.CoachService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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
    public Response<Long> updateCategory(@RequestParam("id") Long id, @RequestParam(value = "name", required = false) String name, @RequestParam(value = "icon", required = false) String icon, @RequestParam(value = "parentId", required = false) Long parentId) {
        Long categoryId = categoryService.edit(new EditCategoryDTO(id, name, icon, parentId));
        return new Response<>(1001, categoryId);
    }

    @RequestMapping("/category/ntree")
    public Response<CategoryItemListVo> getCategoryTree() {
        List<Category> list = categoryService.getList(null, null, null);
        return new Response<>(1001, CategoryItemListVo.getCategoryTree(list));
    }
}
