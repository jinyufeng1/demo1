package com.example.demo1.module.service;

import com.example.demo1.module.entity.Category;
import com.example.demo1.module.mapper.CategoryMapper;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import javax.annotation.Resource;
import java.util.List;
import java.util.Set;

/**
 * <p>
 * 分类信息表 服务类
 * </p>
 *
 * @author 我叫小呆呆
 * @since 2025-03-30
 */
@Service
public class CategoryService {

    @Resource
    private CategoryMapper mapper;

//    **************************五大基础方法**************************
	public Category getById(Long id) {
        return mapper.getById(id);
    }

    public Category extractById(Long id) {
        return mapper.extractById(id);
    }

    public Boolean delete(Long id) {
        if (ObjectUtils.isEmpty(getById(id))) {
            return false;
        }

        long timestamp = System.currentTimeMillis() / 1000;
        return 0 < mapper.delete(id, (int)timestamp);
    }

	public Boolean insert(Category entity) {
        long timestamp = System.currentTimeMillis() / 1000;
        entity.setCreateTime((int)timestamp);
        entity.setUpdateTime((int)timestamp);
        return 0 < mapper.insert(entity);
    }

    public Boolean update(Category entity) {
        Long id = entity.getId();
        if (ObjectUtils.isEmpty(getById(id))) {
            throw new RuntimeException("更新失败，目标id：" + id + "不存在");
        }

        long timestamp = System.currentTimeMillis() / 1000;
        entity.setUpdateTime((int)timestamp);
        return 0 < mapper.update(entity);
    }

    public List<Category> getList(String keyword, Set<Long> ids) {
        return mapper.getList(keyword, ids);
    }
}