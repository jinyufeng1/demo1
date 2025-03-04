package com.example.demo1.app.controller;

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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class AppController {

    @Autowired
    private CoachService service;

    @RequestMapping("/coach/list")
    public CoachItemListVo getCoachList() {
        CoachItemListVo coachItemListVo = new CoachItemListVo();

        List<Coach> coachList = service.getCoachList();
        if (coachList.isEmpty()) {
            return coachItemListVo;
        }

        //如果将vo放到每个端，在controller里就要做这种转换，总觉得不太合适，怎么调整？
        List<CoachItemVo> list = coachList.stream()
                .map(e -> {
                    CoachItemVo coachItemVo = new CoachItemVo();
                    coachItemVo.setId(e.getId());
                    coachItemVo.setName(e.getName());
                    String pics = e.getPics();
                    String pic = StringUtils.hasLength(pics) ? (pics.contains(Constant.PIC_SPLIT) ? pics.split("\\" + Constant.PIC_SPLIT)[0] : pics) : null;
                    coachItemVo.setPic(pic);
                    coachItemVo.setSpeciality(e.getSpeciality());
                    return coachItemVo;
                }).collect(Collectors.toList());


        coachItemListVo.setList(list);
        return coachItemListVo;
    }

    @RequestMapping("/coach/detail")
    public CoachDetailsVo getCoachDetail(@RequestParam(name = "id") BigInteger id) {
        CoachDetailsVo coachDetailsVo = new CoachDetailsVo();
        Coach coachInfo = service.getCoachInfo(id);
        if (ObjectUtils.isEmpty(coachInfo)) {
            return null;
        }

        coachDetailsVo.setIntro(coachInfo.getIntro());
        String pics = coachInfo.getPics();
        if (StringUtils.hasLength(pics)) {
            List<String> picList = new ArrayList<>();
            if (pics.contains(Constant.PIC_SPLIT)) {
                String[] split = pics.split("\\" + Constant.PIC_SPLIT);
                picList.addAll(Arrays.asList(split));
            }
            else {
                picList.add(pics);
            }
            coachDetailsVo.setPics(picList);
        }

        return coachDetailsVo;
    }
}
