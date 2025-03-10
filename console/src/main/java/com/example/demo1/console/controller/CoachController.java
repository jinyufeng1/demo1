package com.example.demo1.console.controller;

import com.example.demo1.module.domain.AddOrUpdateCoachDTO;
import com.example.demo1.module.service.CoachService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigInteger;

@RestController
public class CoachController {

    @Autowired
    private CoachService service;

    //新增教练信息
    @RequestMapping("/coach/add")
    public Boolean addCoach(@RequestParam("pics") String pics, @RequestParam("name")  String name, @RequestParam("speciality") String speciality, @RequestParam("intro")  String intro) {
        // trim to name; Due to required=false in springmvc,name can't be null at all!!!
        return service.addCoach(new AddOrUpdateCoachDTO(null, pics, name.trim(), speciality, intro));
    }

    //删除教练信息
    @RequestMapping("/coach/del")
    public Boolean delCoach(@RequestParam("id") BigInteger id) {
        return service.delCoach(id);
    }

    @RequestMapping("/coach/update")
    public Boolean updateCoach(@RequestParam(name = "id") BigInteger id,@RequestParam("pics") String pics, @RequestParam("name")  String name, @RequestParam("speciality") String speciality, @RequestParam("intro")  String intro) {
        // trim to name;
        return service.updateCoach(new AddOrUpdateCoachDTO(id, pics, name.trim(), speciality, intro));
    }
}
