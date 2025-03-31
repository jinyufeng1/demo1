package com.example.demo1.module.mapper;

import com.example.demo1.module.entity.Coach;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper //代理对象会被注册到 MyBatis 的 SqlSession 中，但不会直接交给 Spring 容器管理
public interface CoachMapper {
//    @Select(
//        "<script>" +
//            "select * from coach " +
//            "where is_deleted = 0 " +
//            "<if test='keyword != null'>" +
//            "   and name like CONCAT('%', #{keyword}, '%')" +
//            "</if>" +
//            "order by id " +
//            "limit #{index}, #{pageSize}" +
//        "</script>"
//    )
//    @Select("select * from coach WHERE is_deleted = 0 order by id limit #{index}, #{pageSize}")
    List<Coach> getPageList(int index, int pageSize, String keyword);

//    @Select("select count(*) from coach WHERE is_deleted = 0")
    int count(String keyword);

//    **************************五大基础方法**************************
    @Select("select * from coach WHERE id = #{id} and is_deleted = 0")
    Coach getById(@Param("id") Long id);

    @Select("select * from coach WHERE id = #{id}")
    Coach extractById(@Param("id") Long id);

//    @Insert(
//            "insert into coach  " +
//            "(`name`,`pics`,`speciality`,`intro`,`create_time`,`update_time`) " +
//            "VALUES(#{coach.name},#{coach.pics},#{coach.speciality},#{coach.intro},#{coach.createTime},#{coach.updateTime})"
//    )
//    todo 尝试@SelectProvider
    int insert(@Param("entity") Coach entity);


    @Update("update coach set is_deleted=1, update_time=#{timestamp} where id=#{id} limit 1")
    int delete(@Param("id") Long id, @Param("timestamp") Integer timestamp);

//    @Update(
//            "update coach " +
//            "set pics=#{coach.pics}, `name`=#{coach.name}, speciality=#{coach.speciality}, intro=#{coach.intro}, update_time=#{coach.updateTime} " +
//            "where id=#{id} limit 1"
//    )
//    todo 尝试@UpdateProvider
    int update(@Param("entity") Coach entity);

    List<Coach> getByProperty(@Param("entity") Coach entity);

    int deleteByProperty(@Param("entity") Coach entity, @Param("timestamp") Integer timestamp);
}
