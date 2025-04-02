package com.example.demo1.module.service;

import com.example.demo1.module.domain.EditCategoryDTO;
import com.example.demo1.module.entity.Category;
import com.example.demo1.module.mapper.CategoryMapper;
import org.springframework.beans.BeanUtils;
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
        return 1 == mapper.delete(id, (int)timestamp);
    }

	public Boolean insert(Category entity) {
        long timestamp = System.currentTimeMillis() / 1000;
        entity.setCreateTime((int)timestamp);
        entity.setUpdateTime((int)timestamp);
        return 1 == mapper.insert(entity);
    }

    public Boolean update(Category entity) {
        Long id = entity.getId();
        if (ObjectUtils.isEmpty(getById(id))) {
            throw new RuntimeException("更新失败，目标id：" + id + "不存在");
        }

        long timestamp = System.currentTimeMillis() / 1000;
        entity.setUpdateTime((int)timestamp);
        return 1 == mapper.update(entity);
    }

    public List<Category> getList(String keyword, Set<Long> ids, Boolean limit) {
        return mapper.getList(keyword, ids, limit);
    }

    /*
    合并 insert & update
 */
    public String edit(EditCategoryDTO dto) {
        if (ObjectUtils.isEmpty(dto)) {
            throw new RuntimeException("CategoryService类，public String edit(EditCategoryDTO dto)方法拒绝处理，dto对象为空对象");
        }

        // 父级校验
        Long parentId = dto.getParentId();
        if (null != parentId) {
            Category category = this.getById(parentId);
            if (ObjectUtils.isEmpty(category)) {
                throw new RuntimeException("使用无效的parentId");
            }
            else if(null != category.getParentId()){
                throw new RuntimeException("非一级节点不能作为父节点");
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
        return result ? category.getId().toString() : null;
    }

    public Boolean deleteHierarchy(Long id) {
        if (ObjectUtils.isEmpty(getById(id))) {
            return false;
        }

        long timestamp = System.currentTimeMillis() / 1000;
        return 0 < mapper.deleteHierarchy(id, (int)timestamp);
    }
}