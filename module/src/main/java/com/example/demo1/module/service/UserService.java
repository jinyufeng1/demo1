package com.example.demo1.module.service;

import com.example.demo1.module.domain.EditUserDTO;
import com.example.demo1.module.entity.User;
import com.example.demo1.module.exception.CustomException;
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
        String position = "UserService [public Boolean delete(Long id)]";
        if (ObjectUtils.isEmpty(getById(id))) {
            log.info(position + "删除失败，目标id：{}不存在", id);
            return false;
        }

        long timestamp = System.currentTimeMillis() / 1000;
        return 1 == mapper.delete(id, (int)timestamp);
    }

	public Boolean insert(User entity) {
        String position = "UserService [public Boolean insert(User entity)]";
        // 缺参数的问题是方法使用的问题，让技术人员知道就好，在程序内部打印错误日志
        if (null == entity) {
            log.error(position + "插入失败，entity为空！");
            return false;
        }

        if (null == entity.getPassword()) {
            log.error(position + "插入失败，密码必填！");
            return false;
        }

        String phone = entity.getPhone();
        if (null == phone) {
            log.error(position + "插入失败，手机号必填！");
            return false;
        }

        // 手机号重复的问题是业务问题，应该让用户知道，所以抛出去
        User user = getByPhone(phone);
        if (null != user) {
            throw new CustomException(2002);
        }

        long timestamp = System.currentTimeMillis() / 1000;
        entity.setCreateTime((int)timestamp);
        entity.setUpdateTime((int)timestamp);
        return 1 == mapper.insert(entity);
    }

    public Boolean update(User entity) {
        String position = "UserService [public Boolean update(User entity)]";
        if (null == entity) {
            log.error(position + "更新失败，entity为空！");
            return false;
        }

        Long id = entity.getId();
        if (null == id) {
            log.error(position + "更新失败，目标id为空！");
            return false;
        }

        if (ObjectUtils.isEmpty(getById(id))) {
            log.error(position + "更新失败，目标id：{}不存在", id);
            return false;
        }

        // 失去修改的意义
        if (null == entity.getName()
                && null == entity.getPassword()
                && null == entity.getPhone()
                && null == entity.getAvatar()) {
            log.error(position + "更新失败，业务字段全为空");
            return false;
        }

        long timestamp = System.currentTimeMillis() / 1000;
        entity.setUpdateTime((int)timestamp);
        return 1 == mapper.update(entity);
    }

    public User getByPhone(String phone) {
        String position = "UserService [public User getByPhone(String phone)]";
        if (!StringUtils.hasLength(phone)) {
            log.error(position + "参数phone为空");
            return null;
        }
        return mapper.getByPhone(phone);
    }


    /**
     * 合并 insert & update
     * @param dto
     * @return
     */
    public Long edit(EditUserDTO dto) {
        String position = "UserService [public Long edit(EditUserDTO dto)]";
        if (ObjectUtils.isEmpty(dto)) {
            log.error(position + "dto对象为空对象");
            return null;
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
        return result ? user.getId() : null;
    }
}