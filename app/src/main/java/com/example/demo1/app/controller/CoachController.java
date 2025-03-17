package com.example.demo1.app.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.demo1.app.domain.CoachDetailsVo;
import com.example.demo1.app.domain.CoachItemListVo;
import com.example.demo1.app.domain.CoachItemVo;
import com.example.demo1.module.common.Constant;
import com.example.demo1.module.entity.Coach;
import com.example.demo1.module.service.CoachService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class CoachController {

    @Autowired
    private CoachService service;

    @RequestMapping("/coach/list")
    public CoachItemListVo getCoachList(@RequestParam("page") Integer page, @RequestParam(name = "keyword", required = false) String keyword) {
        IPage<Coach> pageList = service.getPageList(page, keyword);
        //如果没有数据，getCoachList会拿到一个空的ArrayList对象，list同样
        List<CoachItemVo> list = pageList.getRecords().stream()
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
                    return coachItemVo;
                }).collect(Collectors.toList());

        CoachItemListVo coachItemListVo = new CoachItemListVo();
        coachItemListVo.setList(list);
        coachItemListVo.setIsEnd(list.size() < Constant.PAGE_SIZE);
        return coachItemListVo;
    }

    @RequestMapping("/coach/detail")
    public CoachDetailsVo getCoachDetail(@RequestParam(name = "id") BigInteger id) {
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

        return coachDetailsVo;
    }
}
