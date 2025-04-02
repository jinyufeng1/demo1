package com.example.demo1.module.service;

import com.example.demo1.module.common.Constant;
import com.example.demo1.module.common.CustomUtils;
import com.example.demo1.module.domain.EditCoachDTO;
import com.example.demo1.module.entity.Coach;
import com.example.demo1.module.mapper.CoachMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.util.List;
@Slf4j
@Service
public class CoachService {
    @Autowired
    private CategoryService categoryService; // 谁依赖谁，谁就引入谁，一对多，多引一
    @Resource // 由于mybatis和spring的整合机制，可以和@Autowired注入互换
    private CoachMapper mapper;

    public List<Coach> getPageList(int page, String keyword, String orCategoryIds) {
        return mapper.getPageList((page - 1) * Constant.PAGE_SIZE, Constant.PAGE_SIZE, keyword, orCategoryIds);
    }

    public int count(String keyword, String orCategoryIds) {
        return mapper.count(keyword, orCategoryIds);
    }

    public Coach getById(Long id) {
        return mapper.getById(id);
    }

    public Coach extractById(Long id) {
        return mapper.extractById(id);
    }

    public Boolean delete(Long id) {
        if (ObjectUtils.isEmpty(getById(id))) {
            return false;
        }

        long timestamp = System.currentTimeMillis() / 1000;
        return 1 == mapper.delete(id, (int)timestamp);
    }

    public Boolean insert(Coach coach) {
        long timestamp = System.currentTimeMillis() / 1000;
        coach.setCreateTime((int)timestamp);
        coach.setUpdateTime((int)timestamp);
        return 1 == mapper.insert(coach);
    }

    public Boolean update(Coach coach) {
        Long id = coach.getId();
        if (ObjectUtils.isEmpty(getById(id))) {
            throw new RuntimeException("更新失败，目标id：" + id + "不存在");
        }

        long timestamp = System.currentTimeMillis() / 1000;
        coach.setUpdateTime((int)timestamp);
        return 1 == mapper.update(coach);
    }

    /*
        合并 insert & update
     */
    public String edit(EditCoachDTO dto) {
        if (ObjectUtils.isEmpty(dto)) {
            throw new RuntimeException("CoachService类，public String edit(EditCoachDTO dto)方法拒绝处理，dto对象为空对象");
        }

        // 分类校验
        Long categoryId = dto.getCategoryId();
        if ( null != categoryId && ObjectUtils.isEmpty( categoryService.getById(categoryId) ) ) {
            throw new RuntimeException("使用无效的categoryId");
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
        return result ? coach.getId().toString() : null;
    }

    public List<Coach> getByProperty(Coach entity) {
        // 判断entity，避免无字段条件查到整张表的数据
        if (CustomUtils.isAllFieldsNull(entity)) {
            log.warn("不合理的使用CoachService.getByProperty(Coach entity)，entity参数为null或属性全为null！");
            return null;
        }
        return mapper.getByProperty(entity);
    }

    /**
     * 这是一个我想的万金油的做法
     * @param entity
     * @return
     */
    public Boolean deleteByProperty(Coach entity) {
        // getByProperty已经判断entity了，这里就不用加了
        if (CollectionUtils.isEmpty(getByProperty(entity))) {
            return false;
        }

        long timestamp = System.currentTimeMillis() / 1000;
        return 0 < mapper.deleteByProperty(entity, (int)timestamp);
    }

}
