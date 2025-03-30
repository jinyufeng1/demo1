package com.example.demo1.console.controller;

import com.example.demo1.console.domain.CoachDetailsVo;
import com.example.demo1.console.domain.CoachItemListVo;
import com.example.demo1.console.domain.CoachItemVo;
import com.example.demo1.module.common.Constant;
import com.example.demo1.module.common.CustomUtils;
import com.example.demo1.module.domain.AddOrUpdateCoachDTO;
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
    private CoachService service;

    @Autowired
    private CategoryService categoryService;

    //新增教练信息
    @RequestMapping("/coach/add")
    public String addCoach(@RequestParam(name = "pics",required = false) String pics,
                           @RequestParam("name") String name,
                           @RequestParam("categoryId") Long categoryId,
                           @RequestParam(name = "speciality", required = false) String speciality,
                           @RequestParam(name = "intro", required = false) String intro) {
        try {
            // 分类校验
            if (ObjectUtils.isEmpty(categoryService.getById(categoryId))) {
                throw new RuntimeException("使用无效的categoryId");
            }

            // trim to name; Due to required=false in springmvc,name can't be null at all!!!
            return service.edit(new AddOrUpdateCoachDTO(null, pics, name.trim(), speciality, intro, categoryId));
        }
        catch (RuntimeException e) {
            e.printStackTrace();
            // 返回异常信息
            return "add接口捕获异常信息：" + e.getMessage();
        }
    }

    //删除教练信息
    @RequestMapping("/coach/del")
    public Boolean delCoach(@RequestParam("id") Long id) {
        return service.delete(id);
    }

    //修改教练信息
    @RequestMapping("/coach/update")
    public String updateCoach(@RequestParam(name = "id") Long id,
                              @RequestParam(name = "categoryId", required = false) Long categoryId,
                              @RequestParam(name = "pics",required = false) String pics,
                              @RequestParam(name = "name",required = false) String name,
                              @RequestParam(name = "speciality",required = false) String speciality,
                              @RequestParam(name = "intro",required = false) String intro) {
        try {
            // 分类校验
            if (null != categoryId) {
                if (ObjectUtils.isEmpty(categoryService.getById(categoryId))) {
                    throw new RuntimeException("使用无效的categoryId");
                }
            }

            // trim to name;
            return service.edit(new AddOrUpdateCoachDTO(id, pics, name.trim(), speciality, intro, categoryId));
        }
        catch (RuntimeException e) {
            e.printStackTrace();
            // 返回异常信息
            return "upate接口捕获异常信息：" + e.getMessage();
        }
    }

    @RequestMapping("/coach/list")
    public CoachItemListVo getCoachList(@RequestParam("page") Integer page,
                                        @RequestParam(name = "keyword", required = false) String keyword) {
        CoachItemListVo coachItemListVo = new CoachItemListVo();
        coachItemListVo.setPageSize(Constant.PAGE_SIZE);

        int coachTotal = service.count(keyword);
        coachItemListVo.setTotal(coachTotal);
        // 总是都为0就不用查了，节约数据库访问
        if (0 == coachTotal) {
            coachItemListVo.setList(new ArrayList<>());
            return coachItemListVo;
        }

        //如果没有数据，getCoachList会拿到一个空的ArrayList对象
        List<Coach> pageList = service.getPageList(page, keyword);

        // 没有取全表，而是根据id进行in条件查询
        Set<Long> categoryIds = pageList.stream().map(Coach::getCategoryId).collect(Collectors.toSet());

        // 获取分类映射列表
        Map<Long, String> categoryMap = categoryService.getList(null, categoryIds).stream().collect(Collectors.toMap(Category::getId, Category::getName));

        List<CoachItemVo> list = pageList.stream()
                .map(e -> {
                    // vo就是再controller层做转换
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

        return coachItemListVo;
    }

    @RequestMapping("/coach/detail")
    public CoachDetailsVo getCoachDetail(@RequestParam(name = "id") Long id) {
        CoachDetailsVo coachDetailsVo = new CoachDetailsVo();
        Coach coachInfo = service.getById(id);
        //自己写方法判断
        if (ObjectUtils.isEmpty(coachInfo)) {
            return coachDetailsVo;
        }

        coachDetailsVo.setIntro(coachInfo.getIntro());
        String pics = coachInfo.getPics();
        if (StringUtils.hasLength(pics)) {
            //不需要判断是否包含split参数，没有就不切
            String[] split = pics.split(Constant.PIC_SPLIT);
            coachDetailsVo.setPics(Arrays.asList(split));
        }

        // 获取分类信息
        Category category = categoryService.getById(coachInfo.getCategoryId());
        if (!ObjectUtils.isEmpty(category)) {
            coachDetailsVo.setCategory(category.getName());
            coachDetailsVo.setIcon(category.getPic());
        }

        coachDetailsVo.setCreateTime(CustomUtils.transformTimestamp(coachInfo.getCreateTime()));
        coachDetailsVo.setUpdateTime(CustomUtils.transformTimestamp(coachInfo.getUpdateTime()));
        return coachDetailsVo;
    }
}
