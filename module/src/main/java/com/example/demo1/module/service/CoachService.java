package com.example.demo1.module.service;

import com.example.demo1.module.entity.Coach;
import com.example.demo1.module.mapper.CoachMapper;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.math.BigInteger;
import java.util.List;

@Service
public class CoachService {
    @Resource // 由于mybatis和spring的整合机制，可以和@Autowired注入互换
    private CoachMapper mapper;

    public List<Coach> getCoachList() {
        return mapper.getCoachList();
    }

    public Coach getCoachInfo(BigInteger id) {
        return mapper.getCoachInfo(id);
    }

    public Boolean addCoach(String pics, String name, String speciality, String intro) {
        long timestamp = System.currentTimeMillis() / 1000;
        return mapper.addCoach(pics, name, speciality, intro, (int)timestamp);
    }

    public Boolean delCoach(BigInteger id) {
        Coach coachInfo = getCoachInfo(id);
        if (!ObjectUtils.isEmpty(coachInfo)) {
            long timestamp = System.currentTimeMillis() / 1000;
            return mapper.delCoach(id, (int)timestamp);
        }
        return false;
    }

    public Boolean updateCoach(BigInteger id, String pics, String name, String speciality, String intro) {
        Coach coachInfo = getCoachInfo(id);
        if (!ObjectUtils.isEmpty(coachInfo)) {
            long timestamp = System.currentTimeMillis() / 1000;
            return mapper.updateCoach(id, pics, name, speciality, intro, (int)timestamp);
        }
        return false;
    }
}
