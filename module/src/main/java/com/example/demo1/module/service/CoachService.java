package com.example.demo1.module.service;

import com.example.demo1.module.common.Constant;
import com.example.demo1.module.domain.AddOrUpdateCoachDTO;
import com.example.demo1.module.entity.Coach;
import com.example.demo1.module.mapper.CoachMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.math.BigInteger;
import java.util.List;

@Service
public class CoachService {
    @Resource // 由于mybatis和spring的整合机制，可以和@Autowired注入互换
    private CoachMapper mapper;

    public List<Coach> getCoachList(int page, int pageSize) {
        return mapper.getCoachList((page - 1) * Constant.pageSize, pageSize);
    }

    public int getCoachTotal() {
        return mapper.getCoachCount();
    }

    public Coach getCoachInfo(BigInteger id) {
        return mapper.getCoachInfo(id);
    }

    public Boolean addCoach(AddOrUpdateCoachDTO dto) {
        Coach coach = new Coach();
        BeanUtils.copyProperties(dto, coach);
        long timestamp = System.currentTimeMillis() / 1000;
        coach.setCreateTime((int)timestamp);
        coach.setUpdateTime((int)timestamp);
        return 0 != mapper.addCoach(coach);
    }

    public Boolean delCoach(BigInteger id) {
        if (ObjectUtils.isEmpty(getCoachInfo(id))) {
            return false;
        }

        long timestamp = System.currentTimeMillis() / 1000;
        return 0 != mapper.delCoach(id, (int)timestamp);

    }

    public Boolean updateCoach(AddOrUpdateCoachDTO dto) {
        BigInteger id = dto.getId();
        if (ObjectUtils.isEmpty(getCoachInfo(id))) {
            return false;
        }

        Coach coach = new Coach();
        BeanUtils.copyProperties(dto, coach);
        long timestamp = System.currentTimeMillis() / 1000;
        coach.setUpdateTime((int)timestamp);
        return 0 != mapper.updateCoach(coach);
    }
}
