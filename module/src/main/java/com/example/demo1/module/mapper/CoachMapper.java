package com.example.demo1.module.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.example.demo1.module.common.Constant;
import com.example.demo1.module.entity.Coach;
import org.apache.ibatis.annotations.*;

import java.io.Serializable;
import java.math.BigInteger;


//@Mapper
//代理对象会被注册到 MyBatis 的 SqlSession 中，但不会直接交给 Spring 容器管理，因为是多模块的引用方式，这个注解不会被扫描到，不起作用
//在springboot启动类加@MapperScan扫描整个包，让Spring自动扫描并注册这些接口为Bean
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
//      this.updateById(entity)不更新逻辑删除字段 无法通过实体修改逻辑删除字段，update方法用实体传值也一样
//      this.update(entity,wrapper)更新时没有传递entity，不会触发MetaObjectHandler::updateFill方法
        return this.update(new Coach(), Wrappers.<Coach>lambdaUpdate()
                .eq(Coach::getId, id)
                .set(Coach::getIsDeleted,Constant.LOGIC_DELETE_VALUE));
    }
}
