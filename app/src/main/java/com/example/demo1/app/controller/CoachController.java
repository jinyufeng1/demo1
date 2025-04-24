package com.example.demo1.app.controller;

import com.alibaba.fastjson.JSON;
import com.example.demo1.app.domain.CoachDetailsVo;
import com.example.demo1.app.domain.CoachItemListVo;
import com.example.demo1.app.domain.CoachItemVo;
import com.example.demo1.app.domain.WpVo;
import com.example.demo1.module.common.Constant;
import com.example.demo1.module.common.CustomUtils;
import com.example.demo1.module.common.Response;
import com.example.demo1.module.entity.Category;
import com.example.demo1.module.entity.Coach;
import com.example.demo1.module.service.CategoryService;
import com.example.demo1.module.service.CoachService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@RestController
public class CoachController {

    @Autowired
    private CoachService coachService;

    @Autowired
    private CategoryService categoryService;

    @RequestMapping("/coach/list")
    public Response<CoachItemListVo> getCoachList(@RequestParam(name = "wp", required = false) String wp,
                                                 @RequestParam(name = "keyword", required = false) String keyword) {

        WpVo wpVo = null;
        if (StringUtils.hasLength(wp)) {
            //Base64解码
            String decode = new String(Base64.getUrlDecoder().decode(wp));
            //获取json转实体
            wpVo = JSON.parseObject(decode, WpVo.class);
        }

        CoachItemListVo coachItemListVo = new CoachItemListVo();
        // 如果没有数据，getCoachList会拿到一个空的ArrayList对象
        List<Coach> pageList = coachService.getPageList(null == wpVo ? 1 : wpVo.getPage(), null == wpVo ? keyword : wpVo.getKeyword());
        if (pageList.isEmpty()) {
            coachItemListVo.setIsEnd(true);
            return new Response<>(1001, coachItemListVo);
        }

        // 没有取全表，而是根据id进行in条件查询
        Set<Long> categoryIds = pageList.stream().map(Coach::getCategoryId).collect(Collectors.toSet());

        // 获取分类映射列表
        Map<Long, String> categoryMap = categoryService.getList(null, categoryIds, null).stream().collect(Collectors.toMap(Category::getId, Category::getName));

        // vo就是再controller层做转换
        List<CoachItemVo> list = new ArrayList<>();
        for (Coach coach : pageList) {
            String category = categoryMap.get(coach.getCategoryId());
            if (null == category) {
                continue;
            }

            CoachItemVo coachItemVo = new CoachItemVo();
            coachItemVo.setCategory(category);
            BeanUtils.copyProperties(coach, coachItemVo);
            String pics = coach.getPics();
            //不需要判断是否包含split参数，没有就不切
            String pic = StringUtils.hasLength(pics) ? pics.split(Constant.PIC_SPLIT)[0] : null;
            coachItemVo.setPic(com.example.demo1.app.common.CustomUtils.transformObj(pic));
            list.add(coachItemVo);
        }
        coachItemListVo.setList(list);
        coachItemListVo.setIsEnd(list.size() < Constant.PAGE_SIZE);

        // 构建下一页需要的wp
        if (null == wpVo) {
            //记录第一次进入接口的时间
            wpVo = new WpVo(2, keyword, CustomUtils.transformTimestamp(System.currentTimeMillis(), Constant.DATE_PATTERN_1), null);
        }
        else {
            wpVo.setPage(wpVo.getPage() + 1);
        }

        // 实体转json
        String jsonString = JSON.toJSONString(wpVo);
        // Base64编码
        String wpString = Base64.getUrlEncoder().encodeToString(jsonString.getBytes());
        coachItemListVo.setWp(wpString);

        return new Response<>(1001, coachItemListVo);
    }

    @RequestMapping("/coach/list2")
    public Response<CoachItemListVo> getCoachList2(@RequestParam(name = "wp", required = false) String wp,
                                         @RequestParam(name = "keyword", required = false) String keyword) {
        WpVo wpVo = null;
        if (StringUtils.hasLength(wp)) {
            //Base64解码
            String decode = new String(Base64.getUrlDecoder().decode(wp));
            //获取json转实体
            wpVo = JSON.parseObject(decode, WpVo.class);
        }

        CoachItemListVo coachItemListVo = new CoachItemListVo();
        List<CoachItemVo> list = coachService.getPageListLinkTable(null == wpVo ? 1 : wpVo.getPage(), null == wpVo ? keyword : wpVo.getKeyword())
                .stream()
                .map(e -> {
                    CoachItemVo coachItemVo = new CoachItemVo();
                    BeanUtils.copyProperties(e, coachItemVo);
                    String pics = e.getPics();
                    //不需要判断是否包含split参数，没有就不切
                    String pic = StringUtils.hasLength(pics) ? pics.split(Constant.PIC_SPLIT)[0] : null;
                    coachItemVo.setPic(com.example.demo1.app.common.CustomUtils.transformObj(pic));
                    return coachItemVo;
                }).collect(Collectors.toList());
        coachItemListVo.setList(list);
        coachItemListVo.setIsEnd(list.size() < Constant.PAGE_SIZE);

        // 构建下一页需要的wp
        if (null == wpVo) {
            //记录第一次进入接口的时间
            wpVo = new WpVo(2, keyword, CustomUtils.transformTimestamp(System.currentTimeMillis(), Constant.DATE_PATTERN_1), null);
        }
        else {
            wpVo.setPage(wpVo.getPage() + 1);
        }

        // 实体转json
        String jsonString = JSON.toJSONString(wpVo);
        // Base64编码
        String wpString = Base64.getUrlEncoder().encodeToString(jsonString.getBytes());
        coachItemListVo.setWp(wpString);

        return new Response<>(1001, coachItemListVo);
    }

    @RequestMapping("/coach/detail")
    public Response<CoachDetailsVo> getCoachDetail(@RequestParam(name = "id") Long id) {
        CoachDetailsVo coachDetailsVo = new CoachDetailsVo();
        Coach coachInfo = coachService.getById(id);
        //自己写方法判断
        if (ObjectUtils.isEmpty(coachInfo)) {
            // 不存在不是错误的，用info
            log.info("教练id：{}不存在", id);
            return new Response<>(1001, coachDetailsVo);
        }

        Long categoryId = coachInfo.getCategoryId();
        // 获取分类信息
        Category category = categoryService.getById(categoryId);
        if (ObjectUtils.isEmpty(category)) {
            log.info("分类id：{}不存在", categoryId);
            return new Response<>(1001, coachDetailsVo);
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

        return new Response<>(1001, coachDetailsVo);
    }
}
