package com.example.demo1.module.service;

import com.alibaba.fastjson.JSON;
import com.example.demo1.module.common.Constant;
import com.example.demo1.module.common.CustomUtils;
import com.example.demo1.module.domain.Block;
import com.example.demo1.module.domain.BlockType;
import com.example.demo1.module.domain.CoachItemDTO;
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

    /**
     * 作为额外的条件
     *
     * @param keyword
     * @return
     */
    private String getOrCategoryIdList(String keyword) {
        StringBuilder builder = new StringBuilder();
        categoryService.getList(keyword, null, true)
                .forEach(e -> builder.append(e.getId()).append(","));

        String orCategoryIds = builder.toString();
        // 去掉最后一个逗号
        orCategoryIds = "".equals(orCategoryIds) ? orCategoryIds : orCategoryIds.substring(0, orCategoryIds.length() - 1);

        return orCategoryIds;
    }

    public List<Coach> getPageList(int page, String keyword) {
        return mapper.getPageList((page - 1) * Constant.PAGE_SIZE, Constant.PAGE_SIZE, keyword, getOrCategoryIdList(keyword));
    }

    public List<CoachItemDTO> getPageListLinkTable2(int page, List<Long> leafCategoryIds) {
        return mapper.getPageListLinkTable2((page - 1) * Constant.PAGE_SIZE, Constant.PAGE_SIZE, leafCategoryIds);
    }

    public List<CoachItemDTO> getPageListLinkTable(int page, String keyword) {
        return mapper.getPageListLinkTable((page - 1) * Constant.PAGE_SIZE, Constant.PAGE_SIZE, keyword);
    }

    public int count(String keyword) {
        return mapper.count(keyword, getOrCategoryIdList(keyword));
    }

    public Coach getById(Long id) {
        return mapper.getById(id);
    }

    public Coach extractById(Long id) {
        return mapper.extractById(id);
    }

    public Boolean delete(Long id) {
        String position = "CoachService [public Boolean delete(Long id)]";
        if (ObjectUtils.isEmpty(getById(id))) {
            throw new RuntimeException(position + "删除失败，目标id：" + id + "不存在");
        }

        long timestamp = System.currentTimeMillis() / 1000;
        return 1 == mapper.delete(id, (int) timestamp);
    }

    public Boolean insert(Coach coach) {
        // 缺参数的问题是方法使用的问题，让用户知道程序异常就好，在程序内部打印异常信息让程序员排错
        if (null == coach) {
            throw new RuntimeException("插入失败，entity为空！");
        }

        if (null == coach.getName()) {
            throw new RuntimeException("插入失败，教练名称必填！");
        }

        if (null == coach.getCategoryId()) {
            throw new RuntimeException("插入失败，所属分类id必填！");
        }

        long timestamp = System.currentTimeMillis() / 1000;
        coach.setCreateTime((int) timestamp);
        coach.setUpdateTime((int) timestamp);
        return 1 == mapper.insert(coach);
    }

    public Boolean update(Coach coach) {
        if (null == coach) {
            throw new RuntimeException("更新失败，entity为空！");
        }

        // 失去修改的意义
        if (null == coach.getName()
                && null == coach.getPics()
                && null == coach.getSpeciality()
                && null == coach.getIntro()
                && null == coach.getCategoryId()) {
            throw new RuntimeException("更新失败，业务字段全为空！");
        }

        Long id = coach.getId();
        if (ObjectUtils.isEmpty(getById(id))) {
            throw new RuntimeException("更新失败，目标id：" + id + "不存在");
        }

        long timestamp = System.currentTimeMillis() / 1000;
        coach.setUpdateTime((int) timestamp);
        return 1 == mapper.update(coach);
    }

    /**
     * 合并 insert & update
     * @param dto
     * @return
     */
    public Long edit(EditCoachDTO dto) {
        if (ObjectUtils.isEmpty(dto)) {
            throw new RuntimeException("dto对象为空对象");
        }

        // 分类校验
        Long categoryId = dto.getCategoryId();
        if (null != categoryId && ObjectUtils.isEmpty(categoryService.getById(categoryId))) {
            throw new RuntimeException("分类id：" + categoryId + "不存在");
        }


        // 富文本类型检测
        String intro = dto.getIntro();
        if (null != intro) {
            // 如果intro不是json数组字符串，这里会抛JSONException异常，运行时异常
            List<Block> contents = JSON.parseArray(intro, Block.class);
            for (Block content : contents) {
                if(!BlockType.isArticleContentType(content.getType())){
                    throw new RuntimeException("block type is error");
                }
            }
        }

        // copy
        Coach coach = new Coach();
        BeanUtils.copyProperties(dto, coach);

        Boolean result;
        // id校验
        if (ObjectUtils.isEmpty(coach.getId())) {
            result = insert(coach);
        } else {
            result = update(coach);
        }
        return result ? coach.getId() : null;
    }

    public List<Coach> getByProperty(Coach entity) {
        // 判断entity，避免无字段条件查到整张表的数据
        if (CustomUtils.isAllFieldsNull(entity)) {
            throw new RuntimeException("entity参数为null或属性全为null");
        }
        return mapper.getByProperty(entity);
    }

    /**
     * 这是一个我想的万金油的做法
     *
     * @param entity
     * @return
     */
    public Boolean deleteByProperty(Coach entity) {
        // getByProperty已经判断entity了，这里就不用加了
        if (CollectionUtils.isEmpty(getByProperty(entity))) {
            throw new RuntimeException("对应属性的数据不存在");
        }

        long timestamp = System.currentTimeMillis() / 1000;
        return 0 < mapper.deleteByProperty(entity, (int) timestamp);
    }

}
