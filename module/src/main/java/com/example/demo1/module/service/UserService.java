package com.example.demo1.module.service;

import com.example.demo1.module.common.CustomUtils;
import com.example.demo1.module.entity.User;
import com.example.demo1.module.mapper.UserMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 用户信息表 服务类
 * </p>
 *
 * @author 我叫小呆呆
 * @since 2025-04-19
 */
@Slf4j
@Service
public class UserService {

    @Resource
    private UserMapper mapper;

//    **************************五大基础方法**************************
	public User getById(Long id) {
        return mapper.getById(id);
    }

    public User extractById(Long id) {
        return mapper.extractById(id);
    }

    public Boolean delete(Long id) {
        if (ObjectUtils.isEmpty(getById(id))) {
            return false;
        }

        long timestamp = System.currentTimeMillis() / 1000;
        return 1 == mapper.delete(id, (int)timestamp);
    }

	public Boolean insert(User entity) {
        //todo 加密密码
        long timestamp = System.currentTimeMillis() / 1000;
        entity.setCreateTime((int)timestamp);
        entity.setUpdateTime((int)timestamp);
        return 1 == mapper.insert(entity);
    }

    public Boolean update(User entity) {
        Long id = entity.getId();
        if (ObjectUtils.isEmpty(getById(id))) {
            throw new RuntimeException("更新失败，目标id：" + id + "不存在");
        }

        long timestamp = System.currentTimeMillis() / 1000;
        entity.setUpdateTime((int)timestamp);
        return 1 == mapper.update(entity);
    }

    public List<User> getByProperty(User entity) {
        // 判断entity，避免无字段条件查到整张表的数据
        if (CustomUtils.isAllFieldsNull(entity)) {
            log.warn("不合理的使用UserService.getByProperty(User entity)，entity参数为null或属性全为null！");
            return null;
        }
        return mapper.getByProperty(entity);
    }
}