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
        return mapper.getPageList((page - 1) * Constant.PAGE_SIZE, Constant.PAGE_SIZE, keyword);
    }

    public int count(String keyword) {
        return mapper.count(keyword);
    }

    public Coach getById(BigInteger id) {
        return mapper.getById(id);
    }

    public Coach extractById(BigInteger id) {
        return mapper.extractById(id);
    }

    private Boolean insert(Coach coach) {
        long timestamp = System.currentTimeMillis() / 1000;
        coach.setCreateTime((int)timestamp);
        coach.setUpdateTime((int)timestamp);
        return 0 < mapper.insert(coach);
    }

    public Boolean delete(BigInteger id) {
        if (ObjectUtils.isEmpty(getById(id))) {
            return false;
        }

        long timestamp = System.currentTimeMillis() / 1000;
        return 0 < mapper.delete(id, (int)timestamp);
    }

    private Boolean update(Coach coach) {
        BigInteger id = coach.getId();
        if (ObjectUtils.isEmpty(getById(id))) {
            throw new RuntimeException("更新失败，目标id：" + id + "不存在");
        }

        long timestamp = System.currentTimeMillis() / 1000;
        coach.setUpdateTime((int)timestamp);
        return 0 < mapper.update(coach);
    }

    /*
        合并 insert & update
        id校验
        返回值为id
     */
    public BigInteger edit(AddOrUpdateCoachDTO dto) {
        if (ObjectUtils.isEmpty(dto)) {
            throw new RuntimeException("CoachService类，public BigInteger edit(AddOrUpdateCoachDTO dto)方法拒绝处理，dto对象为空对象");
        }

        // copy
        Coach coach = new Coach();
        BeanUtils.copyProperties(dto, coach);

        Boolean result;
        // id校验
        if (ObjectUtils.isEmpty(coach.getId())) {
            result = insert(coach);
        }
        else {
            result = update(coach);
        }
        return result ? coach.getId() : null;
    }
}
