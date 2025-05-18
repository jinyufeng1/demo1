package com.example.demo1.console.domain;

import com.example.demo1.module.common.Constant;
import com.example.demo1.module.common.CustomUtils;
import com.example.demo1.module.entity.Category;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Data
public class CategoryItemListVo {
    private List<CategoryItemVo> list;

    private static CategoryItemVo shift(Category category, Map<Long, List<Category>> map) {
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

    public static CategoryItemListVo getCategoryTree(List<Category> list) {
        CategoryItemListVo categoryItemListVo = new CategoryItemListVo();
        if (list.isEmpty()) {
            log.info("无分类信息");
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
            log.info("无一级分类信息");
            return categoryItemListVo;
        }

        //构建层级列表
        List<CategoryItemVo> retList = firstList.stream().map(e -> shift(e, map)).collect(Collectors.toList());

        categoryItemListVo.setList(retList);
        return categoryItemListVo;
    }
}
