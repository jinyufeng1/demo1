package com.example.demo1.module.service;

import com.example.demo1.module.domain.EditCategoryDTO;
import com.example.demo1.module.entity.Category;
import com.example.demo1.module.mapper.CategoryMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.util.Collections;
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
@Slf4j
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
        String position = "CategoryService [public Boolean delete(Long id)]";
        if (ObjectUtils.isEmpty(getById(id))) {
            log.info(position + "删除失败，目标id：{}不存在", id);
            return false;
        }

        long timestamp = System.currentTimeMillis() / 1000;
        return 1 == mapper.delete(id, (int)timestamp);
    }

	public Boolean insert(Category entity) {
        String position = "CategoryService [public Boolean insert(Category entity)]";
        if (null == entity) {
            log.error(position + "插入失败，entity为空！");
            return false;
        }

        if (null == entity.getName()) {
            log.error(position + "插入失败，分类名称必填！");
            return false;
        }

        long timestamp = System.currentTimeMillis() / 1000;
        entity.setCreateTime((int)timestamp);
        entity.setUpdateTime((int)timestamp);
        return 1 == mapper.insert(entity);
    }

    public Boolean update(Category entity) {
        String position = "CategoryService [public Boolean update(Category entity)]";
        if (null == entity) {
            log.error(position + "更新失败，entity为空！");
            return false;
        }

        Long id = entity.getId();
        if (ObjectUtils.isEmpty(getById(id))) {
            log.error(position + "更新失败，目标id：{}不存在", id);
            return false;
        }

        // 失去修改的意义
        if (null == entity.getName()
                && null == entity.getPic()
                && null == entity.getParentId()) {
            log.error(position + "更新失败，业务字段全为空");
            return false;
        }

        long timestamp = System.currentTimeMillis() / 1000;
        entity.setUpdateTime((int)timestamp);
        return 1 == mapper.update(entity);
    }

    public List<Category> getList(String keyword, Set<Long> ids, Boolean limit) {
        return mapper.getList(keyword, ids, limit);
    }

    public List<Category> getListByParent(String keyword, List<Long> parentIds) {
        return mapper.getListByParent(keyword, parentIds);
    }

    public List<Category> getFirstList(String keyword) {
        return mapper.getFirstList(keyword);
    }

    /**
     * 合并 insert & update
     * @param dto
     * @return
     */
    public Long edit(EditCategoryDTO dto) {
        String position = "CategoryService [public Long edit(EditCategoryDTO dto)]";
        if (ObjectUtils.isEmpty(dto)) {
            log.error(position + "dto对象为空对象");
            return null;
        }

        // 父级校验
        Long parentId = dto.getParentId();
        if (null != parentId) {
            Category category = this.getById(parentId);
            if (ObjectUtils.isEmpty(category)) {
                log.error(position + "使用无效的parentId");
                return null;
            }
        }

        // copy
        Category category = new Category();
        BeanUtils.copyProperties(dto, category);
        Boolean result;
        // id校验
        if (ObjectUtils.isEmpty(category.getId())) {
            result = insert(category);
        }
        else {
            result = update(category);
        }
        return result ? category.getId() : null;
    }

    public Boolean deleteHierarchy(Long id) {
        String position = "CategoryService [public Boolean deleteHierarchy(Long id)]";
        if (ObjectUtils.isEmpty(getById(id))) {
            log.info(position + "删除失败，目标id：{}不存在", id);
            return false;
        }

        long timestamp = System.currentTimeMillis() / 1000;
        return 0 < mapper.deleteHierarchy(id, (int)timestamp);
    }

    public void collectLeafItemIds(Long parentId, List<Long> leafItemIds) {
        List<Category> children = getListByParent(null , Collections.singletonList(parentId));

        // 判断是否为叶子节点
        if (0 != children.size()) {
            for (Category child : children) {
                collectLeafItemIds(child.getId(), leafItemIds);
            }
        }
        else {
            leafItemIds.add(parentId);
        }
    }
}