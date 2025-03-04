package com.example.demo1.module.mapper;

import com.example.demo1.module.entity.Coach;
import org.apache.ibatis.annotations.*;

import java.math.BigInteger;
import java.util.List;

@Mapper //代理对象会被注册到 MyBatis 的 SqlSession 中，但不会直接交给 Spring 容器管理
public interface CoachMapper {

    @Select("select * from coach WHERE is_deleted = 0")
    List<Coach> getCoachList();


    @Select("select * from coach WHERE is_deleted = 0 and id = #{id}")
    Coach getCoachInfo(@Param("id") BigInteger id);

    @Insert("insert into coach  (`name`,`pics`,`speciality`,`intro`,`create_time`,`update_time`) VALUES(#{name},#{pics},#{speciality},#{intro},#{timestamp},#{timestamp})")
    Boolean addCoach(@Param("pics") String pics, @Param("name") String name,
                     @Param("speciality") String speciality, @Param("intro") String intro,
                     @Param("timestamp") Integer timestamp);


    @Update("update coach set is_deleted=1, update_time=#{timestamp} where id=#{id} limit 1")
    Boolean delCoach(@Param("id") BigInteger id, @Param("timestamp") Integer timestamp);

    @Update("update coach set pics=#{pics}, name=#{name}, speciality=#{speciality}, intro=#{intro}, update_time=#{timestamp} where id=#{id} limit 1")
    Boolean updateCoach(@Param("id") BigInteger id,
                        @Param("pics") String pics, @Param("name") String name,
                        @Param("speciality") String speciality, @Param("intro") String intro,
                        @Param("timestamp") Integer timestamp);
}
