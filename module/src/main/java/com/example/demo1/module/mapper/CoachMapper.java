package com.example.demo1.module.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.example.demo1.module.common.Constant;
import com.example.demo1.module.entity.Coach;
import org.apache.ibatis.annotations.*;

import java.io.Serializable;
import java.math.BigInteger;


@Mapper //代理对象会被注册到 MyBatis 的 SqlSession 中，但不会直接交给 Spring 容器管理
public interface CoachMapper extends BaseMapper<Coach> {
    /*
        BaseMapper中没有开启逻辑删除字段后查询一条数据不区分逻辑删除字段的方法
     */
    @Select("SELECT * FROM coach WHERE id = #{id}")
    Coach extractById(BigInteger id);

    /*
       自定义逻辑删除方法 默认deleteById方法不会触发MetaObjectHandler::updateFill方法
     */
    default int customRemoveById(Serializable id) {
//      this.updateById(entity)不更新逻辑删除字段
//      this.update(entity,wrapper)更新时没有传递entity，不会触发MetaObjectHandler::updateFill方法
        Coach coach = new Coach();
        coach.setIsDeleted(Constant.LOGIC_DELETE_VALUE);
        return this.update(coach, Wrappers.<Coach>lambdaQuery().eq(Coach::getId, id));
    }
}
