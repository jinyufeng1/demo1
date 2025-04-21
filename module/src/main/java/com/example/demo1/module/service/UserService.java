package com.example.demo1.module.service;

import com.example.demo1.module.domain.EditUserDTO;
import com.example.demo1.module.entity.User;
import com.example.demo1.module.mapper.UserMapper;
import lombok.extern.slf4j.Slf4j;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;

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
        if (null == entity.getPassword()) {
            throw new RuntimeException("插入失败，密码必填！");
        }

        String phone = entity.getPhone();
        if (null == phone) {
            throw new RuntimeException("插入失败，手机号必填！");
        }

        User user = getByPhone(phone);
        if (null != user) {
            throw new RuntimeException("手机号：" + phone + "无法重复注册系统用户，请换号！");
        }

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

        // 失去修改的意义
        if (null == entity.getName()
                && null == entity.getPassword()
                && null == entity.getPhone()
                && null == entity.getAvatar()) {
            return false;
        }

        long timestamp = System.currentTimeMillis() / 1000;
        entity.setUpdateTime((int)timestamp);
        return 1 == mapper.update(entity);
    }

    public User getByPhone(String phone) {
        // 判断entity，避免无字段条件查到整张表的数据
        if (!StringUtils.hasLength(phone)) {
            log.warn("不合理的使用UserService.getByProperty(String phone)，phone参数为null或属性全为null！");
            return null;
        }
        return mapper.getByPhone(phone);
    }


    /**
     * 合并 insert & update
     * @param dto
     * @return
     */
    public String edit(EditUserDTO dto) {
        if (ObjectUtils.isEmpty(dto)) {
            throw new RuntimeException("UserService类，public String edit(EditUserDTO dto)方法拒绝处理，dto对象为空对象");
        }

        String password = dto.getPassword();
        //BCrypt算法加密 盐在密文中
        if (null != password) {
            dto.setPassword(BCrypt.hashpw(password, BCrypt.gensalt())); // 生成一个随机的盐值并返回加密后的密码字符串。工作因子默认为10。
        }

        // copy
        User user = new User();
        BeanUtils.copyProperties(dto, user);
        Boolean result;
        // id校验
        if (ObjectUtils.isEmpty(user.getId())) {
            result = insert(user);
        }
        else {
            result = update(user);
        }
        return result ? user.getId().toString() : null;
    }
}