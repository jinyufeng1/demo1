package com.example.demo1.module.mapper;

import com.example.demo1.module.entity.Tag;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * <p>
 * 标签信息表 Mapper 接口
 * </p>
 *
 * @author 我叫小呆呆
 * @since 2025-04-28
 */
public interface TagMapper {
//    **************************五大基础方法**************************
	@Select("select * from tag WHERE id = #{id} and is_deleted = 0")
	Tag getById(@Param("id") Long id);
	
	@Select("select * from tag WHERE id = #{id}")
	Tag extractById(@Param("id") Long id);
	
	@Update("update tag set is_deleted = 1, update_time = #{timestamp} where id = #{id} limit 1")
	int delete(@Param("id") Long id, @Param("timestamp") Integer timestamp);

	int insert(@Param("entity") Tag entity);
	
	int update(@Param("entity") Tag entity);
    List<Tag> getByNames(@Param("names") List<String> names);
}
