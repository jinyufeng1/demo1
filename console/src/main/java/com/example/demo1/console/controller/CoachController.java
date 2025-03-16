package com.example.demo1.console.controller;

import com.example.demo1.console.domain.CoachDetailsVo;
import com.example.demo1.console.domain.CoachItemListVo;
import com.example.demo1.console.domain.CoachItemVo;
import com.example.demo1.module.common.Constant;
import com.example.demo1.module.common.CustomUtils;
import com.example.demo1.module.domain.AddOrUpdateCoachDTO;
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
public class CoachController {

    @Autowired
    private CoachService service;

    //新增教练信息
    @RequestMapping("/coach/add")
    public Boolean addCoach(@RequestParam("pics") String pics, @RequestParam("name")  String name, @RequestParam("speciality") String speciality, @RequestParam("intro")  String intro) {
        // trim to name; Due to required=false in springmvc,name can't be null at all!!!
        return service.insert(new AddOrUpdateCoachDTO(null, pics, name.trim(), speciality, intro));
    }

    //删除教练信息
    @RequestMapping("/coach/del")
    public Boolean delCoach(@RequestParam("id") BigInteger id) {
        return service.delete(id);
    }

    @RequestMapping("/coach/update")
    public Boolean updateCoach(@RequestParam(name = "id") BigInteger id,@RequestParam("pics") String pics, @RequestParam("name")  String name, @RequestParam("speciality") String speciality, @RequestParam("intro")  String intro) {
        // trim to name;
        return service.update(new AddOrUpdateCoachDTO(id, pics, name.trim(), speciality, intro));
    }

    @RequestMapping("/coach/list")
    public CoachItemListVo getCoachList(@RequestParam("page") Integer page, @RequestParam(name = "keyword", required = false) String keyword) {
        CoachItemListVo coachItemListVo = new CoachItemListVo();
        coachItemListVo.setPageSize(Constant.pageSize);

        int coachTotal = service.countAll();
        coachItemListVo.setTotal(coachTotal);
        // 总是都为0就不用查了，节约数据库访问
        if (0 == coachTotal) {
            coachItemListVo.setList(new ArrayList<>());
            return coachItemListVo;
        }

        //如果没有数据，getCoachList会拿到一个空的ArrayList对象，list同样
        List<CoachItemVo> list = service.getCoachList(page, keyword, Constant.pageSize).stream()
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
        coachItemListVo.setList(list);

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
        Integer createTime = coachInfo.getCreateTime();
        if (!ObjectUtils.isEmpty(createTime)) {
            coachDetailsVo.setCreateTime(CustomUtils.transformTimestamp(createTime));
        }

        Integer updateTime = coachInfo.getUpdateTime();
        if (!ObjectUtils.isEmpty(updateTime)) {
            coachDetailsVo.setUpdateTime(CustomUtils.transformTimestamp(updateTime));
        }

        return coachDetailsVo;
    }
}
