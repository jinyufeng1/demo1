package com.example.demo1.module.service;

import com.example.demo1.module.domain.AddOrUpdateCoachDTO;
import com.example.demo1.module.entity.Coach;
import com.example.demo1.module.mapper.CoachMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

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

    public Integer addCoach(AddOrUpdateCoachDTO dto) {
        // trim to name
        String name = dto.getName();
        if (StringUtils.hasLength(name)) {
            dto.setName(name.trim());
        }

        Coach coach = new Coach();
        BeanUtils.copyProperties(dto, coach);
        long timestamp = System.currentTimeMillis() / 1000;
        coach.setCreateTime((int)timestamp);
        coach.setUpdateTime((int)timestamp);
        return mapper.addCoach(coach);
    }

    public Integer delCoach(BigInteger id) {
        Coach coachInfo = getCoachInfo(id);
        if (!ObjectUtils.isEmpty(coachInfo)) {
            long timestamp = System.currentTimeMillis() / 1000;
            return mapper.delCoach(id, (int)timestamp);
        }
        return 0;
    }

    public Integer updateCoach(AddOrUpdateCoachDTO dto) {
        BigInteger id = dto.getId();
        Coach coachInfo = getCoachInfo(id);
        if (!ObjectUtils.isEmpty(coachInfo)) {
            // trim to name
            String name = dto.getName();
            if (StringUtils.hasLength(name)) {
                dto.setName(name.trim());
            }
            Coach coach = new Coach();
            BeanUtils.copyProperties(dto, coach);
            long timestamp = System.currentTimeMillis() / 1000;
            coach.setUpdateTime((int)timestamp);
            return mapper.updateCoach(coach);
        }
        return 0;
    }
}
