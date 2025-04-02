package com.example.demo1.app.controller;

import com.example.demo1.app.domain.*;
import com.example.demo1.module.common.Constant;
import com.example.demo1.module.entity.Category;
import com.example.demo1.module.entity.Coach;
import com.example.demo1.module.service.CategoryService;
import com.example.demo1.module.service.CoachService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;
import java.util.stream.Collectors;

@RestController
public class CoachController {

    @Autowired
    private CoachService coachService;

    @Autowired
    private CategoryService categoryService;

    @RequestMapping("/coach/list")
    public CoachItemListVo getCoachList(@RequestParam("page") Integer page, @RequestParam(name = "keyword", required = false) String keyword) {
        CoachItemListVo coachItemListVo = new CoachItemListVo();
        // 作为额外的条件
        List<Long> orCategoryIdList = categoryService.getList(keyword, null, true).stream().map(Category::getId).collect(Collectors.toList());
        String orCategoryIds = "";
        if (!orCategoryIdList.isEmpty()) {
            StringBuilder builder = new StringBuilder();
            for (Long aLong : orCategoryIdList) {
                builder.append(aLong).append(",");
            }
            orCategoryIds = builder.toString();
            // 去掉最后一个逗号
            orCategoryIds = orCategoryIds.substring(0, orCategoryIds.length() - 1);
        }

        // 如果没有数据，getCoachList会拿到一个空的ArrayList对象
        List<Coach> pageList = coachService.getPageList(page, keyword, orCategoryIds);
        if (pageList.isEmpty()) {
            coachItemListVo.setList(new ArrayList<>());
            coachItemListVo.setIsEnd(true);
            return coachItemListVo;
        }

        // 没有取全表，而是根据id进行in条件查询
        Set<Long> categoryIds = pageList.stream().map(Coach::getCategoryId).collect(Collectors.toSet());

        // 获取分类映射列表
        Map<Long, String> categoryMap = categoryService.getList(null, categoryIds, true).stream().collect(Collectors.toMap(Category::getId, Category::getName));

        // vo就是再controller层做转换
        List<CoachItemVo> list = pageList.stream()
                .map(e -> {
                    CoachItemVo coachItemVo = new CoachItemVo();
                    coachItemVo.setId(e.getId());
                    coachItemVo.setName(e.getName());
                    String pics = e.getPics();
                    //不需要判断是否包含split参数，没有就不切
                    String pic = StringUtils.hasLength(pics) ? pics.split(Constant.PIC_SPLIT)[0] : null;
                    coachItemVo.setPic(pic);
                    coachItemVo.setSpeciality(e.getSpeciality());
                    coachItemVo.setCategory(categoryMap.get(e.getCategoryId()));
                    return coachItemVo;
                }).collect(Collectors.toList());
        coachItemListVo.setList(list);

        // 判断是否有分类数据取空的情况
        if (list.stream().anyMatch(e -> null == e.getCategory())) {
            // 过滤掉category为空的实体元素
            coachItemListVo.setList(list.stream().filter(e -> null != e.getCategory()).collect(Collectors.toList()));
        }

        coachItemListVo.setIsEnd(list.size() < Constant.PAGE_SIZE);
        return coachItemListVo;
    }

    @RequestMapping("/coach/detail")
    public CoachDetailsVo getCoachDetail(@RequestParam(name = "id") Long id) {
        CoachDetailsVo coachDetailsVo = new CoachDetailsVo();
        Coach coachInfo = coachService.getById(id);
        //自己写方法判断
        if (ObjectUtils.isEmpty(coachInfo)) {
            return coachDetailsVo;
        }

        // 获取分类信息
        Category category = categoryService.getById(coachInfo.getCategoryId());
        if (ObjectUtils.isEmpty(category)) {
            return coachDetailsVo;
        }

        coachDetailsVo.setIntro(coachInfo.getIntro());
        String pics = coachInfo.getPics();
        if (StringUtils.hasLength(pics)) {
            //不需要判断是否包含split参数，没有就不切
            String[] split = pics.split(Constant.PIC_SPLIT);
            coachDetailsVo.setPics(Arrays.asList(split));
        }

        coachDetailsVo.setCategory(category.getName());
        coachDetailsVo.setIcon(category.getPic());

        return coachDetailsVo;
    }
}
