package com.example.demo1.module.mapper;

import com.example.demo1.module.entity.User;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * <p>
 * 用户信息表 Mapper 接口
 * </p>
 *
 * @author 我叫小呆呆
 * @since 2025-04-19
 */
public interface UserMapper {
//    **************************五大基础方法**************************
	@Select("select * from user WHERE id = #{id} and is_deleted = 0")
	User getById(@Param("id") Long id);
	
	@Select("select * from user WHERE id = #{id}")
	User extractById(@Param("id") Long id);
	
	@Update("update user set is_deleted = 1, update_time = #{timestamp} where id = #{id} limit 1")
	int delete(@Param("id") Long id, @Param("timestamp") Integer timestamp);

	int insert(@Param("entity") User entity);
	
	int update(@Param("entity") User entity);

	List<User> getByProperty(@Param("entity") User entity);
}
