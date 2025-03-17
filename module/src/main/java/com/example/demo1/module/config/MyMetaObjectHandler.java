package com.example.demo1.module.config;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

@Component
public class MyMetaObjectHandler implements MetaObjectHandler {
    @Override
    public void insertFill(MetaObject metaObject) {
        long timestamp = System.currentTimeMillis() / 1000;
        // 插入时填充创建时间和更新时间
        this.strictInsertFill(metaObject, "createTime", Integer.class, (int)timestamp);
        this.strictInsertFill(metaObject, "updateTime", Integer.class, (int)timestamp);
    }

//    没有传递实体对象不会被触发
    @Override
    public void updateFill(MetaObject metaObject) {
        long timestamp = System.currentTimeMillis() / 1000;
        // 更新时填充更新时间
        this.strictUpdateFill(metaObject, "updateTime", Integer.class, (int)timestamp);
    }
}