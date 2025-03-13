package com.example.demo1.module.mapper;

import com.example.demo1.module.entity.Coach;
import org.apache.ibatis.annotations.*;

import java.math.BigInteger;
import java.util.List;

@Mapper //代理对象会被注册到 MyBatis 的 SqlSession 中，但不会直接交给 Spring 容器管理
public interface CoachMapper {

    @Select("select * from coach WHERE is_deleted = 0 order by id limit #{index}, #{pageSize}")
    List<Coach> getCoachList(int index, int pageSize);

    @Select("select count(*) from coach WHERE is_deleted = 0")
    int getCoachCount();

    @Select("select * from coach WHERE id = #{id} and is_deleted = 0")
    Coach getCoachInfo(@Param("id") BigInteger id);

//    @Insert(
//            "insert into coach  " +
//            "(`name`,`pics`,`speciality`,`intro`,`create_time`,`update_time`) " +
//            "VALUES(#{coach.name},#{coach.pics},#{coach.speciality},#{coach.intro},#{coach.createTime},#{coach.updateTime})"
//    )
//    todo 尝试@SelectProvider
    int addCoach(@Param("coach") Coach coach);


    @Update("update coach set is_deleted=1, update_time=#{timestamp} where id=#{id} limit 1")
    int delCoach(@Param("id") BigInteger id, @Param("timestamp") Integer timestamp);

//    @Update(
//            "update coach " +
//            "set pics=#{coach.pics}, `name`=#{coach.name}, speciality=#{coach.speciality}, intro=#{coach.intro}, update_time=#{coach.updateTime} " +
//            "where id=#{id} limit 1"
//    )
//    todo 尝试@UpdateProvider
    int updateCoach(@Param("coach") Coach coach);
}
