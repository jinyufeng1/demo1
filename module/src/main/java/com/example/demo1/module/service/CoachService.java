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

    public List<Coach> getPageList(int page, String keyword) {
        return mapper.getPageList((page - 1) * Constant.pageSize, Constant.pageSize, keyword);
    }

    public int countAll() {
        return mapper.countAll();
    }

    public Coach getById(BigInteger id) {
        return mapper.getById(id);
    }

    public Boolean insert(AddOrUpdateCoachDTO dto) {
        Coach coach = new Coach();
        BeanUtils.copyProperties(dto, coach);
        long timestamp = System.currentTimeMillis() / 1000;
        coach.setCreateTime((int)timestamp);
        coach.setUpdateTime((int)timestamp);
        return 0 != mapper.insert(coach);
    }

    public Boolean delete(BigInteger id) {
        if (ObjectUtils.isEmpty(getById(id))) {
            return false;
        }

        long timestamp = System.currentTimeMillis() / 1000;
        return 0 != mapper.delete(id, (int)timestamp);

    }

    public Boolean update(AddOrUpdateCoachDTO dto) {
        BigInteger id = dto.getId();
        if (ObjectUtils.isEmpty(getById(id))) {
            return false;
        }

        Coach coach = new Coach();
        BeanUtils.copyProperties(dto, coach);
        long timestamp = System.currentTimeMillis() / 1000;
        coach.setUpdateTime((int)timestamp);
        return 0 != mapper.update(coach);
    }
}
