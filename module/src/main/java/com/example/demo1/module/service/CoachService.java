package com.example.demo1.module.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.demo1.module.common.Constant;
import com.example.demo1.module.domain.AddOrUpdateCoachDTO;
import com.example.demo1.module.entity.Coach;
import com.example.demo1.module.mapper.CoachMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.math.BigInteger;

@Service
public class CoachService {
    @Resource // 由于mybatis和spring的整合机制，可以和@Autowired注入互换
    private CoachMapper mapper;

    public IPage<Coach> getPageList(int page, String keyword) {
        return mapper.selectPage(
                new Page<>(page, Constant.PAGE_SIZE),
                Wrappers.<Coach>lambdaQuery().like(null != keyword, Coach::getName, keyword)
        );
    }

    public Coach getById(BigInteger id) {
        return mapper.selectById(id);
    }

    public Coach extractById(BigInteger id) {
        return mapper.extractById(id);
    }


    public Boolean insert(AddOrUpdateCoachDTO dto) {
        Coach coach = new Coach();
        BeanUtils.copyProperties(dto, coach);
        return 0 < mapper.insert(coach);
    }

    public Boolean delete(BigInteger id) {
        Coach entity = getById(id);
        if (ObjectUtils.isEmpty(entity)) {
            return false;
        }

        return 0 < mapper.customRemoveById(id);
    }

    public Boolean update(AddOrUpdateCoachDTO dto) {
        BigInteger id = dto.getId();
        if (ObjectUtils.isEmpty(getById(id))) {
            return false;
        }

        Coach coach = new Coach();
        BeanUtils.copyProperties(dto, coach);
        return 0 < mapper.updateById(coach);
    }
}
