package com.example.demo1.module.service;

import com.example.demo1.module.entity.RelationTagCoach;
import com.example.demo1.module.entity.Tag;
import com.example.demo1.module.mapper.RelationTagCoachMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 教练标签关联表 服务类
 * </p>
 *
 * @author 我叫小呆呆
 * @since 2025-04-28
 */
@Service
public class RelationTagCoachService {

    @Resource
    private RelationTagCoachMapper mapper;
    public List<Tag> getTagByCoachId(Long coachId) {
        return mapper.getTagByCoachId(coachId);
    }


//    **************************五大基础方法**************************
	public RelationTagCoach getById(Long id) {
        if (null == id) {
            throw new RuntimeException("查询失败，id为空！");
        }

        return mapper.getById(id);
    }

    public RelationTagCoach extractById(Long id) {
        if (null == id) {
            throw new RuntimeException("查询失败，id为空！");
        }

        return mapper.extractById(id);
    }

    public Boolean delete(Long id) {
        if (null == id) {
            throw new RuntimeException("软删除失败，id为空！");
        }

        long timestamp = System.currentTimeMillis() / 1000;
        return 1 == mapper.delete(id, (int)timestamp);
    }

	public Boolean insert(RelationTagCoach entity) {
        if (null == entity) {
            throw new RuntimeException("插入失败，entity为空！");
        }

        if (null == entity.getTagId()) {
            throw new RuntimeException("插入失败，tagId必填！");
        }

        if (null == entity.getCoachId()) {
            throw new RuntimeException("插入失败，coachId必填！");
        }

        long timestamp = System.currentTimeMillis() / 1000;
        entity.setCreateTime((int)timestamp);
        entity.setUpdateTime((int)timestamp);
        return 1 == mapper.insert(entity);
    }

    public Boolean update(RelationTagCoach entity) {
        if (null == entity) {
            throw new RuntimeException("更新失败，entity为空！");
        }

        if (null == entity.getId()) {
            throw new RuntimeException("更新失败，id为空！");
        }

        // 失去修改的意义
        if (null == entity.getCoachId() && null == entity.getTagId()) {
            throw new RuntimeException("更新失败，业务字段全为空！");
        }

        long timestamp = System.currentTimeMillis() / 1000;
        entity.setUpdateTime((int)timestamp);
        return 1 == mapper.update(entity);
    }

    public Boolean deleteByCoachId(Long coachId, List<Long> delTagIds) {
        if (null == coachId) {
            throw new RuntimeException("软删除失败，coachId为空！");
        }

        long timestamp = System.currentTimeMillis() / 1000;
        return 1 <= mapper.deleteByCoachId(coachId, delTagIds, (int)timestamp);
    }
}