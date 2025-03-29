package ${package.Service};

import ${package.Entity}.${entity};
import ${package.Mapper}.${table.mapperName};
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import javax.annotation.Resource;

/**
 * <p>
 * ${table.comment!} 服务类
 * </p>
 *
 * @author ${author}
 * @since ${date}
 */
@Service
public class ${table.serviceName} {

    @Resource
    private ${table.mapperName} mapper;

//    **************************五大基础方法**************************
	public ${entity} getById(Long id) {
        return mapper.getById(id);
    }

    public ${entity} extractById(Long id) {
        return mapper.extractById(id);
    }

    public Boolean delete(Long id) {
        if (ObjectUtils.isEmpty(getById(id))) {
            return false;
        }

        long timestamp = System.currentTimeMillis() / 1000;
        return 0 < mapper.delete(id, (int)timestamp);
    }

	public Boolean insert(${entity} entity) {
        long timestamp = System.currentTimeMillis() / 1000;
        entity.setCreateTime((int)timestamp);
        entity.setUpdateTime((int)timestamp);
        return 0 < mapper.insert(entity);
    }

    public Boolean update(${entity} entity) {
        Long id = entity.getId();
        if (ObjectUtils.isEmpty(getById(id))) {
            throw new RuntimeException("更新失败，目标id：" + id + "不存在");
        }

        long timestamp = System.currentTimeMillis() / 1000;
        entity.setUpdateTime((int)timestamp);
        return 0 < mapper.update(entity);
    }
}