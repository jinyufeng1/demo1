package com.example.demo1.console.controller;

import com.example.demo1.console.domain.CategoryItemListVo;
import com.example.demo1.console.domain.CategoryItemVo;
import com.example.demo1.module.common.CustomUtils;
import com.example.demo1.module.entity.Category;
import com.example.demo1.module.entity.Coach;
import com.example.demo1.module.service.CategoryService;
import com.example.demo1.module.service.CoachService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
public class CategoryController {

    @Autowired
    private CoachService coachService;
    @Autowired
    private CategoryService categoryService;

    @RequestMapping("/category/add")
    public Boolean addCategory(@RequestParam("name") String name, @RequestParam(value = "icon", required = false) String icon) {
        Category category = new Category();
        category.setName(name);
        category.setPic(icon);
        return categoryService.insert(category);
    }

    @RequestMapping("/category/del")
    public Boolean deleteCategory(@RequestParam("id") Long id) {
        Boolean ret = categoryService.delete(id);
        if (ret) {
            //为解决循环依赖，将coachService提升一级，删除依赖这个分类的教练数据
            Coach entity = new Coach();
            entity.setCategoryId(id);
            coachService.deleteByProperty(entity);
        }
        return ret;
    }

    @RequestMapping("/category/update")
    public Boolean updateCategory(@RequestParam("id") Long id, @RequestParam(value = "name", required = false) String name, @RequestParam(value = "icon", required = false) String icon) {
        Category category = new Category();
        category.setId(id);
        category.setName(name);
        category.setPic(icon);
        return categoryService.update(category);
    }

    @RequestMapping("/category/list")
    public CategoryItemListVo getCategoryList(@RequestParam(name = "keyword", required = false) String keyword) {
        List<CategoryItemVo> list = categoryService.getList(keyword, null).stream().map(e -> {
            CategoryItemVo categoryItemVo = new CategoryItemVo();
            categoryItemVo.setId(e.getId());
            categoryItemVo.setName(e.getName());
            categoryItemVo.setIcon(e.getPic());
            categoryItemVo.setCreateTime(CustomUtils.transformTimestamp(e.getCreateTime()));
            categoryItemVo.setUpdateTime(CustomUtils.transformTimestamp(e.getUpdateTime()));
            return categoryItemVo;
        }).collect(Collectors.toList());

        CategoryItemListVo categoryItemListVo = new CategoryItemListVo();
        categoryItemListVo.setList(list);
        return categoryItemListVo;
    }
}
